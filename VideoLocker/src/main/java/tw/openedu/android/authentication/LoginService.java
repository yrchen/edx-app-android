package tw.openedu.android.authentication;

import tw.openedu.android.http.ApiConstants;
import tw.openedu.android.http.HttpException;
import tw.openedu.android.model.api.ProfileModel;
import tw.openedu.android.model.api.ResetPasswordResponse;
import tw.openedu.android.module.prefs.LoginPrefs;

import java.util.Map;

import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

import static tw.openedu.android.http.ApiConstants.URL_MY_USER_INFO;

public interface LoginService {

    /**
     * If there are form validation errors, this method will throw an {@link tw.openedu.android.http.HttpResponseStatusException} with 400 or 409 error code.
     * In case of validation errors the response body will be {@link tw.openedu.android.model.api.FormFieldMessageBody}.
     */
    @FormUrlEncoded
    @POST(ApiConstants.URL_REGISTRATION)
    Response register(@FieldMap Map<String, String> parameters) throws HttpException;

    /**
     * Depending on the query parameters for this endpoint, a different action will be triggered
     * on the server side. In this case, we are sending a user and password to get the AuthResponse.
     */
    @FormUrlEncoded
    @POST(ApiConstants.URL_ACCESS_TOKEN)
    AuthResponse getAccessToken(@Field("grant_type") String grant_type,
                                @Field("client_id") String client_id,
                                @Field("username") String username,
                                @Field("password") String password) throws HttpException;

    /**
     * Depending on the query parameters for this endpoint, a different action will be triggered
     * on the server side. In this case, we are using our refresh_token to get a new AuthResponse.
     */
    @FormUrlEncoded
    @POST(ApiConstants.URL_ACCESS_TOKEN)
    AuthResponse refreshAccessToken(@Field("grant_type") String grant_type,
                                    @Field("client_id") String client_id,
                                    @Field("refresh_token") String refresh_token) throws HttpException;

    /**
     * Authenticate with edX using an access token from a third party OAuth provider.
     * @param accessToken access token retrieved from third party OAuth provider (i.e. Facebook, Google)
     * @param clientId edX OAuth client ID from config
     * @param groupId Group ID as returned from {@link ApiConstants#getOAuthGroupIdForAuthBackend(LoginPrefs.AuthBackend)}
     */
    @FormUrlEncoded
    @POST(ApiConstants.URL_EXCHANGE_ACCESS_TOKEN)
    AuthResponse exchangeAccessToken(@Field("access_token") String accessToken,
                                     @Field("client_id") String clientId,
                                     @Path(ApiConstants.GROUP_ID) String groupId) throws HttpException;

    /**
     * Reset password for account associated with an email address.
     */
    @FormUrlEncoded
    @POST(ApiConstants.URL_PASSWORD_RESET)
    ResetPasswordResponse resetPassword(@Field("email") String email) throws HttpException;


    /**
     * @return basic profile information for currently authenticated user.
     */
    @GET(URL_MY_USER_INFO)
    ProfileModel getProfile() throws HttpException;
}
