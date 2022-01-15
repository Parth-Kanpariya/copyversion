package com.example.copyversion;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button False, True;
    private LinearLayout buttonForChanginProfileEditPost;
    private Uri filePath;
    private String uriString;
    FirebaseAuth auth;
    private String uriIntoString;

    private EditText editText;
    private Bitmap bitmap;
    private ImageView profileIcon;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditProfile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProfile newInstance(String param1, String param2) {
        EditProfile fragment = new EditProfile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        auth = FirebaseAuth.getInstance();

        profileIcon = rootView.findViewById(R.id.EditPostprofilePhoto);
        editText = rootView.findViewById(R.id.EditPostEditText);

        False = rootView.findViewById(R.id.NoChangeInProfile);
        False.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Picasso.get().load(s).resize(2048, 1600).onlyScaleDown() // the image will only be resized if it's bigger than 2048x 1600 pixels.
//                        .into(profileIcon);
////                Picasso.get().load(s).into(profileIcon);
//                x.setVisibility(View.GONE);

                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_profileFragment_to_profile);


            }
        });

        True = rootView.findViewById(R.id.profileUpdated);
        True.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editText.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Enter Valid Name", Toast.LENGTH_SHORT).show();

                } else {
                    setUserName(editText.getText().toString());
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_profileFragment_to_profile);


                }

            }
        });

        buttonForChanginProfileEditPost = rootView.findViewById(R.id.buttonForChanginProfileEditPost);
        buttonForChanginProfileEditPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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


        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("/rotlo/user").child(auth.getCurrentUser().getUid());
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {


                    String nameForPost = (snapshot.child("fullName").getValue(String.class));
                    editText.setText(nameForPost);
                    uriString = snapshot.child("uri").getValue(String.class);


                    DatabaseReference mDatabaseDonor = FirebaseDatabase.getInstance().getReference("/rotlo/post/donation");
                    mDatabaseDonor.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot Snapshot) {
                            for (DataSnapshot snapshot : Snapshot.getChildren()) {

                                if (snapshot.child("uid").getValue(String.class).equals(auth.getCurrentUser().getUid())) {
                                    mDatabaseDonor.child(snapshot.getKey()).child("profilePhtourl").setValue(uriString);
                                    mDatabaseDonor.child(snapshot.getKey()).child("username").setValue(nameForPost);
                                }


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    DatabaseReference mDatabaseSeller = FirebaseDatabase.getInstance().getReference("/rotlo/post/selling");
                    mDatabaseSeller.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot Snapshot) {
                            for (DataSnapshot snapshot : Snapshot.getChildren()) {

                                if (snapshot.child("uid").getValue(String.class).equals(auth.getCurrentUser().getUid())) {
                                    mDatabaseSeller.child(snapshot.getKey()).child("profilePhtourl").setValue(uriString);
//                                        mDatabaseDonor.child(snapshot.getKey()).child("username").setValue(name);
                                }


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    DatabaseReference mDatabaseRaiser = FirebaseDatabase.getInstance().getReference("/rotlo/post/Raising");
                    mDatabaseRaiser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot Snapshot) {
                            for (DataSnapshot snapshot : Snapshot.getChildren()) {

                                if (snapshot.child("uid").getValue(String.class).equals(auth.getCurrentUser().getUid())) {
                                    mDatabaseRaiser.child(snapshot.getKey()).child("profilePhtourl").setValue(uriString);
//                                        mDatabaseDonor.child(snapshot.getKey()).child("username").setValue(name);
                                }


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    String nameForGreet = snapshot.child("fullName").getValue(String.class);
                    if (uriString != null) {


//
//                                Picasso.get().load(s).resize(1800, 1800).onlyScaleDown() // the image will only be resized if it's bigger than 2048x 1600 pixels.
//                                        .into(profileIcon);
//

                        Picasso.get().load(uriString).into(profileIcon);
                    }
//
                    String firstWord;

                    if (nameForGreet.contains(" ")) {
                        firstWord = nameForGreet.substring(0, nameForGreet.indexOf(" "));
//                        Greet.setText("Hii, " + firstWord + "!!");

                    } else {
//                        Greet.setText("Hii, " + nameForGreet.substring(0, 5) + "!!");
                    }


                }


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "no data", Toast.LENGTH_SHORT).show();
            }

        };
        mDatabase.addListenerForSingleValueEvent(eventListener);


        return rootView;
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
                bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getActivity().getContentResolver(),
                                filePath);

                if (bitmap.getByteCount() > 144609280) {
                    int nh = (int) (bitmap.getHeight() * (1024.0 / bitmap.getWidth()));
                    Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1024, nh, true);
                    Matrix matrix = new Matrix();

                    matrix.postRotate(90);
                    scaled = Bitmap.createBitmap(scaled, 0, 0, scaled.getWidth(), scaled.getHeight(), matrix, true);

                    profileIcon.setImageBitmap(scaled);
                } else {
                    profileIcon.setImageBitmap(bitmap);
                }

                uploadImage();

//                profileIcon.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }


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
                            "images/users/"
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
                                            auth = FirebaseAuth.getInstance();
                                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("/rotlo/user").child(auth.getCurrentUser().getUid());

                                            mDatabase.child("uri").setValue(uriIntoString);


                                            //Do what you want with the url
                                        }
                                    });


                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();

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

    private void setUserName(String userName) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("/rotlo/user").child(auth.getCurrentUser().getUid());

        mDatabase.child("fullName").setValue(userName);
    }
}