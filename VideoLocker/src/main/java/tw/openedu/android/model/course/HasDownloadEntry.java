package tw.openedu.android.model.course;

import android.support.annotation.Nullable;

import tw.openedu.android.model.db.DownloadEntry;
import tw.openedu.android.module.storage.IStorage;

public interface HasDownloadEntry {
    @Nullable
    DownloadEntry getDownloadEntry(IStorage storage);
}
