package com.example.copyversion;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FullInfoPostDonor#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FullInfoPostDonor extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String imgUrl;
    String x;

    public FullInfoPostDonor() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FullInfoPostDonor.
     */
    // TODO: Rename and change types and number of parameters
    public static FullInfoPostDonor newInstance(String param1, String param2) {
        FullInfoPostDonor fragment = new FullInfoPostDonor();
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
        View rootView= inflater.inflate(R.layout.fragment_full_info_post_donor, container, false);



        ProgressDialog progressDialog
                = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading..");

        progressDialog.show();



//        Button contactNumber = findViewById(R.id.contact_me);
        Button chat=rootView.findViewById(R.id.Chat_Initiate);
        TextView t2 = rootView.findViewById(R.id.textView2);
        TextView t9 = rootView.findViewById(R.id.textView9);
        TextView t6 = rootView.findViewById(R.id.textView6);
        TextView t4 = rootView.findViewById(R.id.textView4);
        ImageView imageView2 = rootView.findViewById(R.id.image);
        final String[] contact = new String[1];
        final String[] name = new String[1];
        final String[] people = new String[1];
        final String[] maincourse = new String[1];
        final String[] address = new String[1];
        String[] uid=new String[1];
        final String[] ProfilePhotoUrl=new String[1];
        final String[] ProfileName=new String[1];







        String PostId = getArguments().getString("PostId");
        DonorInfo d= (DonorInfo) getArguments().getSerializable("object");


//        Toast.makeText(FullInfoOfPost.this, ""+PostId, Toast.LENGTH_SHORT).show();
        if(PostId!=null) {
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("/rotlo/post/donation").child(PostId);
            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    t2.setText(snapshot.child("donorName").getValue(String.class));
                    t9.setText(snapshot.child("people").getValue(String.class));
                    t6.setText(snapshot.child("donorMainCourse").getValue(String.class));
                    t4.setText(snapshot.child("donorAddress").getValue(String.class));
                    imgUrl = snapshot.child("foodPhotoUrl").getValue(String.class);
                    if (imgUrl != null) {
                        Picasso.get().load(imgUrl).resize(2048, 1600).onlyScaleDown() // the image will only be resized if it's bigger than 2048x 1600 pixels.
                                .into(imageView2);
                    } else {
                        imageView2.setBackgroundResource(R.drawable.food_button1);

                    }

                    contact[0] = snapshot.child("contact").getValue(String.class);
                    name[0] = snapshot.child("donorName").getValue(String.class);
                    people[0] = snapshot.child("people").getValue(String.class);
                    maincourse[0] = snapshot.child("donorMainCourse").getValue(String.class);
                    address[0] = snapshot.child("donorAddress").getValue(String.class);
                    uid[0] = snapshot.child("uid").getValue(String.class);
                    x = uid[0];


                    if (x.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        chat.setVisibility(View.GONE);
                    } else {
                        DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference("/rotlo/user/" + x);
                        rootReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ProfilePhotoUrl[0] = snapshot.child("uri").getValue(String.class);
                                ProfileName[0] = snapshot.child("fullName").getValue(String.class);
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
        }

//        Toast.makeText(FullInfoOfPost.this, x, Toast.LENGTH_SHORT).show();


         imageView2.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Bundle bundle=new Bundle();
                 bundle.putString("imageUrl",imgUrl);
                 Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_fullInfoPostDonor_to_imageOfFood2,bundle);


             }
         });







        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("Name",ProfileName[0]);
                bundle.putString("imageUrl",ProfilePhotoUrl[0]);
                bundle.putString("uidOther",uid[0]);
                bundle.putSerializable("object1",d);
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_fullInfoPostDonor_to_chatApplicationn,bundle);


//                Intent chatIntent =new Intent(FullInfoOfPost.this,ChatApplication.class);
//                chatIntent.putExtra("Name",ProfileName[0]);
//                chatIntent.putExtra("imagUrl",ProfilePhotoUrl[0]);
//                chatIntent.putExtra("uidOther",uid[0]);
//                chatIntent.putExtra("object1",d);
//                startActivity(chatIntent);
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

        return rootView;
    }
}