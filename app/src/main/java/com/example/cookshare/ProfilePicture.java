package com.example.cookshare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cookshare.fragments.UserFragment;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class ProfilePicture extends CreateActivity {

    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 43;
    public static final String TAG = "ProfilePicture";
    private ImageButton mBtnTakePicture;
    private Button mBtnPostProfile;
    private Button mBtnCancel;
    private Button mBtnChoosePicture;
    private ImageView mIvPerson;
    private File mPhotoFile;
    private ParseFile pickedPhotoFile = null;

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
        mBtnChoosePicture = findViewById(R.id.btnChoosePicture);
        mBtnChoosePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPickPhoto(view);
            }
        });
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


                if ((mPhotoFile == null || mIvPerson.getDrawable() == null) && (pickedPhotoFile == null)) {
                    Toast.makeText(ProfilePicture.this, "There is no image!", Toast.LENGTH_SHORT).show();
                    return;

                }

                changePicture(mPhotoFile);
                setResult(UserFragment.UPDATE_IMAGE_ACTIVITY_REQUEST_CODE);
                finish();
            }
        });
    }


    public void onPickPhoto(View view) {
        // Create intent for picking a photo from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);


        if (intent.resolveActivity(getPackageManager()) != null) {
            // Bring up gallery to select a photo
            startActivityForResult(intent, PICK_PHOTO_CODE);
        }
    }

    public Bitmap loadFromUri(Uri photoUri) {
        Bitmap image = null;


        try {
            // check version of Android on device
            if (Build.VERSION.SDK_INT > 27) {
                // on newer versions of Android, use the new decodeBitmap method
                ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), photoUri);
                image = ImageDecoder.decodeBitmap(source);
            } else {
                // support older versions of Android by using getBitmap
                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }



    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((data != null) && requestCode == PICK_PHOTO_CODE) {
            Uri photoUri = data.getData();

            // Load the image located at photoUri into selectedImage
            Bitmap selectedImage = loadFromUri(photoUri);

            // Load the selected image into  preview
            mIvPerson.setImageBitmap(selectedImage);
            InputStream input = null;

            try {
                input = getContentResolver().openInputStream(photoUri);
                pickedPhotoFile = new ParseFile(getBytes(input));
            } catch (FileNotFoundException e) {
                Log.e(TAG, "file not found", e);
            } catch (IOException e) {
                Log.e(TAG, "I/O exception", e);
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        Log.e(TAG, "I/O exception while trying to close input stream", e);
                    }
                }
            }


        }
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

        final ParseUser myUser = ParseUser.getCurrentUser();
        final ParseFile profilePhoto;
        if (pickedPhotoFile != null) {
            profilePhoto = pickedPhotoFile;
        } else {
            profilePhoto = new ParseFile(photoFile);
        }
        myUser.put(User.KEY_PICTURE, profilePhoto);

        myUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if (e != null) {
                    Log.e(TAG, "issue with getting user", e);
                    return;
                }
                Log.i(TAG, "profile picture updated successfully");
            }
        });


    }


}