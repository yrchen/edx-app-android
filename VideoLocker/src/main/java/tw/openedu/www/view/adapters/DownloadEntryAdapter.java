package tw.openedu.www.view.adapters;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import tw.openedu.www.core.IEdxEnvironment;
import tw.openedu.www.util.MemoryUtil;

public abstract class DownloadEntryAdapter extends BaseListAdapter<DownloadEntryAdapter.Item> {

    public DownloadEntryAdapter(Context context, IEdxEnvironment environment) {
        super(context, tw.openedu.www.R.layout.row_download_list, environment);
    }

    @Override
    public void render(BaseViewHolder tag, final Item item) {
        final ViewHolder holder = (ViewHolder) tag;
        holder.title.setText(item.getTitle());
        holder.duration.setText(item.getDuration());
        holder.progress.setProgress(item.getPercent());
        @DrawableRes
        final int progressDrawable;
        final String progressText;
        final String errorText;
        switch (item.getStatus()) {
            case PENDING: {
                progressText = getContext().getString(tw.openedu.www.R.string.download_pending);
                progressDrawable = tw.openedu.www.R.drawable.custom_progress_bar_horizontal_green;
                errorText = null;
                break;
            }
            case DOWNLOADING: {
                progressText = getByteCountProgressText(item);
                progressDrawable = tw.openedu.www.R.drawable.custom_progress_bar_horizontal_green;
                errorText = null;
                break;
            }
            case FAILED: {
                errorText = getContext().getString(tw.openedu.www.R.string.error_download_failed);
                progressDrawable = tw.openedu.www.R.drawable.custom_progress_bar_horizontal_red;
                if (item.getDownloadedByteCount() > 0) {
                    progressText = getByteCountProgressText(item);
                } else {
                    progressText = null;
                }
                break;
            }
            default: {
                throw new IllegalArgumentException(item.getStatus().name());
            }
        }
        holder.progress
                .setProgressDrawable(getContext()
                        .getResources()
                        .getDrawable(progressDrawable));
        if (null == progressText) {
            holder.percent.setVisibility(View.GONE);
        } else {
            holder.percent.setText(progressText);
            holder.percent.setVisibility(View.VISIBLE);
        }
        if (null == errorText) {
            holder.error.setVisibility(View.GONE);
        } else {
            holder.error.setText(errorText);
            holder.error.setVisibility(View.VISIBLE);
        }

        holder.cross_image_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteClicked(item);
            }
        });
    }

    @NonNull
    private String getByteCountProgressText(Item item) {
        final Long totalByteCount = item.getTotalByteCount();
        String downloadedText = MemoryUtil.format(getContext(), item.getDownloadedByteCount());
        if (null != totalByteCount) {
            downloadedText += " / " + MemoryUtil.format(getContext(), totalByteCount);
        }
        return downloadedText;
    }

    @Override
    public BaseViewHolder getTag(View convertView) {
        return new ViewHolder(convertView);
    }

    private static class ViewHolder extends BaseViewHolder {
        final TextView title;
        final TextView duration;
        final TextView percent;
        final LinearLayout cross_image_layout;
        final TextView error;
        final ProgressBar progress;

        public ViewHolder(@NonNull View view) {
            title = (TextView) view.findViewById(tw.openedu.www.R.id.downloads_name);
            duration = (TextView) view
                    .findViewById(tw.openedu.www.R.id.download_time);
            percent = (TextView) view
                    .findViewById(tw.openedu.www.R.id.download_percentage);
            error = (TextView) view
                    .findViewById(tw.openedu.www.R.id.txtDownloadFailed);
            progress = (ProgressBar) view
                    .findViewById(tw.openedu.www.R.id.progressBar);
            cross_image_layout = (LinearLayout) view
                    .findViewById(tw.openedu.www.R.id.close_btn_layout);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        DownloadEntryAdapter.Item item = getItem(position);
        if (item != null) onItemClicked(item);
    }

    public abstract void onItemClicked(DownloadEntryAdapter.Item model);

    public abstract void onDeleteClicked(DownloadEntryAdapter.Item model);

    public interface Item {
        @NonNull
        String getTitle();

        @NonNull
        String getDuration();

        @NonNull
        Status getStatus();

        /**
         * @return Total download size in bytes, or null if size is not yet known
         */
        @Nullable
        Long getTotalByteCount();

        long getDownloadedByteCount();

        int getPercent();

        enum Status {
            PENDING,
            DOWNLOADING,
            FAILED
        }
    }
}
