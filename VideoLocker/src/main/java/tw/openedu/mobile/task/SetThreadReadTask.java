package tw.openedu.mobile.task;

import android.content.Context;
import android.support.annotation.NonNull;

import tw.openedu.mobile.discussion.DiscussionThread;
import tw.openedu.mobile.discussion.DiscussionThreadUpdatedEvent;
import tw.openedu.mobile.http.RetroHttpException;

import de.greenrobot.event.EventBus;

public class SetThreadReadTask extends Task<DiscussionThread> {
    private final DiscussionThread thread;
    private final boolean read;

    public SetThreadReadTask(@NonNull Context context,
                             @NonNull DiscussionThread thread, boolean read) {
        super(context);
        this.thread = thread;
        this.read = read;
    }

    @Override
    public DiscussionThread call() throws RetroHttpException {
        return environment.getDiscussionAPI().setThreadRead(thread, read);
    }

    @Override
    protected void onSuccess(DiscussionThread discussionThread) {
        EventBus.getDefault().post(new DiscussionThreadUpdatedEvent(discussionThread));
    }
}
