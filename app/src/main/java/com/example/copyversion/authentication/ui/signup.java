package com.example.copyversion.authentication.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.copyversion.MainActivity;
import com.example.copyversion.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
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
    private int RC_SIGN_IN=120;
    private SignInButton googleSignInButton;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        FirebaseDatabase mFirebaseDatabase;
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        users = mFirebaseDatabase.getReference("users");

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleSignInButton=findViewById(R.id.sign_in_button_google);
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

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
        if (TextUtils.isEmpty(mFullname.getText().toString()) || mFullname.getText().toString().length() < 3) {
            Toast.makeText(signup.this, "Please Enter a valid Name", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(mEmailAddress.getText().toString()) || !Patterns.EMAIL_ADDRESS.matcher(mEmailAddress.getText().toString()).matches()) {
            Toast.makeText(signup.this, "Please Enter a valid Email Address", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(mPassword.getText().toString())) {
            Toast.makeText(signup.this, "Please Enter a valid Password", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(mPassword.getText().toString()) || mEmailAddress.getText().toString().isEmpty()) {
            Toast.makeText(signup.this, "Please Enter a valid Password and Email Address", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(mConfirmPassword.getText().toString()) || !mEmailAddress.getText().toString().contains("@")) {
            Toast.makeText(signup.this, "Please Enter a valid Password", Toast.LENGTH_LONG).show();
        } else if (!mPassword.getText().toString().equalsIgnoreCase(mConfirmPassword.getText().toString())) {
            Toast.makeText(signup.this, "Passwords do not match", Toast.LENGTH_LONG).show();
            mPassword.setText("");
            mConfirmPassword.setText("");
        }
         else{

            ProgressDialog progressDialog
                    = new ProgressDialog(signup.this);
            progressDialog.setTitle("Signup..");
            progressDialog.show();

            profileUri="https://firebasestorage.googleapis.com/v0/b/copyversion-b749a.appspot.com/o/images%2Fusers%2Fprofileicon.jpeg?alt=media&token=559dd13d-e17c-40fd-a2fa-c601bf83fdb1";

            mAuth.createUserWithEmailAndPassword(mEmailAddress.getText().toString(), mPassword.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {

                            if (task.isSuccessful()) {

                                progressDialog.show();
//                                Toast.makeText(getApplicationContext(),
//                                        "Registration successful!",
//                                        Toast.LENGTH_LONG)
//                                        .show();



                                User user=new User();
//                                user.setUri(profileUri);
                                user.setFullName(mFullname.getText().toString());
                                user.setEmail(mEmailAddress.getText().toString());
                                user.setStatus("");
//                                User(mFullname.getText().toString(),mEmailAddress.getText().toString(),profileUri);
////
                                FirebaseAuth mAuth= FirebaseAuth.getInstance();

                                DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference("/rotlo/user").child(mAuth.getCurrentUser().getUid());

                                mDatabase.setValue(user);



                                Intent intent1
                                        = new Intent(com.example.copyversion.authentication.ui.signup.this,
                                        MainActivity.class);
                                startActivity(intent1);
                                finish();
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
                                finish();



                                // hide the progress bar
                                loadingProgressBar.setVisibility(View.GONE);
                            }
                        }
                    });

            }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            Exception exception=task.getException();
            if(task.isSuccessful())
            {
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    Log.d("ddfd", "firebaseAuthWithGoogle:" + account.getId());
                    firebaseAuthWithGoogle(account.getIdToken());
                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("ncmd", "Google sign in failed", e);
                }

            }else
            {
                Log.w("dfnmdn",exception.toString());
            }

        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("dfd", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            User user1=new User();
//                                user.setUri(profileUri);
                            user1.setFullName(user.getDisplayName());
                            user1.setEmail(user.getEmail());
                            user1.setStatus("");
                            user1.setUri(user.getPhotoUrl().toString());
//                                User(mFullname.getText().toString(),mEmailAddress.getText().toString(),profileUri);
////
                            FirebaseAuth mAuth= FirebaseAuth.getInstance();

                            DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference("/rotlo/user").child(mAuth.getCurrentUser().getUid());

                            mDatabase.setValue(user1);
                            Intent intent=new Intent(signup.this,MainActivity.class);
                            startActivity(intent);
                            finish();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("df", "signInWithCredential:failure", task.getException());
//                            updateUI(null);
                        }
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

}