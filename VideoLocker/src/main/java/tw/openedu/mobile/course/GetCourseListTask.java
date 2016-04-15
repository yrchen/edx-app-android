package tw.openedu.mobile.course;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.inject.Inject;

import tw.openedu.mobile.model.Page;
import tw.openedu.mobile.task.Task;

public abstract class GetCourseListTask extends
        Task<Page<CourseDetail>> {

    @Inject
    private CourseAPI courseAPI;

    final int page;

    public GetCourseListTask(@NonNull Context context, int page) {
        super(context);
        this.page = page;
    }


    public Page<CourseDetail> call() throws Exception {
        return courseAPI.getCourseList(page);
    }
}
