package tw.openedu.android.task;

import android.content.Context;
import android.support.annotation.NonNull;

import tw.openedu.android.model.api.SyncLastAccessedSubsectionResponse;
import tw.openedu.android.services.ServiceManager;

public abstract class GetLastAccessedTask extends Task<SyncLastAccessedSubsectionResponse> {

    @NonNull
    String courseId;
    public GetLastAccessedTask(@NonNull Context context, @NonNull String courseId) {
        super(context);
        this.courseId = courseId;
    }

    @Override
    public SyncLastAccessedSubsectionResponse call() throws Exception{
        ServiceManager api = environment.getServiceManager();
        SyncLastAccessedSubsectionResponse res = api.getLastAccessedSubsection(courseId);
        return res;
    }
}
