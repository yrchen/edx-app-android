package tw.openedu.www.test.screenshot;

import android.test.InstrumentationTestCase;
import android.view.LayoutInflater;
import android.view.View;

import com.facebook.testing.screenshot.Screenshot;

import tw.openedu.www.R;

public class LaunchScreenshotTests extends InstrumentationTestCase {

    public void test_takeScreenshotOf_launchScreen() throws Throwable {
        LayoutInflater inflater = LayoutInflater.from(getInstrumentation().getTargetContext());
        View view = inflater.inflate(R.layout.activity_launch, null, false);
        EdXScreenshotUtils.setDefaultTestResolution(view);
        Screenshot.snap(view)
                .record();
    }
}
