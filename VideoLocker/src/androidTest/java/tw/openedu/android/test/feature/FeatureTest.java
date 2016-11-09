package tw.openedu.android.test.feature;

import android.support.test.runner.AndroidJUnit4;

import tw.openedu.android.base.MainApplication;
import tw.openedu.android.core.EdxEnvironment;
import tw.openedu.android.module.prefs.PrefManager;
import org.junit.Before;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public abstract class FeatureTest {
    protected EdxEnvironment environment;

    @Before
    public void setup() {
        // Ensure we are not logged in
        final MainApplication application = MainApplication.instance();
        environment = application.getInjector().getInstance(EdxEnvironment.class);
        new PrefManager(application, PrefManager.Pref.LOGIN).clearAuth();
        environment.getSegment().resetIdentifyUser();
    }
}
