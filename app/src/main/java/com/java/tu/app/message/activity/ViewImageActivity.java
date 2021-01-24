package com.java.tu.app.message.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.java.tu.app.message.R;
import com.java.tu.app.message.asset.Image;
import com.java.tu.app.message.database.sqlite.AssetImage;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.java.tu.app.message.asset.Const.AVATAR;
import static com.java.tu.app.message.asset.Const.IMAGE;
import static com.java.tu.app.message.asset.Const.KEY;
import static com.java.tu.app.message.asset.Const.NAME;
import static com.java.tu.app.message.asset.Const.PROFILE;

public class ViewImageActivity extends AppCompatActivity {

    private String image;
    private DatabaseReference refDb;
    private FirebaseUser fUser;
    private StorageReference refStg;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.iv_image)
    ImageView iv_image;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        setContentView(R.layout.activity_view_image);
        ButterKnife.bind(this);
        Init();
        setSupportActionBar(toolbar);
        refDb.child(PROFILE).child(fUser.getEmail().hashCode() + "").child(NAME).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    String name = snapshot.getValue(String.class);
                    if (name != null) {
                        if (name.length() > 0) {
                            toolbar.setTitle(name);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        image = getIntent().getStringExtra("image");
        if (image == null) {
            finish();
        }

        new Image(ViewImageActivity.this).getImage(iv_image, image, Long.MAX_VALUE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_image, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_menu) {
            select();
        }
        return super.onOptionsItemSelected(item);
    }

    private void Init() {
        refDb = FirebaseDatabase.getInstance().getReference();
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        refStg = FirebaseStorage.getInstance().getReference();
    }

    private void select() {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(ViewImageActivity.this).inflate(R.layout.view_select_image, null);
        BottomSheetDialog dialog = new BottomSheetDialog(ViewImageActivity.this);
        dialog.setContentView(view);
        view.findViewById(R.id.bt_apply_avatar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refDb.child(PROFILE).child(fUser.getEmail().hashCode() + "").child(AVATAR).setValue(image).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ViewImageActivity.this, "Success !", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });
        view.findViewById(R.id.bt_delete_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refDb.child(PROFILE).child(fUser.getEmail().hashCode() + "").child(AVATAR).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue() != null) {
                            String avatar = snapshot.getValue(String.class);
                            if (avatar != null) {
                                if (avatar.length() > 0) {
                                    if (avatar.equals(image)) {
                                        refDb.child(PROFILE).child(fUser.getEmail().hashCode() + "").child(AVATAR).removeValue();
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                refStg.child(IMAGE).child(image + KEY).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ViewImageActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
                refDb.child(IMAGE).child(fUser.getEmail().hashCode() + "").child(image).removeValue();
                new AssetImage(ViewImageActivity.this).delete(image);
            }
        });
        view.findViewById(R.id.bt_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable drawable = ((BitmapDrawable) iv_image.getDrawable());
                if (drawable != null) {
                    Bitmap bitmap = drawable.getBitmap();
                    if (bitmap != null) {
                        MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, UUID.randomUUID().toString() + KEY, null);
                        Toast.makeText(ViewImageActivity.this, "Success !", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            }
        });
        //TODO
        dialog.show();
    }
}