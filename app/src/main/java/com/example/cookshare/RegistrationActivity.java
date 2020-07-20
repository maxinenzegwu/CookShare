package com.example.cookshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class RegistrationActivity extends AppCompatActivity {

    public static final String TAG = "RegistrationActivity";
    private Button btnRegister;
    private TextView etRegisterUsername;
    private TextView etRegisterPassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        etRegisterUsername = findViewById(R.id.etRegisterUsername);
        etRegisterPassword = findViewById(R.id.etRegisterPassword);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "clicked register button");
                String username = etRegisterUsername.getText().toString();
                String password = etRegisterPassword.getText().toString();
                ParseUser user = new ParseUser();

                user.setUsername(username);
                user.setPassword(password);

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null){
                            Log.e(TAG, "success!", e);
                            goMainActivity1();
                        } else{
                            Log.e(TAG, "issue with sign up", e);
                            return;
                        }
                    }
                });
            }
        });
    }

    private void goMainActivity1() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}