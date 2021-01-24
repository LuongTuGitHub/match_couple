package com.java.tu.app.message.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.java.tu.app.message.R;
import com.java.tu.app.message.activity.ConversationActivity;
import com.java.tu.app.message.adapter.event.OnClickItemRecyclerView;
import com.java.tu.app.message.adapter.holder.ConversationHolder;
import com.java.tu.app.message.asset.Const;
import com.java.tu.app.message.asset.Image;
import com.java.tu.app.message.object.Conversation;
import com.java.tu.app.message.object.Message;
import com.java.tu.app.message.object.Profile;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import static com.java.tu.app.message.asset.Const.CONVERSATION;
import static com.java.tu.app.message.asset.Const.ONLINE;
import static com.java.tu.app.message.asset.Const.PROFILE;
import static com.java.tu.app.message.asset.Const.STATUS;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationHolder> {

    @NotNull
    private final OnClickItemRecyclerView event;
    private ArrayList<String> conversions;
    private final DatabaseReference refDb;
    private final FirebaseUser fUser;
    private final Context context;

    public ConversationAdapter(@NotNull OnClickItemRecyclerView event, @NotNull Context context) {
        this.event = event;
        conversions = new ArrayList<>();
        this.context = context;
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        refDb = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public ConversationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.conversation_view, parent, false);
        return new ConversationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationHolder holder, int position) {
        String conversation = conversions.get(position);
        Intent intent = new Intent(context, ConversationActivity.class);
        intent.putExtra("key", conversation);
        if (conversation != null) {
            refDb.child(CONVERSATION).child(conversation).addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue() != null) {
                        Conversation conversation_object = snapshot.getValue(Conversation.class);
                        if (conversation_object != null) {
                            if (conversation_object.getType() == Const.Conversation.NORMAl) {
                                refDb.child(PROFILE).child(Objects.requireNonNull(fUser.getEmail()).hashCode() + "").addValueEventListener(new ValueEventListener() {
                                    @SuppressLint({"ResourceType", "UseCompatLoadingForDrawables"})
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.getValue() != null) {
                                            Profile profile = snapshot.getValue(Profile.class);
                                            if (profile != null) {
                                                if (profile.getName() != null) {
                                                    ((TextView) holder.itemView.findViewById(R.id.tv_name)).setText(profile.getName());
                                                }
                                                if (profile.getAvatar() != null) {
                                                    if (profile.getAvatar().length() > 0) {
                                                        new Image(context).getImage(holder.itemView.findViewById(R.id.iv_avatar), profile.getAvatar(), Long.MAX_VALUE);
                                                    } else {
                                                        ((ImageView) holder.itemView.findViewById(R.id.iv_avatar)).setImageDrawable(context.getResources().getDrawable(R.color.white));
                                                    }
                                                } else {
                                                    ((ImageView) holder.itemView.findViewById(R.id.iv_avatar)).setImageDrawable(context.getResources().getDrawable(R.color.white));
                                                }
                                            } else {
                                                ((ImageView) holder.itemView.findViewById(R.id.iv_avatar)).setImageDrawable(context.getResources().getDrawable(R.color.white));
                                            }
                                        } else {
                                            ((ImageView) holder.itemView.findViewById(R.id.iv_avatar)).setImageDrawable(context.getResources().getDrawable(R.color.white));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                intent.putExtra("type", Const.Conversation.NORMAl);
                            }
                            if (conversation_object.getType() == Const.Conversation.COUPLE) {
                                new Image(context).getImage(holder.itemView.findViewById(R.id.iv_avatar), conversation, Long.MAX_VALUE);
                                for (String email : conversation_object.getPersons()) {
                                    if (!email.equals(fUser.getEmail())) {
                                        refDb.child(PROFILE).child(email.hashCode() + "").addValueEventListener(new ValueEventListener() {
                                            @SuppressLint({"UseCompatLoadingForDrawables", "ResourceType"})
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.getValue() != null) {
                                                    Profile profile = snapshot.getValue(Profile.class);
                                                    if (profile != null) {
                                                        if (profile.getName() != null) {
                                                            ((TextView) holder.itemView.findViewById(R.id.tv_name)).setText(profile.getName());
                                                        }
                                                        if (profile.getAvatar() != null) {
                                                            if (profile.getAvatar().length() > 0) {
                                                                new Image(context).getImage(holder.itemView.findViewById(R.id.iv_avatar), profile.getAvatar(), Long.MAX_VALUE);
                                                            } else {
                                                                ((ImageView) holder.itemView.findViewById(R.id.iv_avatar)).setImageDrawable(context.getResources().getDrawable(R.color.white));
                                                            }
                                                        } else {
                                                            ((ImageView) holder.itemView.findViewById(R.id.iv_avatar)).setImageDrawable(context.getResources().getDrawable(R.color.white));
                                                        }
                                                    } else {
                                                        ((ImageView) holder.itemView.findViewById(R.id.iv_avatar)).setImageDrawable(context.getResources().getDrawable(R.color.white));
                                                    }
                                                } else {
                                                    ((ImageView) holder.itemView.findViewById(R.id.iv_avatar)).setImageDrawable(context.getResources().getDrawable(R.color.white));
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
                                                            holder.itemView.findViewById(R.id.cv_status).setVisibility(View.VISIBLE);
                                                        } else {
                                                            holder.itemView.findViewById(R.id.cv_status).setVisibility(View.GONE);
                                                        }
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                        intent.putExtra("email", email);
                                        break;
                                    }
                                }
                                intent.putExtra("type", Const.Conversation.COUPLE);
                            }
                            if (conversation_object.getType() == Const.Conversation.GROUP) {
                                String[] person = new String[conversation_object.getPersons().size()];
                                StringBuilder name_conversation = new StringBuilder();
                                if (conversation_object.getName() != null) {
                                    if (conversation_object.getName().length() > 0) {
                                        if (conversation_object.getName().length() > 0) {
                                            if (conversation_object.getName().length() > 9) {
                                                ((TextView) holder.itemView.findViewById(R.id.tv_name)).setText(conversation_object.getName().substring(0, 9));
                                            } else {
                                                ((TextView) holder.itemView.findViewById(R.id.tv_name)).setText(conversation_object.getName());
                                            }
                                        }
                                    } else {
                                        for (int i = 0; i < person.length; i++) {
                                            int finalI = i;
                                            refDb.child(PROFILE).child(conversation_object.getPersons().get(i).hashCode() + "").addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.getValue() != null) {
                                                        Profile profile = snapshot.getValue(Profile.class);
                                                        if (profile != null) {
                                                            if (profile.getName() != null) {
                                                                person[finalI] = profile.getName();
                                                                for (String name : person) {
                                                                    if (name != null) {
                                                                        name_conversation.append(profile.getName()).append(" ");
                                                                    }
                                                                }
                                                                String name = name_conversation.toString();
                                                                if (name.length() > 0) {
                                                                    if (name.length() > 9) {
                                                                        ((TextView) holder.itemView.findViewById(R.id.tv_name)).setText(name.substring(0, 9));
                                                                    } else {
                                                                        ((TextView) holder.itemView.findViewById(R.id.tv_name)).setText(name);
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
                                    for (int i = 0; i < person.length; i++) {
                                        int finalI = i;
                                        refDb.child(PROFILE).child(conversation_object.getPersons().get(i).hashCode() + "").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.getValue() != null) {
                                                    Profile profile = snapshot.getValue(Profile.class);
                                                    if (profile != null) {
                                                        if (profile.getName() != null) {
                                                            person[finalI] = profile.getName();
                                                            for (String name : person) {
                                                                if (name != null) {
                                                                    name_conversation.append(profile.getName()).append(" ");
                                                                }
                                                            }
                                                            String name = name_conversation.toString();
                                                            if (name.length() > 0) {
                                                                if (name.length() > 9) {
                                                                    ((TextView) holder.itemView.findViewById(R.id.tv_name)).setText(name.substring(0, 9));
                                                                } else {
                                                                    ((TextView) holder.itemView.findViewById(R.id.tv_name)).setText(name);
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

                                refDb.child(STATUS).child(conversation_object.getUser_create().hashCode() + "").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.getValue() != null) {
                                            String status = snapshot.getValue(String.class);
                                            if (status != null) {
                                                if (status.equals(ONLINE)) {
                                                    holder.itemView.findViewById(R.id.cv_status).setVisibility(View.VISIBLE);
                                                } else {
                                                    holder.itemView.findViewById(R.id.cv_status).setVisibility(View.GONE);
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                intent.putExtra("email", conversation_object.getUser_create());
                                intent.putExtra("type", Const.Conversation.GROUP);
                            }
                            Message message = conversation_object.getMessage();
                            if (message != null) {
                                // TODO
                                if (conversation_object.getType() == Const.Conversation.NORMAl) {
                                    if (message.getType() == Const.Message.HIDE_TEXT || message.getType() == Const.Message.TEXT) {
                                        ((TextView) holder.itemView.findViewById(R.id.tv_chat)).setText(message.getBody());
                                        //TODO
                                    }
                                }
                                if (conversation_object.getType() == Const.Conversation.GROUP || conversation_object.getType() == Const.Conversation.COUPLE) {
                                    if (message.getType() == Const.Message.HIDE_TEXT || message.getType() == Const.Message.TEXT) {
                                        String body = message.getBody();
                                        if (message.getForm().equals(fUser.getEmail())) {
                                            body = "You:" + body;
                                        }
                                        if ((Calendar.getInstance().getTimeInMillis() - message.getTime()) > 12 * 60 * 60) {
                                            body += (message.getHour() + ":" + message.getMinute());
                                        }
                                        ((TextView) holder.itemView.findViewById(R.id.tv_chat)).setText(body);
                                    }
                                }
                                //TODO
                            } else {
                                ((TextView) holder.itemView.findViewById(R.id.tv_chat)).setTextColor(context.getResources().getColor(R.color.gray));
                                ((TextView) holder.itemView.findViewById(R.id.tv_chat)).setTypeface(Typeface.DEFAULT);
                                ((TextView) holder.itemView.findViewById(R.id.tv_chat)).setText(R.string.please_send_first_message);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            holder.itemView.findViewById(R.id.layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (conversions != null) {
            return conversions.size();
        }
        return 0;
    }

    public ArrayList<String> getConversions() {
        return conversions;
    }

    public void setConversions(ArrayList<String> conversions) {
        this.conversions = conversions;
    }
}
