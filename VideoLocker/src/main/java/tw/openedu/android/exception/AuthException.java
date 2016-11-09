package tw.openedu.android.exception;

import tw.openedu.android.model.api.AuthErrorResponse;

public class AuthException extends Exception {

    private AuthErrorResponse authResponseObject;

    public AuthException(AuthErrorResponse res) {
        this.authResponseObject = res;
    }

    @Override
    public String getMessage() {
        return authResponseObject.detail;
    }
}
