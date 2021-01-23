package com.java.tu.app.message.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.java.tu.app.message.fragment.grid_image.GridImageFragment;
import com.java.tu.app.message.fragment.post.PostFragment;

import java.util.ArrayList;

public class ProfileViewPagerAdapter extends FragmentPagerAdapter {

    private final ArrayList<Fragment> fragments;


    public ProfileViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        fragments = new ArrayList<>();
        fragments.add(new GridImageFragment());
        fragments.add(new PostFragment());
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }



    @Override
    public int getCount() {
        return fragments.size();
    }
}
