package com.example.cookshare.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cookshare.GridAdapter;
import com.example.cookshare.Post;
import com.example.cookshare.PostsAdapter;
import com.example.cookshare.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class UserFragment extends HomeFragment {
    private TextView tvUsername;
    private TextView tvNumPosts;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvUsername = view.findViewById(R.id.tvUsername);
        tvUsername.setText(ParseUser.getCurrentUser().getUsername());

        tvNumPosts = view.findViewById(R.id.tvNumPosts);
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.include(Post.KEY_USER);
        try {
            if (query.count() == 1){
            tvNumPosts.setText(Integer.toString(query.count()) + " post");}
            else{
                tvNumPosts.setText(Integer.toString(query.count()) + " posts");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mSwipeContainer = view.findViewById(R.id.swipeContainer);
        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "fetching new data!");
                mAdapter.clear();
                queryPosts();
            }
        });
        mRvPosts = view.findViewById(R.id.rvPosts);
        mAllPosts = new ArrayList<>();
//        mAdapter = new PostsAdapter(getContext(), mAllPosts);
        mAdapter = new GridAdapter(getContext(), mAllPosts);
        mRvPosts.setAdapter(mAdapter);

        mRvPosts.setLayoutManager(new GridLayoutManager(getContext(), 2));
//        mRvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        queryPosts();
        mSwipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


    }

    @Override
    protected void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.include(Post.KEY_USER);

        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                // check if there is an exception e then return
                if (e != null) {
                    Log.e(TAG, "issue with getting posts", e);
                    return;
                }
                //iterate through each post and log each of them
                for (Post post : posts) {
                    Log.i(TAG, "Post: " + post.getDescription() + " " + post.getUser().getUsername());
                }
                mAllPosts.addAll(posts);
                mAdapter.notifyDataSetChanged();
                mSwipeContainer.setRefreshing(false);
            }
        });
    }

}
