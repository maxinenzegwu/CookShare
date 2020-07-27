package com.example.cookshare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

public class CreateActivity extends AppCompatActivity {
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    public static final String TAG = "CreateActivity";
    private EditText etRecipe;
    private EditText etRecipeName;
    private ImageView btnTakePicture;
    private Button btnPost;
    private Button btnCancel;
    private ImageView ivFood;
    private File photoFile;
    public String photoFileName = "photo.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        etRecipe = findViewById(R.id.etRecipe);
        etRecipeName = findViewById(R.id.etRecipeName);
        btnTakePicture = findViewById(R.id.btnTakePicture);
        btnPost = findViewById(R.id.btnPost);
        ivFood = findViewById(R.id.ivFood);
        btnCancel = findViewById(R.id.btnCancel);

btnCancel.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        finish();
    }
});
        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CreateActivity.this, "button is clicked!", Toast.LENGTH_SHORT).show();
                String recipeName = etRecipeName.getText().toString();
                String recipe = etRecipe.getText().toString();
                //check if recipe name or recipe is empty
                if (recipeName.isEmpty() || recipe.isEmpty()){
                    Toast.makeText(CreateActivity.this, "Must post a recipe and recipe name!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(photoFile == null || ivFood.getDrawable() == null){
                    Toast.makeText(CreateActivity.this, "There is no image!", Toast.LENGTH_SHORT).show();
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(recipeName, recipe, currentUser, photoFile);
            }
        });



    }

    private void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(CreateActivity.this, "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview

                ivFood.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    private void savePost(String recipeName, String recipe, ParseUser currentUser, File photoFile) {
        Post post = new Post();
        post.setDescription(recipeName);
        post.setRecipe(recipe);
        post.setImage(new ParseFile(photoFile));
        post.setUser(currentUser);
        post.setLikes(0);

        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                //check if there is an exception
                if (e!=null){
                    Log.e(TAG, "error while saving", e);
                    Toast.makeText(CreateActivity.this, "error while saving", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "saved post!");
                etRecipe.setText("");
                etRecipeName.setText("");
                ivFood.setImageResource(0);
                finish();
            }
        });
    }



}