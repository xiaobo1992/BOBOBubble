package com.bobo.normalman.bobobubble.view.shot_list;

import android.view.View;
import android.widget.TextView;

import com.bobo.normalman.bobobubble.R;
import com.bobo.normalman.bobobubble.view.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;

/**
 * Created by xiaobozhang on 8/18/17.
 */

public class ShotListViewHolder extends BaseViewHolder{

    public @BindView(R.id.shot_item_image) SimpleDraweeView imageView;
    public @BindView(R.id.shot_item_comment_count) TextView textViewCommentCount;
    public @BindView(R.id.shot_item_like_count) TextView textViewLikeCount;
    public @BindView(R.id.shot_item_view_count) TextView textViewViewCount;
    public @BindView(R.id.shot_item_bucket_count) TextView textViewBucketCount;
    public @BindView(R.id.shot_item_cover) View cover;
    public ShotListViewHolder(View itemView) {
        super(itemView);
    }
}
