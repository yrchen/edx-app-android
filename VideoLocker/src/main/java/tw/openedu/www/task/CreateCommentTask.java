package tw.openedu.www.task;

import android.content.Context;
import android.support.annotation.NonNull;

import tw.openedu.www.discussion.CommentBody;
import tw.openedu.www.discussion.DiscussionComment;

public abstract class CreateCommentTask extends
Task<DiscussionComment> {

    @NonNull
    CommentBody thread;

    public CreateCommentTask(@NonNull Context context, @NonNull CommentBody thread) {
        super(context);
        this.thread = thread;
    }



    public DiscussionComment call( ) throws Exception{
        return environment.getDiscussionAPI().createComment(thread);
    }
}
