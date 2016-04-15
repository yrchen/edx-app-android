package tw.openedu.mobile.task;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ProgressBar;

import com.google.inject.Inject;

import tw.openedu.mobile.core.IEdxEnvironment;
import tw.openedu.mobile.logger.Logger;
import tw.openedu.mobile.util.images.ErrorUtils;
import tw.openedu.mobile.view.common.MessageType;
import tw.openedu.mobile.view.common.TaskMessageCallback;
import tw.openedu.mobile.view.common.TaskProcessCallback;
import tw.openedu.mobile.view.common.TaskProgressCallback;

import java.lang.ref.WeakReference;

import roboguice.util.RoboAsyncTask;

public abstract class Task<T> extends RoboAsyncTask<T> {

    private ProgressBar progressBar;

    @Nullable
    private WeakReference<TaskProgressCallback> progressCallback;
    @Nullable
    private WeakReference<TaskMessageCallback> messageCallback;

    protected final Handler handler = new Handler();
    protected final Logger logger = new Logger(getClass().getName());

    @Inject
    protected IEdxEnvironment environment;

    public Task(Context context) {
        super(context);

        if (context instanceof TaskProcessCallback) {
            setTaskProcessCallback((TaskProcessCallback) context);
        }
    }

    public void setProgressDialog(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void setTaskProcessCallback(@Nullable TaskProcessCallback callback) {
        setProgressCallback(callback);
        setMessageCallback(callback);
    }

    public void setProgressCallback(@Nullable TaskProgressCallback callback) {
        progressCallback = callback == null ? null : new WeakReference<>(callback);
    }

    public void setMessageCallback(@Nullable TaskMessageCallback callback) {
        messageCallback = callback == null ? null : new WeakReference<>(callback);
    }

    @Nullable
    private TaskProgressCallback getProgressCallback() {
        return progressCallback == null ? null : progressCallback.get();
    }

    @Nullable
    private TaskMessageCallback getMessageCallback() {
        return messageCallback == null ? null : messageCallback.get();
    }

    @Override
    protected void onPreExecute() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
        final TaskProgressCallback callback = getProgressCallback();
        if (callback != null) {
            callback.startProcess();
        }
    }

    @Override
    protected void onFinally() {
        stopProgress();
    }

    protected void stopProgress() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
        final TaskProgressCallback callback = getProgressCallback();
        if (callback != null) {
            callback.finishProcess();
        }
    }

    @Override
    protected void onException(Exception ex) {
        final TaskMessageCallback callback = getMessageCallback();
        if (callback == null) {
            return;
        }

        callback.onMessage(MessageType.FLYIN_ERROR, ErrorUtils.getErrorMessage(ex, context));
    }
}
