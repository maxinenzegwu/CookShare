package com.example.cookshare.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cookshare.Post;
import com.example.cookshare.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import static com.example.cookshare.Post.KEY_FAVORITED;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LikedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LikedFragment extends HomeFragment {
//instead of using mAllposts use the adapter and include methods in adapter
    //add method that does both
    //for all fragments

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_liked, container, false);
    }

    @Override
    protected void queryPosts() {

        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereContains(Post.KEY_FAVORITED, ParseUser.getCurrentUser().getObjectId());
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
