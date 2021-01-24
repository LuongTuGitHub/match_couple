package com.java.tu.app.message.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.java.tu.app.message.R;
import com.java.tu.app.message.asset.Image;
import com.java.tu.app.message.database.sqlite.AssetImage;
import com.java.tu.app.message.object.Profile;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.java.tu.app.message.asset.Const.AVATAR;
import static com.java.tu.app.message.asset.Const.IMAGE;
import static com.java.tu.app.message.asset.Const.KEY;
import static com.java.tu.app.message.asset.Const.NAME;
import static com.java.tu.app.message.asset.Const.OFFLINE;
import static com.java.tu.app.message.asset.Const.ONLINE;
import static com.java.tu.app.message.asset.Const.PICK_IMAGE;
import static com.java.tu.app.message.asset.Const.PROFILE;
import static com.java.tu.app.message.asset.Const.STATUS;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.iv_avatar)
    ImageView iv_avatar;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.bt_change_avatar)
    Button bt_change_avatar;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edt_name)
    TextInputEditText edt_name;

    private DatabaseReference refDb;
    private FirebaseUser fUser;
    private ValueEventListener userListener;
    private StorageReference refStg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.WHITE);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        Init();
        refDb.child(PROFILE).child(Objects.requireNonNull(fUser.getEmail()).hashCode() + "").addValueEventListener(userListener);
        refDb.child(STATUS).child(Objects.requireNonNull(fUser.getEmail()).hashCode() + "").setValue(ONLINE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bt_change_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE);
            }
        });
        iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.ok) {
            confirmChange();
        }
        return true;
    }

    private void Init() {
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        refDb = FirebaseDatabase.getInstance().getReference();
        refStg = FirebaseStorage.getInstance().getReference();
        userListener = new ValueEventListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    Profile profile = snapshot.getValue(Profile.class);
                    if (profile != null) {
                        if (profile.getName() != null) {
                            edt_name.setText(profile.getName());
                        }
                        if (profile.getAvatar() != null) {
                            if (profile.getAvatar().length() > 0) {
                                new Image(EditProfileActivity.this).getImage(iv_avatar, profile.getAvatar(), Long.MAX_VALUE);
                            } else {
                                iv_avatar.setImageDrawable(getResources().getDrawable(R.color.white));
                            }
                        } else {
                            iv_avatar.setImageDrawable(getResources().getDrawable(R.color.white));
                        }
                    }
                } else {
                    iv_avatar.setImageDrawable(getResources().getDrawable(R.color.white));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }

    private void confirmChange() {
        if (edt_name != null) {
            String name = Objects.requireNonNull(edt_name.getText()).toString().trim();
            if (name.length() > 0) {
                refDb.child(PROFILE).child(Objects.requireNonNull(fUser.getEmail()).hashCode() + "").removeEventListener(userListener);
                updateProfile(name);
            }
        }
    }

    private void updateProfile(@NotNull String name) {
        refDb.child(PROFILE).child(Objects.requireNonNull(fUser.getEmail()).hashCode() + "").child(NAME).setValue(name).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                refDb.child(PROFILE).child(fUser.getEmail().hashCode() + "").addValueEventListener(userListener);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                String key = Calendar.getInstance().getTimeInMillis() + "" + Objects.requireNonNull(fUser.getEmail()).hashCode();
                //TODO
                if (data != null) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        refStg.child(IMAGE).child(key + KEY).putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    refDb.child(PROFILE).child(fUser.getEmail().hashCode() + "").child(AVATAR).setValue(key).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                refDb.child(IMAGE).child(fUser.getEmail().hashCode() + "").child(key).setValue(key);
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        refDb.child(PROFILE).child(Objects.requireNonNull(fUser.getEmail()).hashCode() + "").removeEventListener(userListener);
        refDb.child(STATUS).child(Objects.requireNonNull(fUser.getEmail()).hashCode() + "").setValue(OFFLINE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refDb.child(PROFILE).child(Objects.requireNonNull(fUser.getEmail()).hashCode() + "").addValueEventListener(userListener);
        refDb.child(STATUS).child(Objects.requireNonNull(fUser.getEmail()).hashCode() + "").setValue(ONLINE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        refDb.child(PROFILE).child(Objects.requireNonNull(fUser.getEmail()).hashCode() + "").removeEventListener(userListener);
        refDb.onDisconnect();
    }

}