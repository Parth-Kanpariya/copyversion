package com.example.copyversion;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FullInfoOfPostSell extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_info_of_post);


        ProgressDialog progressDialog
                = new ProgressDialog(FullInfoOfPostSell.this);
        progressDialog.setTitle("Loading..");

        progressDialog.show();

        Button contactNumber=findViewById(R.id.contact_me);
        TextView t2 = findViewById(R.id.textView2);
        TextView t9 = findViewById(R.id.textView9);
        TextView t6 = findViewById(R.id.textView6);
        TextView t4 = findViewById(R.id.textView4);
        TextView t8=findViewById(R.id.textView8);
        TextView t7=findViewById(R.id.textView7);
        ImageView imageView2 = findViewById(R.id.image);

        final String[] contact = new String[1];
        final String[] name = new String[1];
        final String[] people = new String[1];
        final String[] maincourse = new String[1];
        final String[] address = new String[1];


        t8.setVisibility(View.GONE);
        t9.setVisibility(View.GONE);
        Intent i = getIntent();
        String PostId=(String)i.getSerializableExtra("PostId");


//        Toast.makeText(FullInfoOfPostSell.this, ""+PostId+"SELL", Toast.LENGTH_SHORT).show();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("/rotlo/post/selling").child(PostId);
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                t2.setText(snapshot.child("sellerName").getValue(String.class));
//                t9.setText(snapshot.child("people").getValue(String.class));
                t6.setText(snapshot.child("sellerApproximate").getValue(String.class));
                t4.setText(snapshot.child("sellerAddress").getValue(String.class));
                Picasso.get().load(snapshot.child("foodPhotoUrl").getValue(String.class)).resize(2048, 1600).onlyScaleDown() // the image will only be resized if it's bigger than 2048x 1600 pixels.
                        .into(imageView2);
                contact[0] =snapshot.child("contact").getValue(String.class);
                name[0] = snapshot.child("sellerName").getValue(String.class);
                people[0] =snapshot.child("sellerApproximate").getValue(String.class);
//                maincourse[0] =snapshot.child("donorMainCourse").getValue(String.class);
                address[0] =snapshot.child("sellerAddress").getValue(String.class);
                progressDialog.dismiss();




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        t7.setText("Weight");
//
//        t2.setText(name[0]);
//        t6.setText(people[0]);
////        t9.setText("For Selling");
//        t4.setText(address[0]);
//        String s = (x.getFoodPhotoUrl());
//        if (s != null) {
////            Picasso.get().load(s).into(imageView2);
//            Picasso.get().load(s).resize(2048, 1600).onlyScaleDown() // the image will only be resized if it's bigger than 2048x 1600 pixels.
//                    .into(imageView2);
//
//        }

        contactNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                String Text="https://wa.me/91"+contact[0]+"?text=SellingPost%0A%0AName:-"+name[0]+"%0A"+"Weight:-"+people[0]+"%0A"+"SellerAddress:-"+address[0]+"%0A"+"%0A"+"I'm interested for this";

                Uri uri = Uri.parse(Text);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);

                startActivity(intent);
            }
        });
    }
}