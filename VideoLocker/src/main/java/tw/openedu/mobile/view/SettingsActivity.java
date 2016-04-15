package tw.openedu.mobile.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import tw.openedu.mobile.R;
import tw.openedu.mobile.base.BaseSingleFragmentActivity;

public class SettingsActivity extends BaseSingleFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.settings_txt));
        configureDrawer();
    }

    @Override
    public Fragment getFirstFragment() {
        return new SettingsFragment();
    }

}
