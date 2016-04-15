package tw.openedu.www.task;

import android.content.Context;
import android.support.annotation.NonNull;

import tw.openedu.www.discussion.CourseTopics;

public abstract class GetTopicListTask extends
Task<CourseTopics> {

    @NonNull
    String courseId;

    public GetTopicListTask(@NonNull Context context, @NonNull String courseId) {
        super(context);
        this.courseId = courseId;
    }



    public CourseTopics call( ) throws Exception{
        return environment.getDiscussionAPI().getTopicList(courseId);
    }
}
