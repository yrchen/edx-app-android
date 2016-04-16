package tw.openedu.android.task;

import android.content.Context;
import android.support.annotation.NonNull;

import tw.openedu.android.discussion.DiscussionThread;
import tw.openedu.android.discussion.ThreadBody;

public abstract class CreateThreadTask extends
Task<DiscussionThread> {

    @NonNull
    ThreadBody thread;

    public CreateThreadTask(@NonNull Context context, @NonNull ThreadBody thread) {
        super(context);
        this.thread = thread;
    }



    public DiscussionThread call( ) throws Exception{
        return environment.getDiscussionAPI().createThread(thread);
    }
}
