package com.bobo.normalman.bobobubble.view.shot_detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bobo.normalman.bobobubble.R;
import com.bobo.normalman.bobobubble.dribble.Dribble;
import com.bobo.normalman.bobobubble.model.Shot;
import com.bobo.normalman.bobobubble.util.ModelUtil;
import com.bobo.normalman.bobobubble.view.base.DribbleTask;
import com.bobo.normalman.bobobubble.view.bucket.BucketListFragment;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xiaobozhang on 8/20/17.
 */

public class ShotFragment extends Fragment {
    public static String KEY_SHOT = "SHOT";
    public static int KEY_BUCKET_ACTIVITY = 9000;
    @BindView(R.id.recycle_view)
    RecyclerView recyclerView;
    Shot shot;
    boolean isLiking;
    public static ShotFragment newInstance(Bundle args) {
        ShotFragment fragment = new ShotFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycle_view, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        shot = ModelUtil.toObject(getArguments().getString(KEY_SHOT), new TypeToken<Shot>() {
        });
        getActivity().setTitle(shot.title);
        isLiking = true;
        ShotAdapter adapter = new ShotAdapter(this, shot);
        recyclerView.setAdapter(adapter);
        AsyncTaskCompat.executeParallel(new CheckLikeTask());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == KEY_BUCKET_ACTIVITY && resultCode == Activity.RESULT_OK) {
            String addBucket = data.getStringExtra(BucketListFragment.KEY_BUCKET_ADD);
            String removeBucket = data.getStringExtra(BucketListFragment.KEY_BUCKET_REMOVE);
            List<String> addBucketId = ModelUtil.toObject(addBucket, new TypeToken<List<String>>(){});
            List<String> removeBucketId = ModelUtil.toObject(removeBucket, new TypeToken<List<String>>(){});
            AsyncTaskCompat.executeParallel(new UpdateBucketTask(addBucketId, removeBucketId));
        }
    }

    public void setResult() {
        Intent intent = new Intent();
        String shotstr= ModelUtil.toString(shot, new TypeToken<Shot>(){});
        intent.putExtra(ShotFragment.KEY_SHOT, shotstr);
        getActivity().setResult(Activity.RESULT_OK, intent);
    }

    private class CheckLikeTask extends DribbleTask<Object, Void, Boolean> {

        @Override
        public void onSuccess(Boolean result) {
            isLiking = true;
            shot.liked = result;
            recyclerView.getAdapter().notifyDataSetChanged();
        }

        @Override
        public void onFailure(Exception exception) {
            isLiking = true;
            shot.liked = false;
            recyclerView.getAdapter().notifyDataSetChanged();
        }

        @Override
        public Boolean doJob(Object... params) throws Exception {
            return Dribble.isLikeAShot(shot.id);
        }
    }

    private class LikeATask extends DribbleTask<Object, Void, Void>{


        @Override
        public void onSuccess(Void aVoid) {

        }

        @Override
        public void onFailure(Exception exception) {

        }

        @Override
        public Void doJob(Object... params) throws Exception {
            if (shot.liked) {

            } else {

            }
            return null;
        }
    }


    public class UpdateBucketTask extends DribbleTask<Object, Void, Void> {
        List<String> addBucketID;
        List<String> removeBucketID;

        public UpdateBucketTask(List<String> addBucketID, List<String> removeBucketID) {
            this.addBucketID = addBucketID;
            this.removeBucketID = removeBucketID;
        }

        @Override
        public void onSuccess(Void aVoid) {
            shot.buckets_count += (addBucketID.size() - removeBucketID.size());
            recyclerView.getAdapter().notifyDataSetChanged();
            setResult();
        }

        @Override
        public void onFailure(Exception exception) {

        }

        @Override
        public Void doJob(Object... params) throws Exception {
            for (String bucketID: addBucketID) {
                Dribble.addShotForBucket(shot.id, bucketID);
            }
            for (String bucketID: removeBucketID) {
                Dribble.removeShotForBucket(shot.id, bucketID);
            }
            return null;
        }
    }
}
