package com.bobo.normalman.bobobubble.view.shot_list;

import android.support.v4.app.Fragment;

import com.bobo.normalman.bobobubble.view.base.SingleFragmentActivity;

/**
 * Created by xiaobozhang on 8/27/17.
 */

public class ShotListActivity extends SingleFragmentActivity{

    @Override
    public Fragment newFragment() {
        return ShotListFragment.newInstance(getIntent().getExtras());
    }
}
