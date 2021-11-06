package com.example.copyversion;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.copyversion.ui.main.SectionsPagerAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link frontPage_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class frontPage_Fragment extends Fragment implements FeedAdapter.ListItemClickListener {

    private DatabaseReference databaseReference;
    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;
    private ArrayList<DonorInfo> donationList = new ArrayList<>();
    private ArrayList<DonorInfo> sellingList = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;
    private double longitude, latitude;
    FusedLocationProviderClient fusedLocationProviderClient;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public frontPage_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment frontPage_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static frontPage_Fragment newInstance(String param1, String param2) {
        frontPage_Fragment fragment = new frontPage_Fragment();
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

        View rootView = inflater.inflate(R.layout.activity_front_page, container, false);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        getlocation();



//        ActionBar actionBar;
//        actionBar = getSupportActionBar();
//        // Define ColorDrawable object and parse color
//        // using parseColor method
//        // with color hash code as its parameter
//        ColorDrawable colorDrawable
//                = new ColorDrawable(Color.parseColor("#ff9100"));
//        // Set BackgroundDrawable
//        actionBar.setBackgroundDrawable(colorDrawable);


        getdata(rootView);








        swipeRefreshLayout = rootView.findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                getParentFragmentManager().beginTransaction().detach(frontPage_Fragment.this).commit();
                donationList.clear();
                getParentFragmentManager().beginTransaction().attach(frontPage_Fragment.this).commit();

//                getdata(rootView);


                swipeRefreshLayout.setRefreshing(false);
            }
        });


        return rootView;
    }




    private void getdata(View rootView) {


        RecyclerView l = rootView.findViewById(R.id.list);
        l.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayList<DonorInfo> x = new ArrayList<>();


        final InfoAdapter adapter = new InfoAdapter(getContext(), donationList);

//        l.setBackgroundResource(R.drawable.rounded_corner);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("/rotlo/post/donation");

        ProgressDialog progressDialog
                = new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading..");

        progressDialog.show();

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String donorName = ds.child("donorName").getValue(String.class);
                        String donorAddress = ds.child("donorAddress").getValue(String.class);
                        String people = ds.child("people").getValue(String.class);
                        String mainCourse = ds.child("donorMainCourse").getValue(String.class);
                        String foodPhotUrl = ds.child("foodPhotoUrl").getValue(String.class);
                        String userId = ds.child("uid").getValue(String.class);
                        String username=ds.child("username").getValue(String.class);
                        String profilePhtourl=ds.child("profilePhtourl").getValue(String.class);
                        Date currentTime=ds.child("currentTime").getValue(Date.class);
                        String contact=ds.child("contact").getValue(String.class);
                        double latiitude = ds.child("latitude").getValue(double.class);
                        double longitude = ds.child("longitude").getValue(double.class);
                        String postID = ds.getKey();
//
                        if(foodPhotUrl==null)
                        {
                            foodPhotUrl="https://firebasestorage.googleapis.com/v0/b/copyversion-b749a.appspot.com/o/images%2Fpost%2F43ec27d6-98e2-408d-9001-eee5b9230592?alt=media&token=f35256b0-280c-4621-b961-1b2d1615aad6";
                        }


                        donationList.add(new DonorInfo(donorName, people, mainCourse, donorAddress, foodPhotUrl, userId, postID, latiitude, longitude,profilePhtourl,username,currentTime,contact));


//

                    }
                }

//                MainActivity mainActivity=new MainActivity();
//               Toast.makeText(getContext(),""+latitude+longitude,Toast.LENGTH_SHORT).show();
                Collections.sort(donationList,(o1, o2) -> (int) (o2.getCurrentTime().getTime()-o1.getCurrentTime().getTime()));
                Collections.sort(donationList, new SortPlaces(latitude, longitude));

//                Collections.reverse(donationList);
                adapter.notifyDataSetChanged();
                l.setAdapter(new FeedAdapter(donationList, frontPage_Fragment.this::onListItemClick,getContext()));
                progressDialog.dismiss();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "no data", Toast.LENGTH_SHORT).show();
            }

        };
        rootRef.addListenerForSingleValueEvent(eventListener);


    }


    @Override
    public void onListItemClick(int position) {
        Intent intent = new Intent(getContext(), FullInfoOfPost.class);
        DonorInfo x = donationList.get(position);
        intent.putExtra("PostId", x.getPostID());
        startActivity(intent);
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
//                    address = addressList.get(0).getAddressLine(0);
//                    Toast.makeText(getContext(), address, Toast.LENGTH_SHORT).show();

                }
            }
        });


    }


}