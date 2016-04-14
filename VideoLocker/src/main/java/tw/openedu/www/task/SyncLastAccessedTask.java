package tw.openedu.www.task;

import android.content.Context;
import android.support.annotation.NonNull;

import tw.openedu.www.model.api.SyncLastAccessedSubsectionResponse;
import tw.openedu.www.services.ServiceManager;

public abstract class SyncLastAccessedTask extends Task<SyncLastAccessedSubsectionResponse> {

    @NonNull
    String courseId ;
    @NonNull
    String lastVisitedModuleId ;
    public SyncLastAccessedTask(@NonNull Context context, @NonNull String courseId,
                                @NonNull String lastVisitedModuleId) {
        super(context);
        this.courseId = courseId;
        this.lastVisitedModuleId = lastVisitedModuleId;
    }

    @Override
    public SyncLastAccessedSubsectionResponse call( ) throws Exception{
        ServiceManager api = environment.getServiceManager();
        SyncLastAccessedSubsectionResponse res = api.syncLastAccessedSubsection(
                courseId, lastVisitedModuleId);
        return res;
    }
}
