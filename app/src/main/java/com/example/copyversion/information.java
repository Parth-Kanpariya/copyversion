package com.example.copyversion;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class information extends AppCompatActivity {
    private TextView post;
    private Bitmap photo;
    private EditText people, donorName, mainCourse, donorAddress;
    private Button sendDataButtuon;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> arr;
    private ImageView imageView, photos;

    // creating a variable for our
    // Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;

    // creating a variable for
    // our object class

    DonorInfo donorInfo;
    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#ff9100"));
        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);

        people = findViewById(R.id.people);
        donorAddress = findViewById(R.id.donor_address);
        donorName = findViewById(R.id.donor_name);
        mainCourse = findViewById(R.id.main_course);

//        post=findViewById(R.id.text_view_post1);

        // below line is used to get the
        // instance of our FIrebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("Donor"); //Main reference

        // initializing our object
        // class variable.
        donorInfo = new DonorInfo();


        //to go in camera

        imageView = findViewById(R.id.camera);
        photos = findViewById(R.id.photo);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(forCamera, 123);


            }


        });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            photo = (Bitmap) data.getExtras().get("data");
            photos.setImageBitmap(photo);
        }

    }

    public void onclicked() {

//         getting text from our edittext fields.
        String name = donorName.getText().toString();
        String maincourse = mainCourse.getText().toString();
        String peple = people.getText().toString();
        String address = donorAddress.getText().toString();

        // below line is for checking weather the
        // edittext fields are empty or not.
        if (TextUtils.isEmpty(name) && TextUtils.isEmpty(maincourse) && TextUtils.isEmpty(peple) && TextUtils.isEmpty(address)) {
            // if the text fields are empty
            // then show the below message.
            Toast.makeText(information.this, "Please Add some data", Toast.LENGTH_SHORT).show();
        } else {
            addToFirebase(name, maincourse, peple, address);

        }


//                getdata();
        Intent intent = new Intent(information.this, com.example.copyversion.frontPage.class);

        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.additems, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ic_check:
                onclicked();
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    private void addToFirebase(String name, String maincourse, String peple, String address) {
        donorInfo.setDonorAddress(address);
        donorInfo.setDonorMainCourse(maincourse);
        donorInfo.setDonorPeople(peple);
        donorInfo.setDonorName(name);
//        donorInfo.setFoodPhoto(photo);


        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.push().setValue(donorInfo);
        Toast.makeText(information.this, "Data added", Toast.LENGTH_SHORT).show();


    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);


    }

//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        try {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//        } catch (ActivityNotFoundException e) {
//            // display error state to the user
//        }
//    }


}