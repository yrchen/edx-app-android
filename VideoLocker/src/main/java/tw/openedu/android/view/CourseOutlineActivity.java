package tw.openedu.android.view;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.google.inject.Inject;

import tw.openedu.android.R;
import tw.openedu.android.base.MainApplication;
import tw.openedu.android.model.course.CourseComponent;
import tw.openedu.android.module.analytics.ISegment;
import tw.openedu.android.module.prefs.PrefManager;
import tw.openedu.android.services.CourseManager;
import tw.openedu.android.services.LastAccessManager;


/**
 *  Top level outline for the Course
 */
public class CourseOutlineActivity extends CourseVideoListActivity {

    private CourseOutlineFragment fragment;

    @Inject
    CourseManager courseManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isOnCourseOutline()) {
            environment.getSegment().trackScreenView(
                    ISegment.Screens.COURSE_OUTLINE, courseData.getCourse().getId(), null);
        }
    }

    public void onResume(){
        super.onResume();

        if (isOnCourseOutline()) {
            setTitle(courseData.getCourse().getName());
            LastAccessManager.getSharedInstance().fetchLastAccessed(this, courseData.getCourse().getId());
        }
    }

    @Override
    protected void onLoadData() {
        CourseComponent courseComponent = courseManager.getComponentById(
                courseData.getCourse().getId(), courseComponentId);
        setTitle(courseComponent.getDisplayName());

        if (fragment == null) {
            fragment = new CourseOutlineFragment();
            fragment.setTaskProcessCallback(this);

            Bundle bundle = new Bundle();
            bundle.putSerializable(Router.EXTRA_COURSE_DATA, courseData);
            bundle.putString(Router.EXTRA_COURSE_COMPONENT_ID, courseComponentId);
            bundle.putString(Router.EXTRA_LAST_ACCESSED_ID
                    , getIntent().getStringExtra(Router.EXTRA_LAST_ACCESSED_ID));
            fragment.setArguments(bundle);
            //this activity will only ever hold this lone fragment, so we
            // can afford to retain the instance during activity recreation
            fragment.setRetainInstance(true);

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, fragment, CourseOutlineFragment.TAG);
            fragmentTransaction.disallowAddToBackStack();
            fragmentTransaction.commitAllowingStateLoss();
        }

        if (isOnCourseOutline()) {
            LastAccessManager.getSharedInstance().fetchLastAccessed(this, courseData.getCourse().getId());
        } else {
            environment.getSegment().trackScreenView(
                    ISegment.Screens.SECTION_OUTLINE, courseData.getCourse().getId(), courseComponent.getInternalName());

            // Update the last accessed item reference if we are in the course subsection view
            String prefName = PrefManager.getPrefNameForLastAccessedBy(getProfile()
                    .username, courseComponent.getCourseId());
            final PrefManager prefManager = new PrefManager(MainApplication.instance(), prefName);
            prefManager.putLastAccessedSubsection(courseComponent.getId(), false);
        }
    }

    @Override
    protected String getUrlForWebView() {
        if ( courseComponentId == null ) return "";
        CourseComponent courseComponent = courseManager.getComponentById(courseData.getCourse().getId(), courseComponentId);
        return courseComponent.getWebUrl();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (savedInstanceState != null){
             fragment = (CourseOutlineFragment)
                 getSupportFragmentManager().findFragmentByTag(CourseOutlineFragment.TAG);
        }
    }

    @Override
    public void updateListUI() {
        if( fragment != null ) {
            fragment.reloadList();
            fragment.updateMessageView(null);
        }
    }
}
