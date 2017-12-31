package com.bobo.normalman.bobobubble.view.user_detail;

import com.bobo.normalman.bobobubble.view.base.SingleFragmentActivity;

/**
 * Created by xiaobozhang on 8/21/17.
 */

public class UserActivity extends SingleFragmentActivity {

    @Override
    public UserDetailFragment newFragment() {
        return UserDetailFragment.newInstance(getIntent().getExtras());
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
    }
}
