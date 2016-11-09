package tw.openedu.android.util.observer;

import android.support.annotation.NonNull;

public interface Observable<T> {
    @NonNull
    Subscription subscribe(@NonNull final Observer<T> observer);
}
