package tw.openedu.android.task;

import android.content.Context;
import android.support.annotation.NonNull;

import tw.openedu.android.model.api.EnrolledCoursesResponse;

import java.util.List;

public class GetAllDownloadedVideosTask extends Task<List<EnrolledCoursesResponse>> {
    public GetAllDownloadedVideosTask(@NonNull Context context) {
        super(context);
    }

    @Override
    public List<EnrolledCoursesResponse> call() throws Exception {
        return environment.getStorage().getDownloadedCoursesWithVideoCountAndSize();
    }
}
