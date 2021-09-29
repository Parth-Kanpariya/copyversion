package com.example.copyversion;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link information_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class information_Fragment extends Fragment  {


    private TextView post;
    private Bitmap photo;
    private EditText people, donorName, mainCourse, donorAddress;
    private Button sendDataButtuon;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> arr;
    private ImageView imageView, photos;
    private Uri filePath;
    private String uriIntoString;

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


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public information_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment information_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static information_Fragment newInstance(String param1, String param2) {
        information_Fragment fragment = new information_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123
                && resultCode == Activity.RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getActivity().getContentResolver(),
                                filePath);
                photos.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
        uploadImage();

    }


    private void uploadImage() {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            FirebaseStorage storage = FirebaseStorage.getInstance();
            // Create a storage reference from our app
            StorageReference storageReference = storage.getReference();
            ProgressDialog progressDialog
                    = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "/images/post/"
                                    + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {

//                                    Task<Uri> downloadUri = taskSnapshot.getMetadata().getReference().getDownloadUrl();
//                                    uriIntoString=downloadUri.toString();

                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Uri downloadUrl = uri;
                                            uriIntoString = downloadUrl.toString();
                                            //Do what you want with the url
                                        }
                                    });


                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(getContext(),
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(getContext(),
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int) progress + "%");
                                }
                            });
        }
    }


    public void onclicked(String switched) {

//         getting text from our edittext fields.
        String name = donorName.getText().toString();
        String maincourse = mainCourse.getText().toString();
        String peple = people.getText().toString();
        String address = donorAddress.getText().toString();

        // below line is for checking weather the
        // edittext fields are empty or not.
        if (TextUtils.isEmpty(name) && TextUtils.isEmpty(maincourse) && TextUtils.isEmpty(peple) && TextUtils.isEmpty(address)) {
            Toast.makeText(getContext(), "Please Add complete Data", Toast.LENGTH_SHORT).show();
        } else {
            addToFirebase(name, maincourse, peple, address, uriIntoString,switched);

            donorName.getText().clear();
            mainCourse.getText().clear();
            people.getText().clear();
            donorAddress.getText().clear();
            photos.setImageBitmap(null);
        }


//                getdata();
//        Intent intent = new Intent(getContext(), com.example.copyversion.frontPage.class);
//
//        startActivity(intent);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.additems, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }



    private void addToFirebase(String name, String maincourse, String peple, String address, String photourl, String switched) {
        donorInfo.setDonorAddress(address);
        donorInfo.setDonorMainCourse(maincourse);
        donorInfo.setDonorPeople(peple);
        donorInfo.setDonorName(name);
        donorInfo.setFoodPhotoUrl(photourl);
        donorInfo.setSwitch(switched);


        if(switched=="true")
        {databaseReference = FirebaseDatabase.getInstance().getReference("/rotlo/post/donation");
        databaseReference.push().setValue(donorInfo);}
        else
        {
            databaseReference = FirebaseDatabase.getInstance().getReference("/rotlo/post/selling");
            databaseReference.push().setValue(donorInfo);
        }
        Toast.makeText(getContext(), "Data added", Toast.LENGTH_SHORT).show();


    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_information, container, false);

//
//        ActionBar actionBar;
////        actionBar = getSupportActionBar();
//        // Define ColorDrawable object and parse color
//        // using parseColor method
//        // with color hash code as its parameter
//        ColorDrawable colorDrawable
//                = new ColorDrawable(Color.parseColor("#ff9100"));
//        // Set BackgroundDrawable
//        actionBar.setBackgroundDrawable(colorDrawable);


        SwitchMaterial switchMaterial=rootView.findViewById(R.id.donation);
        final String[] switched = new String[1];
        switched[0]="true";
        switchMaterial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {
                    switched[0] ="true";
                }else {
                    switched[0] ="false";
                }
            }
        });





        Button postButton=rootView.findViewById(R.id.post_button);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclicked(switched[0]);
//                FragmentTransaction fr= getFragmentManager().beginTransaction();
//                fr.replace(R.id.container,new frontPage_Fragment());
//                fr.commit();

            }
        });

        people = rootView.findViewById(R.id.people);
        donorAddress = rootView.findViewById(R.id.donor_address);
        donorName = rootView.findViewById(R.id.donor_name);
        mainCourse = rootView.findViewById(R.id.main_course);

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

        imageView = rootView.findViewById(R.id.camera);
        photos = rootView.findViewById(R.id.photo);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent forCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(forCamera, 123);
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(
                        Intent.createChooser(
                                intent,
                                "Select Image from here..."),
                        123);


            }


        });


        return rootView;
    }



}