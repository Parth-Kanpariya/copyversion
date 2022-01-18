package com.example.copyversion.authentication.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.copyversion.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private EditText email;
    private Button sendEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email=findViewById(R.id.EmailForWhichPasswordChange);
        sendEmail=findViewById(R.id.sendEmailForPassword);

        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        String EmailString=email.getText().toString();

        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(email.getText().toString()) || !Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    Toast.makeText(ForgotPassword.this, "Please Enter a valid Email Address", Toast.LENGTH_LONG).show();
                }else
                {
                    mAuth.sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                // if isSuccessful then done message will be shown
                                // and you can change the password
                                Toast.makeText(ForgotPassword.this,"Done sent",Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(ForgotPassword.this,"Error Occured",Toast.LENGTH_LONG).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(ForgotPassword.this,"Error Failed",Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
        });

    }
}