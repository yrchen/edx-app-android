package tw.openedu.www.view.common;

import android.support.annotation.NonNull;

public interface TaskMessageCallback {
    void onMessage(@NonNull MessageType messageType, @NonNull String message);
}
