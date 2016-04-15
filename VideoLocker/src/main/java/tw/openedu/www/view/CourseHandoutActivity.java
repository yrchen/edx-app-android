package tw.openedu.www.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import tw.openedu.www.R;
import tw.openedu.www.base.BaseSingleFragmentActivity;

public class CourseHandoutActivity extends BaseSingleFragmentActivity {
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Handouts activity should not contain the drawer(Navigation Fragment).
        blockDrawerFromOpening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle(getString(R.string.tab_label_handouts));
    }

    @Override
    public Fragment getFirstFragment() {
        return new CourseHandoutFragment();
    }
}
