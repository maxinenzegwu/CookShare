package com.example.cookshare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.Parse;
import com.parse.ParseFile;

import org.parceler.Parcels;

public class PostRecipeActivity extends AppCompatActivity {

    Post mPost;
    TextView mTvRecipeNameDetails;
    TextView mTvRecipeDetails;
    ImageView mIvFoodDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_recipe);
        mTvRecipeNameDetails = findViewById(R.id.tvRecipeNameDetails);
        mTvRecipeDetails = findViewById(R.id.tvRecipeDetails);
        mIvFoodDetails = findViewById(R.id.ivFoodDetails);
        mPost = (Post) Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));
        Log.d("PostRecipeActivity", String.format("Showing details for '%s'", mPost.getDescription()));
        mTvRecipeNameDetails.setText(mPost.getDescription());
        mTvRecipeDetails.setText(mPost.getRecipe());
        ParseFile image = mPost.getImage();
        if (image != null) {
            Glide.with(this).load(mPost.getImage().getUrl()).into(mIvFoodDetails);
        }

    }
}