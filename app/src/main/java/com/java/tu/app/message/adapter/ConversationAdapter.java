package com.java.tu.app.message.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.java.tu.app.message.asset.Const;
import com.java.tu.app.message.asset.Image;
import com.java.tu.app.message.event.ChangeData;
import com.java.tu.app.message.event.ConversationLive;
import com.java.tu.app.message.object.Conversation;
import com.java.tu.app.message.object.Message;
import com.java.tu.app.message.object.Profile;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.java.tu.app.message.asset.Const.AVATAR;
import static com.java.tu.app.message.asset.Const.CONVERSATION;
import static com.java.tu.app.message.asset.Const.MESSAGE;
import static com.java.tu.app.message.asset.Const.ONLINE;
import static com.java.tu.app.message.asset.Const.PROFILE;
import static com.java.tu.app.message.asset.Const.SEEN;
import static com.java.tu.app.message.asset.Const.STATUS;

public class ConversationAdapter extends RecyclerView.Adapter<ViewHolder> {

    private final ConversationLive conversationLive;
    private final ChangeData changeData;
    private final DatabaseReference refDb;
    private final FirebaseUser fUser;
    private final Context context;

    public ConversationAdapter(@NotNull Context context, @NotNull ConversationLive conversationLive, @NotNull ChangeData changeData) {
        this.conversationLive = conversationLive;
        this.context = context;
        this.changeData = changeData;
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        refDb = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.conversation_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        refDb.child(CONVERSATION).child(conversationLive.getKey().get(position)).addValueEventListener(new ValueEventListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    Conversation conversation____ = snapshot.getValue(Conversation.class);
                    if (conversation____ != null) {
                        if (conversation____.getMessage() != null) {
                            refDb.child(MESSAGE).child(conversationLive.getKey().get(position)).child(conversation____.getMessage()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.getValue() != null) {
                                        conversationLive.change(conversationLive.getKey().get(position), conversation____.getMessage());
                                        changeData.change();
                                        Message message = snapshot.getValue(Message.class);
                                        if (message != null) {
                                            if (message.getType() == Const.Message.TEXT) {
                                                ((TextView) holder.itemView.findViewById(R.id.tv_chat)).setText(message.getBody());
                                                //TODO
                                            }
                                        } else {
                                            ((TextView) holder.itemView.findViewById(R.id.tv_chat)).setText(R.string.please_send_first_message);
                                        }
                                    } else {
                                        ((TextView) holder.itemView.findViewById(R.id.tv_chat)).setText(R.string.please_send_first_message);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            refDb.child(SEEN).child(conversationLive.getKey().get(position)).child(conversation____.getMessage()).child(Objects.requireNonNull(fUser.getEmail()).hashCode() + "").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.getValue() != null) {
                                        String email = snapshot.getValue(String.class);
                                        if (email != null) {
                                            if (email.equals(fUser.getEmail())) {
                                                ((TextView) holder.itemView.findViewById(R.id.tv_chat)).setTypeface(Typeface.DEFAULT);
                                                ((TextView) holder.itemView.findViewById(R.id.tv_chat)).setTextColor(context.getResources().getColor(R.color.gray));
                                                ((TextView) holder.itemView.findViewById(R.id.tv_name)).setTypeface(Typeface.DEFAULT);
                                            } else {
                                                ((TextView) holder.itemView.findViewById(R.id.tv_chat)).setTypeface(Typeface.DEFAULT_BOLD);
                                                ((TextView) holder.itemView.findViewById(R.id.tv_chat)).setTextColor(context.getResources().getColor(R.color.black));
                                                ((TextView) holder.itemView.findViewById(R.id.tv_name)).setTypeface(Typeface.DEFAULT_BOLD);
                                            }
                                        } else {
                                            ((TextView) holder.itemView.findViewById(R.id.tv_chat)).setTypeface(Typeface.DEFAULT_BOLD);
                                            ((TextView) holder.itemView.findViewById(R.id.tv_chat)).setTextColor(context.getResources().getColor(R.color.black));
                                            ((TextView) holder.itemView.findViewById(R.id.tv_name)).setTypeface(Typeface.DEFAULT_BOLD);
                                        }
                                    } else {
                                        ((TextView) holder.itemView.findViewById(R.id.tv_chat)).setTypeface(Typeface.DEFAULT_BOLD);
                                        ((TextView) holder.itemView.findViewById(R.id.tv_chat)).setTextColor(context.getResources().getColor(R.color.black));
                                        ((TextView) holder.itemView.findViewById(R.id.tv_name)).setTypeface(Typeface.DEFAULT_BOLD);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        } else {
                            ((TextView) holder.itemView.findViewById(R.id.tv_chat)).setText(R.string.please_send_first_message);
                            conversationLive.change(conversationLive.getKey().get(position), "0");
                        }
                        if (conversation____.getType() == Const.Conversation.NORMAl) {
                            refDb.child(PROFILE).child(Objects.requireNonNull(fUser.getEmail()).hashCode() + "").child(AVATAR).addValueEventListener(new ValueEventListener() {
                                @SuppressLint("UseCompatLoadingForDrawables")
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.getValue() != null) {
                                        String avatar = snapshot.getValue(String.class);
                                        if (avatar != null) {
                                            if (avatar.length() > 0) {
                                                new Image(context).getImage(holder.itemView.findViewById(R.id.iv_avatar), avatar, Long.MAX_VALUE);
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
                            ((TextView) holder.itemView.findViewById(R.id.tv_name)).setText(R.string.you);
                        }
                        if (conversation____.getType() == Const.Conversation.COUPLE) {
                            for (int i = 0; i < conversation____.getPersons().size(); i++) {
                                if (!conversation____.getPersons().get(i).equals(fUser.getEmail())) {
                                    refDb.child(PROFILE).child(conversation____.getPersons().get(i).hashCode() + "").addValueEventListener(new ValueEventListener() {
                                        @SuppressLint("UseCompatLoadingForDrawables")
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.getValue() != null) {
                                                Profile profile = snapshot.getValue(Profile.class);
                                                if (profile != null) {
                                                    String avatar = profile.getAvatar();
                                                    if (avatar != null) {
                                                        if (avatar.length() > 0) {
                                                            new Image(context).getImage(holder.itemView.findViewById(R.id.iv_avatar), avatar, Long.MAX_VALUE);
                                                        } else {
                                                            ((ImageView) holder.itemView.findViewById(R.id.iv_avatar)).setImageDrawable(context.getResources().getDrawable(R.color.white));
                                                        }
                                                    } else {
                                                        ((ImageView) holder.itemView.findViewById(R.id.iv_avatar)).setImageDrawable(context.getResources().getDrawable(R.color.white));
                                                    }
                                                    if (profile.getName() != null) {
                                                        ((TextView) holder.itemView.findViewById(R.id.tv_name)).setText(profile.getName());
                                                    }
                                                }
                                            } else {
                                                ((ImageView) holder.itemView.findViewById(R.id.iv_avatar)).setImageDrawable(context.getResources().getDrawable(R.color.white));
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    refDb.child(STATUS).child(conversation____.getPersons().get(i).hashCode() + "").addValueEventListener(new ValueEventListener() {
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
                                    break;
                                }
                            }
                        }
                        if (conversation____.getType() == Const.Conversation.GROUP) {
                            refDb.child(STATUS).child(conversation____.getUser_create().hashCode() + "").addValueEventListener(new ValueEventListener() {
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
                            String image = conversation____.getImage();
                            if (image != null) {
                                new Image(context).getImage(holder.itemView.findViewById(R.id.iv_avatar), image, Long.MAX_VALUE);
                            } else {
                                ((ImageView) holder.itemView.findViewById(R.id.iv_avatar)).setImageDrawable(context.getResources().getDrawable(R.color.white));
                            }
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
                Intent intent = new Intent(context, ConversationActivity.class);
                intent.putExtra("key", conversationLive.getKey().get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return conversationLive.size();
    }

}
