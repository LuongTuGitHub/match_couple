package com.java.tu.app.message.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;
import static com.java.tu.app.message.asset.Const.ACCOUNT;
import static com.java.tu.app.message.asset.Const.SESSION;

public class Router {

    private Context context;

    private final FirebaseUser fUser;
    private final DatabaseReference refDb;
    private final SharedPreferences refShared;

    public Router(@NonNull Context context) {
        this.context = context;
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        refDb = FirebaseDatabase.getInstance().getReference();
        refShared = context.getSharedPreferences(SESSION, MODE_PRIVATE);
    }

    public void setContext(@NonNull Context context) {
        this.context = context;
    }

    public void routerHome() {
        String session = UUID.randomUUID().toString();
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = refShared.edit();
        editor.clear();
        editor.putString(SESSION, session);
        editor.apply();

        fUser.reload();
        refDb.child(SESSION).child(String.valueOf(Objects.requireNonNull(fUser.getEmail()).hashCode())).setValue(session).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(context, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                    refDb.child(ACCOUNT).child(fUser.getEmail().hashCode()+"").setValue(fUser.getEmail());
                }
            }
        });
    }


}
