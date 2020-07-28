package com.example.cookshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";
    private Button mBtnLogin;
    private TextView mEtUsername;
    private TextView mEtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //if user is already logged in, go straight to the main activity
        if (ParseUser.getCurrentUser() != null){
            goMainActivity();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
                            if (e!=null){
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_register){
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
}