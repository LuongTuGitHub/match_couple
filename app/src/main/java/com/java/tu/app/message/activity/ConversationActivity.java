package com.java.tu.app.message.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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
import static com.java.tu.app.message.asset.Const.STATUS;

public class ConversationActivity extends AppCompatActivity {

    private FirebaseUser fUser;
    private DatabaseReference refDb;
    private ArrayList<Message> messages;
    private String key;
    private MessageAdapter messageAdapter;
    private int type;

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
        setContentView(R.layout.activity_conversation);
        ButterKnife.bind(this);
        key = getIntent().getStringExtra("key");
        type = getIntent().getIntExtra("type", -1);
        Init();
        LinearLayoutManager manager = new LinearLayoutManager(ConversationActivity.this, RecyclerView.VERTICAL, false);
        manager.setStackFromEnd(true);
        rv_message.setLayoutManager(manager);
        rv_message.setAdapter(messageAdapter);
        rv_message.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //TODO
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //TODO
            }
        });
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
        refDb.child(STATUS).child(Objects.requireNonNull(fUser.getEmail()).hashCode() + "").setValue(ONLINE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (type == Const.Conversation.NORMAl) {
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
                                if (profile.getAvatar().length() > 0) {
                                    new Image(ConversationActivity.this).getImage(iv_avatar, profile.getAvatar(), Long.MAX_VALUE);
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
            });
            refDb.child(STATUS).child(fUser.getEmail().hashCode() + "").addValueEventListener(new ValueEventListener() {
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
        if (type == Const.Conversation.COUPLE) {
            String email = getIntent().getStringExtra("email");
            if (email != null) {
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
                                    if (profile.getAvatar().length() > 0) {
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
                        } else {
                            iv_avatar.setImageDrawable(getResources().getDrawable(R.color.white));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                refDb.child(STATUS).child(email.hashCode() + "").addValueEventListener(new ValueEventListener() {
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
        }
        if (type == Const.Conversation.GROUP) {
            String email = getIntent().getStringExtra("email");
            new Image(ConversationActivity.this).getImage(iv_avatar, key, Long.MAX_VALUE);
            refDb.child(CONVERSATION).child(key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue() != null) {
                        Conversation conversation = snapshot.getValue(Conversation.class);
                        if (conversation != null) {
                            if (conversation.getName() != null) {
                                if (conversation.getName().length() > 0) {
                                    if (conversation.getName().length() > 9) {
                                        toolbar.setTitle(conversation.getName().substring(0, 9));
                                    } else {
                                        toolbar.setTitle(conversation.getName());
                                    }
                                } else {
                                    ArrayList<String> person = conversation.getPersons();
                                    String[] name_person = new String[person.size()];
                                    StringBuilder name_conversation = new StringBuilder();
                                    for (int i = 0; i < person.size(); i++) {
                                        int finalI = i;
                                        refDb.child(PROFILE).child(person.get(i).hashCode() + "").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.getValue() != null) {
                                                    Profile profile = snapshot.getValue(Profile.class);
                                                    if (profile != null) {
                                                        if (profile.getName() != null) {
                                                            name_person[finalI] = profile.getName();
                                                            for (String name : name_person) {
                                                                if (name != null) {
                                                                    name_conversation.append(profile.getName()).append(" ");
                                                                }
                                                            }
                                                            String name = name_conversation.toString();
                                                            if (name.length() > 0) {
                                                                if (name.length() > 9) {
                                                                    toolbar.setTitle(name.substring(0, 9));
                                                                } else {
                                                                    toolbar.setTitle(name);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                }
                            } else {
                                ArrayList<String> person = conversation.getPersons();
                                String[] name_person = new String[person.size()];
                                StringBuilder name_conversation = new StringBuilder();
                                for (int i = 0; i < person.size(); i++) {
                                    int finalI = i;
                                    refDb.child(PROFILE).child(person.get(i).hashCode() + "").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.getValue() != null) {
                                                Profile profile = snapshot.getValue(Profile.class);
                                                if (profile != null) {
                                                    if (profile.getName() != null) {
                                                        name_person[finalI] = profile.getName();
                                                        for (String name : name_person) {
                                                            if (name != null) {
                                                                name_conversation.append(profile.getName()).append(" ");
                                                            }
                                                        }
                                                        String name = name_conversation.toString();
                                                        if (name.length() > 0) {
                                                            if (name.length() > 9) {
                                                                toolbar.setTitle(name.substring(0, 9));
                                                            } else {
                                                                toolbar.setTitle(name);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

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
            });
            if (email != null) {
                refDb.child(STATUS).child(email.hashCode() + "").addValueEventListener(new ValueEventListener() {
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
        }
        refDb.child(MESSAGE).child(key).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getValue() != null) {
                    Message message = snapshot.getValue(Message.class);
                    if (message != null) {
                        messages.add(message);
                        messageAdapter.notifyItemInserted(messages.size() - 1);
                        if (!(rv_message.getScrollState() == RecyclerView.SCROLL_INDICATOR_TOP)) {
                            rv_message.scrollToPosition(messages.size() - 1);
                        }
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

        //TODO
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

    @Override
    protected void onStop() {
        super.onStop();
        refDb.onDisconnect();
    }

    private void Init() {
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        refDb = FirebaseDatabase.getInstance().getReference();
        messages = new ArrayList<>();
        messageAdapter = new MessageAdapter(ConversationActivity.this, type, messages);
    }

    private void add(@NotNull String body, int type) {
        Calendar calendar = Calendar.getInstance();
        int minute = calendar.getTime().getMinutes();
        int hour = calendar.getTime().getHours();
        int day = calendar.getTime().getDay();
        int month = calendar.getTime().getMonth();
        int year = calendar.getTime().getYear();
        String from = fUser.getEmail();
        String key_message = calendar.getTimeInMillis() + "" + Objects.requireNonNull(from).hashCode();
        Message message = new Message(minute, hour, day, month, year, from, key_message, body, type, calendar.getTimeInMillis());

        refDb.child(MESSAGE).child(key).child(key_message).setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    refDb.child(CONVERSATION).child(key).child(MESSAGE).setValue(message);
                }
            }
        });
    }
}