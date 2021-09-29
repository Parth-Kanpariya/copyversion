package com.example.copyversion.ui.main;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.copyversion.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentStateAdapter {

    private FragmentManager fragmentManager;
    public final List<Fragment> fragments = new ArrayList<>();
    public final List<String> fragmentTitle = new ArrayList<>();

    public SectionsPagerAdapter( FragmentActivity fm)
    {
        super(fm);
    }

    public void add(Fragment fragment, String title)
    {
        fragments.add(fragment);
        fragmentTitle.add(title);
    }

//    @NonNull @Override public Fragment getItem(int position)
//    {
//        return fragments.get(position);
//    }

    @Override public int getItemCount()
    {
        return fragments.size();
    }





    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }
}