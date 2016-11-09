package tw.openedu.android.model.course;

import com.google.gson.annotations.SerializedName;

import tw.openedu.android.model.api.TranscriptModel;

/**
 *
 */
public class VideoData extends BlockData {

    @SerializedName("duration")
    public long duration;

    @SerializedName("transcripts")
    public TranscriptModel transcripts;

    @SerializedName("only_on_web")
    public boolean onlyOnWeb;

    @SerializedName("encoded_videos")
    public EncodedVideos encodedVideos;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VideoData videoData = (VideoData) o;

        if (duration != videoData.duration) return false;
        if (onlyOnWeb != videoData.onlyOnWeb) return false;
        if (!transcripts.equals(videoData.transcripts)) return false;
        return encodedVideos.equals(videoData.encodedVideos);

    }
}
