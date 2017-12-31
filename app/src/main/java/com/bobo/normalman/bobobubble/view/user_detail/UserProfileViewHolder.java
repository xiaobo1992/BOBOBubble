package com.bobo.normalman.bobobubble.view.user_detail;

import android.view.View;
import android.widget.TextView;

import com.bobo.normalman.bobobubble.R;
import com.bobo.normalman.bobobubble.view.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;

/**
 * Created by xiaobozhang on 8/22/17.
 */

public class UserProfileViewHolder extends BaseViewHolder {

    @BindView(R.id.user_detail_image)
    SimpleDraweeView imageView;

    @BindView(R.id.user_detail_name)
    TextView user;

    @BindView(R.id.user_detail_location)
    TextView location;

    @BindView(R.id.user_detail_comment_count)
    TextView commentCount;
    @BindView(R.id.user_detail_follower_count)
    TextView followerCount;

    @BindView(R.id.user_detail_like_count)
    TextView likeCount;

    @BindView(R.id.user_detail_shot_count)
    TextView shotConut;

    public UserProfileViewHolder(View itemView) {
        super(itemView);
    }
}
