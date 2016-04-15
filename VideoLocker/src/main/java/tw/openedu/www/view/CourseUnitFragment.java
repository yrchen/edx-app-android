package tw.openedu.www.view;

import android.os.Bundle;

import com.google.inject.Inject;

import tw.openedu.www.core.IEdxEnvironment;
import tw.openedu.www.model.course.CourseComponent;
import tw.openedu.www.view.common.PageViewStateCallback;
import tw.openedu.www.view.common.RunnableCourseComponent;

import tw.openedu.www.base.BaseFragment;

public abstract class CourseUnitFragment extends BaseFragment implements PageViewStateCallback, RunnableCourseComponent {
    public interface HasComponent {
        CourseComponent getComponent();
    }

    protected CourseComponent unit;
    protected HasComponent hasComponentCallback;

    @Inject
    IEdxEnvironment environment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unit = getArguments() == null ? null :
                (CourseComponent) getArguments().getSerializable(Router.EXTRA_COURSE_UNIT);
    }

    @Override
    public void onPageShow() {

    }

    @Override
    public void onPageDisappear() {

    }

    @Override
    public CourseComponent getCourseComponent() {
        return unit;
    }

    @Override
    public abstract void run();

    public void setHasComponentCallback(HasComponent callback) {
        hasComponentCallback = callback;
    }
}
