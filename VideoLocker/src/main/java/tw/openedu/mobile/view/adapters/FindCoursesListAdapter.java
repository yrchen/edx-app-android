package tw.openedu.mobile.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;

import tw.openedu.mobile.core.IEdxEnvironment;
import tw.openedu.mobile.course.CourseDetail;
import tw.openedu.mobile.util.images.CourseCardUtils;


public abstract class FindCoursesListAdapter extends BaseListAdapter<CourseDetail> {
    private long lastClickTime;

    public FindCoursesListAdapter(Context context, IEdxEnvironment environment) {
        super(context, CourseCardViewHolder.LAYOUT, environment);
        lastClickTime = 0;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void render(BaseViewHolder tag, final CourseDetail courseDetail) {
        final CourseCardViewHolder holder = (CourseCardViewHolder) tag;
        holder.setPadding(tag.position == 0);
        holder.setCourseTitle(courseDetail.name);
        holder.setCourseImage(courseDetail.media.course_image.getUri(environment.getConfig().getApiHostURL()));
        holder.setDescription(CourseCardUtils.getDescription(courseDetail.org, courseDetail.number, null),
                CourseCardUtils.getFormattedDate(getContext(), courseDetail));
    }

    @Override
    public BaseViewHolder getTag(View convertView) {
        return new CourseCardViewHolder(convertView);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                            long arg3) {
        //This time is checked to avoid taps in quick succession
        final long currentTime = SystemClock.elapsedRealtime();
        if (currentTime - lastClickTime > MIN_CLICK_INTERVAL) {
            lastClickTime = currentTime;
            CourseDetail model = getItem(position);
            if (model != null) onItemClicked(model);
        }
    }

    public abstract void onItemClicked(CourseDetail model);
}
