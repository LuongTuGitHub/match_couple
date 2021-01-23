package com.java.tu.app.message.activity.sign_up;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.java.tu.app.message.R;
import com.java.tu.app.message.asset.Const;
import com.java.tu.app.message.object.Conversation;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.java.tu.app.message.asset.Const.CHAT;
import static com.java.tu.app.message.asset.Const.CONVERSATION;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edt_email)
    TextInputEditText edt_email;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edt_password)
    TextInputEditText edt_password;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edt_repeat_password)
    TextInputEditText edt_repeat_password;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.bt_sign_up)
    Button bt_sign_up;

    private FirebaseAuth fAuth;
    private DatabaseReference refDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getColor(R.color.white));
        setContentView(R.layout.activity_sign_up);
        Init();
        ButterKnife.bind(this);
        String email = getIntent().getStringExtra("email");
        String password = getIntent().getStringExtra("password");
        if (email != null && email.trim().length() > 0) {
            edt_email.setText(email);
        }
        if (password != null && password.trim().length() > 0) {
            edt_password.setText(password.trim());
            edt_repeat_password.setText(password.trim());
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        bt_sign_up.setOnClickListener(this);

    }

    @Override
    public void onActionModeFinished(ActionMode mode) {
        super.onActionModeFinished(mode);
        mode.setType(ActionMode.TYPE_PRIMARY);
    }

    @SuppressLint("UseCompatLoadingForColorStateLists")
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_sign_up) {
            bt_sign_up.setEnabled(false);
            if (edt_email != null && edt_password != null && edt_repeat_password != null) {
                if (Objects.requireNonNull(edt_email.getText()).toString().trim().length() > 0 && Objects.requireNonNull(edt_password.getText()).toString().trim().length() > 0 && Objects.requireNonNull(edt_repeat_password.getText()).toString().trim().length() > 0) {
                    String email = edt_email.getText().toString();
                    String password = edt_password.getText().toString();
                    String repeat_password = edt_password.getText().toString();
                    if (password.length() > 6) {
                        if (password.contains(repeat_password)) {
                            signUp(email, password);
                        } else {
                            bt_sign_up.setEnabled(true);
                        }
                    } else {
                        bt_sign_up.setEnabled(true);
                    }
                } else {
                    bt_sign_up.setEnabled(true);
                }
            } else {
                bt_sign_up.setEnabled(true);
            }
        }
    }

    private void Init() {
        fAuth = FirebaseAuth.getInstance();
        refDb = FirebaseDatabase.getInstance().getReference();
    }

    private void signUp(@NotNull String email, @NotNull String password) {
        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent();
                    intent.putExtra("email", email);
                    intent.putExtra("password", password);
                    setResult(RESULT_OK, intent);
                    Toast.makeText(SignUpActivity.this, "Success !", Toast.LENGTH_SHORT).show();
                    String email_hash_code = email.hashCode()+"";
                    refDb.child(CHAT).child(email_hash_code).child(email_hash_code+email_hash_code).setValue(email_hash_code+email_hash_code);
                    refDb.child(CONVERSATION).child(email_hash_code+email_hash_code).setValue(new Conversation(null, email, Const.Conversation.NORMAl, null));
                } else {
                    Toast.makeText(SignUpActivity.this, "Fail !", Toast.LENGTH_SHORT).show();
                }
                bt_sign_up.setEnabled(true);
            }
        });
    }
}