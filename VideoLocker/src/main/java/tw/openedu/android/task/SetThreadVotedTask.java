package tw.openedu.android.task;

import android.content.Context;
import android.support.annotation.NonNull;

import tw.openedu.android.discussion.DiscussionThread;

public abstract class SetThreadVotedTask extends Task<DiscussionThread> {
    @NonNull
    private final DiscussionThread thread;
    @NonNull
    private final boolean voted;

    public SetThreadVotedTask(@NonNull Context context,
                              @NonNull DiscussionThread thread, boolean voted) {
        super(context, Type.USER_INITIATED);
        this.thread = thread;
        this.voted = voted;
    }

    public DiscussionThread call() throws Exception {
        return environment.getDiscussionAPI().setThreadVoted(thread, voted);
    }
}
