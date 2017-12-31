package com.bobo.normalman.bobobubble.view.user_detail;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bobo.normalman.bobobubble.R;
import com.bobo.normalman.bobobubble.model.Shot;
import com.bobo.normalman.bobobubble.model.User;
import com.bobo.normalman.bobobubble.util.ModelUtil;
import com.bobo.normalman.bobobubble.view.shot_detail.ShotDetailActivity;
import com.bobo.normalman.bobobubble.view.shot_detail.ShotFragment;
import com.bobo.normalman.bobobubble.view.shot_list.ShotListViewHolder;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by xiaobozhang on 8/21/17.
 */

public class UserDetailAdapter extends RecyclerView.Adapter {
    private final static int USER_DETAIL_PROFILE = 0;
    private final static int USER_DETAIL_SHOT = 1;
    private final static int USER_DETAIL_LOADING = 2;
    private boolean enableLoading;
    List<Shot> data;
    loadMoreListener loadMoreListener;
    User user;

    public UserDetailAdapter(List<Shot> shot, User user, loadMoreListener loadMoreListener) {
        this.data = shot;
        enableLoading = true;
        this.loadMoreListener = loadMoreListener;
        this.user = user;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case USER_DETAIL_PROFILE:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.user_detail_profile, parent, false);
                return new UserProfileViewHolder(view);
            case USER_DETAIL_LOADING:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_loading, parent, false);
                return new RecyclerView.ViewHolder(view) {
                };
            case USER_DETAIL_SHOT:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_shot, parent, false);
                return new ShotListViewHolder(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case USER_DETAIL_PROFILE:
                UserProfileViewHolder userProfileViewHolder = (UserProfileViewHolder) holder;
                userProfileViewHolder.imageView.setImageURI(user.avatar_url);
                userProfileViewHolder.user.setText(user.name);
                userProfileViewHolder.location.setText(user.location);
                userProfileViewHolder.commentCount.setText(String.valueOf(user.comments_received_count));
                userProfileViewHolder.followerCount.setText(String.valueOf(user.followers_count));
                userProfileViewHolder.shotConut.setText(String.valueOf(user.shots_count));
                userProfileViewHolder.likeCount.setText(String.valueOf(user.likes_received_count));
                break;
            case USER_DETAIL_SHOT:
                final Shot shot = data.get(position - 1);
                shot.user = user;
                final ShotListViewHolder shotListViewHolder = (ShotListViewHolder) holder;
                shotListViewHolder.imageView.setImageURI(shot.getImageUrl());
                shotListViewHolder.textViewCommentCount.setText(String.valueOf(shot.comments_count));
                shotListViewHolder.textViewViewCount.setText(String.valueOf(shot.views_count));
                shotListViewHolder.textViewLikeCount.setText(String.valueOf(shot.likes_count));
                shotListViewHolder.cover.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Context context = shotListViewHolder.itemView.getContext();
                        String str = ModelUtil.toString(shot, new TypeToken<Shot>() {
                        });
                        Log.d("shot str", str);
                        Intent intent = new Intent(context, ShotDetailActivity.class);
                        intent.putExtra(ShotFragment.KEY_SHOT, str);
                        context.startActivity(intent);
                    }
                });
                break;
            case USER_DETAIL_LOADING:
                loadMoreListener.onLoadMore();
                break;
        }
    }

    @Override
    public int getItemCount() {
        return enableLoading ? data.size() + 2 : data.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return USER_DETAIL_PROFILE;
        } else if (position <= data.size()) {
            return USER_DETAIL_SHOT;
        } else {
            return USER_DETAIL_LOADING;
        }
    }


    public int getDataCount() {
        return data.size();
    }

    public void append(List<Shot> moreShot) {
        this.data.addAll(moreShot);
        notifyDataSetChanged();
    }

    public void setEnableLoading(boolean enableLoading) {
        this.enableLoading = enableLoading;
        notifyDataSetChanged();
    }

    public interface loadMoreListener {
        void onLoadMore();
    }
}
