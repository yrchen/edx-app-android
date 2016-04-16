package tw.openedu.android.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import tw.openedu.android.R;
import tw.openedu.android.base.BaseFragmentActivity;
import tw.openedu.android.module.analytics.ISegment;
import tw.openedu.android.module.prefs.PrefManager;
import tw.openedu.android.util.AppConstants;

public class LaunchActivity extends BaseFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Register for Login Receiver before checking for logged in user
        enableLoginCallback();

        //We need to stop the Launch Activity from launching if the user has logged in
        PrefManager pm =new PrefManager(LaunchActivity.this, PrefManager.Pref.LOGIN);
        if (pm.getCurrentUserProfile() != null) {
            finish();
            return;
        }
        setContentView(R.layout.activity_launch);

        Button sign_in_tv = (Button) findViewById(R.id.sign_in_tv);
        sign_in_tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                environment.getRouter().showLogin(LaunchActivity.this);
            }
        });

        Button sign_up_button = (Button) findViewById(R.id.sign_up_btn);
        sign_up_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    environment.getSegment().trackUserSignUpForAccount();
                }catch(Exception e){
                    logger.error(e);
                }
                environment.getRouter().showRegistration(LaunchActivity.this);
            }
        });

        environment.getSegment().trackScreenView(ISegment.Screens.LAUNCH_ACTIVITY);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disableLoginCallback();
    }

    //Broadcast Receiver to notify all activities to finish if user logs out
    private BroadcastReceiver loginReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    protected void enableLoginCallback() {
        // register for login listener
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppConstants.USER_LOG_IN);
        registerReceiver(loginReceiver, filter);
    }

    protected void disableLoginCallback() {
        // un-register loginReceiver
        unregisterReceiver(loginReceiver);
    }



    @Override
    protected boolean createOptionsMenu(Menu menu) {
        // Launch screen doesn't have any menu
        return true;
    }
}
