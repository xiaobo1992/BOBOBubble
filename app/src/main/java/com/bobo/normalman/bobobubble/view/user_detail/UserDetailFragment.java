package com.bobo.normalman.bobobubble.view.user_detail;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import com.bobo.normalman.bobobubble.model.User;
import com.bobo.normalman.bobobubble.util.ModelUtil;
import com.bobo.normalman.bobobubble.view.base.SpaceItemDecoration;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xiaobozhang on 8/21/17.
 */

public class UserDetailFragment extends Fragment {
    static int COUNT_PER_PAGE = 12;
    public static final String KEY_USER_DETAIL_SHOTS_URL = "USER_DETAIL_SHOTS_URL";
    public static final String KEY_USER_DETAIL_USER = "USER_DETAIL_USER";
    @BindView(R.id.recycle_view)
    RecyclerView recyclerView;
    UserDetailAdapter adapter;

    public static UserDetailFragment newInstance(Bundle args) {
        UserDetailFragment fragment = new UserDetailFragment();
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
        final User user = ModelUtil.toObject(getArguments().getString(KEY_USER_DETAIL_USER), new TypeToken<User>(){});
        this.adapter = new UserDetailAdapter(new ArrayList<Shot>(), user, new UserDetailAdapter.loadMoreListener() {
            @Override
            public void onLoadMore() {
                AsyncTaskCompat.executeParallel(new loadMoreTask(adapter.getDataCount() / COUNT_PER_PAGE + 1, user.shots_url));
            }
        });

        getActivity().setTitle(user.name);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelOffset(R.dimen.medium_margin)));
        recyclerView.setAdapter(adapter);
    }

    public class loadMoreTask extends AsyncTask<Void, Void, List<Shot>> {
        int page;
        String url;
        public loadMoreTask(int page, String url) {
            this.page = page;
            this.url = url;
        }


        @Override
        protected List<Shot> doInBackground(Void... voids) {
            Request request = Dribble.buildGetRequest(url + "?page=" + page);
            try {
                Response response = Dribble.makeRequest(request);
                return Dribble.parseResponse(response, new TypeToken<List<Shot>>(){});
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Shot> shots) {
            if (shots != null) {
                adapter.append(shots);
                adapter.setEnableLoading(shots.size() == COUNT_PER_PAGE);
            } else {
                Snackbar.make(getView(), "Error!", Snackbar.LENGTH_LONG).show();
            }
        }
    }

}
