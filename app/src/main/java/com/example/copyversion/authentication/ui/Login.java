package com.example.copyversion.authentication.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.copyversion.MainActivity;
import com.example.copyversion.NavigationActivity;
import com.example.copyversion.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private EditText mEmail, mPassword;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        // Check if user is already signed in


        mEmail = findViewById(R.id.login_email_address);
        mPassword = findViewById(R.id.login_password);
        Button logintostart = findViewById(R.id.login_start);
//         ProgressBar loadingProgressBar = findViewById(R.id.loading);

//        loadingProgressBar.setVisibility(View.INVISIBLE);


        logintostart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mEmail.getText().toString()) && !Patterns.EMAIL_ADDRESS.matcher(mEmail.getText().toString()).matches()) {
                    Toast.makeText(Login.this, "Please Enter a valid Email Address", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(mPassword.getText().toString())) {
                    Toast.makeText(Login.this, "Please Enter a valid Password", Toast.LENGTH_LONG).show();
                } else {
                    //Register User
//                    loadingProgressBar.setVisibility(View.VISIBLE);
                    auth.signInWithEmailAndPassword(mEmail.getText().toString(), mPassword.getText().toString())
                            .addOnCompleteListener(
                                    new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(
                                                @NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(),
                                                        "Login successful!!",
                                                        Toast.LENGTH_LONG)
                                                        .show();
                                                Intent intent1
                                                        = new Intent(com.example.copyversion.authentication.ui.Login.this,
                                                        NavigationActivity.class);

                                                startActivity(intent1);
                                            } else {
                                                // sign-in failed
                                                Toast.makeText(getApplicationContext(),
                                                        "Login failed!!",
                                                        Toast.LENGTH_LONG)
                                                        .show();
                                                Intent intent
                                                        = new Intent(com.example.copyversion.authentication.ui.Login.this,
                                                        MainActivity.class);
                                                startActivity(intent);

                                            }
                                        }
                                    });


                }
            }
        });


    }
}