package com.example.copyversion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

public class NavigationActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    Button postButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

//        Button chatInitiate= findViewById(R.id.chatInitiate);
//        chatInitiate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent=new Intent(NavigationActivity.this,ChatInitiate.class);
//                startActivity(intent);
//            }
//        });










        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);


        NavigationUI.setupWithNavController(bottomNavigationView,
                navHostFragment.getNavController());




//        bottomNavigationView.setOnNavigationItemSelectedListener(this);
//
//
//
//        bottomNavigationView.setSelectedItemId(R.id.home_feed);
//        fr.beginTransaction().add(R.id.container,profileFragment,"3").hide(profileFragment).commit();
//        fr.beginTransaction().add(R.id.container,optionForDaS,"2").hide(optionForDaS).commit();
//        fr.beginTransaction().add(R.id.container,homePager_fragment,"1").commit();



    }

    final information_Fragment information_fragment=new information_Fragment();
    final frontPage_Fragment frontPage_fragment=new frontPage_Fragment();
    final HomePager_fragment homePager_fragment=new HomePager_fragment();
    final ProfileFragment profileFragment=new ProfileFragment();
    final OptionForDaS optionForDaS =new OptionForDaS();
     Fragment active=frontPage_fragment;

    final FragmentManager fr=getSupportFragmentManager();





    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

//        FragmentTransaction x;
//        switch (item.getItemId()) {
//            case R.id.add_post:
////                  fr.beginTransaction().replace(R.id.container,optionForDaS).setReorderingAllowed(true).commit();
//                fr.beginTransaction().hide(active).show(optionForDaS).commit();
//                  active=optionForDaS;
////                getSupportFragmentManager().beginTransaction().replace(R.id.container, information_fragment).commit();
//                return true;
//
//            case R.id.home_feed:
//
////                getSupportFragmentManager().beginTransaction().replace(R.id.container, frontPage_fragment).commit();
////                  x=getSupportFragmentManager().beginTransaction();
//
//                  fr.beginTransaction().hide(active).show(homePager_fragment).commit();
//                  active=homePager_fragment;
//
////                fr.beginTransaction().replace(R.id.container,homePager_fragment).setReorderingAllowed(true).commit();
//
////                x.detach(frontPage_fragment);
////                x.attach(frontPage_fragment);
////                x.commit();
//                return true;
//
//            case R.id.user_id:
//                fr.beginTransaction().hide(active).show(profileFragment).commit();
//                active=profileFragment;
//
////                fr.beginTransaction().replace(R.id.container,profileFragment).setReorderingAllowed(true).commit();
////                active=profileFragment;
//                return true;
//
//
//        }
        return false;
    }


}