package tw.openedu.android.view;

import android.os.Bundle;

import com.google.inject.Inject;

import tw.openedu.android.core.IEdxEnvironment;
import tw.openedu.android.model.course.CourseComponent;
import tw.openedu.android.view.common.PageViewStateCallback;
import tw.openedu.android.view.common.RunnableCourseComponent;

import tw.openedu.android.base.BaseFragment;

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
