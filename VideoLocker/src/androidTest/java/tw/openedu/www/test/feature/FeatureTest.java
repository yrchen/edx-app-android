package tw.openedu.www.test.feature;

import android.support.test.runner.AndroidJUnit4;

import tw.openedu.www.base.MainApplication;
import tw.openedu.www.core.EdxEnvironment;
import tw.openedu.www.module.prefs.PrefManager;
import org.junit.Before;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public abstract class FeatureTest {
    @Before
    public void setup() {
        // Ensure we are not logged in
        final MainApplication application = MainApplication.instance();
        final EdxEnvironment environment = application.getInjector().getInstance(EdxEnvironment.class);
        new PrefManager(application, PrefManager.Pref.LOGIN).clearAuth();
        environment.getSegment().resetIdentifyUser();
    }
}
