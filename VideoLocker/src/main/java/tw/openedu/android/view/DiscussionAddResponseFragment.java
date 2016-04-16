package tw.openedu.android.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.inject.Inject;

import tw.openedu.android.R;
import tw.openedu.android.base.BaseFragment;
import tw.openedu.android.discussion.CommentBody;
import tw.openedu.android.discussion.DiscussionComment;
import tw.openedu.android.discussion.DiscussionCommentPostedEvent;
import tw.openedu.android.discussion.DiscussionTextUtils;
import tw.openedu.android.discussion.DiscussionThread;
import tw.openedu.android.logger.Logger;
import tw.openedu.android.module.analytics.ISegment;
import tw.openedu.android.task.CreateCommentTask;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

public class DiscussionAddResponseFragment extends BaseFragment {

    static public String TAG = DiscussionAddResponseFragment.class.getCanonicalName();

    @InjectExtra(value = Router.EXTRA_DISCUSSION_TOPIC_OBJ, optional = true)
    private DiscussionThread discussionThread;

    protected final Logger logger = new Logger(getClass().getName());

    @InjectView(R.id.etNewComment)
    private EditText editTextNewComment;

    @InjectView(R.id.btnAddComment)
    private ViewGroup buttonAddComment;

    @InjectView(R.id.progress_indicator)
    private ProgressBar createCommentProgressBar;

    @InjectView(R.id.tvTitle)
    private TextView textViewTitle;

    @InjectView(R.id.tvResponse)
    private TextView textViewResponse;

    @InjectView(R.id.tvTimeAuthor)
    private TextView textViewTimeAuthor;
    private CreateCommentTask createCommentTask;

    @Inject
    private Router router;

    @Inject
    ISegment segIO;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Map<String, String> values = new HashMap<>();
        values.put(ISegment.Keys.TOPIC_ID, discussionThread.getTopicId());
        values.put(ISegment.Keys.THREAD_ID, discussionThread.getIdentifier());
        segIO.trackScreenView(ISegment.Screens.FORUM_ADD_RESPONSE,
                discussionThread.getCourseId(), discussionThread.getTitle(), values);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_response, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textViewTitle.setText(discussionThread.getTitle());
        textViewResponse.setText(Html.fromHtml(discussionThread.getRenderedBody()));
        DiscussionTextUtils.setAuthorAttributionText(textViewTimeAuthor,
                DiscussionTextUtils.AuthorAttributionLabel.POST,
                discussionThread, new Runnable() {
                    @Override
                    public void run() {
                        router.showUserProfile(getActivity(), discussionThread.getAuthor());
                    }
                });
        buttonAddComment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createComment();
            }
        });
        buttonAddComment.setEnabled(false);
        editTextNewComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                buttonAddComment.setEnabled(s.toString().trim().length() > 0);
            }
        });
    }

    private void createComment() {
        buttonAddComment.setEnabled(false);

        if (createCommentTask != null) {
            createCommentTask.cancel(true);
        }

        final CommentBody commentBody = new CommentBody();
        commentBody.setRawBody(editTextNewComment.getText().toString());
        commentBody.setThreadId(discussionThread.getIdentifier());
        commentBody.setParentId(null);

        createCommentTask = new CreateCommentTask(getActivity(), commentBody) {
            @Override
            public void onSuccess(@NonNull DiscussionComment thread) {
                logger.debug(thread.toString());
                EventBus.getDefault().post(new DiscussionCommentPostedEvent(thread, null));
                getActivity().finish();
            }

            @Override
            public void onException(Exception ex) {
                super.onException(ex);
                buttonAddComment.setEnabled(true);
            }
        };
        createCommentTask.setTaskProcessCallback(null);
        createCommentTask.setProgressDialog(createCommentProgressBar);
        createCommentTask.execute();
    }
}
