package com.java.tu.app.message.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.java.tu.app.message.R;
import com.java.tu.app.message.adapter.HomePagerAdapter;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.java.tu.app.message.asset.Const.OFFLINE;
import static com.java.tu.app.message.asset.Const.ONLINE;
import static com.java.tu.app.message.asset.Const.SESSION;
import static com.java.tu.app.message.asset.Const.STATUS;

public class HomeActivity extends AppCompatActivity {


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.vp_home)
    ViewPager vp_home;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.bt_view)
    BottomNavigationView bottomNavigationView;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.bt_add)
    Button bt_add;


    private HomePagerAdapter adapter;

    private DatabaseReference refDb;
    private SharedPreferences refShared;
    private FirebaseUser fUser;
    private FirebaseAuth fAuth;
    private ValueEventListener sessionChange;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getColor(R.color.white));
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        Init();
        vp_home.setAdapter(adapter);
        vp_home.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().getItem(0).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().getItem(1).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().getItem(3).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().getItem(4).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_home:
                        vp_home.setCurrentItem(0);
                        break;
                    case R.id.item_chat:
                        vp_home.setCurrentItem(1);
                        break;
                    case R.id.item_profile:
                        vp_home.setCurrentItem(2);
                        break;
                    case R.id.item_setting:
                        vp_home.setCurrentItem(3);
                        break;
                }
                return true;
            }
        });
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "???", Toast.LENGTH_SHORT).show();
            }
        });
        refDb.child(STATUS).child(Objects.requireNonNull(fUser.getEmail()).hashCode() + "").setValue(ONLINE);
        refDb.child(SESSION).child(Objects.requireNonNull(fUser.getEmail()).hashCode() + "").addValueEventListener(sessionChange);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void Init() {
        refDb = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        refShared = getSharedPreferences(SESSION, MODE_PRIVATE);
        adapter = new HomePagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        sessionChange = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    String session = snapshot.getValue(String.class);
                    if (session != null) {
                        String sessionCurrent = refShared.getString(SESSION, "");
                        if (!session.equals(sessionCurrent)) {
                            SharedPreferences.Editor editor = refShared.edit();
                            editor.clear().apply();
                            if (fUser != null) {
                                refDb.child(SESSION).child(Objects.requireNonNull(fUser.getEmail()).hashCode() + "").removeEventListener(sessionChange);
                                refDb.child(STATUS).child(Objects.requireNonNull(fUser.getEmail()).hashCode() + "").setValue(OFFLINE).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            fAuth.signOut();
                                        }
                                    }
                                });
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        refDb.child(STATUS).child(Objects.requireNonNull(fUser.getEmail()).hashCode() + "").setValue(ONLINE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refDb.child(STATUS).child(Objects.requireNonNull(fUser.getEmail()).hashCode() + "").setValue(ONLINE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        refDb.child(STATUS).child(Objects.requireNonNull(fUser.getEmail()).hashCode() + "").setValue(OFFLINE);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        refDb.child(STATUS).child(Objects.requireNonNull(fUser.getEmail()).hashCode() + "").setValue(OFFLINE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fUser != null) {
            refDb.child(SESSION).child(Objects.requireNonNull(fUser.getEmail()).hashCode() + "").removeEventListener(sessionChange);
            refDb.child(STATUS).child(Objects.requireNonNull(fUser.getEmail()).hashCode() + "").setValue(OFFLINE).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        refDb.onDisconnect();
                    }
                }
            });
        }
    }
}