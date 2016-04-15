package tw.openedu.www.http.serialization;

import com.google.gson.annotations.SerializedName;

import tw.openedu.www.model.json.SuccessResponse;

/**
 * Created by yervant on 1/19/15.
 */
public class ShareCourseResult extends SuccessResponse {

    @SerializedName("share_with_facebook_friends")
    private boolean shareWithFacebookFriends;

    public ShareCourseResult() {
        super(false);
    }

    @Override
    public boolean isSuccess() {
        return shareWithFacebookFriends;
    }

    @Override
    public void setSuccess(boolean success) {
        super.setSuccess(success);
        shareWithFacebookFriends = success;
    }
}
