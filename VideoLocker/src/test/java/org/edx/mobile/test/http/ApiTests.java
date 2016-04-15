package org.openedu.www.test.http;

import tw.openedu.www.http.OkHttpUtil;
import tw.openedu.www.model.Filter;
import tw.openedu.www.model.api.AnnouncementsModel;
import tw.openedu.www.model.api.EnrolledCoursesResponse;
import tw.openedu.www.model.api.HandoutModel;
import tw.openedu.www.model.api.ResetPasswordResponse;
import tw.openedu.www.model.api.SectionEntry;
import tw.openedu.www.model.api.SyncLastAccessedSubsectionResponse;
import tw.openedu.www.model.api.VideoResponseModel;
import tw.openedu.www.model.course.BlockPath;
import tw.openedu.www.model.course.BlockType;
import tw.openedu.www.model.course.CourseComponent;
import tw.openedu.www.model.course.HasDownloadEntry;
import tw.openedu.www.model.course.IBlock;
import tw.openedu.www.model.course.VideoBlockModel;
import tw.openedu.www.module.registration.model.RegistrationDescription;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;

/**
 * This class contains unit tests for API calls to server.
 * <p/>
 * We don't really want to have unit tests that talk to a live server and we can't run
 * them on CI since we don't even know what server to talk to.
 * As such, these are disabled until we figure out what we want to do about them.
 * Probably we want to mock the HTTP responses so we can still exercise the rest of the
 * API logic.
 *
 * If you want to run them locally, you can just temporarily remove the @Ignore annotation
 */

@Ignore
public class ApiTests extends HttpBaseTestCase {


    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testSyncLastSubsection() throws Exception {
        assumeFalse(shouldSkipTest);

        login();

        EnrolledCoursesResponse e = api.getEnrolledCourses().get(0);
        Map<String, SectionEntry> map = api.getCourseHierarchy(e.getCourse().getId(), false);
        Entry<String, SectionEntry> entry = map.entrySet().iterator().next();
        Entry<String, ArrayList<VideoResponseModel>> subsection = entry.getValue().sections.entrySet().iterator().next();

        String courseId = e.getCourse().getId();
        String lastVisitedModuleId = subsection.getValue().get(0).getSection().getId();

        assertNotNull(courseId);
        assertNotNull(lastVisitedModuleId);

        print(String.format("course= %s ; sub-section= %s", courseId, lastVisitedModuleId));

        // TODO: lastVisitedModuleId must be section.id (id is now available)


        SyncLastAccessedSubsectionResponse model = api.syncLastAccessedSubsection(courseId, lastVisitedModuleId);
        assertNotNull(model);
        print("sync returned: " + model.last_visited_module_id);
    }

    @Test
    public void testGetLastAccessedModule() throws Exception {
        assumeFalse(shouldSkipTest);

        login();

        EnrolledCoursesResponse e = api.getEnrolledCourses().get(0);

        String courseId = e.getCourse().getId();
        assertNotNull(courseId);

        print(String.format("course= %s", courseId));

        SyncLastAccessedSubsectionResponse model = api.getLastAccessedSubsection(courseId);
        assertNotNull(model);
        //  print(model.json);
    }

    @Test
    public void testResetPassword() throws Exception {
        assumeFalse(shouldSkipTest);

        print("test: reset password");
        ResetPasswordResponse model = api.resetPassword("user@edx.org");
        assertTrue(model != null);
        print(model.value);
        print("test: finished: reset password");
    }

    @Test
    public void testHandouts() throws Exception {
        assumeFalse(shouldSkipTest);

        login();

        // get a course id for this test
        List<EnrolledCoursesResponse> courses = api.getEnrolledCourses();
        assertTrue("Must have enrolled to at least one course",
                courses != null && courses.size() > 0);
        String handoutURL = courses.get(0).getCourse().getCourse_handouts();

        HandoutModel model = api.getHandout(handoutURL, false);
        assertTrue(model != null);
        print(model.handouts_html);
    }

    @Test
    public void testChannelId() throws Exception {
        assumeFalse(shouldSkipTest);

        login();

        // get a course id for this test
        List<EnrolledCoursesResponse> courses = api.getEnrolledCourses();
        assertTrue("Must have enrolled to at least one course",
                courses != null && courses.size() > 0);
        String subscription_id = courses.get(0).getCourse().getSubscription_id();
        //should the channelId be mandatory?
        assertTrue(subscription_id != null);
    }

    @Test
    public void testCourseStructure() throws Exception {
        assumeFalse(shouldSkipTest);
        login();

        // get a course id for this test
        List<EnrolledCoursesResponse> courses = api.getEnrolledCourses();
        assertTrue("Must have enrolled to at least one course",
                courses != null && courses.size() > 0);
        String courseId = courses.get(0).getCourse().getId();

        Map<String, SectionEntry> chapters = api.getCourseHierarchy(courseId, false);
        for (Entry<String, SectionEntry> entry : chapters.entrySet()) {
            print("---------------" + entry.getKey() + "---------------");
            for (Entry<String, ArrayList<VideoResponseModel>> se : entry.getValue().sections.entrySet()) {
                print("------------" + se.getKey() + "------------");
                for (VideoResponseModel v : se.getValue()) {
                    print(v.getSummary().getDisplayName());
                }
            }
        }
    }

    @Test
    @Override
    public void login() throws Exception {
        assumeFalse(shouldSkipTest);
        super.login();
    }

    @Test
    public void testGetAnnouncement() throws Exception {
        assumeFalse(shouldSkipTest);

        login();

        // get a course id for this test
        List<EnrolledCoursesResponse> courses = api.getEnrolledCourses();
        assertTrue("Must have enrolled to at least one course",
                courses != null && courses.size() > 0);
        String updatesUrl = courses.get(0).getCourse().getCourse_updates();

        List<AnnouncementsModel> res = api.getAnnouncement(updatesUrl, false);
        assertTrue(res != null);
        for (AnnouncementsModel r : res) {
            print(r.getDate());
        }
    }

    @Test
    public void testReadRegistrationDescription() throws Exception {
        assumeFalse(shouldSkipTest);

        RegistrationDescription form = api.getRegistrationDescription();

        assertNotNull(form);
        assertNotNull(form.getEndpoint());
        assertNotNull(form.getMethod());
        assertNotNull(form.getFields());
        assertTrue(form.getFields().size() > 0);

        // verify if enum type is parsed
        assertNotNull(form.getFields().get(0).getFieldType());
    }

    @Test
    public void testEnrollInACourse() throws Exception {
        assumeFalse(shouldSkipTest);

        login();

        print("test: Enroll in a course");

        EnrolledCoursesResponse e = api.getEnrolledCourses().get(0);
        String courseId = e.getCourse().getId();
        boolean success = api.enrollInACourse(courseId, true);
        assertTrue(success);
        print("success");
        print("test: finished: reset password");
    }

    @Test
    public void testGetCourseStructure() throws Exception {
        assumeFalse(shouldSkipTest);

        login();

        // General overall testing of CourseComponent API without recursion
        EnrolledCoursesResponse e = api.getEnrolledCourses().get(0);
        final String courseId = e.getCourse().getId();
        final CourseComponent courseComponent = serviceManager.getCourseStructure(courseId,
                OkHttpUtil.REQUEST_CACHE_TYPE.IGNORE_CACHE);
        assertNotNull(courseComponent);
        assertNotNull(courseComponent.getRoot());
        assertEquals(courseId, courseComponent.getCourseId());

        List<IBlock> children = courseComponent.getChildren();
        assertNotNull(children);
        List<CourseComponent> childContainers = new ArrayList<>();
        List<CourseComponent> childLeafs = new ArrayList<>();
        for (IBlock c : children) {
            assertTrue(c instanceof CourseComponent);
            final CourseComponent child = (CourseComponent) c;
            assertEquals(child, courseComponent.find(new Filter<CourseComponent>() {
                @Override
                public boolean apply(CourseComponent component) {
                    return child.getId().equals(component.getId());
                }
            }));
            List<IBlock> grandchildren = child.getChildren();
            for (IBlock gc : grandchildren) {
                assertTrue(gc instanceof CourseComponent);
                final CourseComponent grandchild = (CourseComponent) c;
                assertEquals(grandchild, courseComponent.find(new Filter<CourseComponent>() {
                    @Override
                    public boolean apply(CourseComponent component) {
                        return grandchild.getId().equals(component.getId());
                    }
                }));
            }
            assertNull(child.find(new Filter<CourseComponent>() {
                @Override
                public boolean apply(CourseComponent component) {
                    return courseComponent.getId().equals(component.getId());
                }
            }));
            if (child.isContainer()) {
                childContainers.add(child);
            } else {
                childLeafs.add(child);
            }
        }
        assertEquals(childContainers, courseComponent.getChildContainers());
        assertEquals(childLeafs, courseComponent.getChildLeafs());

        assertTrue(courseComponent.isLastChild());
        int childrenSize = children.size();
        assertTrue(childrenSize > 0);
        assertTrue(((CourseComponent)
                children.get(childrenSize - 1)).isLastChild());

        BlockType blockType = courseComponent.getType();
        assertSame(courseComponent,
                courseComponent.getAncestor(Integer.MAX_VALUE));
        assertSame(courseComponent,
                courseComponent.getAncestor(EnumSet.of(blockType)));

        List<VideoBlockModel> videos = courseComponent.getVideos();
        assertNotNull(videos);
        for (HasDownloadEntry video : videos) {
            assertNotNull(video);
            assertTrue(video instanceof CourseComponent);
            CourseComponent videoComponenet = (CourseComponent) video;
            assertFalse(videoComponenet.isContainer());
            assertEquals(BlockType.VIDEO, videoComponenet.getType());
        }

        for (BlockType type : BlockType.values()) {
            EnumSet<BlockType> typeSet = EnumSet.of(type);
            List<CourseComponent> typeComponents = new ArrayList<>();
            courseComponent.fetchAllLeafComponents(typeComponents, typeSet);
            for (CourseComponent typeComponent : typeComponents) {
                assertEquals(type, typeComponent.getType());
            }

            if (type != blockType) {
                assertNotSame(courseComponent,
                        courseComponent.getAncestor(EnumSet.of(type)));
                break;
            }
        }

        BlockPath path = courseComponent.getPath();
        assertNotNull(path);
        assertEquals(1, path.getPath().size());
        assertSame(courseComponent, path.get(0));
        List<CourseComponent> leafComponents = new ArrayList<>();
        courseComponent.fetchAllLeafComponents(leafComponents,
                EnumSet.allOf(BlockType.class));
        for (CourseComponent leafComponent : leafComponents) {
            BlockPath leafPath = leafComponent.getPath();
            assertNotNull(leafPath);
            int pathSize = leafPath.getPath().size();
            assertTrue(pathSize > 1);
            CourseComponent component = leafComponent;
            for (int i = pathSize - 1; i >= 0; i--) {
                assertSame(component, leafPath.get(i));
                component = component.getParent();
            }
        }
    }

}
