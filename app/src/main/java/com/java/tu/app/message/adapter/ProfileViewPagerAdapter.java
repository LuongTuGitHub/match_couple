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



    public ProfileViewPagerAdapter(@NonNull FragmentManager fm, int behavior, @NotNull Context context) {
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
