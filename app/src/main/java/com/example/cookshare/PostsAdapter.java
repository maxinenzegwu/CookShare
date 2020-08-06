package com.example.cookshare;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;


/**
 * describe what class does for all classes
 */
public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    //remove unnecesssary spaces

    public static final String TAG = "PostsAdapter";
    protected Context mContext;
    protected List<Post> mPosts;
    private List<Post> mPostsCopy;

    public PostsAdapter(Context context, List<Post> posts) {
        this.mContext = context;
        this.mPosts = posts;
        this.mPostsCopy = new ArrayList<>(posts);
    }

    public void clear() {
        mPosts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<Post> list) {
        mPosts.addAll(list);
        notifyDataSetChanged();

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = mPosts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvUsername;
        private TextView tvRecipeName;
        private ImageView ivPost;
        private ImageButton btnSave;
        private TextView tvLikes;


        public ViewHolder(@NonNull final View itemView) {
            super(itemView);


            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvRecipeName = itemView.findViewById(R.id.tvRecipeNameDetails);
            ivPost = itemView.findViewById(R.id.ivProfilePicture);
            tvLikes = itemView.findViewById(R.id.tvLikes);
            btnSave = itemView.findViewById(R.id.btnSave);


            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Post post = mPosts.get(position);


                    if (!(post.getFavorited().contains(ParseUser.getCurrentUser().getObjectId()))) {

                        post.getFavorited().add(ParseUser.getCurrentUser().getObjectId());
                        post.setFavorited(post.getFavorited());
                        view.setBackgroundResource(R.drawable.ic_baseline_star_24);


                    } else if (post.getFavorited().contains(ParseUser.getCurrentUser().getObjectId())) {

                        post.getFavorited().remove(ParseUser.getCurrentUser().getObjectId());
                        post.setFavorited(post.getFavorited());
                        post.put("favorited", post.getFavorited());
                        view.setBackgroundResource(android.R.color.transparent);


                    }

                    savePost(post);
                    notifyDataSetChanged();

                }

            });


            //double tap listener
            itemView.setOnTouchListener(new OnDoubleTapListener(mContext) {
                @Override
                public void onSingleTapConfirmed(MotionEvent e) {
                    int position = getAdapterPosition();
                    // make sure the position is valid, i.e. actually exists in the view
                    if (position != RecyclerView.NO_POSITION) {
                        // get the post at the position, this won't work if the class is static
                        Post post = mPosts.get(position);
                        // create intent for the new activity
                        Intent intent = new Intent(mContext, PostRecipeActivity.class);
                        // serialize the post using parceler, use its short name as a key
                        intent.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
                        // show the activity
                        mContext.startActivity(intent);

                    }
                }

                @Override
                public void onDoubleTap(MotionEvent e) {
                    int position = getAdapterPosition();
                    Post post = mPosts.get(position);

                    if (!(post.getFavorited().contains(ParseUser.getCurrentUser().getObjectId()))) {

                        post.getFavorited().add(ParseUser.getCurrentUser().getObjectId());
                        post.setFavorited(post.getFavorited());
                        btnSave.setBackgroundResource(R.drawable.ic_baseline_star_24);


                    } else if (post.getFavorited().contains(ParseUser.getCurrentUser().getObjectId())) {

                        post.getFavorited().remove(ParseUser.getCurrentUser().getObjectId());
                        post.setFavorited(post.getFavorited());
                        post.put("favorited", post.getFavorited());
                        btnSave.setBackgroundResource(android.R.color.transparent);


                    }

                    savePost(post);
                    notifyDataSetChanged();
                }
            });
            itemView.setOnClickListener(this);
        }


        public void bind(Post post) {
            if (post.getFavorited().contains(ParseUser.getCurrentUser().getObjectId())) {
                btnSave.setBackgroundResource(R.drawable.ic_baseline_star_24);
            } else {
                btnSave.setBackgroundResource(android.R.color.transparent);
            }
            tvUsername.setText(post.getUser().getUsername());
            tvRecipeName.setText(post.getDescription());
            tvLikes.setText(Integer.toString(post.getFavorited().size()));
            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(mContext).load(post.getImage().getUrl()).into(ivPost);
            }
        }

        private void savePost(Post post) {


            post.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    //check if there is an exception
                    if (e != null) {
                        Log.e(TAG, "error while saving", e);
                        Toast.makeText(mContext, "error while saving", Toast.LENGTH_SHORT).show();
                    }

                    Log.i(TAG, "saved post!");

                }
            });
        }



        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the post at the position, this won't work if the class is static
                Post post = mPosts.get(position);
                // create intent for the new activity
                Intent intent = new Intent(mContext, PostRecipeActivity.class);
                // serialize the post using parceler, use its short name as a key
                intent.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
                // show the activity
                mContext.startActivity(intent);

            }
        }


    }
}
