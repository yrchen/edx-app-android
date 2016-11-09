package tw.openedu.android.core;


import com.google.inject.Inject;
import com.google.inject.Singleton;

import tw.openedu.android.discussion.DiscussionAPI;
import tw.openedu.android.module.analytics.ISegment;
import tw.openedu.android.module.db.IDatabase;
import tw.openedu.android.module.download.IDownloadManager;
import tw.openedu.android.module.notification.NotificationDelegate;
import tw.openedu.android.module.prefs.UserPrefs;
import tw.openedu.android.module.storage.IStorage;
import tw.openedu.android.services.ServiceManager;
import tw.openedu.android.util.Config;
import tw.openedu.android.view.Router;

import de.greenrobot.event.EventBus;

@Singleton
public class EdxEnvironment implements IEdxEnvironment {

    @Inject
    IDatabase database;

    @Inject
    IStorage storage;

    @Inject
    IDownloadManager downloadManager;

    @Inject
    UserPrefs userPrefs;

    @Inject
    ISegment segment;

    @Inject
    NotificationDelegate notificationDelegate;

    @Inject
    Router router;

    @Inject
    Config config;

    @Inject
    ServiceManager serviceManager;

    @Inject
    DiscussionAPI discussionAPI;

    @Inject
    EventBus eventBus;

    @Override
    public IDatabase getDatabase() {
        return database;
    }

    @Override
    public IDownloadManager getDownloadManager() {
        return downloadManager;
    }

    @Override
    public UserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public ISegment getSegment() {
        return segment;
    }

    @Override
    public NotificationDelegate getNotificationDelegate() {
        return notificationDelegate;
    }

    @Override
    public Router getRouter() {
        return router;
    }

    @Override
    public Config getConfig() {
        return config;
    }

    @Override
    public IStorage getStorage() {
        return storage;
    }

    @Override
    public ServiceManager getServiceManager() {
        return serviceManager;
    }

    @Override
    public DiscussionAPI getDiscussionAPI() {
        return discussionAPI;
    }

    public EventBus getEventBus() {
        return eventBus;
    }
}
