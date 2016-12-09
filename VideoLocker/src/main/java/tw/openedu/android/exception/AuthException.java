package tw.openedu.android.exception;

import android.support.annotation.NonNull;

public class AuthException extends Exception {
    public AuthException(@NonNull String message) {
        super(message);
    }

    public AuthException(@NonNull Throwable cause) {
        super(cause);
    }
}
