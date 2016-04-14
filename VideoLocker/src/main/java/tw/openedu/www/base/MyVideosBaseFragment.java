package tw.openedu.www.base;

import android.os.Bundle;

import com.google.inject.Inject;

import tw.openedu.www.core.IEdxEnvironment;
import tw.openedu.www.model.api.EnrolledCoursesResponse;
import tw.openedu.www.view.Router;

public abstract class MyVideosBaseFragment extends BaseFragment {
    @Inject
    protected IEdxEnvironment environment;

    protected EnrolledCoursesResponse courseData;
    protected String courseComponentId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Call this function when Video completes downloading
     * so that downloaded videos appears in MyVideos listing
     */
    public abstract void reloadList();

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if ( courseData != null)
            outState.putSerializable(Router.EXTRA_ENROLLMENT, courseData);
        if ( courseComponentId != null )
            outState.putString(Router.EXTRA_COURSE_COMPONENT_ID, courseComponentId);
        super.onSaveInstanceState(outState);
    }

    protected void restore(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            courseData = (EnrolledCoursesResponse) savedInstanceState.getSerializable(Router.EXTRA_ENROLLMENT);
            courseComponentId = (String) savedInstanceState.getString(Router.EXTRA_COURSE_COMPONENT_ID);
        }
    }
}
