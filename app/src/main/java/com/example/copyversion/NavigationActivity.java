package com.example.copyversion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavigationActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    Button postButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home_feed);

//        FragmentTransaction fr=getSupportFragmentManager().beginTransaction();
//        fr.add(R.id.container,new frontPage_Fragment());
//        fr.commit();

        fr.beginTransaction().add(R.id.container,information_fragment,"2").hide(information_fragment).commit();
        fr.beginTransaction().add(R.id.container,frontPage_fragment,"1").commit();



    }

    final information_Fragment information_fragment=new information_Fragment();
    final frontPage_Fragment frontPage_fragment=new frontPage_Fragment();
     Fragment active=frontPage_fragment;

    final FragmentManager fr=getSupportFragmentManager();





    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        FragmentTransaction x;
        switch (item.getItemId()) {
            case R.id.add_post:
                  fr.beginTransaction().hide(active).show(information_fragment).commit();
                  active=information_fragment;
//                getSupportFragmentManager().beginTransaction().replace(R.id.container, information_fragment).commit();
                return true;

            case R.id.home_feed:

//                getSupportFragmentManager().beginTransaction().replace(R.id.container, frontPage_fragment).commit();
//                  x=getSupportFragmentManager().beginTransaction();

                  fr.beginTransaction().hide(active).show(frontPage_fragment).commit();
                  active=frontPage_fragment;

//                x.detach(frontPage_fragment);
//                x.attach(frontPage_fragment);
//                x.commit();
                return true;


        }
        return false;
    }


}