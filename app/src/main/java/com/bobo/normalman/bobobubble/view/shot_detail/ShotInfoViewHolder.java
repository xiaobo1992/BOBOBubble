package com.bobo.normalman.bobobubble.view.shot_detail;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bobo.normalman.bobobubble.R;
import com.bobo.normalman.bobobubble.view.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;

/**
 * Created by xiaobozhang on 8/20/17.
 */

public class ShotInfoViewHolder extends BaseViewHolder {

    //@BindView(R.id.shot_detail_description)
    //WebView TextViewShotDescription;
    @BindView(R.id.shot_detail_comment_count) TextView comment;
    @BindView(R.id.shot_detail_view_count) TextView view;
    @BindView(R.id.shot_detail_like_count) TextView like;
    @BindView(R.id.shot_detail_profile) SimpleDraweeView profile;
    @BindView(R.id.shot_detail_name) TextView name;
    @BindView(R.id.shot_detail_user) LinearLayout cover;
    @BindView(R.id.shot_detail_like_image) ImageView likeImage;
    @BindView(R.id.shot_detail_description) TextView description;
    @BindView(R.id.shot_detail_bucket_count) TextView bucket;
    @BindView(R.id.shot_detail_buckets) LinearLayout bucketCover;
    public ShotInfoViewHolder(View itemView) {
        super(itemView);
    }
}
