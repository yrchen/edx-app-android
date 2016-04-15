package tw.openedu.www.core;


import com.google.inject.Inject;
import com.google.inject.Singleton;

import tw.openedu.www.discussion.DiscussionAPI;
import tw.openedu.www.module.analytics.ISegment;
import tw.openedu.www.module.db.IDatabase;
import tw.openedu.www.module.download.IDownloadManager;
import tw.openedu.www.module.notification.NotificationDelegate;
import tw.openedu.www.module.prefs.UserPrefs;
import tw.openedu.www.module.storage.IStorage;
import tw.openedu.www.services.ServiceManager;
import tw.openedu.www.util.Config;
import tw.openedu.www.view.Router;

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
