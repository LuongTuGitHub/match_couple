package com.java.tu.app.message.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.java.tu.app.message.fragment.ChatFragment;
import com.java.tu.app.message.fragment.HomeFragment;
import com.java.tu.app.message.fragment.ProfileFragment;
import com.java.tu.app.message.fragment.SettingFragment;

import java.util.ArrayList;

public class HomePagerAdapter extends FragmentPagerAdapter {

    private final ArrayList<Fragment> fragments;


    public HomePagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new ChatFragment());
        fragments.add(new ProfileFragment());
        fragments.add(new SettingFragment());
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
