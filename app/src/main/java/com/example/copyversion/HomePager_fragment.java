package com.example.copyversion;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Parcelable;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.copyversion.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomePager_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomePager_fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomePager_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomePager_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomePager_fragment newInstance(String param1, String param2) {
        HomePager_fragment fragment = new HomePager_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//           viewPager=savedInstanceState.getParcelable("viewPage");
//        }
    }
    ViewPager viewPager;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        }
        if(rootView==null) {



                rootView = inflater.inflate(R.layout.fragment_home_pager, container, false);
//        binding = ActivityHomePagerBinding.inflate(getLayoutInflater());

                ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

                SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());

                sectionsPagerAdapter.add(new frontPage_Fragment(), "Donation");
                sectionsPagerAdapter.add(new RaiseFoodFragment(), "Raise");
               sectionsPagerAdapter.add(new sellingFragment(), "Sell");




                viewPager = rootView.findViewById(R.id.view_pager);
                viewPager.setAdapter(sectionsPagerAdapter);
                TabLayout tabs = rootView.findViewById(R.id.tabs);
                tabs.setupWithViewPager(viewPager);
                viewPager.setOffscreenPageLimit(4);








        }
        return rootView;
    }

//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putParcelable("viewPage", (Parcelable) viewPager);
//    }
}