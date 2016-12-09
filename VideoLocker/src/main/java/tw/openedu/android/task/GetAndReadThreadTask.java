package tw.openedu.android.task;

import android.content.Context;
import android.support.annotation.NonNull;

import tw.openedu.android.discussion.DiscussionThread;
import tw.openedu.android.discussion.DiscussionThreadUpdatedEvent;
import tw.openedu.android.http.HttpException;

import de.greenrobot.event.EventBus;

public class GetAndReadThreadTask extends Task<DiscussionThread> {
    private final DiscussionThread thread;

    public GetAndReadThreadTask(@NonNull Context context,
                                @NonNull DiscussionThread thread) {
        super(context);
        this.thread = thread;
    }

    @Override
    public DiscussionThread call() throws HttpException {
        return environment.getDiscussionAPI().setThreadRead(thread, true);
    }

    @Override
    protected void onSuccess(DiscussionThread discussionThread) {
        EventBus.getDefault().post(new DiscussionThreadUpdatedEvent(discussionThread));
    }
}
