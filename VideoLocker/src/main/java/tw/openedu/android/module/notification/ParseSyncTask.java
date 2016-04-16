package tw.openedu.android.module.notification;

import android.content.Context;

import com.parse.ParseInstallation;

import tw.openedu.android.task.Task;

import java.util.List;

/**
 * Sync with Parse server
 */
public abstract class ParseSyncTask extends Task<Void> {

    List<String> subscribedChannels;
    public ParseSyncTask(Context context) {
        super(context);
    }

    @Override
    public Void call( ) throws Exception{
        subscribedChannels = ParseInstallation.getCurrentInstallation().getList("channels");
        return null;
    }
}