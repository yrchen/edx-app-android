package tw.openedu.www.util;

import android.content.Context;

import com.google.inject.Inject;

import tw.openedu.www.logger.Logger;
import tw.openedu.www.services.ServiceManager;

import roboguice.RoboGuice;

public abstract class TranscriptDownloader implements Runnable {

    private String srtUrl;
    @Inject
    ServiceManager localApi;
    private final Logger logger = new Logger(TranscriptDownloader.class.getName());

    public TranscriptDownloader(Context context, String url) {
        this.srtUrl = url;
        RoboGuice.getInjector(context).injectMembers(this);
    }

    @Override
    public void run() {
        try {
            String response = localApi.downloadTranscript(srtUrl);
            onDownloadComplete(response);
        } catch (Exception localException) {
            handle(localException);
            logger.error(localException);
        }
    }

    public abstract void handle(Exception ex);

    public abstract void onDownloadComplete(String response);
}
