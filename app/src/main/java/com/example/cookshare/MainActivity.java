package com.example.cookshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.cookshare.fragments.HomeFragment;
import com.example.cookshare.fragments.LikedFragment;
import com.example.cookshare.fragments.SavedFragment;
import com.example.cookshare.fragments.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    public static final String TAG = "MainActivity";
    private BottomNavigationView mBottomNavigationView;
    final FragmentManager mFragmentManager = getSupportFragmentManager();
       private List<Post> mPosts;

//    private PostsAdapter adapter = new PostsAdapter(MainActivity.this, mPosts);


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create, menu);
//        queryPosts();
//        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
//        SearchView searchView = (SearchView)searchItem.getActionView();
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                adapter.getFilter().filter(s);
//                return false;
//            }
//        });
        return true;
    }


protected void queryPosts() {
    ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
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
            mPosts.addAll(posts);


        }
    });
}
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == R.id.app_bar_search) {
//            Intent i = new Intent(this, SearchActivity.class);
//            startActivity(i);
//            return true;
//        }
        if (item.getItemId() == R.id.action_create) {
            Intent i = new Intent(MainActivity.this, CreateActivity.class);
            startActivity(i);
            return true;
        }
        if (item.getItemId() == R.id.action_logout) {
            ParseUser.logOut();
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            Toast.makeText(MainActivity.this, "logout!", Toast.LENGTH_SHORT).show();
            finish();
            return true;
        }
        if (item.getItemId() == R.id.action_favorites) {
            Fragment fragment;
            fragment = new LikedFragment();
            mFragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNavigationView = findViewById(R.id.bottom_navigation);
//        queryPosts();


        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.action_home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.action_saved:
                        fragment = new SavedFragment();
                        break;
                    case R.id.action_profile:
                    default:
                        fragment = new UserFragment();
                        break;
                }
                mFragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        // Set default selection
        mBottomNavigationView.setSelectedItemId(R.id.action_home);

    }
}