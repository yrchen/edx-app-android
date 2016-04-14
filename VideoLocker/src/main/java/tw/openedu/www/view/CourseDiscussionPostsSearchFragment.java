package tw.openedu.www.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SearchView;

import tw.openedu.www.R;
import tw.openedu.www.discussion.DiscussionThread;
import tw.openedu.www.model.Page;
import tw.openedu.www.task.SearchThreadListTask;
import tw.openedu.www.util.ResourceUtil;
import tw.openedu.www.util.SoftKeyboardUtil;
import tw.openedu.www.view.adapters.InfiniteScrollUtils;
import tw.openedu.www.view.common.MessageType;
import tw.openedu.www.view.common.TaskProcessCallback;

import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

public class CourseDiscussionPostsSearchFragment extends CourseDiscussionPostsBaseFragment {

    @InjectExtra(value = Router.EXTRA_SEARCH_QUERY, optional = true)
    private String searchQuery;

    @InjectView(R.id.discussion_topics_searchview)
    private SearchView discussionTopicsSearchView;

    private SearchThreadListTask searchThreadListTask;

    private int nextPage = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discussion_search_posts, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        discussionTopicsSearchView.setQuery(searchQuery, false);
        discussionTopicsSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query == null || query.trim().isEmpty())
                    return false;
                SoftKeyboardUtil.hide(getActivity());
                searchQuery = query;
                nextPage = 1;
                controller.reset();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void loadNextPage(@NonNull final InfiniteScrollUtils.PageLoadCallback<DiscussionThread> callback) {
        ((TaskProcessCallback) getActivity()).onMessage(MessageType.EMPTY, "");
        if (searchThreadListTask != null) {
            searchThreadListTask.cancel(true);
        }
        searchThreadListTask = new SearchThreadListTask(getActivity(), courseData.getCourse().getId(), searchQuery, nextPage) {
            @Override
            public void onSuccess(Page<DiscussionThread> threadsPage) {
                ++nextPage;
                callback.onPageLoaded(threadsPage);
                Activity activity = getActivity();
                if (activity instanceof TaskProcessCallback) {
                    if (discussionPostsAdapter.getCount() == 0) {
                        String escapedTitle = TextUtils.htmlEncode(searchQuery);
                        String resultsText = ResourceUtil.getFormattedString(
                                getContext().getResources(),
                                R.string.forum_no_results_for_search_query,
                                "search_query",
                                escapedTitle
                        ).toString();
                        // CharSequence styledResults = Html.fromHtml(resultsText);
                        ((TaskProcessCallback) activity).onMessage(MessageType.ERROR, resultsText);
                    } else {
                        ((TaskProcessCallback) activity).onMessage(MessageType.EMPTY, "");
                    }
                }
            }

            @Override
            protected void onException(Exception ex) {
                // Don't display any error message if we're doing a silent
                // refresh, as that would be confusing to the user.
                if (!callback.isRefreshingSilently()) {
                    super.onException(ex);
                }
                callback.onError();
                nextPage = 1;
            }
        };
        searchThreadListTask.setProgressCallback(null);
        searchThreadListTask.execute();

    }

    @Override
    public void onResume() {
        super.onResume();
        discussionTopicsSearchView.post(new Runnable() {
            @Override
            public void run() {
                discussionTopicsSearchView.clearFocus();
            }
        });
    }

    @Override
    public void onRestart() {
        /*
        If the activity/fragment needs to be reinstantiated upon restoration,
        then in some cases the onRestart() callback maybe invoked before view
        initialization, and thus the controller might not be initialized, and
        therefore we need to guard this with a null check.
         */
        if (controller != null) {
            nextPage = 1;
            controller.resetSilently();
        }
    }

    @Override
    public void onItemClick(DiscussionThread thread, AdapterView<?> parent, View view,
                            int position, long id) {}
}
