package tw.openedu.mobile.task;

import android.content.Context;
import android.support.annotation.NonNull;

import tw.openedu.mobile.model.api.ResetPasswordResponse;
import tw.openedu.mobile.services.ServiceManager;

public abstract class ResetPasswordTask extends Task<ResetPasswordResponse> {

    @NonNull
    String emailId;
    public ResetPasswordTask(@NonNull Context context, @NonNull String emailId) {
        super(context);
        this.emailId = emailId;
    }

    @Override
    public ResetPasswordResponse call() throws Exception{
        ServiceManager api = environment.getServiceManager();
        ResetPasswordResponse res = api.resetPassword(emailId);
        return res;
    }
}
