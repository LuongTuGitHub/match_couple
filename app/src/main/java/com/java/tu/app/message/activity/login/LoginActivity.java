package com.java.tu.app.message.activity.login;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.java.tu.app.message.R;
import com.java.tu.app.message.activity.Router;
import com.java.tu.app.message.activity.sign_up.SignUpActivity;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.java.tu.app.message.asset.Const.SIGN_UP;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.bt_sign_up)
    Button bt_sign_up;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.bt_sign_in)
    FloatingActionButton bt_sign_in;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.bt_reset)
    Button bt_reset;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edt_email)
    TextInputEditText edt_email;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edt_password)
    TextInputEditText edt_password;


    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Init();
        bt_sign_in.setOnClickListener(this);
        bt_sign_up.setOnClickListener(this);
        bt_reset.setOnClickListener(this);
    }

    @Override
    public void onActionModeStarted(ActionMode mode) {
        super.onActionModeStarted(mode);
        mode.setType(ActionMode.TYPE_FLOATING);
    }

    private void Init() {
        fAuth = FirebaseAuth.getInstance();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_sign_in:
                bt_sign_in.setEnabled(false);
                if (Objects.requireNonNull(edt_email.getText()).toString().length() > 0 && Objects.requireNonNull(edt_password.getText()).toString().length() > 0) {
                    signIn(edt_email.getText().toString().trim(), edt_password.getText().toString().trim());
                } else {
                    bt_sign_in.setEnabled(true);
                }
                break;
            case R.id.bt_sign_up:
                signUp(Objects.requireNonNull(edt_email.getText()).toString().trim(), Objects.requireNonNull(edt_password.getText()).toString().trim());
                break;
            case R.id.bt_reset:
                View view = LayoutInflater.from(LoginActivity.this).inflate(R.layout.reset_alert, null);
                AlertDialog dialog = new AlertDialog.Builder(LoginActivity.this).setView(view).create();
                Button bt_send = view.findViewById(R.id.bt_send);
                Button bt_cancel = view.findViewById(R.id.bt_cancel);
                TextInputEditText edt_email_alert = view.findViewById(R.id.edt_email);
                bt_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                bt_send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edt_email_alert != null) {
                            String email = Objects.requireNonNull(edt_email_alert.getText()).toString();
                            if (email.trim().length() > 0) {
                                fAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(LoginActivity.this, "Success !", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Fail !", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_UP) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    String email = data.getStringExtra("email");
                    String password = data.getStringExtra("password");
                    if (email != null) {
                        edt_email.setText(email);
                    }
                    if (password != null) {
                        edt_password.setText(password);
                    }
                }
            }
        }
    }


    private void signUp(String email, String password) {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        if (email != null) {
            if (email.trim().length() > 0) {
                intent.putExtra("email", email);
            }
        }
        if (password != null) {
            if (password.trim().length() > 0) {
                intent.putExtra("password", password);
            }
        }

        startActivityForResult(intent, SIGN_UP);
    }

    private void reset(String email) {
        if (email != null) {
            if (email.trim().length() > 0) {
                Toast.makeText(this, email, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void signIn(@NotNull String email, @NotNull String password) {
        fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    new Router(LoginActivity.this).routerHome();
                } else {
                    bt_sign_in.setEnabled(true);
                    Toast.makeText(LoginActivity.this, "Email Password Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}