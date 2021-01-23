package com.java.tu.app.message.fragment.setting;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.java.tu.app.message.R;

public class SettingFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}