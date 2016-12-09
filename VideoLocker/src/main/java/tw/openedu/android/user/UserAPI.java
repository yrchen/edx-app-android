package tw.openedu.android.user;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.webkit.MimeTypeMap;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import tw.openedu.android.event.AccountDataLoadedEvent;
import tw.openedu.android.event.ProfilePhotoUpdatedEvent;
import tw.openedu.android.http.ApiConstants;
import tw.openedu.android.http.HttpConnectivityException;
import tw.openedu.android.http.HttpException;
import tw.openedu.android.http.cache.CacheManager;
import tw.openedu.android.logger.Logger;
import tw.openedu.android.model.Page;
import tw.openedu.android.model.api.EnrolledCoursesResponse;
import tw.openedu.android.profiles.BadgeAssertion;
import tw.openedu.android.util.Config;
import tw.openedu.android.util.IOUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedInput;

@Singleton
public class UserAPI {
    @NonNull
    private final UserService userService;

    private Logger logger = new Logger(UserAPI.class.getName());

    @Inject
    Config config;

    @Inject
    CacheManager cache;

    @Inject
    Gson gson;

    @Inject
    public UserAPI(@NonNull RestAdapter restAdapter) {
        userService = restAdapter.create(UserService.class);
    }

    public Account getAccount(@NonNull String username) throws HttpException {
        final Account account = userService.getAccount(username);
        EventBus.getDefault().post(new AccountDataLoadedEvent(account));
        return account;
    }

    public Account updateAccount(@NonNull String username, @NonNull String field, @Nullable Object value) throws HttpException {
        final Account updatedAccount = userService.updateAccount(username, Collections.singletonMap(field, value));
        EventBus.getDefault().post(new AccountDataLoadedEvent(updatedAccount));
        return updatedAccount;
    }

    public void setProfileImage(@NonNull String username, @NonNull final File file) throws HttpException, IOException {
        final String mimeType = "image/jpeg";
        logger.debug("Uploading file of type " + mimeType + " from " + file.toString());
        userService.setProfileImage(
                username,
                "attachment;filename=filename." + MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType),
                new TypedFile(mimeType, file));
        EventBus.getDefault().post(new ProfilePhotoUpdatedEvent(username, Uri.fromFile(file)));
    }

    public void deleteProfileImage(@NonNull String username) throws HttpException {
        userService.deleteProfileImage(username);
        EventBus.getDefault().post(new ProfilePhotoUpdatedEvent(username, null));
    }

    public Page<BadgeAssertion> getBadges(@NonNull String username, int page) throws HttpException {
        return userService.getBadges(username, page, ApiConstants.STANDARD_PAGE_SIZE);
    }

    public
    @NonNull
    String getUserEnrolledCoursesURL(@NonNull String username) {
        return config.getApiHostURL() + "/api/mobile/v0.5/users/" + username + "/course_enrollments";
    }

    public
    @NonNull
    List<EnrolledCoursesResponse> getUserEnrolledCourses(@NonNull String username, boolean tryCache) throws HttpException {
        String json = null;

        final String cacheKey = getUserEnrolledCoursesURL(username);

        // try to get from cache if we should
        if (tryCache) {
            try {
                json = cache.get(cacheKey);
            } catch (IOException | NoSuchAlgorithmException e) {
                logger.debug(e.toString());
            }
        }

        // if we don't have a json yet, get it from userService
        if (json == null) {
            try {
                Response response = userService.getUserEnrolledCourses(username);
                TypedInput input = response.getBody();
                try {
                    json = IOUtils.toString(input.in(), Charset.defaultCharset());
                } catch (IOException e) {
                    throw new HttpConnectivityException(RetrofitError.networkError(null, e));
                }
                // cache result
                try {
                    cache.put(cacheKey, json);
                } catch (IOException | NoSuchAlgorithmException e) {
                    logger.debug(e.toString());
                }
            } catch (HttpConnectivityException connectivityException) {
                // Cache has already been checked, and connectivity
                // can't be established, so throw the exception.
                if (tryCache) throw connectivityException;
                // Otherwise fall back to fetching from the cache
                try {
                    json = cache.get(cacheKey);
                } catch (IOException | NoSuchAlgorithmException e) {
                    logger.debug(e.toString());
                    throw connectivityException;
                }
                // If the cache is empty, throw the exception.
                if (json == null) throw connectivityException;
            }
        }

        // We aren't use TypeToken here because it throws NoClassDefFoundError
        final JsonArray ary = gson.fromJson(json, JsonArray.class);
        final List<EnrolledCoursesResponse> ret = new ArrayList<>(ary.size());
        for (int cnt = 0; cnt < ary.size(); ++cnt) {
            ret.add(gson.fromJson(ary.get(cnt), EnrolledCoursesResponse.class));
        }
        return ret;
    }
}