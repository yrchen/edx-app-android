package tw.openedu.www.base;

import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import tw.openedu.www.module.db.DataCallback;
import tw.openedu.www.util.NetworkUtil;
import tw.openedu.www.view.custom.ProgressWheel;

public abstract class BaseVideosDownloadStateActivity extends BaseFragmentActivity {
    private ProgressWheel progressWheel;
    private MenuItem progressMenuItem;
    private Runnable updateDownloadProgressRunnable;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(tw.openedu.www.R.menu.download_state, menu);
        MenuItem newProgressMenuItem = menu.findItem(tw.openedu.www.R.id.download_progress);
        View progressView = newProgressMenuItem.getActionView();
        ProgressWheel newProgressWheel = (ProgressWheel)
                progressView.findViewById(tw.openedu.www.R.id.progress_wheel);
        if (progressMenuItem != null) {
            newProgressMenuItem.setVisible(progressMenuItem.isVisible());
            newProgressWheel.setProgress(progressWheel.getProgress());
        }
        progressMenuItem = newProgressMenuItem;
        progressWheel = newProgressWheel;
        progressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                environment.getRouter().showDownloads(BaseVideosDownloadStateActivity.this);
            }
        });
        if (updateDownloadProgressRunnable == null) {
            updateDownloadProgressRunnable = new Runnable() {
                @Override
                public void run() {
                    if (!NetworkUtil.isConnected(BaseVideosDownloadStateActivity.this) ||
                            !environment.getDatabase().isAnyVideoDownloading(null)) {
                        progressMenuItem.setVisible(false);
                    } else {
                        progressMenuItem.setVisible(true);
                        environment.getStorage().getAverageDownloadProgress(
                                new DataCallback<Integer>() {
                            @Override
                            public void onResult(Integer result) {
                                int progressPercent = result;
                                if (progressPercent >= 0 && progressPercent <= 100) {
                                    progressWheel.setProgressPercent(progressPercent);
                                }
                            }
                            @Override
                            public void onFail(Exception ex) {
                                logger.error(ex);
                            }
                        });
                    }
                    progressWheel.postDelayed(this, DateUtils.SECOND_IN_MILLIS);
                }
            };
            updateDownloadProgressRunnable.run();
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (updateDownloadProgressRunnable != null) {
            updateDownloadProgressRunnable.run();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (updateDownloadProgressRunnable != null) {
            progressWheel.removeCallbacks(updateDownloadProgressRunnable);
        }
    }
}
