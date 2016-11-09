package tw.openedu.android.task;

import android.content.Context;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;

import tw.openedu.android.authentication.AuthResponse;
import tw.openedu.android.authentication.LoginAPI;
import tw.openedu.android.model.api.RegisterResponse;
import tw.openedu.android.module.analytics.ISegment;
import tw.openedu.android.module.prefs.PrefManager;
import tw.openedu.android.services.ServiceManager;
import tw.openedu.android.social.SocialFactory;


public abstract class RegisterTask extends Task<RegisterResponse> {

    private Bundle parameters;
    private AuthResponse auth;
    private SocialFactory.SOCIAL_SOURCE_TYPE backstoreType;
    private String accessToken;

    public RegisterTask(Context context, Bundle parameters, String accessToken, SocialFactory.SOCIAL_SOURCE_TYPE backstoreType) {
        super(context);
        this.parameters = parameters;
        this.accessToken = accessToken;
        this.backstoreType = backstoreType;
    }

    @Inject
    LoginAPI loginAPI;

    @Override
    public RegisterResponse call() throws Exception {
        ServiceManager api = environment.getServiceManager();
        RegisterResponse res = api.register(parameters);

        if (!res.isSuccess()) {
            return res;
        }

        switch (backstoreType) {
            case TYPE_GOOGLE:
                auth = api.loginByGoogle(accessToken);
                break;
            case TYPE_FACEBOOK:
                auth = api.loginByFacebook(accessToken);
                break;
            default: // normal email address login
                String username = parameters.getString("username");
                String password = parameters.getString("password");

                auth = loginAPI.getAccessToken(username, password);

                // store auth token response
                Gson gson = new GsonBuilder().create();
                PrefManager pref = new PrefManager(context, PrefManager.Pref.LOGIN);
                pref.put(PrefManager.Key.AUTH_JSON, gson.toJson(auth));
                pref.put(PrefManager.Key.SEGMENT_KEY_BACKEND, ISegment.Values.PASSWORD);

                if (auth.isSuccess()) {
                    logger.debug("login succeeded after email registration");
                }
        }
        if (auth != null && auth.isSuccess()) {
            auth.profile = api.getProfile();
        }
        return res;
    }

    public AuthResponse getAuth() {
        return auth;
    }
}
