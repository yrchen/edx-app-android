package tw.openedu.android.services;

import android.os.Bundle;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import tw.openedu.android.base.MainApplication;
import tw.openedu.android.http.HttpManager;
import tw.openedu.android.http.HttpRequestDelegate;
import tw.openedu.android.http.HttpRequestEndPoint;
import tw.openedu.android.http.IApi;
import tw.openedu.android.http.OkHttpUtil;
import tw.openedu.android.http.cache.CacheManager;
import tw.openedu.android.interfaces.SectionItemInterface;
import tw.openedu.android.logger.Logger;
import tw.openedu.android.model.Filter;
import tw.openedu.android.model.api.AnnouncementsModel;
import tw.openedu.android.model.api.AuthResponse;
import tw.openedu.android.model.api.EnrolledCoursesResponse;
import tw.openedu.android.model.api.HandoutModel;
import tw.openedu.android.model.api.ProfileModel;
import tw.openedu.android.model.api.RegisterResponse;
import tw.openedu.android.model.api.ResetPasswordResponse;
import tw.openedu.android.model.api.SectionEntry;
import tw.openedu.android.model.api.SyncLastAccessedSubsectionResponse;
import tw.openedu.android.model.api.TranscriptModel;
import tw.openedu.android.model.api.VideoResponseModel;
import tw.openedu.android.model.course.CourseComponent;
import tw.openedu.android.model.course.CourseStructureJsonHandler;
import tw.openedu.android.model.course.CourseStructureV1Model;
import tw.openedu.android.module.prefs.PrefManager;
import tw.openedu.android.module.registration.model.RegistrationDescription;
import tw.openedu.android.util.Config;

import java.io.UnsupportedEncodingException;
import java.net.HttpCookie;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * This class is introduced to respect normal java application's layer architecture.
 * controller -> service -> dao -> data source
 * <p/>
 * also, api is designed in a way to make future migration to RetroFit easy
 * <p/>
 * UI layer should call ServiceManager, not IApi directly.
 */
@Singleton
public class ServiceManager {
    protected final Logger logger = new Logger(getClass().getName());


    private final CacheManager cacheManager;
    //TODO - we will move this logic into DI framework

    @Inject
    Config config;

    @Inject
    IApi api;

    public ServiceManager() {
        cacheManager = new CacheManager(MainApplication.instance());
    }

    private HttpRequestEndPoint getEndPointCourseStructure(final String courseId) {
        return new HttpRequestEndPoint() {
            public String getUrl() {
                try {
                    PrefManager pref = new PrefManager(MainApplication.instance(), PrefManager.Pref.LOGIN);
                    String username = URLEncoder.encode(pref.getCurrentUserProfile().username, "UTF-8");
                    String block_counts = URLEncoder.encode("video", "UTF-8");
                    String requested_fields = URLEncoder.encode("graded,format,student_view_multi_device", "UTF-8");
                    String student_view_data = URLEncoder.encode("video", "UTF-8");
                    String cId = URLEncoder.encode(courseId, "UTF-8");

                    String url = config.getApiHostURL() + "/api/courses/v1/blocks/?course_id=" + cId + "&username="
                            + username + "&depth=all&requested_fields=" + requested_fields + "&student_view_data=" + student_view_data + "&block_counts=" + block_counts + "&nav_depth=3";

                    logger.debug("GET url for enrolling in a Course: " + url);
                    return url;
                } catch (UnsupportedEncodingException e) {
                    logger.error(e);
                }
                return "";
            }

            public String getCacheKey() {
                return config.getApiHostURL() + "/api/courses/v1/blocks/?course_id=" + courseId;
            }

            public Map<String, String> getParameters() {
                return null;
            }
        };
    }

    public CourseComponent getCourseStructureFromCache(final String courseId) throws Exception {
        return getCourseStructure(courseId, OkHttpUtil.REQUEST_CACHE_TYPE.ONLY_CACHE);
    }

    public CourseComponent getCourseStructure(final String courseId,
                                              OkHttpUtil.REQUEST_CACHE_TYPE requestCacheType) throws Exception {
        HttpRequestDelegate<CourseComponent> delegate = new HttpRequestDelegate<CourseComponent>(
                api, cacheManager, getEndPointCourseStructure(courseId)) {
            @Override
            public CourseComponent fromJson(String json) throws Exception {
                CourseStructureV1Model model = new CourseStructureJsonHandler().processInput(json);
                return (CourseComponent) CourseManager.normalizeCourseStructure(model, courseId);
            }

            @Override
            public HttpManager.HttpResult invokeHttpCall() throws Exception {
                return api.getCourseStructure(this);
            }

        };

        return delegate.fetchData(requestCacheType);
    }


    public List<SectionItemInterface> getLiveOrganizedVideosByChapter(String courseId, final String chapter) throws Exception {
        CourseComponent course = this.getCourseStructureFromCache(courseId);
        if (course == null) {  //it means we cache the old data model in the file system
            return api.getLiveOrganizedVideosByChapter(courseId, chapter);
        } else {
            return CourseManager.mappingAllVideoResponseModelFrom(course, new Filter<VideoResponseModel>() {
                @Override
                public boolean apply(VideoResponseModel videoResponseModel) {
                    return videoResponseModel != null && videoResponseModel.getChapterName().equals(chapter);
                }
            });
        }
    }

    public Map<String, SectionEntry> getCourseHierarchy(String courseId) throws Exception {
        CourseComponent course = this.getCourseStructureFromCache(courseId);
        if (course == null) {  //it means we cache the old data model in the file system
            return api.getCourseHierarchy(courseId, true);
        } else {
            return CourseManager.mappingCourseHierarchyFrom(course);
        }
    }

    public VideoResponseModel getVideoById(String courseId, String videoId)
            throws Exception {
        CourseComponent course = this.getCourseStructureFromCache(courseId);
        if (course == null) {  //it means we cache the old data model in the file system
            return api.getVideoById(courseId, videoId);
        } else {
            return CourseManager.getVideoById(course, videoId);
        }
    }

    public TranscriptModel getTranscriptsOfVideo(String enrollmentId,
                                                 String videoId) throws Exception {
        try {
            TranscriptModel transcript;
            VideoResponseModel vidModel = getVideoById(enrollmentId, videoId);
            if (vidModel != null) {
                if (vidModel.getSummary() != null) {
                    transcript = vidModel.getSummary().getTranscripts();
                    return transcript;
                }
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }


    public ResetPasswordResponse resetPassword(String emailId) throws Exception {
        return api.resetPassword(emailId);
    }

    public AuthResponse auth(String username, String password) throws Exception {
        return api.auth(username, password);
    }

    public ProfileModel getProfile() throws Exception {
        return api.getProfile();
    }

    public List<EnrolledCoursesResponse> getEnrolledCourses() throws Exception {
        return api.getEnrolledCourses();
    }

    public EnrolledCoursesResponse getCourseById(String courseId) {
        return api.getCourseById(courseId);
    }

    public List<EnrolledCoursesResponse> getEnrolledCourses(boolean fetchFromCache) throws Exception {
        return api.getEnrolledCourses(fetchFromCache);
    }

    public HandoutModel getHandout(String url, boolean fetchFromCache) throws Exception {
        return api.getHandout(url, fetchFromCache);
    }

    public List<AnnouncementsModel> getAnnouncement(String url, boolean preferCache) throws Exception {
        return api.getAnnouncement(url, preferCache);
    }

    public String downloadTranscript(String url) throws Exception {
        return api.downloadTranscript(url);
    }

    public AuthResponse loginByFacebook(String accessToken) throws Exception {
        return api.loginByFacebook(accessToken);
    }


    public AuthResponse loginByGoogle(String accessToken) throws Exception {
        return api.loginByGoogle(accessToken);
    }


    public SyncLastAccessedSubsectionResponse syncLastAccessedSubsection(String courseId, String lastVisitedModuleId) throws Exception {
        return api.syncLastAccessedSubsection(courseId, lastVisitedModuleId);
    }


    public SyncLastAccessedSubsectionResponse getLastAccessedSubsection(String courseId) throws Exception {
        return api.getLastAccessedSubsection(courseId);
    }

    public RegisterResponse register(Bundle parameters) throws Exception {
        return api.register(parameters);
    }


    public RegistrationDescription getRegistrationDescription() throws Exception {
        return api.getRegistrationDescription();
    }


    public Boolean enrollInACourse(String courseId, boolean email_opt_in) throws Exception {
        return api.enrollInACourse(courseId, email_opt_in);
    }

    public List<HttpCookie> getSessionExchangeCookie() throws Exception {
        return api.getSessionExchangeCookie();
    }
}
