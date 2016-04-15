package tw.openedu.www.view;

import android.os.Bundle;
import android.webkit.WebView;

import tw.openedu.www.R;
import tw.openedu.www.base.FindCoursesBaseActivity;
import tw.openedu.www.module.analytics.ISegment;

import roboguice.inject.ContentView;

@ContentView(R.layout.activity_find_course_info)
public class CourseInfoActivity extends FindCoursesBaseActivity {

    public static final String EXTRA_PATH_ID = "path_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        environment.getSegment().trackScreenView(ISegment.Screens.COURSE_INFO_SCREEN);
    }

    @Override
    protected void onStart() {
        super.onStart();

        String pathId = getIntent().getStringExtra(EXTRA_PATH_ID);
        String url = environment.getConfig().getCourseDiscoveryConfig()
                .getCourseInfoUrlTemplate()
                .replace("{" + EXTRA_PATH_ID + "}", pathId);
        WebView webview = (WebView) findViewById(R.id.webview);
        webview.loadUrl(url);
    }

    @Override
    protected boolean isAllLinksExternal() {
        // treat all links on this screen as external links, so that they open in external browser
        return true;
    }
}
