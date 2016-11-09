package tw.openedu.android.test.screenshot.test;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.facebook.testing.screenshot.Screenshot;

import tw.openedu.android.R;
import tw.openedu.android.view.LaunchActivity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LaunchScreenshotTests {

    @Rule
    public ActivityTestRule<LaunchActivity> mActivityRule =
            new ActivityTestRule<>(LaunchActivity.class);

    @Test
    public void testScreenshot_recordLaunchActivity() throws Throwable {
        View view = mActivityRule.getActivity().findViewById(R.id.root_view);
        Screenshot.snap(view).record();
    }
}
