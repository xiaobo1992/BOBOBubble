package com.bobo.normalman.bobobubble.view.shot_list;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bobo.normalman.bobobubble.R;
import com.bobo.normalman.bobobubble.dribble.Dribble;
import com.bobo.normalman.bobobubble.model.Shot;
import com.bobo.normalman.bobobubble.util.ModelUtil;
import com.bobo.normalman.bobobubble.view.base.DribbleTask;
import com.bobo.normalman.bobobubble.view.base.SpaceItemDecoration;
import com.bobo.normalman.bobobubble.view.shot_detail.ShotFragment;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xiaobozhang on 8/18/17.
 */

public class ShotListFragment extends Fragment {
    static int COUNT_PER_PAGE = 12;
    public static final String KEY_LIST_TYPE = "KEY_LIST_TYPE";
    public static final String KEY_BUCKET_ID = "KEY_BUCKET_ID";

    protected static final int KEY_SHOT_DETAIL_ACTIVITY = 100;

    public static final int KEY_POPULAR_SHOT = 0;
    public static final int KEY_TEAMS_SHOT = 1;
    public static final int KEY_DEBUTS_SHOT = 2;
    public static final int KEY_ATTACHMENTS_SHOT = 3;
    public static final int KEY_ANIMATED_SHOT = 4;
    public static final int KEY_LIKE_SHOT = 7;
    public static final int KEY_BUCKET_SHOT = 5;


    public static String KEY_ANIMATED_LIST = "animated";
    public static String KEY_TEAMS_LIST = "teams";
    public static String KEY_DEBUTS_LIST = "debuts";
    public static String KEY_ATTACHMENTS_LIST = "attachments";


    int listType;

    @BindView(R.id.recycle_view)
    RecyclerView recyclerView;
    ShotListAdapter adapter;
    String bucketId;

    public static ShotListFragment newInstance(int listType) {
        ShotListFragment fragment = new ShotListFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_LIST_TYPE, listType);
        fragment.setArguments(args);
        return fragment;
    }

    public static ShotListFragment newInstance(Bundle args) {
        ShotListFragment fragment = new ShotListFragment();
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
        listType = getArguments().getInt(KEY_LIST_TYPE, -1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SpaceItemDecoration(getResources()
                .getDimensionPixelOffset(R.dimen.small_margin)));
        if (listType == KEY_BUCKET_SHOT) {
            bucketId = getArguments().getString(KEY_BUCKET_ID);
            adapter = new ShotListAdapter(this, new ArrayList<Shot>(),
                    new ShotListAdapter.loadMoreListener() {
                @Override
                public void onLoadMore() {
                    AsyncTaskCompat.executeParallel(new LoadShotForBucket(bucketId,
                            adapter.getDataCount() / COUNT_PER_PAGE));
                }
            });
        } else {

            adapter = new ShotListAdapter(this, new ArrayList<Shot>(), new ShotListAdapter.loadMoreListener() {
                @Override
                public void onLoadMore() {
                    AsyncTaskCompat.executeParallel(new loadMoreTask(adapter.getDataCount() / COUNT_PER_PAGE + 1, listType))
                    ;
                }
            });
        }
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == KEY_SHOT_DETAIL_ACTIVITY && resultCode == Activity.RESULT_OK) {
            Shot targetShot = ModelUtil.toObject(data.getStringExtra(ShotFragment.KEY_SHOT), new TypeToken<Shot>() {
            });
            for (Shot shot : adapter.data) {
                if (TextUtils.equals(shot.id, targetShot.id)) {
                    shot.liked = targetShot.liked;
                    shot.likes_count = targetShot.likes_count;
                    shot.buckets_count = targetShot.buckets_count;
                    adapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    private class loadMoreTask extends AsyncTask<Void, Void, List<Shot>> {
        int page;
        int listType;

        public loadMoreTask(int page, int listType) {
            this.page = page;
            this.listType = listType;
        }

        @Override
        protected List<Shot> doInBackground(Void... voids) {
            try {
                switch (listType) {
                    case KEY_TEAMS_SHOT:
                        return Dribble.getShots(page, KEY_TEAMS_LIST);
                    case KEY_ANIMATED_SHOT:
                        return Dribble.getShots(page, KEY_ANIMATED_LIST);
                    case KEY_ATTACHMENTS_SHOT:
                        return Dribble.getShots(page, KEY_ATTACHMENTS_LIST);
                    case KEY_DEBUTS_SHOT:
                        return Dribble.getShots(page, KEY_DEBUTS_LIST);
                    case KEY_POPULAR_SHOT:
                        return Dribble.getShots(page);
                    case KEY_LIKE_SHOT:
                        return Dribble.getLikeShots(page);
                    default:
                        return null;
                }

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Shot> shots) {
            if (shots != null) {
                adapter.append(shots);
                adapter.setShowLoading(shots.size() == COUNT_PER_PAGE);
            } else {
                Snackbar.make(getView(), "Error!", Snackbar.LENGTH_LONG).show();
            }

        }
    }

    public class LoadShotForBucket extends DribbleTask<Object, Void, List<Shot>> {
        String bucketId;
        int page;

        public LoadShotForBucket(String bucket_id, int page) {
            this.bucketId = bucket_id;
        }

        @Override
        public void onSuccess(List<Shot> shots) {
            adapter.append(shots);
            adapter.setShowLoading(shots.size() == COUNT_PER_PAGE);
        }

        @Override
        public void onFailure(Exception exception) {

        }

        @Override
        public List<Shot> doJob(Object... params) throws Exception {
            return Dribble.listShotForBucket(bucketId, page);
        }
    }
}
