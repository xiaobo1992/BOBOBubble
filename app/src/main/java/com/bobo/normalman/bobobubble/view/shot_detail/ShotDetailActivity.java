package com.bobo.normalman.bobobubble.view.shot_detail;

import android.support.v4.app.Fragment;

import com.bobo.normalman.bobobubble.view.base.SingleFragmentActivity;

/**
 * Created by xiaobozhang on 8/20/17.
 */

public class ShotDetailActivity extends SingleFragmentActivity {

    @Override
    public Fragment newFragment() {
        return ShotFragment.newInstance(getIntent().getExtras());
    }

}
