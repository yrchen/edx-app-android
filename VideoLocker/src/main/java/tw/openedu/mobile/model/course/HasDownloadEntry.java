package tw.openedu.mobile.model.course;

import android.support.annotation.Nullable;

import tw.openedu.mobile.model.db.DownloadEntry;
import tw.openedu.mobile.module.storage.IStorage;

public interface HasDownloadEntry {
    @Nullable
    DownloadEntry getDownloadEntry(IStorage storage);
}
