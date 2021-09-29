package com.example.copyversion;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link frontPage_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class sellingFragment extends Fragment implements FeedAdapter.ListItemClickListener {

    private DatabaseReference databaseReference;
    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;
    private ArrayList<DonorInfo> donationList = new ArrayList<>();
    private ArrayList<SellerInfo> sellingList = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;




    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public sellingFragment() {
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

        View rootView=inflater.inflate(R.layout.fragment_selling, container, false);


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





        swipeRefreshLayout=rootView.findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                getParentFragmentManager().beginTransaction().detach(sellingFragment.this).commit();
                sellingList.clear();
                getParentFragmentManager().beginTransaction().attach(sellingFragment.this).commit();

//                getdata(rootView);


                swipeRefreshLayout.setRefreshing(false);
            }
        });





        viewPager=rootView.findViewById(R.id.view_pager);





        return rootView;
    }



    private void getdata(View rootView) {

        // calling add value event listener method
        // for getting the values from database.

//        ListView l = findViewById(R.id.list);


        //for Recycler view
//        RecyclerView l = rootView.findViewById(R.id.list);
//        l.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerView sl=rootView.findViewById(R.id.sellinglist);
        sl.setLayoutManager(new LinearLayoutManager(getContext()));


        ArrayList<SellerInfo> x = new ArrayList<>();


        final InfoAdapter adapter = new InfoAdapter(getContext(), donationList);

//        l.setBackgroundResource(R.drawable.rounded_corner);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("/rotlo/post/selling");


        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String donorName = ds.child("sellerName").getValue(String.class);
                        String donorAddress = ds.child("sellerAddress").getValue(String.class);

                        String mainCourse = ds.child("sellerApproximate").getValue(String.class);
                        String foodPhotUrl = ds.child("foodPhotoUrl").getValue(String.class);



                            sellingList.add(new SellerInfo(donorName, mainCourse, donorAddress, foodPhotUrl));


                    }
                }

                Collections.reverse(sellingList);
                Collections.reverse(donationList);
                adapter.notifyDataSetChanged();
//                l.setAdapter(new FeedAdapter(donationList,sellingFragment.this::onListItemClick));
                sl.setAdapter(new FeedAdapterSell(sellingList,sellingFragment.this::onListItemClick));

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
        Intent intent = new Intent(getContext(), FullInfoOfPostSell.class);
        SellerInfo x = sellingList.get(position);
        intent.putExtra("hi", x);
        startActivity(intent);
    }



}