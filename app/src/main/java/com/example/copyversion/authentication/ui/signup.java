package com.example.copyversion.authentication.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.copyversion.MainActivity;
import com.example.copyversion.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signup extends AppCompatActivity {

    DatabaseReference users;
    private EditText mFullname, mEmailAddress, mPassword, mConfirmPassword;
    private String profileUri;
    private ProgressBar loadingProgressBar;
    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        FirebaseDatabase mFirebaseDatabase;
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        users = mFirebaseDatabase.getReference("users");

        loadingProgressBar = findViewById(R.id.loading);
        mFullname = findViewById(R.id.sign_up_Full_Name);
        mEmailAddress = findViewById(R.id.sign_up_Email_address);
        mPassword = findViewById(R.id.sign_up_Password);
        mConfirmPassword = findViewById(R.id.sign_up_Confirm_Password);
//        TextView mSignInLink = findViewById(R.id.signInLink);
        Button mSignUpButton = findViewById(R.id.sign_up_start);
        loadingProgressBar.setVisibility(View.INVISIBLE);

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpButton();
            }
        });

    }

    private void signUpButton() {
        if (TextUtils.isEmpty(mFullname.getText().toString()) && mFullname.getText().toString().length() < 3) {
            Toast.makeText(signup.this, "Please Enter a valid Name", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(mEmailAddress.getText().toString()) && !Patterns.EMAIL_ADDRESS.matcher(mEmailAddress.getText().toString()).matches()) {
            Toast.makeText(signup.this, "Please Enter a valid Email Address", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(mPassword.getText().toString())) {
            Toast.makeText(signup.this, "Please Enter a valid Password", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(mPassword.getText().toString()) && mEmailAddress.getText().toString().isEmpty()) {
            Toast.makeText(signup.this, "Please Enter a valid Password and Email Address", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(mConfirmPassword.getText().toString()) && !mEmailAddress.getText().toString().contains("@")) {
            Toast.makeText(signup.this, "Please Enter a valid Password", Toast.LENGTH_LONG).show();
        } else if (!mPassword.getText().toString().equalsIgnoreCase(mConfirmPassword.getText().toString())) {
            Toast.makeText(signup.this, "Passwords do not match", Toast.LENGTH_LONG).show();
            mPassword.setText("");
            mConfirmPassword.setText("");
        }
         else{



            profileUri="https://firebasestorage.googleapis.com/v0/b/copyversion-b749a.appspot.com/o/images%2Fusers%2Fprofileicon.jpeg?alt=media&token=559dd13d-e17c-40fd-a2fa-c601bf83fdb1";

            mAuth.createUserWithEmailAndPassword(mEmailAddress.getText().toString(), mPassword.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {

                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(),
                                        "Registration successful!",
                                        Toast.LENGTH_LONG)
                                        .show();



                                User user=new User();
//                                user.setUri(profileUri);
                                user.setFullName(mFullname.getText().toString());
                                user.setEmail(mEmailAddress.getText().toString());
//                                User(mFullname.getText().toString(),mEmailAddress.getText().toString(),profileUri);
////
                                FirebaseAuth mAuth= FirebaseAuth.getInstance();

                                DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference("/rotlo/user").child(mAuth.getCurrentUser().getUid());

                                mDatabase.setValue(user);



                                Intent intent1
                                        = new Intent(com.example.copyversion.authentication.ui.signup.this,
                                        MainActivity.class);
                                startActivity(intent1);
                            }
                            else {

                                // Registration failed
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Registration failed!!"
                                                + " Please try again later",
                                        Toast.LENGTH_LONG)
                                        .show();

                                Intent intent2
                                        = new Intent(com.example.copyversion.authentication.ui.signup.this,
                                        MainActivity.class);
                                startActivity(intent2);



                                // hide the progress bar
                                loadingProgressBar.setVisibility(View.GONE);
                            }
                        }
                    });

            }

    }

}