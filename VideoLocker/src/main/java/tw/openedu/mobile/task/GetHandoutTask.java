package tw.openedu.mobile.task;

import android.content.Context;
import android.support.annotation.NonNull;

import tw.openedu.mobile.model.api.EnrolledCoursesResponse;
import tw.openedu.mobile.model.api.HandoutModel;
import tw.openedu.mobile.services.ServiceManager;

public abstract class GetHandoutTask extends Task<HandoutModel> {
    @NonNull
    EnrolledCoursesResponse enrollment;
    public GetHandoutTask(@NonNull Context context, @NonNull EnrolledCoursesResponse enrollment) {
        super(context);
        this.enrollment = enrollment;
    }

    @Override
    public HandoutModel call() throws Exception{
        ServiceManager api = environment.getServiceManager();

        // return instant data from cache if available
        HandoutModel model = api.getHandout
                (enrollment.getCourse().getCourse_handouts(), true);
        if (model == null) {
            model = api.getHandout(enrollment.getCourse()
                    .getCourse_handouts(), false);;
        }

        return model;
    }

}
