package com.example.cookshare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private EditText etRecipe;
    private EditText etRecipeName;
    private ImageButton btnTakePicture;
    private Button btnPost;
    private ImageView ivFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etRecipe = findViewById(R.id.etRecipe);
        etRecipeName = findViewById(R.id.etRecipeName);
        btnTakePicture = findViewById(R.id.btnTakePicture);
        btnPost = findViewById(R.id.btnPost);
        ivFood = findViewById(R.id.ivFood);
        queryPosts();
        
    }

    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                // check if there is an exception e then return
                if (e!=null){
                    Log.e(TAG, "issue with getting posts", e);
                    return;
                }
                //iterate through each post and log each of them
                for (Post post: posts){
                    Log.i(TAG, "Post: " + post.getDescription() +  " " + post.getUser().getUsername());
                }

            }
        });
    }
}