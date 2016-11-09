package tw.openedu.android.authentication;

import android.support.annotation.NonNull;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import tw.openedu.android.http.RetroHttpException;
import tw.openedu.android.util.Config;

import retrofit.RestAdapter;

/**
 * Created by cleeedx on 4/12/16.
 */

@Singleton
public class LoginAPI {

    @NonNull
    private final LoginService loginService;

    @Inject
    Config config;

    @Inject
    public LoginAPI(@NonNull RestAdapter restAdapter) {
        loginService = restAdapter.create(LoginService.class);
    }

    public AuthResponse getAccessToken(@NonNull String username,
                                       @NonNull String password) throws RetroHttpException {
        String grantType = "password";
        String clientID = config.getOAuthClientId();
        return loginService.getAccessToken(grantType, clientID, username, password);
    }
}
