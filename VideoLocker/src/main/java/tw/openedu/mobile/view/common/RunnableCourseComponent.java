package tw.openedu.mobile.view.common;

import tw.openedu.mobile.model.course.CourseComponent;

/**
 * Created by hanning on 6/9/15.
 */
public interface RunnableCourseComponent extends Runnable{
    CourseComponent getCourseComponent();
}
