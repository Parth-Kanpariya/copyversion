package com.example.copyversion;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OptionForDaS#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OptionForDaS extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button DonationButton,SellButoon;

    public OptionForDaS() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OptionForDaS.
     */
    // TODO: Rename and change types and number of parameters
    public static OptionForDaS newInstance(String param1, String param2) {
        OptionForDaS fragment = new OptionForDaS();
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

        View rootView= inflater.inflate(R.layout.fragment_option_for_da_s, container, false);

        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();

        information_Fragment information_fragment=new information_Fragment();
        SellingPostFragment sellingPostFragment=new SellingPostFragment();
        OptionForDaS optionForDaS =new OptionForDaS();

       fragmentManager.beginTransaction().add(R.id.visible,information_fragment).hide(information_fragment).commit();
       fragmentManager.beginTransaction().add(R.id.visible,sellingPostFragment).hide(sellingPostFragment).commit();
        DonationButton=rootView.findViewById(R.id.DonatButton);
        SellButoon=rootView.findViewById(R.id.SellButtom);
        LinearLayout linearLayout=rootView.findViewById(R.id.OptionDandS);

        DonationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                linearLayout.setVisibility(View.GONE);
//                 fragmentManager.beginTransaction().replace(R.id.container, information_fragment).setReorderingAllowed(true).commit();
                fragmentManager.beginTransaction().remove(optionForDaS).show(information_fragment).commit();




            }
        });

        SellButoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                linearLayout.setVisibility(View.GONE);
                fragmentManager.beginTransaction().remove(optionForDaS).show(sellingPostFragment).commit();
//                fragmentManager.beginTransaction().replace(R.id.container, sellingPostFragment).setReorderingAllowed(true).commit();
            }
        });


        return rootView;
    }
}