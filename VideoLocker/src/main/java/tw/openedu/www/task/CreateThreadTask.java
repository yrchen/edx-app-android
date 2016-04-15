package tw.openedu.www.task;

import android.content.Context;
import android.support.annotation.NonNull;

import tw.openedu.www.discussion.DiscussionThread;
import tw.openedu.www.discussion.ThreadBody;

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
