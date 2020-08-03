package com.example.cookshare.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.cookshare.GridAdapter;
import com.example.cookshare.Post;
import com.example.cookshare.PostsAdapter;
import com.example.cookshare.ProfilePicture;
import com.example.cookshare.R;
import com.example.cookshare.User;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class UserFragment extends HomeFragment {
    private TextView mTvUsername;
    private TextView mTvNumPosts;
    private TextView mBtnChangeProfilePicture;
    private ImageView mIvProfilePicture;


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override

        protected void queryFilterPosts(String s) {
            ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
            query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());

        query.whereMatches(Post.KEY_DESCRIPTION,"(?i)" + s);
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
                    //clear here before adding posts
                    mAllPosts.clear();
                    mAllPosts.addAll(posts);
                    mAdapter.notifyDataSetChanged();
                    mSwipeContainer.setRefreshing(false);
                }
            });
        }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTvUsername = view.findViewById(R.id.tvUsername);
        mTvUsername.setText(ParseUser.getCurrentUser().getUsername());
        mIvProfilePicture = view.findViewById(R.id.ivProfilePicture);
        mBtnChangeProfilePicture = view.findViewById(R.id.btnChangeProfilePicture);
        mBtnChangeProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ProfilePicture.class);
                startActivity(i);
            }
        });

        ParseFile profileImage = ParseUser.getCurrentUser().getParseFile(User.KEY_PICTURE);

        if (profileImage != null) {
            Glide.with(getContext()).load(profileImage.getUrl()).into(mIvProfilePicture);
        }


        //post query
        mTvNumPosts = view.findViewById(R.id.tvNumPosts);
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.include(Post.KEY_USER);
        try {
            if (query.count() == 1) {
                mTvNumPosts.setText(Integer.toString(query.count()) + " post");
            } else {
                mTvNumPosts.setText(Integer.toString(query.count()) + " posts");
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
    private void loadImages(ParseFile thumbnail, final ImageView img) {

        if (thumbnail != null) {
            Log.i(TAG, "thumbnail is not null");
            thumbnail.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                        img.setImageBitmap(bmp);
                    } else {
                    }
                }
            });
        } else {
            Log.i(TAG, "thumbnail is null");
            img.setImageResource(R.drawable.ic_baseline_person_24);
        }
    }// load image
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
                mAllPosts.clear();
                mAllPosts.addAll(posts);
                mAdapter.notifyDataSetChanged();
                mSwipeContainer.setRefreshing(false);
            }
        });
    }

}
