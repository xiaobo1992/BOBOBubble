package com.bobo.normalman.bobobubble.view.shot_list;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bobo.normalman.bobobubble.R;
import com.bobo.normalman.bobobubble.model.Shot;
import com.bobo.normalman.bobobubble.util.ModelUtil;
import com.bobo.normalman.bobobubble.view.shot_detail.ShotDetailActivity;
import com.bobo.normalman.bobobubble.view.shot_detail.ShotFragment;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by xiaobozhang on 8/18/17.
 */

public class ShotListAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_SHOT = 1;
    private static final int VIEW_TYPE_LOADING = 2;
    private boolean showLoading;

    protected List<Shot> data;
    private loadMoreListener loadMoreListener;
    ShotListFragment shotListFragment;
    public ShotListAdapter(ShotListFragment shotListFragment, List<Shot> data, loadMoreListener loadMoreListener) {
        this.shotListFragment = shotListFragment;
        this.data = data;
        this.showLoading = true;
        this.loadMoreListener = loadMoreListener;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_loading, parent, false);
            return new RecyclerView.ViewHolder(view) {
            };
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_shot, parent, false);
            return new ShotListViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final int viewType = getItemViewType(position);
        if (viewType == VIEW_TYPE_LOADING) {
            loadMoreListener.onLoadMore();
        } else if (viewType == VIEW_TYPE_SHOT) {
            final ShotListViewHolder viewHolder = (ShotListViewHolder) holder;
            final Shot shot = data.get(position);
            viewHolder.textViewViewCount.setText(String.valueOf(shot.views_count));
            viewHolder.textViewLikeCount.setText(String.valueOf(shot.likes_count));
            viewHolder.textViewCommentCount.setText(String.valueOf(shot.comments_count));
            viewHolder.textViewBucketCount.setText(String.valueOf(shot.buckets_count));
            if (shot.getImageUrl() != null) {
                viewHolder.imageView.setImageURI(Uri.parse(shot.getImageUrl()));
            }
            viewHolder.cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = holder.itemView.getContext();
                    String str = ModelUtil.toString(shot, new TypeToken<Shot>() {
                    });
                    Intent intent = new Intent(context, ShotDetailActivity.class);
                    intent.putExtra(ShotFragment.KEY_SHOT, str);
                    shotListFragment.startActivityForResult(intent, ShotListFragment.KEY_SHOT_DETAIL_ACTIVITY);
                }
            });

        }
    }


    @Override
    public int getItemCount() {
        return showLoading == true ? data.size() + 1 : data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position < data.size() ? VIEW_TYPE_SHOT : VIEW_TYPE_LOADING;
    }

    public void append(List<Shot> moreShots) {
        data.addAll(moreShots);
        notifyDataSetChanged();
    }

    public int getDataCount() {
        return data.size();
    }

    public void setShowLoading(boolean showLoading) {
        this.showLoading = showLoading;
        notifyDataSetChanged();
    }

    public interface loadMoreListener {
        void onLoadMore();
    }

}
