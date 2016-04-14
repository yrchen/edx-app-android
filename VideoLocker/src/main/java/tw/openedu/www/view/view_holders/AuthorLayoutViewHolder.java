package tw.openedu.www.view.view_holders;

import android.view.View;
import android.widget.TextView;

import tw.openedu.www.R;

public class AuthorLayoutViewHolder {

    public final TextView discussionAuthorTextView;

    public AuthorLayoutViewHolder(View itemView) {
        discussionAuthorTextView = (TextView) itemView.
                findViewById(R.id.discussion_author_layout_author_text_view);
    }
}
