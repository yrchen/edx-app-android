package tw.openedu.android.user;

import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.inject.Inject;

import tw.openedu.android.task.Task;
import tw.openedu.android.third_party.crop.CropUtil;

import java.io.File;

public abstract class SetAccountImageTask extends
        Task<Void> {

    @Inject
    private UserAPI userAPI;

    @NonNull
    private final String username;

    @NonNull
    private final Uri uri;

    @NonNull
    private final Rect cropRect;

    public SetAccountImageTask(@NonNull Context context, @NonNull String username, @NonNull Uri uri, @NonNull Rect cropRect) {
        super(context);
        this.username = username;
        this.uri = uri;
        this.cropRect = cropRect;
    }


    public Void call() throws Exception {
        final File cropped = new File(context.getExternalCacheDir(), "cropped-image" + System.currentTimeMillis() + ".jpg");
        CropUtil.crop(getContext(), uri, cropRect, 500, 500, cropped);
        userAPI.setProfileImage(username, cropped);
        return null;
    }
}
