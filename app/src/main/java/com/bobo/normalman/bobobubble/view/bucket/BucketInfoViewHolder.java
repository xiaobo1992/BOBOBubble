package com.bobo.normalman.bobobubble.view.bucket;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bobo.normalman.bobobubble.R;
import com.bobo.normalman.bobobubble.view.base.BaseViewHolder;

import butterknife.BindView;

/**
 * Created by xiaobozhang on 8/27/17.
 */

public class BucketInfoViewHolder extends BaseViewHolder {
    @BindView(R.id.bucket_list_bucket_name)
    TextView name;
    @BindView(R.id.bucket_list_shot_count)
    TextView shotcount;
    @BindView(R.id.bucket_list_cover)
    View cover;
    @BindView(R.id.bucket_list_checkbox)
    CheckBox checkBox;

    public BucketInfoViewHolder(View itemView) {
        super(itemView);
    }
}
