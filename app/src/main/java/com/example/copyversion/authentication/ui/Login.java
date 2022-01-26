package com.example.copyversion.authentication.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
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
import com.example.copyversion.NavigationActivity;
import com.example.copyversion.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private EditText mEmail, mPassword;
    private FirebaseAuth auth;
    private int RC_SIGN_IN=120;
    private SignInButton googleSignInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private TextView forgetPassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        // Check if user is already signed in


        mEmail = findViewById(R.id.login_email_address);
        mPassword = findViewById(R.id.login_password);
        Button logintostart = findViewById(R.id.login_start);
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

        forgetPassword=findViewById(R.id.forgotPasswordInLogin);
        String html = "<u>forgot password</u>";
        forgetPassword.setText(Html.fromHtml(html));
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,ForgotPassword.class);
                startActivity(intent);
            }
        });



//         ProgressBar loadingProgressBar = findViewById(R.id.loading);

//        loadingProgressBar.setVisibility(View.INVISIBLE);




        logintostart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mEmail.getText().toString()) || !Patterns.EMAIL_ADDRESS.matcher(mEmail.getText().toString()).matches()) {
                    Toast.makeText(Login.this, "Please Enter a valid Email Address", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(mPassword.getText().toString())) {
                    Toast.makeText(Login.this, "Please Enter a valid Password", Toast.LENGTH_LONG).show();
                } else {
                    //Register User
//                    loadingProgressBar.setVisibility(View.VISIBLE);
                    ProgressDialog progressDialog
                            = new ProgressDialog(Login.this);
                    progressDialog.setTitle("Login..");
                    progressDialog.show();

                    auth.signInWithEmailAndPassword(mEmail.getText().toString(), mPassword.getText().toString())
                            .addOnCompleteListener(
                                    new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(
                                                @NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                progressDialog.dismiss();
//                                                Toast.makeText(getApplicationContext(),
//                                                        "Login successful!!",
//                                                        Toast.LENGTH_LONG)
//                                                        .show();
                                                Intent intent1
                                                        = new Intent(com.example.copyversion.authentication.ui.Login.this,
                                                        NavigationActivity.class);

                                                startActivity(intent1);
                                                finish();
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
                                                finish();

                                            }
                                        }
                                    });


                }
            }
        });


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
                    firebaseAuthWithGoogle(account.getIdToken());
                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                }

            }else
            {
            }

        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = auth.getCurrentUser();
                            User user1=new User();
//                                user.setUri(profileUri);
                            user1.setFullName(user.getDisplayName());
                            user1.setEmail(user.getEmail());
                            user1.setUri(user.getPhotoUrl().toString());
                            user1.setStatus("");
//                                User(mFullname.getText().toString(),mEmailAddress.getText().toString(),profileUri);
////
                            FirebaseAuth mAuth= FirebaseAuth.getInstance();


                            DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference("/rotlo/user");
                            DatabaseReference mDatabase1= FirebaseDatabase.getInstance().getReference("/rotlo/user").child(mAuth.getCurrentUser().getUid());
                            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                               int temp=0;
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists())
                                    {
                                        for(DataSnapshot ds:snapshot.getChildren())
                                        {

                                            if(ds.getKey().equals(mAuth.getCurrentUser().getUid()))
                                            {
                                                temp=1;

                                            }
                                        }

                                    }
                                    if(temp==0)
                                    {
                                        mDatabase1.setValue(user1);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    mDatabase1.setValue(user1);
                                }

                            });



                            Intent intent=new Intent(Login.this,NavigationActivity.class);
                            startActivity(intent);
                             finish();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
//                            updateUI(null);
                        }
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    public void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }
}