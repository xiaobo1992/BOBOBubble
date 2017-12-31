package com.bobo.normalman.bobobubble.view.bucket;

import com.bobo.normalman.bobobubble.view.base.SingleFragmentActivity;

/**
 * Created by xiaobozhang on 8/28/17.
 */

public class BucketListActivity extends SingleFragmentActivity {
    public static final String KEY_CHOOSE_MODE = "choosing_mode";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_SHOT_ID = "shot_id";
    public static final String KEY_BUCKETS_COUNT = "buckets_count";

    @Override
    public BucketListFragment newFragment() {
        boolean isChoosingMode = getIntent().getBooleanExtra(KEY_CHOOSE_MODE, false);
        String shotID = getIntent().getStringExtra(KEY_SHOT_ID);

        return BucketListFragment.newInstance(isChoosingMode, shotID);
    }

}
