package tw.openedu.www.view;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.inject.Inject;

import tw.openedu.www.R;
import tw.openedu.www.event.EnrolledInCourseEvent;
import tw.openedu.www.exception.AuthException;
import tw.openedu.www.http.Api;
import tw.openedu.www.loader.AsyncTaskResult;
import tw.openedu.www.loader.CoursesAsyncLoader;
import tw.openedu.www.model.api.EnrolledCoursesResponse;
import tw.openedu.www.module.analytics.ISegment;
import tw.openedu.www.module.prefs.PrefManager;
import tw.openedu.www.user.UserAPI;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class MyCourseListTabFragment extends CourseListTabFragment {

    private final int MY_COURSE_LOADER_ID = 0x905000;

    protected TextView noCourseText;
    private boolean refreshOnResume;

    @Inject
    private UserAPI userAPI;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        environment.getSegment().trackScreenView(ISegment.Screens.MY_COURSES);
        EventBus.getDefault().register(this);
    }

    @Override
    public void handleCourseClick(EnrolledCoursesResponse model) {
        environment.getRouter().showCourseDashboardTabs(getActivity(), environment.getConfig(), model, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        noCourseText = (TextView) view.findViewById(R.id.no_course_tv);
        return view;
    }

    protected void loadData(boolean showProgress) {
        //This Show progress is used to display the progress when a user enrolls in a Course
        if (showProgress && progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
        getLoaderManager().restartLoader(MY_COURSE_LOADER_ID, null, this);
    }

    @Override
    protected int getViewResourceID() {
        return R.layout.fragment_my_course_list_tab;
    }

    @Override
    public Loader<AsyncTaskResult<List<EnrolledCoursesResponse>>> onCreateLoader(int i, Bundle bundle) {
        return new CoursesAsyncLoader(getActivity(), environment);
    }

    @Override
    public void onLoadFinished(Loader<AsyncTaskResult<List<EnrolledCoursesResponse>>> asyncTaskResultLoader, AsyncTaskResult<List<EnrolledCoursesResponse>> result) {

        progressBar.setVisibility(View.GONE);

        if (result == null) {
            logger.warn("result is found null, was expecting non-null");
            return;
        }

        myCourseList.setVisibility(View.VISIBLE);

        if (result.getEx() != null) {
            logger.error(result.getEx());
            if (result.getEx() instanceof AuthException) {
                PrefManager prefs = new PrefManager(getActivity(), PrefManager.Pref.LOGIN);
                prefs.clearAuth();
                getActivity().finish();
            } else if (result.getEx() instanceof Api.HttpAuthRequiredException) {
                environment.getRouter().forceLogout(
                        getContext(),
                        environment.getSegment(),
                        environment.getNotificationDelegate());
            }
        } else if (result.getResult() != null) {
            invalidateSwipeFunctionality();

            ArrayList<EnrolledCoursesResponse> newItems = new ArrayList<EnrolledCoursesResponse>(result.getResult());

            ((MyCoursesListActivity) getActivity()).updateDatabaseAfterDownload(newItems);

            if (result.getResult().size() == 0) {
                adapter.clear();
            } else {
                adapter.setItems(newItems);
                adapter.notifyDataSetChanged();
            }

        } else {
            adapter.clear();
        }
    }

    @Override
    public void onLoaderReset(Loader<AsyncTaskResult<List<EnrolledCoursesResponse>>> asyncTaskResultLoader) {
        adapter.clear();
        adapter.notifyDataSetChanged();
        myCourseList.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(EnrolledInCourseEvent event) {
        refreshOnResume = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (refreshOnResume) {
            loadData(true);
            refreshOnResume = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
