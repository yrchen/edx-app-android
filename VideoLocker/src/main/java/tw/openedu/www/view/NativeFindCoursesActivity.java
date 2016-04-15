package tw.openedu.www.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import tw.openedu.www.R;
import tw.openedu.www.base.BaseSingleFragmentActivity;
import tw.openedu.www.module.analytics.ISegment;
import tw.openedu.www.view.dialog.NativeFindCoursesFragment;

public class NativeFindCoursesActivity extends BaseSingleFragmentActivity {

    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, NativeFindCoursesActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.find_courses_title));
        environment.getSegment().trackScreenView(ISegment.Screens.FIND_COURSES);
        configureDrawer();
    }

    @Override
    public Fragment getFirstFragment() {
        return new NativeFindCoursesFragment();
    }
}
