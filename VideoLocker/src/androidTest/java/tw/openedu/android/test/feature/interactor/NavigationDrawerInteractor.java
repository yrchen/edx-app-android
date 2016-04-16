package tw.openedu.android.test.feature.interactor;

import tw.openedu.android.test.feature.matcher.ActionBarMatcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;

public class NavigationDrawerInteractor {
    public static NavigationDrawerInteractor open() {
        onView(allOf(ActionBarMatcher.isInActionBar(), withContentDescription(tw.openedu.android.R.string.label_open_drawer))).perform(click());
        return new NavigationDrawerInteractor();
    }

    public LogInScreenInteractor logOut() {
        onView(withText(tw.openedu.android.R.string.logout)).perform(click());
        return new LogInScreenInteractor();
    }
}
