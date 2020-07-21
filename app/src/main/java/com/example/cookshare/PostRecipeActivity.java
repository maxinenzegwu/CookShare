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

    Post post;
    TextView tvRecipeNameDetails;
    TextView tvRecipeDetails;
    ImageView ivFoodDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_recipe);
        tvRecipeNameDetails = findViewById(R.id.tvRecipeNameDetails);
        tvRecipeDetails = findViewById(R.id.tvRecipeDetails);
        ivFoodDetails = findViewById(R.id.ivFoodDetails);
        post = (Post) Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));
        Log.d("PostRecipeActivity", String.format("Showing details for '%s'", post.getDescription()));
        tvRecipeNameDetails.setText(post.getDescription());
        tvRecipeDetails.setText(post.getRecipe());
        ParseFile image = post.getImage();
        if (image != null) {
            Glide.with(this).load(post.getImage().getUrl()).into(ivFoodDetails);
        }

    }
}