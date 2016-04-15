package tw.openedu.www.model.course;

import android.support.annotation.Nullable;

import tw.openedu.www.model.db.DownloadEntry;
import tw.openedu.www.module.storage.IStorage;

public interface HasDownloadEntry {
    @Nullable
    DownloadEntry getDownloadEntry(IStorage storage);
}
