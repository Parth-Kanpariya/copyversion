package com.example.copyversion;

import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link frontPage_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class my_post_raise extends Fragment implements FeedAdapter.ListItemClickListener {

    private DatabaseReference databaseReference;
    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;
    private ArrayList<DonorInfo> donationList = new ArrayList<>();
    private ArrayList<SellerInfo> sellingList = new ArrayList<>();
    private ArrayList<FoodRaiseInfo> RaisingList = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;
    private double longitude, latitude;
    String uidOfOtherUser=null;
    FusedLocationProviderClient fusedLocationProviderClient;

    String deci;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public my_post_raise() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
//    public static frontPage_Fragment newInstance(String param1, String param2) {
//        frontPage_Fragment fragment = new frontPage_Fragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle=getArguments();
            if(bundle!=null)
            {
                uidOfOtherUser=bundle.getString("uidOfOther");
            }
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_my_post_raise, container, false);


        if(RaisingList!=null)
        {
            RaisingList.clear();
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        getlocation();
//
        Bundle bundle1=getArguments();
        if(bundle1!=null)
        {
            deci=bundle1.getString("YesOrNo");

        }


        getdata(rootView);


        swipeRefreshLayout = rootView.findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                getParentFragmentManager().beginTransaction().detach(my_post_raise.this).commit();
                RaisingList.clear();
                getParentFragmentManager().beginTransaction().attach(my_post_raise.this).commit();

//                getdata(rootView);


                swipeRefreshLayout.setRefreshing(false);
            }
        });


        viewPager = rootView.findViewById(R.id.view_pager);


        return rootView;
    }


    private void getdata(View rootView) {

        // calling add value event listener method
        // for getting the values from database.

//        ListView l = findViewById(R.id.list);


        //for Recycler view
//        RecyclerView l = rootView.findViewById(R.id.list);
//        l.setLayoutManager(new LinearLayoutManager(getContext()));


        RecyclerView sl = rootView.findViewById(R.id.Raisinglist);
        sl.setLayoutManager(new LinearLayoutManager(getContext()));


        ArrayList<FoodRaiseInfo> x = new ArrayList<>();


        final InfoAdapter adapter = new InfoAdapter(getContext(), donationList);

//        l.setBackgroundResource(R.drawable.rounded_corner);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("/rotlo/post/Raising");


        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String RaiserName = ds.child("raiserName").getValue(String.class);
                        String RaiserAddress = ds.child("raiserAddress").getValue(String.class);

                        String People = ds.child("people").getValue(String.class);
                        String foodPhotUrl = ds.child("foodPhotoUrl").getValue(String.class);
                        String uid = ds.child("uid").getValue(String.class);
                        String username = ds.child("username").getValue(String.class);
                        String profilePhtourl = ds.child("profilePhtourl").getValue(String.class);
                        Date currentTime = ds.child("currentTime").getValue(Date.class);
                        String contact = ds.child("contact").getValue(String.class);
                        double latiitude = ds.child("latitude").getValue(double.class);
                        double longitude = ds.child("longitude").getValue(double.class);
                        String postID = ds.getKey();

                        if (foodPhotUrl == null) {
                            foodPhotUrl = "https://firebasestorage.googleapis.com/v0/b/copyversion-b749a.appspot.com/o/images%2Fpost%2Fselling%2F8f62ebcb-3d7f-4f54-a06d-fc50e2d84c73?alt=media&token=95d18879-1068-4be1-9e1d-651fddde9151";
                        }

                        Bundle bundle=getArguments();
                        String decision=null;
                        if(bundle!=null)
                        {
                            decision=bundle.getString("decision");
                        }
                        boolean isClickable;
                        if(decision=="NO")
                        {
                            isClickable=false;
                        }else
                        {
                            isClickable=true;
                        }

                        if(uidOfOtherUser!=null)
                        {
                            if(uid.equals(uidOfOtherUser))
                            {
                            RaisingList.add(new FoodRaiseInfo(RaiserName, People, RaiserAddress, foodPhotUrl, uid, postID, latiitude, longitude, profilePhtourl, username, currentTime, contact,isClickable));
                            }
                        }
                        else
                        {
                            if (uid.equals(FirebaseAuth.getInstance().getUid())) {
                                RaisingList.add(new FoodRaiseInfo(RaiserName, People, RaiserAddress, foodPhotUrl, uid, postID, latiitude, longitude, profilePhtourl, username, currentTime, contact,isClickable));

                            }
                        }


                    }
                }


                Collections.sort(RaisingList, (o1, o2) -> (int) (o2.getCurrentTime().getTime() - o1.getCurrentTime().getTime()));
                Collections.sort(RaisingList, new SortPlacesRaising(latitude, longitude));
                adapter.notifyDataSetChanged();
//                l.setAdapter(new FeedAdapter(donationList,sellingFragment.this::onListItemClick));
                sl.setAdapter(new FeedAdapterRaise(RaisingList, my_post_raise.this::onListItemClick, getContext()));

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "no data", Toast.LENGTH_SHORT).show();
            }

        };
        rootRef.addListenerForSingleValueEvent(eventListener);


    }

    ViewPager2 viewPager;
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        TabLayout tabLayout = view.findViewById(R.id.tabs);
//        new TabLayoutMediator(tabLayout, viewPager , (tab, position) -> tab.setText("OBJECT " + (position + 1))).attach();
//    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onListItemClick(int position) {
        Bundle bundle=new Bundle();
        FoodRaiseInfo x = RaisingList.get(position);
        bundle.putString("PostId",x.getPostID());
        bundle.putSerializable("object1foodraise",x);

        if(deci=="YES")
        {
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_my_post_raise_to_fullInfoOfRaise ,bundle);


        }else {
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_my_post_to_fullInfoOfRaise2, bundle);
        }
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