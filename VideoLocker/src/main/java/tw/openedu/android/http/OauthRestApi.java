package tw.openedu.android.http;

import tw.openedu.android.http.model.EnrollmentRequestBody;
import tw.openedu.android.model.api.EnrolledCoursesResponse;
import tw.openedu.android.model.api.ProfileModel;
import tw.openedu.android.model.api.SyncLastAccessedSubsectionResponse;
import tw.openedu.android.model.api.VideoResponseModel;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * we group all the mobile endpoints which require oauth token together
 */
public interface OauthRestApi {

    /* GET calls */

    /**
     * Returns user's basic profile information for current active session.
     * @return
     * @throws Exception
     */
    @GET(ApiConstants.URL_MY_USER_INFO)
    ProfileModel getProfile();

    @GET(ApiConstants.URL_VIDEO_OUTLINE)
    List<VideoResponseModel> getCourseHierarchy(@Path(ApiConstants.COURSE_ID) String courseId);

    @Headers("Cache-Control: no-cache")
    @GET(ApiConstants.URL_COURSE_OUTLINE)
    String getCourseOutlineNoCache(@Query("course_id") String courseId,
                                   @Query("user") String username,
                                   @Query("requested_fields") String fields,
                                   @Query("student_view_data") String blockJson,
                                   @Query("block_counts") String blockCount);

    @GET(ApiConstants.URL_COURSE_OUTLINE)
    String getCourseOutline(@Query("course_id") String courseId,
                            @Query("user") String username,
                            @Query("requested_fields") String fields,
                            @Query("student_view_data") String blockJson,
                            @Query("block_counts") String blockCount);

    /**
     * Returns enrolled courses of given user.
     *
     * @return
     * @throws Exception
     */
    @GET(ApiConstants.URL_COURSE_ENROLLMENTS)
    List<EnrolledCoursesResponse> getEnrolledCourses(@Path(ApiConstants.USER_NAME) String username);

    @Headers("Cache-Control: no-cache")
    @GET(ApiConstants.URL_COURSE_ENROLLMENTS)
    List<EnrolledCoursesResponse> getEnrolledCoursesNoCache(@Path(ApiConstants.USER_NAME) String username);

    /* POST Calls */

    @POST(ApiConstants.URL_VIDEO_OUTLINE)
    List<VideoResponseModel> getVideosByCourseId(@Path(ApiConstants.COURSE_ID) String courseId);

    @PUT(ApiConstants.URL_LAST_ACCESS_FOR_COURSE)
    SyncLastAccessedSubsectionResponse syncLastAccessedSubsection(@Body EnrollmentRequestBody.LastAccessRequestBody body,
                                                                  @Path(ApiConstants.USER_NAME) String username,
                                                                  @Path(ApiConstants.COURSE_ID) String courseId);

    @GET(ApiConstants.URL_LAST_ACCESS_FOR_COURSE)
    SyncLastAccessedSubsectionResponse getLastAccessedSubsection( @Path(ApiConstants.USER_NAME) String username,
                                                                  @Path(ApiConstants.COURSE_ID) String courseId);


    @POST(ApiConstants.URL_ENROLLMENT)
    String enrollACourse(@Body EnrollmentRequestBody body);

}
