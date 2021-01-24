package com.java.tu.app.message.fragment.profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.java.tu.app.message.R;
import com.java.tu.app.message.activity.EditProfileActivity;
import com.java.tu.app.message.activity.LoginActivity;
import com.java.tu.app.message.adapter.ProfileViewPagerAdapter;
import com.java.tu.app.message.asset.Image;
import com.java.tu.app.message.object.Profile;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.java.tu.app.message.asset.Const.PROFILE;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private ProfileViewModel mViewModel;
    private View view;
    private FirebaseUser fUser;
    private DatabaseReference refDb;
    private ValueEventListener userListener;
    private ProfileViewPagerAdapter adapter;


    private Profile profile;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.bt_log_out)
    Button bt_log_out;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.bt_edit_profile)
    Button bt_edit_profile;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.iv_avatar)
    ImageView iv_avatar;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.vp_view)
    ViewPager vp_view;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tb_view)
    TabLayout tb_view;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_name)
    TextView tv_name;

    public ProfileFragment() {
        Init();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.profile_fragment, container, false);
        }
        ButterKnife.bind(this, view);
        refDb.child(PROFILE).child(Objects.requireNonNull(fUser.getEmail()).hashCode() + "").addValueEventListener(userListener);
        bt_edit_profile.setOnClickListener(ProfileFragment.this);
        bt_log_out.setOnClickListener(ProfileFragment.this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Account();
            }
        });
        if (adapter == null) {
            adapter = new ProfileViewPagerAdapter(requireActivity().getSupportFragmentManager(), FragmentPagerAdapter.POSITION_UNCHANGED);
            vp_view.setAdapter(adapter);
        }
        tb_view.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp_view.setCurrentItem(tab.getPosition());
                Objects.requireNonNull(tab.getIcon()).setTint(getResources().getColor(R.color.black));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Objects.requireNonNull(tab.getIcon()).setTint(getResources().getColor(R.color.gray));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        vp_view.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tb_view.selectTab(tb_view.getTabAt(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        adapter.notifyDataSetChanged();
        Objects.requireNonNull(Objects.requireNonNull(tb_view.getTabAt(vp_view.getCurrentItem())).getIcon()).setTint(getResources().getColor(R.color.black));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_log_out:
                logOut();
                break;
            case R.id.bt_edit_profile:
                editProfile();
                break;
        }
    }


    private void Init() {
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        refDb = FirebaseDatabase.getInstance().getReference();
        userListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    profile = snapshot.getValue(Profile.class);
                    if (profile != null) {
                        if (profile.getName() != null) {
                            tv_name.setText(profile.getName());
                        }
                        if (profile.getAvatar() != null) {
                            if (profile.getAvatar().length() > 0) {
                                new Image(requireContext()).getImage(iv_avatar, profile.getAvatar(), Long.MAX_VALUE);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }

    private void logOut() {
        @SuppressLint("InflateParams") View view_dialog = LayoutInflater.from(getContext()).inflate(R.layout.log_out_bottom_sheet, null);
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(view_dialog);
        view_dialog.findViewById(R.id.bt_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        view_dialog.findViewById(R.id.bt_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        dialog.show();
    }

    private void editProfile() {
        Intent intent = new Intent(getContext(), EditProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        refDb.onDisconnect();
    }

    private void Account() {
        @SuppressLint("InflateParams") View bottom_view = LayoutInflater.from(requireContext()).inflate(R.layout.bottom_account_view, null);
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(bottom_view);
        ((TextView) bottom_view.findViewById(R.id.tv_email)).setText(fUser.getEmail());
        refDb.child(PROFILE).child(Objects.requireNonNull(fUser.getEmail()).hashCode() + "").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    Profile profile = snapshot.getValue(Profile.class);
                    if (profile != null) {
                        if (profile.getName() != null) {
                            if (profile.getName().length() > 0) {
                                ((TextView) bottom_view.findViewById(R.id.tv_name)).setText(profile.getName());
                            }
                        }
                        if (profile.getAvatar() != null) {
                            if (profile.getAvatar().length() > 0) {
                                new Image(requireContext()).getImage(bottom_view.findViewById(R.id.iv_avatar), profile.getAvatar(), Long.MAX_VALUE);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        dialog.show();
    }
}