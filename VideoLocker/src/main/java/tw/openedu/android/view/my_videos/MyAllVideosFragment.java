package tw.openedu.android.view.my_videos;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.inject.Inject;

import tw.openedu.android.R;
import tw.openedu.android.base.BaseFragment;
import tw.openedu.android.core.IEdxEnvironment;
import tw.openedu.android.logger.Logger;
import tw.openedu.android.model.api.EnrolledCoursesResponse;
import tw.openedu.android.module.analytics.ISegment;
import tw.openedu.android.module.storage.DownloadCompletedEvent;
import tw.openedu.android.module.storage.DownloadedVideoDeletedEvent;
import tw.openedu.android.task.GetAllDownloadedVideosTask;
import tw.openedu.android.util.AppConstants;
import tw.openedu.android.view.Router;
import tw.openedu.android.view.VideoListActivity;
import tw.openedu.android.view.adapters.MyAllVideoCourseAdapter;

import java.util.List;

import de.greenrobot.event.EventBus;

public class MyAllVideosFragment extends BaseFragment {

    private MyAllVideoCourseAdapter myCoursesAdaptor;
    protected final Logger logger = new Logger(getClass().getName());
    private GetAllDownloadedVideosTask getAllDownloadedVideosTask;

    @Inject
    protected IEdxEnvironment environment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        environment.getSegment().trackScreenView(ISegment.Screens.MY_VIDEOS_ALL);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_all_videos, container, false);

        ListView myCourseList = (ListView) view.findViewById(R.id.videos_course_list);
        myCourseList.setEmptyView(view.findViewById(R.id.empty_list_view));

        myCoursesAdaptor = new MyAllVideoCourseAdapter(getActivity(), environment) {
            @Override
            public void onItemClicked(EnrolledCoursesResponse model) {
                AppConstants.myVideosDeleteMode = false;
                Intent videoIntent = new Intent(getActivity(), VideoListActivity.class);
                videoIntent.putExtra(Router.EXTRA_COURSE_DATA, model);
                videoIntent.putExtra("FromMyVideos", true);
                startActivity(videoIntent);
            }
        };

        myCourseList.setAdapter(myCoursesAdaptor);
        myCourseList.setOnItemClickListener(myCoursesAdaptor);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        addMyAllVideosData();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getAllDownloadedVideosTask != null) {
            getAllDownloadedVideosTask.cancel(true);
            getAllDownloadedVideosTask = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    protected void addMyAllVideosData() {
        if (myCoursesAdaptor == null) {
            return;
        }
        if (getAllDownloadedVideosTask != null) {
            getAllDownloadedVideosTask.cancel(true);
        }
        getAllDownloadedVideosTask = new GetAllDownloadedVideosTask(getActivity()) {
            @Override
            protected void onSuccess(List<EnrolledCoursesResponse> enrolledCoursesResponses) throws Exception {
                super.onSuccess(enrolledCoursesResponses);
                myCoursesAdaptor.clear();
                if (enrolledCoursesResponses != null) {
                    for (EnrolledCoursesResponse m : enrolledCoursesResponses) {
                        if (m.isIs_active()) {
                            myCoursesAdaptor.add(m);
                        }
                    }
                }
            }
        };
        getAllDownloadedVideosTask.execute();
    }

    public void onEventMainThread(DownloadedVideoDeletedEvent e) {
        addMyAllVideosData();
    }
    public void onEventMainThread(DownloadCompletedEvent e) {
        addMyAllVideosData();
    }
}
