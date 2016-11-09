package tw.openedu.android.module.download;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;

import com.google.inject.Inject;

import tw.openedu.android.core.IEdxEnvironment;
import tw.openedu.android.logger.Logger;
import tw.openedu.android.model.VideoModel;
import tw.openedu.android.model.db.DownloadEntry;
import tw.openedu.android.model.download.NativeDownloadModel;
import tw.openedu.android.module.analytics.ISegment;
import tw.openedu.android.module.db.DataCallback;

import roboguice.receiver.RoboBroadcastReceiver;

public class DownloadCompleteReceiver extends RoboBroadcastReceiver {
    private final Logger logger = new Logger(getClass().getName());

    @Inject
    private IEdxEnvironment environment;


    @Override
    protected void handleReceive(final Context context, Intent data){
        try {
            if (data != null && data.hasExtra(DownloadManager.EXTRA_DOWNLOAD_ID)) {
                long id = data.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (id != -1) {
                    logger.debug("Received download notification for id: " + id);

                    // check if download was SUCCESSFUL
                    NativeDownloadModel nm = environment.getDownloadManager().getDownload(id);

                    if (nm == null || nm.status != DownloadManager.STATUS_SUCCESSFUL) {
                        logger.debug("Download seems failed or cancelled for id : " + id);
                        return;
                    } else {
                        logger.debug("Download successful for id : " + id);
                    }

                    // mark download as completed
                    environment.getStorage().markDownloadAsComplete(id, new DataCallback<VideoModel>() {
                        @Override
                        public void onResult(VideoModel result) {
                            if(result!=null){
                                DownloadEntry download = (DownloadEntry) result;
                                
                                ISegment segIO = environment.getSegment();
                                segIO.trackDownloadComplete(download.videoId, download.eid, 
                                        download.lmsUrl);
                            }
                        }

                        @Override
                        public void onFail(Exception ex) {
                            logger.error(ex);
                        }
                    });
                }
            }
        } catch(Exception ex) {
            logger.error(ex);
        }

    }

}
