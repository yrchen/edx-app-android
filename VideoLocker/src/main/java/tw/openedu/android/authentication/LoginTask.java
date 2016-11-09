package tw.openedu.android.authentication;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;

import tw.openedu.android.R;
import tw.openedu.android.exception.LoginErrorMessage;
import tw.openedu.android.exception.LoginException;
import tw.openedu.android.http.RetroHttpException;
import tw.openedu.android.module.analytics.ISegment;
import tw.openedu.android.module.prefs.PrefManager;
import tw.openedu.android.services.ServiceManager;
import tw.openedu.android.task.Task;

import java.io.IOException;

import retrofit.RetrofitError;

public abstract class LoginTask extends Task<AuthResponse>  {

    @NonNull
    private final String username;
    @NonNull
    private final String password;

    @Inject
    private LoginAPI loginAPI;

    public LoginTask(@NonNull Context context, @NonNull String username, @NonNull String password) {
        super(context);
        this.username = username;
        this.password = password;
    }

    @Override
    public AuthResponse call() throws Exception {
        AuthResponse response = loginAPI.getAccessToken(username, password);

        // store auth token response
        Gson gson = new GsonBuilder().create();
        PrefManager pref = new PrefManager(context, PrefManager.Pref.LOGIN);
        pref.put(PrefManager.Key.AUTH_JSON, gson.toJson(response));
        pref.put(PrefManager.Key.SEGMENT_KEY_BACKEND, ISegment.Values.PASSWORD);

        ServiceManager api = environment.getServiceManager();

        // get profile of this user
        if (response.isSuccess()) {
            try {
                response.profile = api.getProfile();
            } catch (IOException e) {
                throw new RetroHttpException(RetrofitError.networkError(null, e));
            } catch (Exception e) {
                /* TODO: Move the API for getting the user profile data
                 * to Retrofit, and then post a message appropriate for
                 * the type of error encountered during the request.
                 */
                throwLoginException();
            }
            if (!response.hasValidProfile()) {
                throwLoginException();
            }

        }
        return response;
    }

    /**
     * Throw a generic exception about login failure.
     *
     * @throws LoginException The exception.
     */
    private void throwLoginException() throws LoginException {
        throw new LoginException(new LoginErrorMessage(
                context.getString(R.string.login_error),
                context.getString(R.string.login_failed)));
    }
}
