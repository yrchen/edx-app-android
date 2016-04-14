package tw.openedu.www.test.feature.interactor;

import tw.openedu.www.test.feature.matcher.ActionBarMatcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;

public class NavigationDrawerInteractor {
    public static NavigationDrawerInteractor open() {
        onView(allOf(ActionBarMatcher.isInActionBar(), withContentDescription(tw.openedu.www.R.string.label_open_drawer))).perform(click());
        return new NavigationDrawerInteractor();
    }

    public LogInScreenInteractor logOut() {
        onView(withText(tw.openedu.www.R.string.logout)).perform(click());
        return new LogInScreenInteractor();
    }
}
