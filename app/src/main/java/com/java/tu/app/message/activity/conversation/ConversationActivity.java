package com.java.tu.app.message.activity.conversation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.java.tu.app.message.R;
import com.java.tu.app.message.asset.Const;
import com.java.tu.app.message.asset.Image;
import com.java.tu.app.message.object.Conversation;
import com.java.tu.app.message.object.Profile;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.java.tu.app.message.asset.Const.CONVERSATION;
import static com.java.tu.app.message.asset.Const.OFFLINE;
import static com.java.tu.app.message.asset.Const.ONLINE;
import static com.java.tu.app.message.asset.Const.PROFILE;
import static com.java.tu.app.message.asset.Const.STATUS;

public class ConversationActivity extends AppCompatActivity {

    private FirebaseUser fUser;
    private DatabaseReference refDb;
    private int type;
    private String key;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.iv_avatar)
    ImageView iv_avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        ButterKnife.bind(this);
        key = getIntent().getStringExtra("key");
        type = getIntent().getIntExtra("type", -1);
        Init();
        refDb.child(STATUS).child(Objects.requireNonNull(fUser.getEmail()).hashCode() + "").setValue(ONLINE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (type == Const.Conversation.NORMAl) {
            refDb.child(PROFILE).child(fUser.getEmail().hashCode() + "").addValueEventListener(new ValueEventListener() {
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
                                }
                            }
                        }
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
                                    }
                                }
                            }
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

            refDb.child(CONVERSATION).child(key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue() != null) {
                        Conversation conversation = snapshot.getValue(Conversation.class);
                        if (conversation != null) {
                            if (conversation.getName() != null) {
                                if (conversation.getName().length() > 0) {
                                    toolbar.setTitle(conversation.getName());
                                } else {
                                    ArrayList<String> person = conversation.getPersons();
                                    String[] name_person = new String[person.size()];
                                    StringBuilder name_conversation = new StringBuilder();
                                    if (person != null) {
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
                            } else {
                                ArrayList<String> person = conversation.getPersons();
                                String[] name_person = new String[person.size()];
                                StringBuilder name_conversation = new StringBuilder();
                                if (person != null) {
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
    }

}