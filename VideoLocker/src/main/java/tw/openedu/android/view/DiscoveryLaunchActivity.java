package tw.openedu.android.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.inject.Inject;

import tw.openedu.android.R;
import tw.openedu.android.base.BaseFragmentActivity;
import tw.openedu.android.databinding.ActivityDiscoveryLaunchBinding;
import tw.openedu.android.module.analytics.ISegment;
import tw.openedu.android.module.prefs.LoginPrefs;

public class DiscoveryLaunchActivity extends BaseFragmentActivity {

    @Inject
    LoginPrefs loginPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityDiscoveryLaunchBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_discovery_launch);
        binding.discoverCourses.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                environment.getSegment().trackDiscoverCoursesClicked();
                environment.getRouter().showFindCourses(DiscoveryLaunchActivity.this);
            }
        });
        if (environment.getConfig().getCourseDiscoveryConfig().isWebviewCourseDiscoveryEnabled()
                && !TextUtils.isEmpty(environment.getConfig().getCourseDiscoveryConfig().getWebViewConfig().getExploreSubjectsUrl())) {
            binding.exploreSubjects.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    environment.getSegment().trackExploreSubjectsClicked();
                    environment.getRouter().showExploreSubjects(DiscoveryLaunchActivity.this);
                }
            });
        } else {
            // Explore Subjects is only supported for web course discovery
            binding.exploreSubjects.setVisibility(View.INVISIBLE);
        }
        environment.getSegment().trackScreenView(ISegment.Screens.LAUNCH_ACTIVITY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (environment.getLoginPrefs().getUsername() != null) {
            finish();
            environment.getRouter().showMyCourses(this);
        }
    }

    @Override
    protected boolean createOptionsMenu(Menu menu) {
        return false; // Disable menu inherited from BaseFragmentActivity
    }
}
