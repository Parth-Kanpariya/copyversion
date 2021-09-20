package com.example.copyversion;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class FullInfoOfPost extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_info_of_post);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#ff9100"));
        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);

        TextView t2 = findViewById(R.id.textView2);
        TextView t9 = findViewById(R.id.textView9);
        TextView t6 = findViewById(R.id.textView6);
        TextView t4 = findViewById(R.id.textView4);
        ImageView imageView2 = findViewById(R.id.image);


        Intent i = getIntent();
        DonorInfo x = (DonorInfo) i.getSerializableExtra("hi");
        t2.setText(x.getDonorName());
        t9.setText(x.getPeople());
        t6.setText(x.getDonorMainCourse());
        t4.setText(x.getDonorAddress());
        String s = (x.getFoodPhotoUrl());
        if (s != null) {
            Picasso.get().load(s).into(imageView2);

        }
    }
}