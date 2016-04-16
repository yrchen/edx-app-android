package tw.openedu.android.core;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

import com.google.inject.AbstractModule;

import tw.openedu.android.base.MainApplication;
import tw.openedu.android.discussion.DiscussionTextUtils;
import tw.openedu.android.http.Api;
import tw.openedu.android.http.IApi;
import tw.openedu.android.http.OkHttpUtil;
import tw.openedu.android.http.RestApiManager;
import tw.openedu.android.module.analytics.ISegment;
import tw.openedu.android.module.analytics.ISegmentEmptyImpl;
import tw.openedu.android.module.analytics.ISegmentImpl;
import tw.openedu.android.module.analytics.ISegmentTracker;
import tw.openedu.android.module.analytics.ISegmentTrackerImpl;
import tw.openedu.android.module.db.IDatabase;
import tw.openedu.android.module.db.impl.IDatabaseImpl;
import tw.openedu.android.module.download.IDownloadManager;
import tw.openedu.android.module.download.IDownloadManagerImpl;
import tw.openedu.android.module.notification.DummyNotificationDelegate;
import tw.openedu.android.module.notification.NotificationDelegate;
import tw.openedu.android.module.notification.ParseNotificationDelegate;
import tw.openedu.android.module.storage.IStorage;
import tw.openedu.android.module.storage.Storage;
import tw.openedu.android.util.BrowserUtil;
import tw.openedu.android.util.Config;
import tw.openedu.android.util.MediaConsentUtils;

import de.greenrobot.event.EventBus;
import okhttp3.OkHttpClient;
import retrofit.RestAdapter;

public class EdxDefaultModule extends AbstractModule {
    //if your module requires a context, add a constructor that will be passed a context.
    private Context context;

    //with RoboGuice 3.0, the constructor for AbstractModule will use an `Application`, not a `Context`
    public EdxDefaultModule(Context context) {
        this.context = context;
    }

    @Override
    public void configure() {
        Config config = new Config(context);

        bind(IDatabase.class).to(IDatabaseImpl.class);
        bind(IStorage.class).to(Storage.class);
        bind(ISegmentTracker.class).to(ISegmentTrackerImpl.class);
        if (config.getSegmentConfig().isEnabled()) {
            bind(ISegment.class).to(ISegmentImpl.class);
        } else {
            bind(ISegment.class).to(ISegmentEmptyImpl.class);
        }

        bind(IDownloadManager.class).to(IDownloadManagerImpl.class);

        bind(OkHttpClient.class).toInstance(OkHttpUtil.getOAuthBasedClient(context));

        if (MainApplication.RETROFIT_ENABLED) {
            bind(IApi.class).to(RestApiManager.class);
        } else {
            bind(IApi.class).to(Api.class);
        }

        if (config.isNotificationEnabled()) {
            Config.ParseNotificationConfig parseNotificationConfig =
                    config.getParseNotificationConfig();
            if (parseNotificationConfig.isEnabled()) {
                bind(NotificationDelegate.class).to(ParseNotificationDelegate.class);
            } else {
                bind(NotificationDelegate.class).to(DummyNotificationDelegate.class);
            }
        } else {
            bind(NotificationDelegate.class).to(DummyNotificationDelegate.class);
        }

        bind(IEdxEnvironment.class).to(EdxEnvironment.class);

        bind(LinearLayoutManager.class).toProvider(LinearLayoutManagerProvider.class);

        bind(RestAdapter.class).toProvider(RestAdapterProvider.class);

        bind(EventBus.class).toInstance(EventBus.getDefault());

        requestStaticInjection(BrowserUtil.class, MediaConsentUtils.class,
                DiscussionTextUtils.class);
    }
}
