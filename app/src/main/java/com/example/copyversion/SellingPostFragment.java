package com.example.copyversion;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.Nullable;
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
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link information_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SellingPostFragment extends Fragment {


    private TextView post;
    private Bitmap photo;
    private EditText sellerName, Approximate, sellerAddress,ContactNumberSeller;
    private Button sendDataButtuon;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> arr;
    private ImageView imageView, photos;
    private Uri filePath;
    private String uriIntoString,username,profilrphotopost;
    private FirebaseAuth mAuth;
    private FirebaseAuth Auth;
    private double longitude, latitude;
    FusedLocationProviderClient fusedLocationProviderClient;
    String address;

    // creating a variable for our
    // Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;

    // creating a variable for
    // our object class

    SellerInfo sellerInfo;
    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SellingPostFragment() {
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


                if(bitmap.getByteCount()>144609280)
                { int nh = (int) ( bitmap.getHeight() * (1024.0 / bitmap.getWidth()) );
                    Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1024, nh, true);
                    Matrix matrix = new Matrix();

                    matrix.postRotate(90);
                    scaled = Bitmap.createBitmap(scaled, 0, 0, scaled.getWidth(), scaled.getHeight(), matrix, true);

                    imageView.setImageBitmap(scaled);}
                else
                {
                    imageView.setImageBitmap(bitmap);
                }




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
                            "/images/post/selling/"
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


    public void onclicked() {

//         getting text from our edittext fields.
        String name = sellerName.getText().toString();
        String maincourse = Approximate.getText().toString();
        String address = sellerAddress.getText().toString();
        String mobile=ContactNumberSeller.getText().toString();

        // below line is for checking weather the
        // edittext fields are empty or not.
        if (TextUtils.isEmpty(name) && TextUtils.isEmpty(maincourse) && TextUtils.isEmpty(address)&&TextUtils.isEmpty(mobile)) {
            Toast.makeText(getContext(), "Please Add complete Data", Toast.LENGTH_SHORT).show();


        }
        else  if(mobile.length()<10 || mobile.length()>10 )
        {
            Toast.makeText(getContext(), "Please Enter valid contact number", Toast.LENGTH_SHORT).show();
        }

        else {
            addToFirebase(name, maincourse, address, uriIntoString,mobile);

            sellerName.getText().clear();
            Approximate.getText().clear();
            sellerAddress.getText().clear();
            imageView.setImageBitmap(null);
            ContactNumberSeller.getText().clear();
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


    private void addToFirebase(String name, String maincourse, String address, String photourl,String mobile) {
        sellerInfo.setSellerAddress(address);
        sellerInfo.setSellerApproximate(maincourse);
        sellerInfo.setSellerName(name);
        sellerInfo.setFoodPhotoUrl(photourl);
        mAuth = FirebaseAuth.getInstance();
        sellerInfo.setUid(mAuth.getUid());
        sellerInfo.setLatitude(latitude);
        sellerInfo.setLongitude(longitude);
        sellerInfo.setCurrentTime(new Date());
        sellerInfo.setContact(mobile);


        databaseReference = FirebaseDatabase.getInstance().getReference("/rotlo/post/selling");
        databaseReference.push().setValue(sellerInfo);

        FirebaseMessaging.getInstance().subscribeToTopic("all");

        Auth=FirebaseAuth.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("/rotlo/user").child(Auth.getCurrentUser().getUid());


        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                    String nameForPost =(snapshot.child("fullName").getValue(String.class));


                FcmNotificationsSender notificationsSender=new FcmNotificationsSender("/topics/all","Seller", nameForPost +" Added the new Post"
                        ,getContext(),getActivity());
                notificationsSender.SendNotifications();


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "no data", Toast.LENGTH_SHORT).show();
            }

        };
        mDatabase.addListenerForSingleValueEvent(eventListener);









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
        View rootView = inflater.inflate(R.layout.fragment_selling_post, container, false);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        getlocation();

        Auth=FirebaseAuth.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("/rotlo/user").child(Auth.getCurrentUser().getUid());

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {




                username=(snapshot.child("fullName").getValue(String.class));
                profilrphotopost= snapshot.child("uri").getValue(String.class);
                sellerInfo.setUsername(username);
                sellerInfo.setProfilePhtourl(profilrphotopost);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "no data", Toast.LENGTH_SHORT).show();
            }
        };
        mDatabase.addValueEventListener(eventListener);


        Button postButton = rootView.findViewById(R.id.post_button);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclicked();
//                FragmentTransaction fr= getFragmentManager().beginTransaction();
//                fr.replace(R.id.container,new frontPage_Fragment());
//                fr.commit();

            }
        });


        sellerAddress = rootView.findViewById(R.id.seller_address);
        sellerName = rootView.findViewById(R.id.seller_name);
        Approximate = rootView.findViewById(R.id.Approximate_weight);
//        ContactNumberSeller=rootView.findViewById(R.id.mobileNumberSeller);

//        post=findViewById(R.id.text_view_post1);

        // below line is used to get the
        // instance of our FIrebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("Donor"); //Main reference

        // initializing our object
        // class variable.
        sellerInfo = new SellerInfo();


        //to go in camera

        imageView = rootView.findViewById(R.id.camera_seller);
//        photos = rootView.findViewById(R.id.photo_seller);
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

    @SuppressLint("MissingPermission")
    private void getlocation() {

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();

                    Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                    List<Address> addressList = null;
                    try {
                        addressList = geocoder.getFromLocation(latitude, longitude, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    address = addressList.get(0).getAddressLine(0);
//                    Toast.makeText(getContext(), address, Toast.LENGTH_SHORT).show();

                }
            }
        });


    }


}