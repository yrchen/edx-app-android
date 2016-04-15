package tw.openedu.mobile.core;


import tw.openedu.mobile.discussion.DiscussionAPI;
import tw.openedu.mobile.module.analytics.ISegment;
import tw.openedu.mobile.module.db.IDatabase;
import tw.openedu.mobile.module.download.IDownloadManager;
import tw.openedu.mobile.module.notification.NotificationDelegate;
import tw.openedu.mobile.module.prefs.UserPrefs;
import tw.openedu.mobile.module.storage.IStorage;
import tw.openedu.mobile.services.ServiceManager;
import tw.openedu.mobile.util.Config;
import tw.openedu.mobile.view.Router;

/**
 * TODO - we should decompose this class into environment setting and service provider settings.
 */
public interface IEdxEnvironment {

    IDatabase getDatabase();

    IStorage getStorage();

    IDownloadManager getDownloadManager();

    UserPrefs getUserPrefs();

    ISegment getSegment();

    NotificationDelegate getNotificationDelegate();

    Router getRouter();

    Config getConfig();

    ServiceManager getServiceManager();

    //TODO - it should be part of ServiceManager
    DiscussionAPI getDiscussionAPI();
}
