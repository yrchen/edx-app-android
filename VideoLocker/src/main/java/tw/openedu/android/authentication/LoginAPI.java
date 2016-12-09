package tw.openedu.android.authentication;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import tw.openedu.android.util.Config;
import tw.openedu.android.exception.AuthException;
import tw.openedu.android.http.ApiConstants;
import tw.openedu.android.http.HttpResponseStatusException;
import tw.openedu.android.http.HttpException;
import tw.openedu.android.model.api.FormFieldMessageBody;
import tw.openedu.android.model.api.ProfileModel;
import tw.openedu.android.model.api.ResetPasswordResponse;
import tw.openedu.android.module.analytics.ISegment;
import tw.openedu.android.module.notification.NotificationDelegate;
import tw.openedu.android.module.prefs.LoginPrefs;
import tw.openedu.android.util.Config;
import tw.openedu.android.util.observer.BasicObservable;
import tw.openedu.android.util.observer.Observable;

import java.util.HashMap;
import java.util.Map;

import retrofit.RestAdapter;

@Singleton
public class LoginAPI {

    @NonNull
    private final LoginService loginService;

    @NonNull
    private final Config config;

    @NonNull
    private final LoginPrefs loginPrefs;

    @NonNull
    private final ISegment segment;

    @NonNull
    private final NotificationDelegate notificationDelegate;

    @NonNull
    private final BasicObservable<LogInEvent> logInEvents = new BasicObservable<>();

    @NonNull
    private final Gson gson;

    @Inject
    public LoginAPI(@NonNull RestAdapter restAdapter,
                    @NonNull Config config,
                    @NonNull LoginPrefs loginPrefs,
                    @NonNull ISegment segment,
                    @NonNull NotificationDelegate notificationDelegate,
                    @NonNull Gson gson) {
        this.config = config;
        this.loginPrefs = loginPrefs;
        this.segment = segment;
        this.notificationDelegate = notificationDelegate;
        this.gson = gson;
        loginService = restAdapter.create(LoginService.class);
    }

    @NonNull
    public AuthResponse getAccessToken(@NonNull String username,
                                       @NonNull String password) throws HttpException {
        String grantType = "password";
        String clientID = config.getOAuthClientId();
        return loginService.getAccessToken(grantType, clientID, username, password);
    }

    @NonNull
    public AuthResponse logInUsingEmail(@NonNull String email, @NonNull String password) throws Exception {
        try {
            final AuthResponse response = getAccessToken(email, password);
            if (!response.isSuccess()) {
                throw new AuthException(response.error);
            }
            finishLogIn(response, LoginPrefs.AuthBackend.PASSWORD, email.trim());
            return response;
        } catch (HttpResponseStatusException ex) {
            if (ex.getStatusCode() >= 400 && ex.getStatusCode() < 500) {
                throw new AuthException(ex);
            }
            throw ex;
        }
    }

    @NonNull
    public AuthResponse logInUsingFacebook(String accessToken) throws Exception {
        return finishSocialLogIn(accessToken, LoginPrefs.AuthBackend.FACEBOOK);
    }

    @NonNull
    public AuthResponse logInUsingGoogle(String accessToken) throws Exception {
        return finishSocialLogIn(accessToken, LoginPrefs.AuthBackend.GOOGLE);
    }

    @NonNull
    private AuthResponse finishSocialLogIn(@NonNull String accessToken, @NonNull LoginPrefs.AuthBackend authBackend) throws Exception {
        final String backend = ApiConstants.getOAuthGroupIdForAuthBackend(authBackend);
        final AuthResponse response = loginService.exchangeAccessToken(accessToken, config.getOAuthClientId(), backend);
        if (response.error != null && response.error.equals("401")) {
            // TODO: Introduce a more explicit error code to indicate that an account is not linked.
            throw new AccountNotLinkedException();
        }
        finishLogIn(response, authBackend, "");
        return response;
    }

    private void finishLogIn(@NonNull AuthResponse response, @NonNull LoginPrefs.AuthBackend authBackend, @NonNull String usernameUsedToLogIn) throws Exception {
        loginPrefs.storeAuthTokenResponse(response, authBackend);
        try {
            response.profile = getProfile();
        } catch (Throwable e) {
            // The app doesn't properly handle the scenario that we are logged in but we don't have
            // a cached profile. So if we fail to fetch the profile, let's erase the stored token.
            // TODO: A better approach might be to fetch the profile *before* storing the token.
            loginPrefs.clearAuthTokenResponse();
            throw e;
        }
        loginPrefs.setLastAuthenticatedEmail(usernameUsedToLogIn);
        segment.identifyUser(
                response.profile.id.toString(),
                response.profile.email,
                usernameUsedToLogIn);
        final String backendKey = loginPrefs.getAuthBackendKeyForSegment();
        if (backendKey != null) {
            segment.trackUserLogin(backendKey);
        }
        notificationDelegate.resubscribeAll();
        logInEvents.sendData(new LogInEvent());
    }

    @NonNull
    public AuthResponse registerUsingEmail(@NonNull Bundle parameters) throws Exception {
        register(parameters);
        return logInUsingEmail(parameters.getString("username"), parameters.getString("password"));
    }

    @NonNull
    public AuthResponse registerUsingGoogle(@NonNull Bundle parameters, @NonNull String accessToken) throws Exception {
        register(parameters);
        return logInUsingGoogle(accessToken);
    }

    @NonNull
    public AuthResponse registerUsingFacebook(@NonNull Bundle parameters, @NonNull String accessToken) throws Exception {
        register(parameters);
        return logInUsingFacebook(accessToken);
    }

    @NonNull
    public Observable<LogInEvent> getLogInEvents() {
        return logInEvents;
    }

    @NonNull
    public ResetPasswordResponse resetPassword(@NonNull String email) throws HttpException {
        return loginService.resetPassword(email);
    }

    @NonNull
    private void register(Bundle parameters) throws Exception {
        try {
            final Map<String, String> parameterMap = new HashMap<>();
            for (String key : parameters.keySet()) {
                parameterMap.put(key, parameters.getString(key));
            }
            loginService.register(parameterMap);
        } catch (HttpResponseStatusException e) {
            if ((e.getStatusCode() == 400 || e.getStatusCode() == 409) && !android.text.TextUtils.isEmpty(e.getBody())) {
                try {
                    final FormFieldMessageBody body = gson.fromJson(e.getBody(), FormFieldMessageBody.class);
                    if (body != null && body.size() > 0) {
                        throw new RegistrationException(body);
                    }
                } catch (JsonSyntaxException ex) {
                    // Looks like the response does not contain form validation errors.
                }
            }
            throw e;
        }
    }

    @NonNull
    public ProfileModel getProfile() throws Exception {
        ProfileModel res = loginService.getProfile();
        loginPrefs.storeUserProfile(res);
        return res;
    }

    public static class AccountNotLinkedException extends Exception {
    }

    public static class RegistrationException extends Exception {
        @NonNull
        private final FormFieldMessageBody formErrorBody;

        public RegistrationException(@NonNull FormFieldMessageBody formErrorBody) {
            this.formErrorBody = formErrorBody;
        }

        @NonNull
        public FormFieldMessageBody getFormErrorBody() {
            return formErrorBody;
        }
    }
}
