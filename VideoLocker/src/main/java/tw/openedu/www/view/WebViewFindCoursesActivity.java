package tw.openedu.www.view;

import android.os.Bundle;
import android.webkit.WebView;

import tw.openedu.www.R;
import tw.openedu.www.base.FindCoursesBaseActivity;
import tw.openedu.www.module.analytics.ISegment;

import roboguice.inject.ContentView;

@ContentView(R.layout.activity_find_courses)
public class WebViewFindCoursesActivity extends FindCoursesBaseActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // configure slider layout. This should be called only once and
        // hence is shifted to onCreate() function
        configureDrawer();

        environment.getSegment().trackScreenView(ISegment.Screens.FIND_COURSES);

        webView = (WebView) findViewById(R.id.webview);
        webView.loadUrl(environment.getConfig().getCourseDiscoveryConfig().getCourseSearchUrl());
    }

    @Override
    protected void onOnline() {
        super.onOnline();
        if (!isWebViewLoaded()) {
            webView.reload();
        }
    }
}
