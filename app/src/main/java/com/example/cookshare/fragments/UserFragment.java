package com.example.cookshare.fragments;

import android.util.Log;

import com.example.cookshare.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class UserFragment extends HomeFragment {

    @Override
    protected void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.include(Post.KEY_USER);
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
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });
    }

}
