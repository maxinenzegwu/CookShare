package com.example.cookshare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.File;
import java.util.List;

public class ProfilePicture extends CreateActivity {

    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 43;
    public static final String TAG = "ProfilePicture";
    private ImageButton mBtnTakePicture;
    private Button mBtnPostProfile;
    private Button mBtnCancel;
    private ImageView mIvPerson;
    private File mPhotoFile;

    public String mPhotoFileName = "photo.jpg";

    @Override
        protected void launchCamera() {
            // create Intent to take a picture and return control to the calling application
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Create a File reference for future access
            mPhotoFile = getPhotoFileUri(mPhotoFileName);
            // wrap File object into a content provider
            Uri fileProvider = FileProvider.getUriForFile(ProfilePicture.this, "com.codepath.fileprovider", mPhotoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
            if (intent.resolveActivity(getPackageManager()) != null) {
                // Start the image capture intent to take photo
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_picture);
        mBtnTakePicture = findViewById(R.id.btnTakePicture);
        mBtnPostProfile = findViewById(R.id.btnPostProfile);
        mIvPerson = findViewById(R.id.ivPerson);
        mBtnCancel = findViewById(R.id.btnCancel);
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mBtnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
            }
        });
        mBtnPostProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mPhotoFile == null || mIvPerson.getDrawable() == null) {
                    Toast.makeText(ProfilePicture.this, "There is no image!", Toast.LENGTH_SHORT).show();
                    return;
                }
                changePicture(mPhotoFile);
                finish();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(mPhotoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                mIvPerson.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void changePicture(final File photoFile) {
        Log.i(TAG, "trying to change picture");
        ParseQuery<User> query = ParseQuery.getQuery(User.class);
        query.whereEqualTo(User.KEY_OBJECTID, ParseUser.getCurrentUser().getObjectId());

        query.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> users, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "issue with getting user", e);
                    return;
                }
                //iterate through each post and log each of them
                for (User user : users) {
                    user.setPicture(new ParseFile(photoFile));
                    Log.i(TAG, "changing picture");
                }
            }
        });


    }
}