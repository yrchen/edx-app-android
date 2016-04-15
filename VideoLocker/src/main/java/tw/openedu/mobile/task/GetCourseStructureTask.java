package tw.openedu.mobile.task;

import android.content.Context;
import android.support.annotation.NonNull;

import tw.openedu.mobile.base.MainApplication;
import tw.openedu.mobile.http.OkHttpUtil;
import tw.openedu.mobile.model.course.CourseComponent;
import tw.openedu.mobile.module.prefs.PrefManager;

import java.util.Date;

public abstract class GetCourseStructureTask extends
Task<CourseComponent> {

    @NonNull
    String courseId;

    public GetCourseStructureTask(@NonNull Context context, @NonNull String courseId) {
        super(context);
        this.courseId = courseId;
    }

    public CourseComponent call( ) throws Exception{
        PrefManager.UserPrefManager prefManager = new PrefManager.UserPrefManager(MainApplication.instance());
        long lastFetchTime = prefManager.getLastCourseStructureFetch(courseId);
        long curTime = new Date().getTime();
        OkHttpUtil.REQUEST_CACHE_TYPE useCacheType = OkHttpUtil.REQUEST_CACHE_TYPE.PREFER_CACHE;
        //if last fetch happened over one hour ago, re-fetch data
        if ( lastFetchTime + 3600 * 1000 < curTime ){
            useCacheType =  OkHttpUtil.REQUEST_CACHE_TYPE.IGNORE_CACHE;;
            prefManager.setLastCourseStructureFetch(courseId, curTime);
        }
        return environment.getServiceManager().getCourseStructure(courseId, useCacheType);
    }
}
