package tw.openedu.android.task;

import android.content.Context;
import android.support.annotation.NonNull;

import tw.openedu.android.authentication.LoginAPI;
import tw.openedu.android.model.api.ResetPasswordResponse;
import tw.openedu.android.services.ServiceManager;

import javax.inject.Inject;

public abstract class ResetPasswordTask extends Task<ResetPasswordResponse> {

    @Inject
    LoginAPI loginAPI;

    @NonNull
    String emailId;

    public ResetPasswordTask(@NonNull Context context, @NonNull String emailId) {
        super(context);
        this.emailId = emailId;
    }

    @Override
    public ResetPasswordResponse call() throws Exception{
        return loginAPI.resetPassword(emailId);
    }
}
