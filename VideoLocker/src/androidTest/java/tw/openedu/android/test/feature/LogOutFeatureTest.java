package tw.openedu.android.test.feature;

import tw.openedu.android.test.feature.data.TestValues;
import tw.openedu.android.test.feature.interactor.AppInteractor;
import org.junit.Test;

public class LogOutFeatureTest extends FeatureTest {

    @Test
    public void afterLogOut_withActiveAccount_logInScreenIsDisplayed() {
        new AppInteractor()
                .launchApp()
                .observeLandingScreen()
                .navigateToLogInScreen()
                .logIn(TestValues.ACTIVE_USER_CREDENTIALS)
                .openNavigationDrawer()
                .logOut()
                .observeLogInScreen();
    }
}
