package com.bobo.normalman.bobobubble.view.bucket;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bobo.normalman.bobobubble.R;
import com.bobo.normalman.bobobubble.model.Bucket;
import com.bobo.normalman.bobobubble.view.shot_list.ShotListActivity;
import com.bobo.normalman.bobobubble.view.shot_list.ShotListFragment;

import java.util.List;

/**
 * Created by xiaobozhang on 8/27/17.
 */

public class BucketListAdapter extends RecyclerView.Adapter {
    static final int BUCKET_LIST_LOADING = 0;
    static final int BUCKET_LIST_INFO = 1;
    List<Bucket> data;
    LoadMoreListener loadMoreListener;
    boolean enableLoading;
    BucketListFragment fragment;
    boolean isChoosingMode;

    public BucketListAdapter(BucketListFragment fragment, List<Bucket> buckets, LoadMoreListener listener, boolean isChoosingMode) {
        this.data = buckets;
        this.loadMoreListener = listener;
        this.fragment = fragment;
        this.isChoosingMode = isChoosingMode;
        if (!isChoosingMode) {
            this.enableLoading = true;
        } else {
            this.enableLoading = false;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case BUCKET_LIST_INFO:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_bucket_item, parent, false);
                return new BucketInfoViewHolder(view);
            case BUCKET_LIST_LOADING:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_loading, parent, false);
                return new RecyclerView.ViewHolder(view) {
                };
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case BUCKET_LIST_LOADING:
                if (!isChoosingMode) {
                    loadMoreListener.loadMore();
                }
                break;
            case BUCKET_LIST_INFO:
                final Bucket bucket = data.get(position);
                final BucketInfoViewHolder bucketInfoViewHolder = (BucketInfoViewHolder) holder;
                if (isChoosingMode) {
                    bucketInfoViewHolder.checkBox.setVisibility(View.VISIBLE);
                    bucketInfoViewHolder.checkBox.setChecked(bucket.isChoosing);

                    bucketInfoViewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            bucket.isChoosing = !bucket.isChoosing;
                            notifyDataSetChanged();
                        }
                    });

                } else {
                    bucketInfoViewHolder.checkBox.setVisibility(View.INVISIBLE);
                }
                bucketInfoViewHolder.name.setText(bucket.name);
                bucketInfoViewHolder.shotcount.setText(String.valueOf(bucket.shots_count));
                bucketInfoViewHolder.name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Context context = bucketInfoViewHolder.itemView.getContext();
                        Intent intent = new Intent(context, ShotListActivity.class);
                        intent.putExtra(ShotListFragment.KEY_LIST_TYPE, ShotListFragment.KEY_BUCKET_SHOT);
                        intent.putExtra(ShotListFragment.KEY_BUCKET_ID, bucket.id);
                        context.startActivity(intent);
                    }
                });
                break;
            default:
                return;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < data.size()) {
            return BUCKET_LIST_INFO;
        } else {
            return BUCKET_LIST_LOADING;
        }
    }

    public void setData(List<Bucket> buckets) {
        this.data = buckets;
        notifyDataSetChanged();
    }

    public void appendData(List<Bucket> moreBucket) {
        data.addAll(moreBucket);
        notifyDataSetChanged();
    }

    public void addBucket(Bucket bucket) {
        data.add(bucket);
        notifyDataSetChanged();
    }


    public void setEnableLoading(boolean loading) {
        this.enableLoading = loading;
        notifyDataSetChanged();
    }

    public int getDataCount() {
        return data.size();
    }

    @Override
    public int getItemCount() {
        return enableLoading == true ? data.size() + 1 : data.size();
    }

    public interface LoadMoreListener {
        public void loadMore();
    }
}
