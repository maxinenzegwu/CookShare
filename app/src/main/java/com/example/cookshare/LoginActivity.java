package com.example.cookshare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";
    private Button mBtnLogin;
    private TextView mEtUsername;
    private TextView mEtPassword;
    private LoginButton mBtnLoginFacebook;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //if user is already logged in, go straight to the main activity
        if (ParseUser.getCurrentUser() != null) {
            goMainActivity();
        }

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login);
        mBtnLoginFacebook = findViewById(R.id.btnLoginFacebook);
        mBtnLoginFacebook.setReadPermissions(Arrays.asList("email", "public profile"));
        callbackManager = CallbackManager.Factory.create();

        checkLoginStatus();
        mBtnLoginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(LoginActivity.this, "success!", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "test for success");
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "cancel!", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "test for cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "error!", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "test for error on login");

            }
        });

        mEtUsername = findViewById(R.id.etUsername);
        mEtPassword = findViewById(R.id.etPassword);
        mBtnLogin = findViewById(R.id.btnLogin);
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "clicked login button");
                final String username = mEtUsername.getText().toString();
                String password = mEtPassword.getText().toString();

                mBtnLogin.setEnabled(false);

                //log in the user
                ParseUser.logInInBackground(username, password, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        //if there is a problem with login, don't continue
                        if (e != null) {
                            Log.e(TAG, "issue with login", e);
                            mBtnLogin.setEnabled(true);
                            return;
                        }
                        //if login is okay, navigate to the main activity
                        Log.i(TAG, "login is okay for user " + username);
                        goMainActivity();


                    }
                });

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            if (currentAccessToken == null) {
                Toast.makeText(LoginActivity.this, "user logged out", Toast.LENGTH_SHORT).show();
            } else {
                loadUserProfile(currentAccessToken);
            }
        }
    };

    private void loadUserProfile(AccessToken newAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                try {
                    final String first_name = object.getString("first_name");
                    String last_name = object.getString("last_name");
                    String email = object.getString("email");
                    String id = object.getString("id");
                    final String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";

                    final ParseUser user = new ParseUser();

                    user.setUsername(first_name);
                    user.setPassword(first_name);

                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Log.e(TAG, "success!", e);
                                goMainActivity();
                                user.put(User.KEY_PICTURE, image_url);
                            } else {
                                Log.e(TAG, "issue with sign up", e);
                                return;

                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name, last_name, email, id");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_register) {
            Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void goMainActivity() {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void checkLoginStatus() {
        if (AccessToken.getCurrentAccessToken() != null) {
            loadUserProfile(AccessToken.getCurrentAccessToken());
            goMainActivity();
        }
    }
}