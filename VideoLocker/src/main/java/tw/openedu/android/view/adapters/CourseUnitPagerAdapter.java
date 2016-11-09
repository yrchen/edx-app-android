package tw.openedu.android.view.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import tw.openedu.android.model.api.EnrolledCoursesResponse;
import tw.openedu.android.model.course.BlockType;
import tw.openedu.android.model.course.CourseComponent;
import tw.openedu.android.model.course.DiscussionBlockModel;
import tw.openedu.android.model.course.HtmlBlockModel;
import tw.openedu.android.model.course.VideoBlockModel;
import tw.openedu.android.util.Config;
import tw.openedu.android.view.CourseUnitDiscussionFragment;
import tw.openedu.android.view.CourseUnitEmptyFragment;
import tw.openedu.android.view.CourseUnitFragment;
import tw.openedu.android.view.CourseUnitMobileNotSupportedFragment;
import tw.openedu.android.view.CourseUnitOnlyOnYoutubeFragment;
import tw.openedu.android.view.CourseUnitVideoFragment;
import tw.openedu.android.view.CourseUnitWebViewFragment;

import java.util.List;

public class CourseUnitPagerAdapter extends FragmentStatePagerAdapter {
    private Config config;
    private List<CourseComponent> unitList;
    private EnrolledCoursesResponse courseData;
    private CourseUnitFragment.HasComponent callback;

    public CourseUnitPagerAdapter(FragmentManager manager,
                                  Config config,
                                  List<CourseComponent> unitList,
                                  EnrolledCoursesResponse courseData,
                                  CourseUnitFragment.HasComponent callback) {
        super(manager);
        this.config = config;
        this.unitList = unitList;
        this.courseData = courseData;
        this.callback = callback;
    }

    public CourseComponent getUnit(int pos) {
        if (pos >= unitList.size())
            pos = unitList.size() - 1;
        if (pos < 0)
            pos = 0;
        return unitList.get(pos);
    }

    @Override
    public Fragment getItem(int pos) {
        CourseComponent unit = getUnit(pos);
        CourseUnitFragment unitFragment;
        //FIXME - for the video, let's ignore studentViewMultiDevice for now
        if (unit instanceof VideoBlockModel &&
                ((VideoBlockModel) unit).getData().encodedVideos.getPreferredVideoInfo() != null) {
            unitFragment = CourseUnitVideoFragment.newInstance((VideoBlockModel) unit);
        } else if (unit instanceof VideoBlockModel &&
                ((VideoBlockModel) unit).getData().encodedVideos.getYoutubeVideoInfo() != null) {
            unitFragment = CourseUnitOnlyOnYoutubeFragment.newInstance(unit);
        } else if (config.isDiscussionsEnabled() && unit instanceof DiscussionBlockModel) {
            unitFragment = CourseUnitDiscussionFragment.newInstance(unit, courseData);
        } else if (!unit.isMultiDevice()) {
            unitFragment = CourseUnitMobileNotSupportedFragment.newInstance(unit);
        } else if (unit.getType() != BlockType.VIDEO &&
                unit.getType() != BlockType.HTML &&
                unit.getType() != BlockType.OTHERS &&
                unit.getType() != BlockType.DISCUSSION &&
                unit.getType() != BlockType.PROBLEM) {
            unitFragment = CourseUnitEmptyFragment.newInstance(unit);
        } else if (unit instanceof HtmlBlockModel) {
            unitFragment = CourseUnitWebViewFragment.newInstance((HtmlBlockModel) unit);
        }
        //fallback
        else {
            unitFragment = CourseUnitMobileNotSupportedFragment.newInstance(unit);
        }

        unitFragment.setHasComponentCallback(callback);

        return unitFragment;
    }

    @Override
    public int getCount() {
        return unitList.size();
    }
}
