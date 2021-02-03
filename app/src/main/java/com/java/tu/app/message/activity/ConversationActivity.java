package com.java.tu.app.message.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.java.tu.app.message.R;
import com.java.tu.app.message.adapter.MessageAdapter;
import com.java.tu.app.message.asset.Const;
import com.java.tu.app.message.asset.Image;
import com.java.tu.app.message.object.Conversation;
import com.java.tu.app.message.object.Message;
import com.java.tu.app.message.object.Profile;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.java.tu.app.message.asset.Const.CONVERSATION;
import static com.java.tu.app.message.asset.Const.MESSAGE;
import static com.java.tu.app.message.asset.Const.OFFLINE;
import static com.java.tu.app.message.asset.Const.ONLINE;
import static com.java.tu.app.message.asset.Const.PROFILE;
import static com.java.tu.app.message.asset.Const.SEEN;
import static com.java.tu.app.message.asset.Const.STATUS;

public class ConversationActivity extends AppCompatActivity {

    private FirebaseUser fUser;
    private DatabaseReference refDb;
    private ArrayList<Message> messages;
    private String key;
    private MessageAdapter messageAdapter;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.iv_avatar)
    ImageView iv_avatar;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edt_message)
    EditText edt_message;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.bt_send)
    Button bt_send;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_message)
    RecyclerView rv_message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(getColor(R.color.white));

        setContentView(R.layout.activity_conversation);
        key = getIntent().getStringExtra("key");


        ButterKnife.bind(this);

        Init();
        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String body = edt_message.getText().toString().trim();
                if (body.length() > 0) {
                    add(body, Const.Message.TEXT);
                    edt_message.setText("");
                }

            }
        });


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        refDb.child(STATUS).child(Objects.requireNonNull(fUser.getEmail()).hashCode() + "").setValue(ONLINE);

        refDb.child(CONVERSATION).child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    Conversation conversation = snapshot.getValue(Conversation.class);
                    if (conversation != null) {
                        if (conversation.getType() == Const.Conversation.NORMAl) {
                            refDb.child(PROFILE).child(fUser.getEmail().hashCode() + "").addValueEventListener(new ValueEventListener() {
                                @SuppressLint("UseCompatLoadingForDrawables")
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.getValue() != null) {
                                        Profile profile = snapshot.getValue(Profile.class);
                                        if (profile != null) {
                                            if (profile.getName() != null) {
                                                toolbar.setTitle(profile.getName());
                                            }
                                            if (profile.getAvatar() != null) {
                                                new Image(ConversationActivity.this).getImage(iv_avatar, profile.getAvatar(), Long.MAX_VALUE);
                                            } else {
                                                iv_avatar.setImageDrawable(getResources().getDrawable(R.color.white));
                                            }
                                        } else {
                                            iv_avatar.setImageDrawable(getResources().getDrawable(R.color.white));
                                        }
                                    } else {
                                        iv_avatar.setImageDrawable(getResources().getDrawable(R.color.white));
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            refDb.child(STATUS).child(Objects.requireNonNull(fUser.getEmail()).hashCode() + "").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.getValue() != null) {
                                        String status = snapshot.getValue(String.class);
                                        if (status != null) {
                                            if (status.equals(ONLINE)) {
                                                toolbar.setSubtitle(R.string.active);
                                            } else {
                                                toolbar.setSubtitle(R.string.inactive);
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                        if (conversation.getType() == Const.Conversation.COUPLE) {
                            for (int i = 0; i < conversation.getPersons().size(); i++) {
                                if (!conversation.getPersons().get(i).equals(fUser.getEmail())) {
                                    String email = conversation.getPersons().get(i);
                                    refDb.child(PROFILE).child(email.hashCode() + "").addValueEventListener(new ValueEventListener() {
                                        @SuppressLint("UseCompatLoadingForDrawables")
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.getValue() != null) {
                                                Profile profile = snapshot.getValue(Profile.class);
                                                if (profile != null) {
                                                    if (profile.getName() != null) {
                                                        toolbar.setTitle(profile.getName());
                                                    }
                                                    if (profile.getAvatar() != null) {
                                                        new Image(ConversationActivity.this).getImage(iv_avatar, profile.getAvatar(), Long.MAX_VALUE);
                                                    } else {
                                                        iv_avatar.setImageDrawable(getResources().getDrawable(R.color.white));
                                                    }
                                                } else {
                                                    iv_avatar.setImageDrawable(getResources().getDrawable(R.color.white));
                                                }
                                            } else {
                                                iv_avatar.setImageDrawable(getResources().getDrawable(R.color.white));
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    refDb.child(STATUS).child(Objects.requireNonNull(email).hashCode() + "").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.getValue() != null) {
                                                String status = snapshot.getValue(String.class);
                                                if (status != null) {
                                                    if (status.equals(ONLINE)) {
                                                        toolbar.setSubtitle(R.string.active);
                                                    } else {
                                                        toolbar.setSubtitle(R.string.inactive);
                                                    }
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    break;
                                }
                            }
                        }

                        // TODO
                        if (messageAdapter == null) {
                            messageAdapter = new MessageAdapter(ConversationActivity.this, conversation.getType(), messages, key);
                            LinearLayoutManager manager = new LinearLayoutManager(ConversationActivity.this, RecyclerView.VERTICAL, false);
                            manager.setStackFromEnd(true);
                            rv_message.setLayoutManager(manager);
                            rv_message.setAdapter(messageAdapter);
                            rv_message.setItemViewCacheSize(9);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        refDb.child(MESSAGE).child(key).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getValue() != null) {
                    Message message = snapshot.getValue(Message.class);
                    if (message != null) {
                        refDb.child(SEEN).child(key).child(message.getKey()).child(Objects.requireNonNull(fUser.getEmail()).hashCode() + "").setValue(fUser.getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                messages.add(message);
                                if (messageAdapter != null) {
                                    messageAdapter.notifyItemChanged(messages.size() - 1);
                                    rv_message.scrollToPosition(messages.size() - 1);
                                    for (int i = 0; i < messages.size() - 1; i++) {
                                        for (int j = i + 1; j < messages.size(); j++) {
                                            if (messages.get(i).getKey().compareTo(messages.get(j).getKey()) > 0) {
                                                Message m = messages.get(i);
                                                messages.set(i, messages.get(j));
                                                messages.set(j, m);
                                                messageAdapter.notifyItemChanged(i);
                                                messageAdapter.notifyItemChanged(j);
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getValue() != null) {
                    Message message = snapshot.getValue(Message.class);
                    if (message != null) {
                        for (int i = 0; i < messages.size(); i++) {
                            if (messages.get(i).getKey().equals(message.getKey())) {
                                messages.set(i, message);
                                messageAdapter.notifyItemChanged(i);
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    Message message = snapshot.getValue(Message.class);
                    if (message != null) {
                        for (int i = 0; i < messages.size(); i++) {
                            if (messages.get(i).getKey().equals(message.getKey())) {
                                messages.remove(i);
                                messageAdapter.notifyItemRemoved(i);
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        refDb.child(STATUS).child(Objects.requireNonNull(fUser.getEmail()).hashCode() + "").setValue(OFFLINE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refDb.child(STATUS).child(Objects.requireNonNull(fUser.getEmail()).hashCode() + "").setValue(ONLINE);
    }

    private void Init() {
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        refDb = FirebaseDatabase.getInstance().getReference();
        messages = new ArrayList<>();
    }

    private void add(@NotNull String body, int type_message) {
        Calendar calendar = Calendar.getInstance();
        int minute = calendar.getTime().getMinutes();
        int hour = calendar.getTime().getHours();
        int day = calendar.getTime().getDay();
        int month = calendar.getTime().getMonth();
        int year = calendar.getTime().getYear();
        String from = fUser.getEmail();
        String key_message = calendar.getTimeInMillis() + "" + Objects.requireNonNull(from).hashCode();
        Message message = new Message(minute, hour, day, month, year, from, key_message, body, type_message, calendar.getTimeInMillis());
        refDb.child(MESSAGE).child(key).child(key_message).setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    refDb.child(CONVERSATION).child(key).child(MESSAGE).setValue(key_message);
                }
            }
        });
    }
}