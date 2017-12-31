package com.bobo.normalman.bobobubble.view.shot_detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bobo.normalman.bobobubble.R;
import com.bobo.normalman.bobobubble.dribble.Dribble;
import com.bobo.normalman.bobobubble.model.Shot;
import com.bobo.normalman.bobobubble.model.User;
import com.bobo.normalman.bobobubble.util.ModelUtil;
import com.bobo.normalman.bobobubble.view.bucket.BucketListActivity;
import com.bobo.normalman.bobobubble.view.user_detail.UserActivity;
import com.bobo.normalman.bobobubble.view.user_detail.UserDetailFragment;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

/**
 * Created by xiaobozhang on 8/20/17.
 */

public class ShotAdapter extends RecyclerView.Adapter {
    Shot shot;
    public static final int VIEW_TYPE_IMAGE = 0;
    public static final int VIEW_TYPE_INFO = 1;
    ShotFragment shotFragment;

    public ShotAdapter(@NonNull ShotFragment shotFragment, @NonNull Shot shot) {
        this.shotFragment = shotFragment;
        this.shot = shot;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEW_TYPE_IMAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shot_detail_image, parent, false);
                return new ShotInfoImageViewHolder(view);
            case VIEW_TYPE_INFO:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shot_detail_info, parent, false);
                return new ShotInfoViewHolder(view);
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case VIEW_TYPE_IMAGE:
                final ShotInfoImageViewHolder shotInfoImageViewHolder = (ShotInfoImageViewHolder) holder;
                shotInfoImageViewHolder.imageView.setImageURI(Uri.parse(shot.getImageUrl()));
                break;
            case VIEW_TYPE_INFO:
                final ShotInfoViewHolder shotInfoViewHolder = (ShotInfoViewHolder) holder;
                //checkLikeAShot(shot);
                //shotInfoViewHolder.TextViewShotDescription.loadData(shot.description, "text/html", null);
                shot.description = shot.description == null ? "" : shot.description;
                shotInfoViewHolder.description.setText(Html.fromHtml(shot.description));
                shotInfoViewHolder.like.setText(String.valueOf(shot.likes_count));
                shotInfoViewHolder.comment.setText(String.valueOf(shot.comments_count));
                shotInfoViewHolder.view.setText(String.valueOf(shot.views_count));
                shotInfoViewHolder.name.setText(shot.user.name);
                shotInfoViewHolder.bucket.setText(String.valueOf(shot.buckets_count));
                shotInfoViewHolder.profile.setImageURI(Uri.parse(shot.user.avatar_url));
                shotInfoViewHolder.cover.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Context context = shotInfoViewHolder.itemView.getContext();
                        Intent intent = new Intent(context, UserActivity.class);
                        String userStr = ModelUtil.toString(shot.user, new TypeToken<User>() {
                        });
                        intent.putExtra(UserDetailFragment.KEY_USER_DETAIL_USER, userStr);
                        context.startActivity(intent);
                    }
                });

                Drawable drawable = shot.liked ?
                        ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite_pink_24px) :
                        ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite_black_24px);
                shotInfoViewHolder.likeImage.setImageDrawable(drawable);

                shotInfoViewHolder.likeImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (shot.liked) {
                            dislikeAShot(shot, shotInfoViewHolder);
                        } else {
                            likeAShot(shot, shotInfoViewHolder);
                        }
                    }
                });

                shotInfoViewHolder.bucketCover.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Context context = shotInfoViewHolder.itemView.getContext();
                        Intent intent = new Intent(context, BucketListActivity.class);
                        intent.putExtra(BucketListActivity.KEY_SHOT_ID, shot.id);
                        intent.putExtra(BucketListActivity.KEY_CHOOSE_MODE, true);
                        shotFragment.startActivityForResult(intent, ShotFragment.KEY_BUCKET_ACTIVITY);
                    }
                });

                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return VIEW_TYPE_IMAGE;
            case 1:
                return VIEW_TYPE_INFO;
            default:
                return -1;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }


    public void likeAShot(final Shot shot, final ShotInfoViewHolder holder) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "Bearer " + Dribble.getAccessToken());
        String url = Dribble.SHOT_ENDPOINT_URL + "/" + shot.id + "/like";

        client.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                shot.liked = true;
                shot.likes_count += 1;
                shotFragment.setResult();
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("result ", "like failed");
                Log.d("response body", statusCode + "");
            }
        });
    }

    public void dislikeAShot(final Shot shot, final ShotInfoViewHolder holder) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "Bearer " + Dribble.getAccessToken());
        String url = Dribble.SHOT_ENDPOINT_URL + "/" + shot.id + "/like";
        client.delete(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                shot.liked = false;
                shot.likes_count -= 1;
                shotFragment.setResult();
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("result ", "dislike failed");
            }
        });
    }

    public Context getContext() {
        return shotFragment.getContext();
    }
}
