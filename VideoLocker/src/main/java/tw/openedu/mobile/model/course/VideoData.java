package tw.openedu.mobile.model.course;

import com.google.gson.annotations.SerializedName;

import tw.openedu.mobile.model.api.TranscriptModel;

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

}
