package com.example.copyversion;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;

import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.copyversion.authentication.ui.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private TextView name;
    private TextView email, Greet;
    private ImageView profileIcon;
    private Uri filePath;
    private String uriIntoString;
    private View rootView;
    private Bitmap bitmap;
    private String s;
    private GoogleSignInClient mGoogleSignInClient;

    private LinearLayout logOutLayout, myPostLayout, deleteAccountLayout,profilNameAndPhoto;
    Map<String, Object> postValues = new HashMap<String, Object>();


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FirebaseAuth auth;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle=getArguments();
        if(bundle!=null)
        {
            name.setText(bundle.getString("name"));
            email.setText(bundle.getString("email"));
            Picasso.get().load(bundle.getString("Image")).into(profileIcon);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if (rootView==null) {

            rootView = inflater.inflate(R.layout.fragment_profile, container, false);


            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();


            logOutLayout = rootView.findViewById(R.id.linearLayoutForLogout);
            logOutLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle("Rotlo");
                    alert.setMessage("Do you want to Log out the account?");
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            logOut(rootView);
                        }
                    });
                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alert.create().show();

                }
            });

            profilNameAndPhoto=rootView.findViewById(R.id.profileNameAndPhoto);
            profilNameAndPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_profileFragment_to_editProfile);

                }
            });

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.web_client_id))
                    .requestEmail()
                    .build();

            mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

            deleteAccountLayout = rootView.findViewById(R.id.linearLayoutForDeleteAccount);
            deleteAccountLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle("Rotlo");
                    alert.setMessage("Do you want to delete the account?");
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteAuthentication();
                            deletePost(auth);
                            deleteUser(auth);
                            deleteChat(auth);
                            mGoogleSignInClient.signOut();
                        }
                    });
                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alert.create().show();
                }
            });

            myPostLayout = rootView.findViewById(R.id.linearLayoutForMyPost);
            myPostLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_profileFragment_to_my_post);
                }
            });


            name = rootView.findViewById(R.id.UserName);
            email = rootView.findViewById(R.id.UserEmail);
            profileIcon = rootView.findViewById(R.id.profilePhoto);
            Greet = rootView.findViewById(R.id.greetings);


            Bundle bundle=new Bundle();

            auth = FirebaseAuth.getInstance();


            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("/rotlo/user").child(auth.getCurrentUser().getUid());
            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()) {


                        email.setText(snapshot.child("email").getValue(String.class));
                        String nameForPost = (snapshot.child("fullName").getValue(String.class));
                        name.setText(snapshot.child("fullName").getValue(String.class));
                        s = snapshot.child("uri").getValue(String.class);
                        bundle.putString("name",nameForPost);
                        bundle.putString("email",snapshot.child("email").getValue(String.class));
                        bundle.putString("Image",snapshot.child("uri").getValue(String.class));



                        DatabaseReference mDatabaseDonor = FirebaseDatabase.getInstance().getReference("/rotlo/post/donation");
                        mDatabaseDonor.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot Snapshot) {
                                for (DataSnapshot snapshot : Snapshot.getChildren()) {

                                    if (snapshot.child("uid").getValue(String.class).equals(auth.getCurrentUser().getUid())) {
                                        mDatabaseDonor.child(snapshot.getKey()).child("profilePhtourl").setValue(s);
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
                                        mDatabaseSeller.child(snapshot.getKey()).child("profilePhtourl").setValue(s);
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
                                        mDatabaseRaiser.child(snapshot.getKey()).child("profilePhtourl").setValue(s);
//                                        mDatabaseDonor.child(snapshot.getKey()).child("username").setValue(name);
                                    }


                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                        String nameForGreet = snapshot.child("fullName").getValue(String.class);
                        if (s != null) {


//
//                                Picasso.get().load(s).resize(1800, 1800).onlyScaleDown() // the image will only be resized if it's bigger than 2048x 1600 pixels.
//                                        .into(profileIcon);
//

                            Picasso.get().load(s).into(profileIcon);
                        }
//
                        String firstWord;

                        if (nameForGreet.contains(" ")) {
                            firstWord = nameForGreet.substring(0, nameForGreet.indexOf(" "));
                            Greet.setText("Hii, " + firstWord + "!!");

                        } else {
                            Greet.setText("Hii, " + nameForGreet.substring(0, 5) + "!!");
                        }


                    }


                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "no data", Toast.LENGTH_SHORT).show();
                }

            };
            mDatabase.addListenerForSingleValueEvent(eventListener);


        }

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


//                profileIcon.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }



//


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

    private void deletePost(FirebaseAuth auth) {


        DatabaseReference rootRef1 = FirebaseDatabase.getInstance().getReference("/rotlo/post/Raising");
        rootRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot s : snapshot.getChildren()) {
                        DatabaseReference rootRef11 = FirebaseDatabase.getInstance().getReference("/rotlo/post/Raising").child(s.getKey());
                        if (auth.getCurrentUser().getUid().equals(s.child("uid").getValue(String.class))) {
                            rootRef11.removeValue();
                        }


                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//
        DatabaseReference rootRef2 = FirebaseDatabase.getInstance().getReference("/rotlo/post/donation/");
        rootRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot s : snapshot.getChildren()) {
                        DatabaseReference rootRef22 = FirebaseDatabase.getInstance().getReference("/rotlo/post/donation").child(s.getKey());
                        if (auth.getCurrentUser().getUid().equals(s.child("uid").getValue(String.class))) {
                            rootRef22.removeValue();
                        }


                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        Toast.makeText(getContext(), ""+rootRef2.getParent(), Toast.LENGTH_SHORT).show();

        DatabaseReference rootRef3 = FirebaseDatabase.getInstance().getReference("/rotlo/post/selling");
        rootRef3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot s : snapshot.getChildren()) {
                        DatabaseReference rootRef33 = FirebaseDatabase.getInstance().getReference("/rotlo/post/selling").child(s.getKey());
                        if (auth.getCurrentUser().getUid().equals(s.child("uid").getValue(String.class))) {
                            rootRef33.removeValue();
                        }


                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void deleteUser(FirebaseAuth auth) {

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("/rotlo/user/");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot s : snapshot.getChildren()) {
                        if (s.getKey().equals(auth.getCurrentUser().getUid())) {
                            s.getRef().removeValue();
                        }

                    }

//                    Toast.makeText(getContext(), ""+snapshot.getChildren(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void deleteAuthentication() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.


//        AuthCredential credential_for_Normal_user = EmailAuthProvider.getCredential(email, password);
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
//        AuthCredential credential_for_google_user = GoogleAuthProvider.getCredential(account.getIdToken(), email);
        final int[] temp = {0};

//

        // Prompt the user to re-provide their sign-in credentials
        if (user != null) {
            user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                Intent intent = new Intent(getContext(), MainActivity.class);
                                startActivity(intent);
                                Toast.makeText(getContext(), "Deleted User Successfully,", Toast.LENGTH_LONG).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "No delete,", Toast.LENGTH_LONG).show();
                }
            });

        }

    }

    void deleteChat(FirebaseAuth auth) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("/rotlo/chat/");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot s : snapshot.getChildren()) {
                        if (s.getKey().contains(auth.getCurrentUser().getUid())) {
                            DatabaseReference mDatabaseForDeleteChat = FirebaseDatabase.getInstance().getReference("/rotlo/chat/").child(s.getKey());
                            mDatabaseForDeleteChat.removeValue();
                        }


                    }

//                    Toast.makeText(getContext(), ""+snapshot.getChildren(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void logOut(View view) {
        auth = FirebaseAuth.getInstance();
        auth.signOut();
        mGoogleSignInClient.signOut();
//        if (mGoogleSignInClient.) {
//            Plus.AccountApi.clearDefaultAccount(mGoogleSignInClient);
//            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleSignInClient);
//            mGoogleApiClient.disconnect();
//            mGoogleApiClient.connect();
//        }
        Intent intent = new Intent(view.getContext(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}