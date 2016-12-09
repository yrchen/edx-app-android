package tw.openedu.android.course;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import tw.openedu.android.http.HttpException;
import tw.openedu.android.model.Page;
import tw.openedu.android.model.api.ProfileModel;
import tw.openedu.android.module.prefs.UserPrefs;

import retrofit.RestAdapter;

@Singleton
public class CourseAPI {

    @NonNull
    private final CourseService courseService;
    @NonNull
    private final UserPrefs userPrefs;


    @Inject
    public CourseAPI(@NonNull RestAdapter restAdapter, @NonNull UserPrefs userPrefs) {
        courseService = restAdapter.create(CourseService.class);
        this.userPrefs = userPrefs;
    }

    public
    @NonNull
    Page<CourseDetail> getCourseList(int page) throws HttpException {
        return courseService.getCourseList(getUsername(), true, page);
    }

    public
    @NonNull
    CourseDetail getCourseDetail(@NonNull String courseId) throws HttpException {
        // Empty courseId will return a 200 for a list of course details, instead of a single course
        if (TextUtils.isEmpty(courseId)) throw new IllegalArgumentException();
        return courseService.getCourseDetail(courseId, getUsername());
    }

    @Nullable
    private String getUsername() {
        final ProfileModel profile = userPrefs.getProfile();
        return null == profile ? null : profile.username;
    }
}
