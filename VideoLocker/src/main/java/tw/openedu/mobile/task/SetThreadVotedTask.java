package tw.openedu.mobile.task;

import android.content.Context;
import android.support.annotation.NonNull;

import tw.openedu.mobile.discussion.DiscussionThread;

public abstract class SetThreadVotedTask extends Task<DiscussionThread> {
    private final DiscussionThread thread;
    private final boolean voted;

    public SetThreadVotedTask(@NonNull Context context,
                              @NonNull DiscussionThread thread, boolean voted) {
        super(context);
        this.thread = thread;
        this.voted = voted;
    }

    public DiscussionThread call() throws Exception {
        return environment.getDiscussionAPI().setThreadVoted(thread, voted);
    }
}
