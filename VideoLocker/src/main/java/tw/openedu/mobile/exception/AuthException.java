package tw.openedu.mobile.exception;

import tw.openedu.mobile.model.api.AuthErrorResponse;

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
