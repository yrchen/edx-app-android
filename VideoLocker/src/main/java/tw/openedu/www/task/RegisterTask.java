package tw.openedu.www.task;

import android.content.Context;
import android.os.Bundle;

import tw.openedu.www.model.api.AuthResponse;
import tw.openedu.www.model.api.ProfileModel;
import tw.openedu.www.model.api.RegisterResponse;
import tw.openedu.www.services.ServiceManager;
import tw.openedu.www.social.SocialFactory;


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

    @Override
    public RegisterResponse call( ) throws Exception{
        ServiceManager api = environment.getServiceManager();
        RegisterResponse res = api.register(parameters);

        if (res.isSuccess()) {
            switch ( backstoreType ){
                case  TYPE_GOOGLE :
                case  TYPE_FACEBOOK :
                    // do SOCIAL LOGIN first
                    if ( backstoreType == SocialFactory.SOCIAL_SOURCE_TYPE.TYPE_FACEBOOK ) {
                        auth = api.loginByFacebook(accessToken);
                    } else if ( backstoreType == SocialFactory.SOCIAL_SOURCE_TYPE.TYPE_GOOGLE ) {
                        auth = api.loginByGoogle(accessToken);
                    }
                    if (auth != null && auth.isSuccess()) {
                        // we got a valid accessToken so profile can be fetched
                        ProfileModel profile =  api.getProfile();

                    }
                    break;
                default: //normal email addrss login
                    String username = parameters.getString("username");
                    String password = parameters.getString("password");

                    auth =  getAuthResponse(context, username, password);
                    if (auth.isSuccess()) {
                        logger.debug("login succeeded after email registration");
                    }
            }
        }

        return res;
    }

    public  AuthResponse getAuthResponse(Context context, String username, String password) throws Exception {
        ServiceManager api = environment.getServiceManager();
        AuthResponse res = api.auth(username, password);

        // get profile of this user
        if (res.isSuccess()) {
            res.profile = api.getProfile();

        }
        return res;
    }
    public AuthResponse getAuth() {
        return auth;
    }
}
