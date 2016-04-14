package tw.openedu.www.test.feature;

import tw.openedu.www.base.MainApplication;
import tw.openedu.www.core.EdxEnvironment;
import tw.openedu.www.module.prefs.PrefManager;
import tw.openedu.www.services.ServiceManager;
import tw.openedu.www.test.feature.data.TestValues;
import tw.openedu.www.test.feature.interactor.AppInteractor;
import org.junit.Test;

public class LaunchFeatureTest extends FeatureTest {

    @Test
    public void whenAppLaunched_withAnonymousUser_landingScreenIsShown() {
        new AppInteractor()
                .launchApp()
                .observeLandingScreen();
    }

    @Test
    public void whenAppLaunched_withValidUser_myCoursesScreenIsShown() throws Exception {
        final MainApplication application = MainApplication.instance();
        final EdxEnvironment environment = application.getInjector().getInstance(EdxEnvironment.class);
        ServiceManager api = environment.getServiceManager();
        //Get and cache user login data before app launch
        api.auth(TestValues.ACTIVE_USER_CREDENTIALS.email, TestValues.ACTIVE_USER_CREDENTIALS.password);
        api.getProfile();

        new AppInteractor()
                .launchApp()
                .observeMyCoursesScreen();
    }

    @Test
    public void whenAppLaunched_withInvalidAuthToken_logInScreenIsShown() {
        final MainApplication application = MainApplication.instance();
        PrefManager pref = new PrefManager(application, PrefManager.Pref.LOGIN);
        //Skip login if any profile is set
        pref.put(PrefManager.Key.PROFILE_JSON, TestValues.DUMMY_PROFILE_JSON);
        pref.put(PrefManager.Key.AUTH_JSON, TestValues.INVALID_AUTH_JSON);
        new AppInteractor()
                .launchApp()
                .observeLogInScreen()
                .navigateBack()
                .observeLandingScreen();
    }
}
