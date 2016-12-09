package tw.openedu.android.core;


import tw.openedu.android.discussion.DiscussionAPI;
import tw.openedu.android.module.analytics.ISegment;
import tw.openedu.android.module.db.IDatabase;
import tw.openedu.android.module.download.IDownloadManager;
import tw.openedu.android.module.notification.NotificationDelegate;
import tw.openedu.android.module.prefs.LoginPrefs;
import tw.openedu.android.module.prefs.UserPrefs;
import tw.openedu.android.module.storage.IStorage;
import tw.openedu.android.services.ServiceManager;
import tw.openedu.android.util.Config;
import tw.openedu.android.view.Router;

/**
 * TODO - we should decompose this class into environment setting and service provider settings.
 */
public interface IEdxEnvironment {

    IDatabase getDatabase();

    IStorage getStorage();

    IDownloadManager getDownloadManager();

    UserPrefs getUserPrefs();

    LoginPrefs getLoginPrefs();

    ISegment getSegment();

    NotificationDelegate getNotificationDelegate();

    Router getRouter();

    Config getConfig();

    ServiceManager getServiceManager();

    //TODO - it should be part of ServiceManager
    DiscussionAPI getDiscussionAPI();
}
