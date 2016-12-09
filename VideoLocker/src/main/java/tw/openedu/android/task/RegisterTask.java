package tw.openedu.android.task;

import android.content.Context;
import android.os.Bundle;

import com.google.inject.Inject;

import tw.openedu.android.authentication.AuthResponse;
import tw.openedu.android.authentication.LoginAPI;
import tw.openedu.android.social.SocialFactory;


public abstract class RegisterTask extends Task<AuthResponse> {

    private Bundle parameters;
    private SocialFactory.SOCIAL_SOURCE_TYPE backstoreType;
    private String accessToken;

    @Inject
    LoginAPI loginAPI;

    public RegisterTask(Context context, Bundle parameters, String accessToken, SocialFactory.SOCIAL_SOURCE_TYPE backstoreType) {
        super(context);
        this.parameters = parameters;
        this.accessToken = accessToken;
        this.backstoreType = backstoreType;
    }

    @Override
    public AuthResponse call() throws Exception {
        switch (backstoreType) {
            case TYPE_GOOGLE:
                return loginAPI.registerUsingGoogle(parameters, accessToken);
            case TYPE_FACEBOOK:
                return loginAPI.registerUsingFacebook(parameters, accessToken);
            default: // normal email address login
                return loginAPI.registerUsingEmail(parameters);
        }
    }
}
