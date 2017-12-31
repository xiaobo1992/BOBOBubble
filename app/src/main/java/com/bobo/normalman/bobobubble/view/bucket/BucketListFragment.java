package com.bobo.normalman.bobobubble.view.bucket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bobo.normalman.bobobubble.R;
import com.bobo.normalman.bobobubble.dribble.Dribble;
import com.bobo.normalman.bobobubble.model.Bucket;
import com.bobo.normalman.bobobubble.util.ModelUtil;
import com.bobo.normalman.bobobubble.view.base.DribbleTask;
import com.bobo.normalman.bobobubble.view.base.SpaceItemDecoration;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xiaobozhang on 8/27/17.
 */

public class BucketListFragment extends Fragment {
    @BindView(R.id.recycle_view)
    RecyclerView recyclerView;
    @BindView(R.id.bucket_list_add)
    FloatingActionButton addFab;
    final static int COUNT_PER_PAGE = 12;
    BucketListAdapter adapter;

    public static String KEY_BUCKET_NAME = "name";
    public static String KEY_BUCKET_DESCRIPTION = "description";
    public static String KEY_BUCKET_ADD = "add_bucket";
    public static String KEY_BUCKET_REMOVE = "remove_bucket";

    public static int REQ_NEW_BUCKET = 1000;
    public HashSet<String> chosenBucket;
    public boolean isChoosingMode;
    public String shotId;
    public int bucket_count;

    public static BucketListFragment newInstance(boolean isChoosingMode, String shotId) {
        BucketListFragment fragment = new BucketListFragment();
        Bundle args = new Bundle();
        args.putBoolean(BucketListActivity.KEY_CHOOSE_MODE, isChoosingMode);
        args.putString(BucketListActivity.KEY_SHOT_ID, shotId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycle_view_with_fab, container, false);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SpaceItemDecoration(
                getResources().getDimensionPixelOffset(R.dimen.medium_margin)));
        isChoosingMode = getArguments().getBoolean(BucketListActivity.KEY_CHOOSE_MODE);
        if (!isChoosingMode) {
            getActivity().setTitle(R.string.buckets);
        } else {
            getActivity().setTitle(R.string.choosebuckets);
        }
        if (isChoosingMode) {
            shotId = getArguments().getString(BucketListActivity.KEY_SHOT_ID);
            AsyncTaskCompat.executeParallel(new LoadAllBucketsTask(shotId));
            adapter = new BucketListAdapter(this, new ArrayList<Bucket>(), new BucketListAdapter.LoadMoreListener() {
                @Override
                public void loadMore() {
                    AsyncTaskCompat.executeParallel(new LoadMoreBuckets(adapter.getDataCount() / COUNT_PER_PAGE + 1));
                }
            }, true);
        } else {
            adapter = new BucketListAdapter(this, new ArrayList<Bucket>(), new BucketListAdapter.LoadMoreListener() {
                @Override
                public void loadMore() {
                    AsyncTaskCompat.executeParallel(new LoadMoreBuckets(adapter.getDataCount() / COUNT_PER_PAGE + 1));
                }
            }, false);

        }


        recyclerView.setAdapter(adapter);
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewBucketDialogFragment dialog = NewBucketDialogFragment.newInstance();
                dialog.setTargetFragment(BucketListFragment.this, REQ_NEW_BUCKET);
                dialog.show(getFragmentManager(), NewBucketDialogFragment.TAG);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_NEW_BUCKET && resultCode == Activity.RESULT_OK) {
            String name = data.getStringExtra(KEY_BUCKET_NAME);
            String description = data.getStringExtra(KEY_BUCKET_DESCRIPTION);
            if (!TextUtils.isEmpty(name)) {
                AsyncTaskCompat.executeParallel(new addABucketTask(name, description));
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (isChoosingMode) {
            inflater.inflate(R.menu.menu_save, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save_save:
                List<String> addBucketId = new ArrayList<String>();
                List<String> removeBucketId = new ArrayList<String>();

                for (Bucket bucket : adapter.data) {
                    if (bucket.isChoosing && !chosenBucket.contains(bucket.id)) {
                        addBucketId.add(bucket.id);
                    }
                }

                for (Bucket bucket : adapter.data) {
                    if (!bucket.isChoosing && chosenBucket.contains(bucket.id)) {
                        removeBucketId.add(bucket.id);
                    }
                }
                Intent intent = new Intent();
                String addBucket = ModelUtil.toString(addBucketId, new TypeToken<List<String>>() {
                });
                String removeBucket = ModelUtil.toString(removeBucketId, new TypeToken<List<String>>() {
                });
                intent.putExtra(KEY_BUCKET_ADD, addBucket);
                intent.putExtra(KEY_BUCKET_REMOVE, removeBucket);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class addABucketTask extends DribbleTask<Object, Void, Bucket> {
        String name;
        String description;

        public addABucketTask(String name, String description) {
            this.name = name;
            this.description = description;
        }


        @Override
        public void onSuccess(Bucket bucket) {
            if (bucket != null) {
                adapter.addBucket(bucket);
            }
        }

        @Override
        public void onFailure(Exception exception) {

        }

        @Override
        public Bucket doJob(Object... params) throws Exception {
            return Dribble.addABucket(name, description);
        }
    }

    public class LoadMoreBuckets extends DribbleTask<Object, Void, List<Bucket>> {
        int page;

        public LoadMoreBuckets(int page) {
            this.page = page;
        }

        @Override
        public void onSuccess(List<Bucket> buckets) {
            if (buckets != null) {
                adapter.appendData(buckets);
                adapter.setEnableLoading(buckets.size() == COUNT_PER_PAGE);
            }
        }

        @Override
        public void onFailure(Exception exception) {

        }

        @Override
        public List<Bucket> doJob(Object... params) throws Exception {
            return Dribble.getBuckets(page);
        }
    }

    public class LoadAllBucketsTask extends DribbleTask<Object, Void, List<Bucket>> {
        String shotID;

        public LoadAllBucketsTask(String shotID) {
            this.shotID = shotID;
        }

        @Override
        public void onSuccess(List<Bucket> buckets) {
            adapter.setData(buckets);
        }

        @Override
        public void onFailure(Exception exception) {

        }

        @Override
        public List<Bucket> doJob(Object... params) throws Exception {
            List<Bucket> userBucket = Dribble.getAllUserBucket();
            List<Bucket> allBucket = Dribble.getAllBucketForShot(shotID);
            chosenBucket = new HashSet<String>();
            for (Bucket bucket : allBucket) {
                chosenBucket.add(bucket.id);
            }
            for (Bucket bucket : userBucket) {
                if (chosenBucket.contains(bucket.id)) {
                    bucket.isChoosing = true;
                }
            }
            return userBucket;
        }
    }
}
