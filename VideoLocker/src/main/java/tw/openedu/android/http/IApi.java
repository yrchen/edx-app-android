package tw.openedu.android.http;

import tw.openedu.android.interfaces.SectionItemInterface;
import tw.openedu.android.model.api.AnnouncementsModel;
import tw.openedu.android.model.api.EnrolledCoursesResponse;
import tw.openedu.android.model.api.HandoutModel;
import tw.openedu.android.model.api.SectionEntry;
import tw.openedu.android.model.api.SyncLastAccessedSubsectionResponse;
import tw.openedu.android.model.api.VideoResponseModel;
import tw.openedu.android.module.registration.model.RegistrationDescription;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TODO - we won't need this api when we fully migrate the code to okhttp
 */
public interface IApi {
    List<EnrolledCoursesResponse> getEnrolledCourses()
            throws Exception;

    EnrolledCoursesResponse getCourseById(String courseId);

    List<EnrolledCoursesResponse> getEnrolledCourses(boolean fetchFromCache) throws Exception;

    HandoutModel getHandout(String url, boolean fetchFromCache) throws Exception;

    List<AnnouncementsModel> getAnnouncement(String url, boolean preferCache)
            throws Exception;


    String downloadTranscript(String url)
            throws Exception;

    SyncLastAccessedSubsectionResponse syncLastAccessedSubsection(String courseId,
                                                                  String lastVisitedModuleId) throws Exception;

    SyncLastAccessedSubsectionResponse getLastAccessedSubsection(String courseId) throws Exception;

    RegistrationDescription getRegistrationDescription() throws Exception;

    Boolean enrollInACourse(String courseId, boolean email_opt_in) throws Exception;

    List<HttpCookie> getSessionExchangeCookie() throws Exception;

    @Deprecated
    VideoResponseModel getVideoById(String courseId, String videoId)
            throws Exception;

    @Deprecated
    Map<String, SectionEntry> getCourseHierarchy(String courseId, boolean preferCache) throws Exception;

    @Deprecated
    ArrayList<SectionItemInterface> getLiveOrganizedVideosByChapter
            (String courseId, String chapter);

    HttpManager.HttpResult getCourseStructure(HttpRequestDelegate delegate) throws Exception;
}
