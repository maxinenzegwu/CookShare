package com.example.cookshare;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    public static final String TAG = "PostsAdapter";
    private Context context;
    private List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvUsername;
        private TextView tvRecipeName;
        private ImageView ivPost;
        private ImageButton btnSave;
        private TextView tvLikes;
        private boolean mClicked = false;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

//            int position = getAdapterPosition();
//            Post post = posts.get(position);
//            if (post.getIsLiked() == 1){
//                btnSave.setBackgroundResource(R.drawable.ic_baseline_star_24);
//            }
//            else{
//                btnSave.setBackgroundResource(android.R.color.transparent);
//            }
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvRecipeName = itemView.findViewById(R.id.tvRecipeNameDetails);
            ivPost = itemView.findViewById(R.id.ivPost);
            tvLikes = itemView.findViewById(R.id.tvLikes);
            btnSave = itemView.findViewById(R.id.btnSave);
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Post post = posts.get(position);

                    if (2==2) {
                        post.getLikedUsers().add(ParseUser.getCurrentUser());

                        likePost(post);
                        view.setBackgroundResource(R.drawable.ic_baseline_star_24);

                    }
                    if (5==2) {
                        post.getLikedUsers().remove(ParseUser.getCurrentUser());

                        unLikePost(post);
                        view.setBackgroundResource(android.R.color.transparent);

                    }
                    notifyDataSetChanged();

                }

            });
            itemView.setOnClickListener(this);
        }


        public void bind(Post post) {
            tvUsername.setText(post.getUser().getUsername());
            tvRecipeName.setText(post.getDescription());
            tvLikes.setText(Integer.toString(post.getLikes()));
            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context).load(post.getImage().getUrl()).into(ivPost);
            }
        }

        public void likePost(Post post) {

            Toast.makeText(context, "liking a post", Toast.LENGTH_SHORT).show();
            post.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    //check if there is an exception
                    if (e != null) {
                        Log.e(TAG, "error while saving", e);
                        Toast.makeText(context, "error while saving", Toast.LENGTH_SHORT).show();
                    }

                    Log.i(TAG, "saved post!");

                }
            });
            notifyDataSetChanged();
        }

        public void unLikePost(Post post) {

            post.setLikes(post.getLikes() - 1);
            Toast.makeText(context, "liking a post", Toast.LENGTH_SHORT).show();
            post.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    //check if there is an exception
                    if (e != null) {
                        Log.e(TAG, "error while saving", e);
                        Toast.makeText(context, "error while saving", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(context, "saved post!", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "saved post!");

                }
            });
            notifyDataSetChanged();
        }

        @Override
        public void onClick(View view) {
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the post at the position, this won't work if the class is static
                Post post = posts.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, PostRecipeActivity.class);
                // serialize the post using parceler, use its short name as a key
                intent.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
                // show the activity
                context.startActivity(intent);

            }
        }
    }
}
