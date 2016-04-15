package org.openedu.www.view;

import android.view.View;
import android.webkit.WebView;

import tw.openedu.mobile.R;
import tw.openedu.mobile.http.OkHttpUtil;
import tw.openedu.mobile.model.api.EnrolledCoursesResponse;
import tw.openedu.mobile.model.course.BlockType;
import tw.openedu.mobile.model.course.CourseComponent;
import tw.openedu.mobile.model.course.HtmlBlockModel;
import tw.openedu.mobile.view.CourseUnitWebViewFragment;

import org.junit.Test;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class CourseUnitWebViewFragmentTest extends UiTest {
    /**
     * Method for iterating through the mock course response data, and
     * returning the first video block leaf.
     *
     * @return The first {@link HtmlBlockModel} leaf in the mock course data
     */
    private HtmlBlockModel getHtmlUnit() {
        EnrolledCoursesResponse courseData;
        CourseComponent courseComponent;
        try {
            courseData = api.getEnrolledCourses().get(0);
            courseComponent = serviceManager.getCourseStructure(
                    courseData.getCourse().getId(),
                    OkHttpUtil.REQUEST_CACHE_TYPE.IGNORE_CACHE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        List<CourseComponent> htmlBlockUnits = new ArrayList<>();
        courseComponent.fetchAllLeafComponents(htmlBlockUnits,
                EnumSet.of(BlockType.HTML));
        return (HtmlBlockModel) htmlBlockUnits.get(0);
    }

    /**
     * Testing initialization
     */
    @Test
    public void initializeTest() {
        CourseUnitWebViewFragment fragment = CourseUnitWebViewFragment.newInstance(getHtmlUnit());
        SupportFragmentTestUtil.startVisibleFragment(fragment);
        View view = fragment.getView();
        assertNotNull(view);

        View courseUnitWebView = view.findViewById(R.id.course_unit_webView);
        assertNotNull(courseUnitWebView);
        assertThat(courseUnitWebView).isInstanceOf(WebView.class);
        WebView webView = (WebView) courseUnitWebView;
        assertTrue(webView.getSettings().getJavaScriptEnabled());
    }
}
