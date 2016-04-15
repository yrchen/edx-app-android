package tw.openedu.www.http;

import android.content.Context;

import tw.openedu.www.logger.Logger;
import tw.openedu.www.model.api.AuthResponse;
import tw.openedu.www.module.prefs.PrefManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 *  this interceptor inject oauth token to request header
 **/
public final class OauthHeaderRequestInterceptor implements Interceptor {
    protected final Logger logger = new Logger(getClass().getName());
    private Context context;
    public OauthHeaderRequestInterceptor(Context context){
        this.context = context;
    }
    @Override public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        Request.Builder buider = originalRequest.newBuilder();
        // generate auth headers
        PrefManager pref = new PrefManager(context, PrefManager.Pref.LOGIN);
        AuthResponse auth = pref.getCurrentAuth();

        if (auth == null || !auth.isSuccess()) {
            // this might be a login with Facebook or Google
            String token = pref.getString(PrefManager.Key.AUTH_TOKEN_SOCIAL);
            if (token != null) {
                buider.addHeader("Authorization", token);
            } else {
                logger.warn("Token cannot be null when AUTH_JSON is also null, something is WRONG!");
            }
        } else {
            buider.addHeader("Authorization", String.format("%s %s", auth.token_type, auth.access_token));
        }
        return chain.proceed(buider.build());
    }

}