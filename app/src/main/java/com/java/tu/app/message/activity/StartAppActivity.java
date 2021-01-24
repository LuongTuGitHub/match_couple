package com.java.tu.app.message.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.ActionMode;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.java.tu.app.message.R;

import static com.java.tu.app.message.asset.Const.TIME_SLASH;

public class StartAppActivity extends AppCompatActivity {

    private FirebaseUser fUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_start_app);
        Init();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (fUser == null) {
                    routerLogin();
                } else {
                    new Router(StartAppActivity.this).routerHome();
                }
            }
        }, TIME_SLASH);
    }

    @Override
    public void onActionModeStarted(ActionMode mode) {
        super.onActionModeStarted(mode);
        mode.setType(ActionMode.TYPE_FLOATING);
    }

    @Override
    public void onActionModeFinished(ActionMode mode) {
        super.onActionModeFinished(mode);
        mode.setType(ActionMode.TYPE_FLOATING);
    }

    private void Init() {
        fUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    private void routerLogin() {
        Intent intent = new Intent(StartAppActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


}