package tw.openedu.mobile.view.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

import tw.openedu.mobile.BuildConfig;

public class EdxWebView extends WebView {
    @SuppressLint("SetJavaScriptEnabled")
    public EdxWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        final WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(false);
        settings.setSupportZoom(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setUserAgentString(
                settings.getUserAgentString() + " " +
                        context.getString(tw.openedu.mobile.R.string.app_name) + "/" +
                        BuildConfig.APPLICATION_ID + "/" +
                        BuildConfig.VERSION_NAME
        );
    }
}
