package tw.openedu.www.task;

import android.content.Context;
import android.support.annotation.NonNull;

import tw.openedu.www.discussion.DiscussionComment;
import tw.openedu.www.discussion.ResponseBody;

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
