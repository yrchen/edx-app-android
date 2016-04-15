package tw.openedu.www.view;

import android.os.Bundle;

import tw.openedu.www.base.BaseFragmentActivity;

public class SplashActivity extends BaseFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        finish();

        if (!isTaskRoot()) {
            return; // This stops from opening again from the Splash screen when minimized
        }

        if (environment.getUserPrefs().getProfile() != null) {
            environment.getRouter().showMyCourses(SplashActivity.this);
        } else {
            environment.getRouter().showLaunchScreen(SplashActivity.this);
        }
    }
}
