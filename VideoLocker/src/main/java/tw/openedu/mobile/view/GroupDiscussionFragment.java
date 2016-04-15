package tw.openedu.mobile.view;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tw.openedu.mobile.R;
import tw.openedu.mobile.base.BaseFragment;

public class GroupDiscussionFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_group_discussion, container, false);
    }
}
