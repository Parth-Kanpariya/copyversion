package com.example.copyversion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileOfOtherUser#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileOfOtherUser extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageView imageView;
    private TextView textView;
    private Button chatButton;
    private LinearLayout Donor,Raiser,seller;
    private  String uidOfOther;

    public ProfileOfOtherUser() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileOfOtherUser.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileOfOtherUser newInstance(String param1, String param2) {
        ProfileOfOtherUser fragment = new ProfileOfOtherUser();
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
        View rootView= inflater.inflate(R.layout.fragment_profile_of_other_user, container, false);

         Bundle bundle=getArguments();
         DonorInfo donorInfo= (DonorInfo) bundle.getSerializable("Object");
         SellerInfo sellerInfo= (SellerInfo) bundle.getSerializable("ObjectSeller");
         FoodRaiseInfo foodRaiseInfo= (FoodRaiseInfo) bundle.getSerializable("ObjectRaiser");

         String name=bundle.getString("NameFromChat");
         String imageUrl=bundle.getString("PhotFromChat");
         String uidOfOtherFromChat=bundle.getString("uidOfOther");



         imageView=rootView.findViewById(R.id.imageViewInProfileOfOther);
         textView=rootView.findViewById(R.id.UserNameInProfileOfOther);
         chatButton=rootView.findViewById(R.id.Chat_Initiate_with_other_User);
         if(donorInfo!=null)
         {
             uidOfOther=donorInfo.getUid();
             Picasso.get().load(donorInfo.getProfilePhtourl()).into(imageView);
             textView.setText(donorInfo.getUsername());
         }else if(sellerInfo!=null)
         {
             uidOfOther=sellerInfo.getUid();
             Picasso.get().load(sellerInfo.getProfilePhtourl()).into(imageView);
             textView.setText(sellerInfo.getUsername());
         }else if(foodRaiseInfo!=null)
         {
             uidOfOther=foodRaiseInfo.getUid();
             Picasso.get().load(foodRaiseInfo.getProfilePhtourl()).into(imageView);
             textView.setText(foodRaiseInfo.getUsername());
         }

         if(donorInfo==null&&sellerInfo==null&&foodRaiseInfo==null)
         {
             uidOfOther=uidOfOtherFromChat;
             Picasso.get().load(imageUrl).into(imageView);
             textView.setText(name);
         }


         chatButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(donorInfo!=null)
                 {
                     String nameOfUser = donorInfo.getUsername();
                     String profileImageUri = donorInfo.getProfilePhtourl();

                     Bundle bundle=new Bundle();
                     bundle.putString("Name",nameOfUser);
                     bundle.putString("imageUrl",profileImageUri);
                     bundle.putString("uidOther",uidOfOther);
//
                     Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_profileOfOtherUser_to_chatApplicationn,bundle);

                 }else if(sellerInfo!=null)
                 {
                     String nameOfUser = sellerInfo.getUsername();
                     String profileImageUri = sellerInfo.getProfilePhtourl();


                     Bundle bundle=new Bundle();
                     bundle.putString("Name",nameOfUser);
                     bundle.putString("imageUrl",profileImageUri);
                     bundle.putString("uidOther",uidOfOther);
//
                     Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_profileOfOtherUser_to_chatApplicationn,bundle);

                 }else if(foodRaiseInfo!=null)
                 {
                     String nameOfUser = foodRaiseInfo.getUsername();
                     String profileImageUri = foodRaiseInfo.getProfilePhtourl();

                     Bundle bundle=new Bundle();
                     bundle.putString("Name",nameOfUser);
                     bundle.putString("imageUrl",profileImageUri);
                     bundle.putString("uidOther",uidOfOther);
//
                     Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_profileOfOtherUser_to_chatApplicationn,bundle);

                 }else
                 {

                     Bundle bundle=new Bundle();
                     bundle.putString("Name",name);
                     bundle.putString("imageUrl",imageUrl);
                     bundle.putString("uidOther",uidOfOtherFromChat);
                     Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_profileOfOtherUser_to_chatApplicationn,bundle);

                 }

             }
         });

         Donor=rootView.findViewById(R.id.linearLayoutForOtherPostDonor);
         Donor.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Bundle bundle = new Bundle();
                 bundle.putString("decision", "NO");
                 if(donorInfo!=null)
                 {
                     bundle.putString("uidOfOther", uidOfOther);
                 }else if(sellerInfo!=null)
                 {
                     bundle.putString("uidOfOther", uidOfOther);
                 }else if(foodRaiseInfo!=null)
                 {
                     bundle.putString("uidOfOther", uidOfOther);
                 }else
                 {
                     bundle.putString("uidOfOther", uidOfOtherFromChat);

                 }

                 bundle.putString("YesOrNo","YES");

                 Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_profileOfOtherUser_to_my_post_donor,bundle);

             }
         });

        Raiser=rootView.findViewById(R.id.linearLayoutForOtherRaiser);
        Raiser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("decision", "NO");

                if(donorInfo!=null)
                {
                    bundle.putString("uidOfOther", uidOfOther);

                }else if(sellerInfo!=null)
                {
                    bundle.putString("uidOfOther", uidOfOther);

                }else if(foodRaiseInfo!=null)
                {
                    bundle.putString("uidOfOther", uidOfOther);

                }
                else
                {
                    bundle.putString("uidOfOther", uidOfOtherFromChat);

                }
                bundle.putString("YesOrNo","YES");

                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_profileOfOtherUser_to_my_post_raise,bundle);

            }
        });

        seller=rootView.findViewById(R.id.linearLayoutForOtherPostSeller);
        seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("decision", "NO");

                if(donorInfo!=null)
                {
                    bundle.putString("uidOfOther", uidOfOther);

                }else if(sellerInfo!=null)
                {
                    bundle.putString("uidOfOther", uidOfOther);

                }else if(foodRaiseInfo!=null)
                {
                    bundle.putString("uidOfOther", uidOfOther);

                }
                else
                {
                    bundle.putString("uidOfOther", uidOfOtherFromChat);

                }
                bundle.putString("YesOrNo","YES");

                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_profileOfOtherUser_to_my_post_sell,bundle);

            }
        });




        return rootView;
    }

}