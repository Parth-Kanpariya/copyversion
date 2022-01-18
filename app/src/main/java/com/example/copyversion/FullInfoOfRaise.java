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
 * Use the {@link FullInfoOfRaise#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FullInfoOfRaise extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String x;
    private  String imgUrl;

    public FullInfoOfRaise() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FullInfoOfRaise.
     */
    // TODO: Rename and change types and number of parameters
    public static FullInfoOfRaise newInstance(String param1, String param2) {
        FullInfoOfRaise fragment = new FullInfoOfRaise();
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
        View rootView= inflater.inflate(R.layout.fragment_full_info_of_raise, container, false);


        ProgressDialog progressDialog
                = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading..");

        progressDialog.show();


//        Button contactNumber=findViewById(R.id.contact_me);

        Button chat=rootView.findViewById(R.id.Chat_Initiate);
        TextView t2 =rootView.findViewById(R.id.textView2);
        TextView t9 = rootView.findViewById(R.id.textView9);
//        TextView t6 = findViewById(R.id.textView6);
        TextView t4 =rootView.findViewById(R.id.textView4);



        ImageView imageView2 = rootView.findViewById(R.id.image);


        final String[] contact = new String[1];
        final String[] name = new String[1];
        final String[] people = new String[1];
        final String[] maincourse = new String[1];
        final String[] address = new String[1];
        String[] uid = new String[1];
        final String[] ProfilePhotoUrl=new String[1];
        final String[] ProfileName=new String[1];



        String PostId = (String) getArguments().getString("PostId");
        FoodRaiseInfo foodRaiseInfo= (FoodRaiseInfo)getArguments().getSerializable("object1foodraise");


//        Toast.makeText(FullInfoOfPostRaise.this, ""+PostId, Toast.LENGTH_SHORT).show();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("/rotlo/post/Raising").child(PostId);
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                t2.setText(snapshot.child("raiserName").getValue(String.class));
                t9.setText(snapshot.child("people").getValue(String.class));
//                t6.setText(snapshot.child("sellerApproximate").getValue(String.class));
                t4.setText(snapshot.child("raiserAddress").getValue(String.class));
                imgUrl = snapshot.child("foodPhotoUrl").getValue(String.class);
                if (imgUrl != null) {
                    Picasso.get().load(imgUrl).resize(2048, 1600).onlyScaleDown() // the image will only be resized if it's bigger than 2048x 1600 pixels.
                            .into(imageView2);
                } else {
                    imageView2.setBackgroundResource(R.drawable.raisefood);

                }
                contact[0] = snapshot.child("contact").getValue(String.class);
                name[0] = snapshot.child("raiserName").getValue(String.class);
                people[0] = snapshot.child("people").getValue(String.class);
//                maincourse[0] =snapshot.child("donorMainCourse").getValue(String.class);
                address[0] = snapshot.child("raiserAddress").getValue(String.class);
                uid[0] = snapshot.child("uid").getValue(String.class);
                x = uid[0];




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


            imageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle=new Bundle();
                    bundle.putString("imageUrl",imgUrl);
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_fullInfoOfRaise_to_imageOfFood2,bundle);


                }
            });





//        t6.setVisibility(View.GONE);

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle=new Bundle();
                bundle.putString("Name",ProfileName[0]);
                bundle.putString("imageUrl",ProfilePhotoUrl[0]);
                bundle.putString("uidOther",uid[0]);
                bundle.putSerializable("object1foodraise",foodRaiseInfo);

                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_fullInfoOfRaise_to_chatApplicationn,bundle);


            }
        });
//        Intent i = getIntent();
//        FoodRaiseInfo x = (FoodRaiseInfo) i.getSerializableExtra("hi");
//        t2.setText(x.getRaiserName());
//        t9.setText(x.getPeople());
//   //     t6.setText("For Raising");
//        t4.setText(x.getRaiserAddress());
//        String s = (x.getFoodPhotoUrl());
//        if (s != null) {
//
//            Picasso.get().load(s).resize(2048, 1600).onlyScaleDown() // the image will only be resized if it's bigger than 2048x 1600 pixels.
//                    .into(imageView2);
//
//        }

//        contactNumber.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//
//
//                String Text="https://wa.me/91"+contact[0]+"?text=RaisePost%0A%0AName:-"+name[0]+"%0A"+"People:-"+people[0]+"%0A"+"Address:-"+address[0]+"%0A"+"%0A"+"I'm interested for this";
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