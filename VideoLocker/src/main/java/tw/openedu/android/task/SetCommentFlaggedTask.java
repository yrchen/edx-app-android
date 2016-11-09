package tw.openedu.android.task;

import android.content.Context;
import android.support.annotation.NonNull;

import tw.openedu.android.discussion.DiscussionComment;

public abstract class SetCommentFlaggedTask extends Task<DiscussionComment> {
    @NonNull
    private final DiscussionComment comment;
    @NonNull
    private final boolean flagged;

    public SetCommentFlaggedTask(@NonNull Context context,
                                 @NonNull DiscussionComment comment, boolean flagged) {
        super(context, Type.USER_INITIATED);
        this.comment = comment;
        this.flagged = flagged;
    }

    public DiscussionComment call() throws Exception {
        return environment.getDiscussionAPI().setCommentFlagged(comment, flagged);
    }
}
