package com.java.tu.app.message.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.java.tu.app.message.R;
import com.java.tu.app.message.object.Post;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.java.tu.app.message.asset.Const.IMAGE;
import static com.java.tu.app.message.asset.Const.KEY;
import static com.java.tu.app.message.asset.Const.PICK_IMAGE;
import static com.java.tu.app.message.asset.Const.POST;

public class CreatePostActivity extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edt_text)
    EditText edt_text;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.iv_image)
    ImageView iv_image;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.create)
    Button bt_create;


    private ArrayList<Uri> uris;
    private FirebaseUser fUser;
    private StorageReference refStg;
    private DatabaseReference refDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        uris = new ArrayList<>();
        refDb = FirebaseDatabase.getInstance().getReference();
        refStg = FirebaseStorage.getInstance().getReference();
        ButterKnife.bind(this);
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE);
            }
        });
        bt_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_text != null) {
                    String text = edt_text.getText().toString().trim();
                    if (text != null || text.length() > 0 || uris.size() != 0) {
                        long time = Calendar.getInstance().getTimeInMillis();
                        String key = time + "" + fUser.getEmail().hashCode();
                        Post post = new Post(fUser.getEmail(), key, time, text, key, null);
                        FirebaseStorage.getInstance().getReference().child(IMAGE+"/"+key+KEY).putFile(uris.get(0)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                refDb.child(POST).child(key).setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        refDb.child(POST).child(fUser.getEmail().hashCode() + "").child(key).setValue(key);
                                    }
                                });
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Uri uri = data.getData();
                    uris.add(uri);
                }
            }
        }
    }
}