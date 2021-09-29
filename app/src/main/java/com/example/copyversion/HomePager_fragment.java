package com.example.copyversion;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.copyversion.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView= inflater.inflate(R.layout.fragment_home_pager, container, false);

//        binding = ActivityHomePagerBinding.inflate(getLayoutInflater());


        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter( getActivity());
        sectionsPagerAdapter.add(new frontPage_Fragment(),"Donation");
        sectionsPagerAdapter.add(new sellingFragment(),"Sell");



        ViewPager2 viewPager = rootView.findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
//        TabLayout tabs = rootView.findViewById(R.id.tabs);

//        tabs.setupWithViewPager(viewPager);

        viewPager=rootView.findViewById(R.id.view_pager);


        return rootView;
    }

    ViewPager2 viewPager;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        viewPager.setAdapter(new SectionsPagerAdapter(getActivity()));
        TabLayout tabLayout = view.findViewById(R.id.tabs);
        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText("hii");//Sets tabs names as mentioned in ViewPagerAdapter fragmentNames array, this can be implemented in many different ways.
                    }
                }
        ).attach();


//        TabLayout tabLayout = view.findViewById(R.id.tabs);
//        new TabLayoutMediator(tabLayout, viewPager , (tab, position) -> tab.setText("OBJECT " + (position + 1))).attach();
    }

}