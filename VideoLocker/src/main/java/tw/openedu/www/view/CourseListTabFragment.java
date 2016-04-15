package tw.openedu.www.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.inject.Inject;

import tw.openedu.www.core.IEdxEnvironment;
import tw.openedu.www.interfaces.NetworkObserver;
import tw.openedu.www.interfaces.NetworkSubject;
import tw.openedu.www.loader.AsyncTaskResult;
import tw.openedu.www.logger.Logger;
import tw.openedu.www.model.api.EnrolledCoursesResponse;
import tw.openedu.www.module.prefs.PrefManager;
import tw.openedu.www.util.NetworkUtil;
import tw.openedu.www.util.ViewAnimationUtil;
import tw.openedu.www.view.adapters.MyCourseAdapter;

import java.util.List;

import tw.openedu.www.base.BaseFragment;

public abstract class CourseListTabFragment extends BaseFragment implements NetworkObserver, LoaderManager.LoaderCallbacks<AsyncTaskResult<List<EnrolledCoursesResponse>>> {

    protected MyCourseAdapter adapter;

    protected SwipeRefreshLayout swipeLayout;
    protected LinearLayout offlinePanel;
    protected View offlineBar;
    protected ProgressBar progressBar;

    protected PrefManager pmFeatures;

    @Inject
    protected IEdxEnvironment environment;

    protected ListView myCourseList;

    protected Logger logger = new Logger(getClass().getSimpleName());

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof NetworkSubject) {
            ((NetworkSubject) activity).registerNetworkObserver(this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (getActivity() instanceof NetworkSubject) {
            ((NetworkSubject) getActivity()).unregisterNetworkObserver(this);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pmFeatures = new PrefManager(getActivity(), PrefManager.Pref.FEATURES);
        adapter = new MyCourseAdapter(getActivity(), environment) {

            @Override
            public void onItemClicked(EnrolledCoursesResponse model) {
                handleCourseClick(model);
            }

            @Override
            public void onAnnouncementClicked(EnrolledCoursesResponse model) {
                environment.getRouter().showCourseDashboardTabs(getActivity(), environment.getConfig(), model, true);
            }
        };

        loadData(true);
    }

    public abstract void handleCourseClick(EnrolledCoursesResponse model);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getViewResourceID(), container, false);

        offlineBar = view.findViewById(tw.openedu.www.R.id.offline_bar);
        offlinePanel = (LinearLayout) view.findViewById(tw.openedu.www.R.id.offline_panel);
        progressBar = (ProgressBar) view.findViewById(tw.openedu.www.R.id.loading_indicator);
        swipeLayout = (SwipeRefreshLayout) view.findViewById(tw.openedu.www.R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Hide the progress bar as swipe functionality has its own Progress indicator
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
                loadData(false);
            }
        });

        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                tw.openedu.www.R.color.grey_act_background, tw.openedu.www.R.color.grey_act_background,
                tw.openedu.www.R.color.grey_act_background);

        myCourseList = (ListView) view.findViewById(tw.openedu.www.R.id.my_course_list);
        //As per docs, the footer needs to be added before adapter is set to the ListView
        setupFooter(myCourseList);

        myCourseList.setAdapter(adapter);
        myCourseList.setOnItemClickListener(adapter);

        if (!(NetworkUtil.isConnected(getActivity()))) {
            onOffline();
        } else {
            onOnline();
        }

        return view;
    }

    protected abstract int getViewResourceID();

    protected abstract void loadData(boolean showProgress);

    protected void invalidateSwipeFunctionality() {
        swipeLayout.setRefreshing(false);
    }

    @Override
    public void onOnline() {
        if (offlineBar != null && swipeLayout != null) {
            offlineBar.setVisibility(View.GONE);
            hideOfflinePanel();
            swipeLayout.setEnabled(true);
        }
    }

    public void hideOfflinePanel() {
        ViewAnimationUtil.stopAnimation(offlinePanel);
        if (offlinePanel.getVisibility() == View.VISIBLE) {
            offlinePanel.setVisibility(View.GONE);
        }
    }

    public void showOfflinePanel() {
        ViewAnimationUtil.showMessageBar(offlinePanel);
    }

    @Override
    public void onOffline() {
        offlineBar.setVisibility(View.VISIBLE);
        showOfflinePanel();
        //Disable swipe functionality and hide the loading view
        swipeLayout.setEnabled(false);
        invalidateSwipeFunctionality();
    }

    @Override
    public void onStop() {
        super.onStop();
        hideOfflinePanel();
    }

    /**
     * Adds a footer view to the list, which has "FIND A COURSE" button.
     *
     * @param myCourseList - ListView
     */
    private void setupFooter(ListView myCourseList) {
        try {
            View footer = LayoutInflater.from(getActivity()).inflate(tw.openedu.www.R.layout.panel_find_course, null);
            myCourseList.addFooterView(footer, null, false);
            Button course_btn = (Button) footer.findViewById(tw.openedu.www.R.id.course_btn);
            course_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        environment.getSegment().trackUserFindsCourses();
                    } catch (Exception e) {
                        logger.error(e);
                    }

                    environment.getRouter().showFindCourses(getActivity());
                }
            });

            TextView courseNotListedTv = (TextView) footer.findViewById(tw.openedu.www.R.id.course_not_listed_tv);
            courseNotListedTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCourseNotListedDialog();
                }
            });
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public void showCourseNotListedDialog() {
        environment.getRouter().showWebViewDialog((getActivity()), getString(tw.openedu.www.R.string.course_not_listed_file_name), null);
    }
}
