package com.example.copyversion;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class frontPage extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);


        ActionBar actionBar;
        actionBar = getSupportActionBar();
        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#ff9100"));
        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(frontPage.this, information.class);
                startActivity(intent);
            }
        });


        //temporary
//          String name= getIntent().getStringExtra("name");
//          String address= getIntent().getStringExtra("address");
//          String maincourse= getIntent().getStringExtra("maincourse");
//          String peple= getIntent().getStringExtra("peple");
//          TextView post1=findViewById(R.id.text_view_post1);
//        TextView post2=findViewById(R.id.text_view_post2);
//        TextView post3=findViewById(R.id.text_view_post3);
//        TextView post4=findViewById(R.id.text_view_post4);
//          post1.setText(name);
//        post2.setText(address);
//        post3.setText(maincourse);
//        post4.setText(peple);
        //


//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            }, 10000);

        //The key argument here must match that used in the other activity
//        }


        getdata();


    }

    private void getdata() {

        // calling add value event listener method
        // for getting the values from database.
        ArrayList<DonorInfo> List = new ArrayList<>();
        ListView l = findViewById(R.id.list);

//        List.add(new DonorInfo("dd","dd","dd","Dd"));
//        List.add(new DonorInfo("dd","dd","dd","Dd"));
//        List.add(new DonorInfo("dd","dd","dd","Dd"));

        final InfoAdapter adapter = new InfoAdapter(this, List);
        l.setAdapter(adapter);
//        l.setBackgroundResource(R.drawable.rounded_corner);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();


        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String donorName = ds.child("donorName").getValue(String.class);
                        String donorAddress = ds.child("donorAddress").getValue(String.class);
                        String people = ds.child("people").getValue(String.class);
                        String mainCourse = ds.child("donorMainCourse").getValue(String.class);
                        String foodPhotUrl=ds.child("foodPhotoUrl").getValue(String.class);
                        List.add(new DonorInfo(donorName, people, mainCourse, donorAddress,foodPhotUrl));
                    }
                }


                Collections.reverse(List);
                adapter.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(frontPage.this, "no data", Toast.LENGTH_SHORT).show();
            }

        };
        rootRef.addListenerForSingleValueEvent(eventListener);

        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(frontPage.this, FullInfoOfPost.class);
                DonorInfo x = (DonorInfo) l.getAdapter().getItem(position);
                intent.putExtra("hi", x);
                startActivity(intent);
            }
        });


    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }


}