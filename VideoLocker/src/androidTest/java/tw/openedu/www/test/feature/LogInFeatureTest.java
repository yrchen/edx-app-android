package tw.openedu.www.test.feature;

import tw.openedu.www.test.feature.data.TestValues;
import tw.openedu.www.test.feature.interactor.AppInteractor;
import org.junit.Test;

public class LogInFeatureTest extends FeatureTest {

    @Test
    public void afterEmailLogIn_withActiveAccount_myCoursesScreenIsDisplayed() {
        new AppInteractor()
                .launchApp()
                .observeLandingScreen()
                .navigateToLogInScreen()
                .logIn(TestValues.ACTIVE_USER_CREDENTIALS)
                .observeMyCoursesScreen();
    }
}
