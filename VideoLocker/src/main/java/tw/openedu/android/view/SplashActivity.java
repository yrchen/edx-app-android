package tw.openedu.android.view;

import android.app.Activity;
import android.os.Bundle;

import tw.openedu.android.base.MainApplication;
import tw.openedu.android.core.IEdxEnvironment;

// We are extending the normal Activity class here so that we can use Theme.NoDisplay, which does not support AppCompat activities
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        finish();

        if (!isTaskRoot()) {
            return; // This stops from opening again from the Splash screen when minimized
        }

        final IEdxEnvironment environment = MainApplication.getEnvironment(this);
        if (environment.getUserPrefs().getProfile() != null) {
            environment.getRouter().showMyCourses(SplashActivity.this);
        } else {
            environment.getRouter().showLaunchScreen(SplashActivity.this);
        }
    }
}
