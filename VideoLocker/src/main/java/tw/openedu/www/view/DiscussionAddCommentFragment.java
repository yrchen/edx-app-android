package tw.openedu.www.view;

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

import tw.openedu.www.base.BaseFragment;
import tw.openedu.www.discussion.CommentBody;
import tw.openedu.www.discussion.DiscussionComment;
import tw.openedu.www.discussion.DiscussionCommentPostedEvent;
import tw.openedu.www.discussion.DiscussionTextUtils;
import tw.openedu.www.discussion.DiscussionThread;
import tw.openedu.www.logger.Logger;
import tw.openedu.www.module.analytics.ISegment;
import tw.openedu.www.task.CreateCommentTask;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

public class DiscussionAddCommentFragment extends BaseFragment {

    static public String TAG = DiscussionAddCommentFragment.class.getCanonicalName();

    @InjectExtra(value = Router.EXTRA_DISCUSSION_COMMENT, optional = true)
    DiscussionComment discussionResponse;

    @InjectExtra(Router.EXTRA_DISCUSSION_THREAD)
    private DiscussionThread discussionThread;

    protected final Logger logger = new Logger(getClass().getName());

    @InjectView(tw.openedu.www.R.id.etNewComment)
    private EditText editTextNewComment;

    @InjectView(tw.openedu.www.R.id.btnAddComment)
    private ViewGroup buttonAddComment;

    @InjectView(tw.openedu.www.R.id.progress_indicator)
    private ProgressBar createCommentProgressBar;

    @InjectView(tw.openedu.www.R.id.tvResponse)
    private TextView textViewResponse;

    @InjectView(tw.openedu.www.R.id.tvTimeAuthor)
    private TextView textViewTimeAuthor;

    @InjectView(tw.openedu.www.R.id.discussion_responses_answer_text_view)
    private TextView responseAnswerTextView;

    @Inject
    private Router router;

    @Inject
    ISegment segIO;

    private CreateCommentTask createCommentTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Map<String, String> values = new HashMap<>();
        values.put(ISegment.Keys.TOPIC_ID, discussionThread.getTopicId());
        values.put(ISegment.Keys.THREAD_ID, discussionThread.getIdentifier());
        values.put(ISegment.Keys.RESPONSE_ID, discussionResponse.getIdentifier());
        segIO.trackScreenView(ISegment.Screens.FORUM_ADD_RESPONSE_COMMENT,
                discussionThread.getCourseId(), discussionThread.getTitle(), values);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(tw.openedu.www.R.layout.fragment_add_comment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textViewResponse.setText(Html.fromHtml(discussionResponse.getRenderedBody()));
        DiscussionTextUtils.setEndorsedState(responseAnswerTextView, discussionThread, discussionResponse);
        DiscussionTextUtils.setAuthorAttributionText(textViewTimeAuthor,
                DiscussionTextUtils.AuthorAttributionLabel.POST,
                discussionResponse,new Runnable() {
                    @Override
                    public void run() {
                        router.showUserProfile(getActivity(), discussionResponse.getAuthor());
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
        commentBody.setThreadId(discussionResponse.getThreadId());
        commentBody.setParentId(discussionResponse.getIdentifier());

        createCommentTask = new CreateCommentTask(getActivity(), commentBody) {
            @Override
            public void onSuccess(@NonNull DiscussionComment thread) {
                logger.debug(thread.toString());
                EventBus.getDefault().post(new DiscussionCommentPostedEvent(thread, discussionResponse));
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
