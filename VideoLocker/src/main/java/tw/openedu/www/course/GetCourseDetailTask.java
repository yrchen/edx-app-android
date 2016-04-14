package tw.openedu.www.course;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.inject.Inject;

import tw.openedu.www.task.Task;

public abstract class GetCourseDetailTask extends Task<CourseDetail> {

    @NonNull
    private final String courseId;

    @Inject
    private CourseAPI courseAPI;

    public GetCourseDetailTask(@NonNull Context context, @NonNull String courseId) {
        super(context);
        if (TextUtils.isEmpty(courseId)) throw new IllegalArgumentException();
        this.courseId = courseId;
    }

    public CourseDetail call() throws Exception {
        return courseAPI.getCourseDetail(courseId);
    }
}
