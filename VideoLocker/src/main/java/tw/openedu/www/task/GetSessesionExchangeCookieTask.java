package tw.openedu.www.task;

import android.content.Context;

import tw.openedu.www.services.ServiceManager;

import java.net.HttpCookie;
import java.util.List;

public abstract class GetSessesionExchangeCookieTask extends Task<List<HttpCookie>> {

    public GetSessesionExchangeCookieTask(Context context) {
        super(context);
    }

    @Override
    public List<HttpCookie> call( ) throws Exception{
        ServiceManager api = environment.getServiceManager();
        return api.getSessionExchangeCookie();
    }

}
