package tw.openedu.android.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import tw.openedu.android.R;
import tw.openedu.android.base.BaseSingleFragmentActivity;
import tw.openedu.android.module.analytics.ISegment;
import tw.openedu.android.view.dialog.NativeFindCoursesFragment;

public class NativeFindCoursesActivity extends BaseSingleFragmentActivity {

    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, NativeFindCoursesActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.find_courses_title));
        environment.getSegment().trackScreenView(ISegment.Screens.FIND_COURSES);
        if (environment.getLoginPrefs().getUsername() != null) {
            configureDrawer();
        }
    }

    @Override
    public Fragment getFirstFragment() {
        return new NativeFindCoursesFragment();
    }
}
