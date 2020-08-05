package com.example.cookshare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

public class PostRecipeActivity extends AppCompatActivity {

    public static final String TAG = "PostRecipeActivity";
    Post mPost;
    TextView mTvRecipeNameDetails;
    TextView mTvRecipeDetails;
    ImageView mIvFoodDetails;
    ImageButton mBtnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_recipe);

        mBtnSave = findViewById(R.id.btnSave);
        mBtnSave.setBackgroundResource(android.R.color.transparent);
        mTvRecipeNameDetails = findViewById(R.id.tvRecipeNameDetails);
        mTvRecipeDetails = findViewById(R.id.tvRecipeDetails);
        mIvFoodDetails = findViewById(R.id.ivFoodDetails);
        mPost = (Post) Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));
        Log.d("PostRecipeActivity", String.format("Showing details for '%s'", mPost.getDescription()));

        if (mPost.getFavorited().contains(ParseUser.getCurrentUser().getObjectId())){
            mBtnSave.setBackgroundResource(R.drawable.ic_baseline_star_24);
        }
        else {
            mBtnSave.setBackgroundResource(android.R.color.transparent);
        }

        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!(mPost.getFavorited().contains(ParseUser.getCurrentUser().getObjectId()))) {

                    mPost.getFavorited().add(ParseUser.getCurrentUser().getObjectId());
                    mPost.setFavorited(mPost.getFavorited());
                    view.setBackgroundResource(R.drawable.ic_baseline_star_24);


                } else if (mPost.getFavorited().contains(ParseUser.getCurrentUser().getObjectId())) {

                    mPost.getFavorited().remove(ParseUser.getCurrentUser().getObjectId());
                    mPost.setFavorited(mPost.getFavorited());
                    mPost.put("favorited", mPost.getFavorited());
                    view.setBackgroundResource(android.R.color.transparent);


                }

                savePost(mPost);
//                notifyDataSetChanged();
            }
        });
        mTvRecipeNameDetails.setText(mPost.getDescription());
        mTvRecipeDetails.setText(mPost.getRecipe());
        ParseFile image = mPost.getImage();
        if (image != null) {
            Glide.with(this).load(mPost.getImage().getUrl()).into(mIvFoodDetails);
        }

    }
    private void savePost(Post post) {


        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                //check if there is an exception
                if (e != null) {
                    Log.e(TAG, "error while saving", e);
                    Toast.makeText(getApplicationContext(), "error while saving", Toast.LENGTH_SHORT).show();
                }

                Log.i(TAG, "saved post!");

            }
        });
    }
}