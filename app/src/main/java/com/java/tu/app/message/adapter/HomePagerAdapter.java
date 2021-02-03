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



    public HomePagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }


}
