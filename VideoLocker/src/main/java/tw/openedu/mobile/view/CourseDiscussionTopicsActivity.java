package tw.openedu.mobile.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.google.inject.Inject;

import tw.openedu.mobile.R;
import tw.openedu.mobile.base.BaseSingleFragmentActivity;
import tw.openedu.mobile.model.api.EnrolledCoursesResponse;
import tw.openedu.mobile.module.analytics.ISegment;

import roboguice.inject.InjectExtra;

public class CourseDiscussionTopicsActivity extends BaseSingleFragmentActivity {

    @Inject
    private CourseDiscussionTopicsFragment discussionFragment;

    @InjectExtra(Router.EXTRA_COURSE_DATA)
    private EnrolledCoursesResponse courseData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        blockDrawerFromOpening();
        environment.getSegment().trackScreenView(ISegment.Screens.FORUM_VIEW_TOPICS,
                courseData.getCourse().getId(), null, null);
    }

    @Override
    public Fragment getFirstFragment() {
        if (courseData != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Router.EXTRA_COURSE_DATA, courseData);
            discussionFragment.setArguments(bundle);
        }
        discussionFragment.setRetainInstance(true);
        return discussionFragment;
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle(getString(R.string.discussion_topics_title));
    }
}
