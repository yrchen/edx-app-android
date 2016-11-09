package tw.openedu.android.util.images;

import android.content.Context;
import android.support.annotation.NonNull;

import tw.openedu.android.R;
import tw.openedu.android.http.HttpConnectivityException;
import tw.openedu.android.http.HttpResponseStatusException;
import tw.openedu.android.logger.Logger;
import tw.openedu.android.util.NetworkUtil;

import java.net.HttpURLConnection;

public enum ErrorUtils {
    ;

    protected static final Logger logger = new Logger(ErrorUtils.class.getName());

    @NonNull
    public static String getErrorMessage(@NonNull Throwable ex, @NonNull Context context) {
        String errorMessage = null;
        if (ex instanceof HttpConnectivityException) {
            if (NetworkUtil.isConnected(context)) {
                errorMessage = context.getString(R.string.network_connected_error);
            } else {
                errorMessage = context.getString(R.string.reset_no_network_message);
            }
        } else if (ex instanceof HttpResponseStatusException) {
            final int status = ((HttpResponseStatusException) ex).getStatusCode();
            switch (status) {
                case HttpURLConnection.HTTP_UNAVAILABLE:
                    errorMessage = context.getString(R.string.network_service_unavailable);
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                case HttpURLConnection.HTTP_INTERNAL_ERROR:
                    errorMessage = context.getString(R.string.action_not_completed);
                    break;
            }
        }
        if (null == errorMessage) {
            logger.error(ex, true /* Submit crash report since this is an unknown type of error */);
            errorMessage = context.getString(R.string.error_unknown);
        }
        return errorMessage;
    }
}
