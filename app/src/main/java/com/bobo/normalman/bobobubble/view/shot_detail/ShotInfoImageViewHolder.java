package com.bobo.normalman.bobobubble.view.shot_detail;

import android.view.View;

import com.bobo.normalman.bobobubble.R;
import com.bobo.normalman.bobobubble.view.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;

/**
 * Created by xiaobozhang on 8/20/17.
 */

public class ShotInfoImageViewHolder extends BaseViewHolder {
    @BindView(R.id.shot_detail_image)
    SimpleDraweeView imageView;

    public ShotInfoImageViewHolder(View itemView) {
        super(itemView);
    }
}
