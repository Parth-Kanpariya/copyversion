package com.example.copyversion;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.squareup.picasso.Picasso;

public class FullInfoOfPost extends AppCompatActivity {

    String imgUrl;
    String x;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_info_of_post);


        ProgressDialog progressDialog
                = new ProgressDialog(FullInfoOfPost.this);
        progressDialog.setTitle("Loading..");

        progressDialog.show();


//        Button contactNumber = findViewById(R.id.contact_me);
        Button chat=findViewById(R.id.Chat_Initiate);
        TextView t2 = findViewById(R.id.textView2);
        TextView t9 = findViewById(R.id.textView9);
        TextView t6 = findViewById(R.id.textView6);
        TextView t4 = findViewById(R.id.textView4);
        ImageView imageView2 = findViewById(R.id.image);
        final String[] contact = new String[1];
        final String[] name = new String[1];
        final String[] people = new String[1];
        final String[] maincourse = new String[1];
        final String[] address = new String[1];
        String[] uid=new String[1];
        final String[] ProfilePhotoUrl=new String[1];
        final String[] ProfileName=new String[1];



        Intent i = getIntent();
        String PostId = (String) i.getSerializableExtra("PostId");
        DonorInfo d= (DonorInfo) i.getSerializableExtra("object");


//        Toast.makeText(FullInfoOfPost.this, ""+PostId, Toast.LENGTH_SHORT).show();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("/rotlo/post/donation").child(PostId);
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                t2.setText(snapshot.child("donorName").getValue(String.class));
                t9.setText(snapshot.child("people").getValue(String.class));
                t6.setText(snapshot.child("donorMainCourse").getValue(String.class));
                t4.setText(snapshot.child("donorAddress").getValue(String.class));
                imgUrl=snapshot.child("foodPhotoUrl").getValue(String.class);
                if(imgUrl!=null)
                {
                    Picasso.get().load(imgUrl).resize(2048, 1600).onlyScaleDown() // the image will only be resized if it's bigger than 2048x 1600 pixels.
                            .into(imageView2);
                }else
                {
                    imageView2.setBackgroundResource(R.drawable.food_button1);

                }

                contact[0] = snapshot.child("contact").getValue(String.class);
                name[0] = snapshot.child("donorName").getValue(String.class);
                people[0] = snapshot.child("people").getValue(String.class);
                maincourse[0] = snapshot.child("donorMainCourse").getValue(String.class);
                address[0] = snapshot.child("donorAddress").getValue(String.class);
                uid[0]=snapshot.child("uid").getValue(String.class);
                x=uid[0];


                if(x.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                {
                    chat.setVisibility(View.GONE);
                }
                else
                {
                    DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference("/rotlo/user/"+x);
                    rootReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ProfilePhotoUrl[0]=snapshot.child("uri").getValue(String.class);
                            ProfileName[0]=snapshot.child("fullName").getValue(String.class);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
                progressDialog.dismiss();



            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        Toast.makeText(FullInfoOfPost.this, x, Toast.LENGTH_SHORT).show();






        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chatIntent =new Intent(FullInfoOfPost.this,ChatApplication.class);
                chatIntent.putExtra("Name",ProfileName[0]);
                chatIntent.putExtra("imagUrl",ProfilePhotoUrl[0]);
                chatIntent.putExtra("uidOther",uid[0]);
                chatIntent.putExtra("object1",d);
                startActivity(chatIntent);
            }
        });


//        DonorInfo x = (DonorInfo) i.getSerializableExtra("hi");
//        t2.setText(x.getDonorName());
//        t9.setText(x.getPeople());
//        t6.setText(x.getDonorMainCourse());
//        t4.setText(x.getDonorAddress());
//        String s = (x.getFoodPhotoUrl());
//        if (s != null) {
////            Picasso.get().load(s).into(imageView2);
//            Picasso.get().load(s).resize(2048, 1600).onlyScaleDown() // the image will only be resized if it's bigger than 2048x 1600 pixels.
//                    .into(imageView2);
//
//        }
//
//        contactNumber.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                String Text = "https://wa.me/91" + contact[0] + "?text=DonorPost%0A%0AName:-" + name[0] + "%0A" + "People:-" + people[0] + "%0A" + "MainCourse:-" + maincourse[0] + "%0A" + "Address:-" + address[0] + "%0A" + "%0A" + "I'm interested for this";
//
//                Uri uri = Uri.parse(Text);
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//
//                startActivity(intent);
//            }
//        });
    }
}