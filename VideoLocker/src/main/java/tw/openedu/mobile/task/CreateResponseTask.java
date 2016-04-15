package tw.openedu.mobile.task;

import android.content.Context;
import android.support.annotation.NonNull;

import tw.openedu.mobile.discussion.DiscussionComment;
import tw.openedu.mobile.discussion.ResponseBody;

public abstract class CreateResponseTask extends
Task<DiscussionComment> {

    @NonNull
    ResponseBody thread;

    public CreateResponseTask(@NonNull Context context, @NonNull ResponseBody thread) {
        super(context);
        this.thread = thread;
    }



    public DiscussionComment call( ) throws Exception{
        return environment.getDiscussionAPI().createResponse(thread);
    }
}
