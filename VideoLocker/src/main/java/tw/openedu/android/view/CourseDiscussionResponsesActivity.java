package tw.openedu.android.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.google.inject.Inject;

import tw.openedu.android.R;
import tw.openedu.android.base.BaseSingleFragmentActivity;
import tw.openedu.android.discussion.DiscussionThread;

import roboguice.inject.InjectExtra;

public class CourseDiscussionResponsesActivity extends BaseSingleFragmentActivity {

    @Inject
    CourseDiscussionResponsesFragment courseDiscussionResponsesFragment;

    @InjectExtra(Router.EXTRA_DISCUSSION_THREAD)
    private DiscussionThread discussionThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        blockDrawerFromOpening();

        switch (discussionThread.getType()) {
            case DISCUSSION:
                setTitle(R.string.discussion_title);
                break;
            case QUESTION:
                setTitle(discussionThread.isHasEndorsed() ?
                        R.string.course_discussion_answered_title :
                        R.string.course_discussion_unanswered_title);
                break;
        }
    }

    @Override
    public Fragment getFirstFragment() {
        courseDiscussionResponsesFragment.setArguments(getIntent().getExtras());
        courseDiscussionResponsesFragment.setRetainInstance(true);

        return courseDiscussionResponsesFragment;
    }
}
