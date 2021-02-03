package com.java.tu.app.message.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.java.tu.app.message.fragment.GridImageFragment;
import com.java.tu.app.message.fragment.PostFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ProfileViewPagerAdapter extends FragmentPagerAdapter {

    private final ArrayList<Fragment> fragments;


    public ProfileViewPagerAdapter(@NonNull FragmentManager fm, int behavior, @NotNull Context context) {
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
