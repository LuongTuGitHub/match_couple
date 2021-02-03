package com.java.tu.app.message.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.java.tu.app.message.R;
import com.java.tu.app.message.object.Message;

import java.util.ArrayList;
import java.util.Objects;

import static com.java.tu.app.message.asset.Const.CONVERSATION;
import static com.java.tu.app.message.asset.Const.REACT;

public class MessageAdapter extends RecyclerView.Adapter<ViewHolder> {

    private final Context context;
    private final int type;
    private final ArrayList<Message> messages;
    private final DatabaseReference refDb;
    private final FirebaseUser fUser;
    private final String conversation;

    public MessageAdapter(Context context, int type, ArrayList<Message> messages, String key) {
        this.context = context;
        this.type = type;
        this.messages = messages;
        refDb = FirebaseDatabase.getInstance().getReference();
        this.conversation = key;
        fUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_text_right, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ArrayList<String> reacts = new ArrayList<>();
        ((TextView) holder.itemView.findViewById(R.id.tv_body)).setText(messages.get(position).getBody());


        holder.itemView.findViewById(R.id.tv_body).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean toggle = false;
                for (int i = 0; i < reacts.size(); i++) {
                    if (reacts.get(i).equals(fUser.getEmail())) {
                        toggle = true;
                        refDb.child(REACT).child(CONVERSATION).child(conversation).child(messages.get(position).getKey()).child(Objects.requireNonNull(fUser.getEmail()).hashCode() + "").removeValue();
                        break;
                    }
                }
                if (!toggle) {
                    refDb.child(REACT).child(CONVERSATION).child(conversation).child(messages.get(position).getKey()).child(Objects.requireNonNull(fUser.getEmail()).hashCode() + "").setValue(fUser.getEmail());
                }

            }
        });
        refDb.child(REACT).child(CONVERSATION).child(conversation).child(messages.get(position).getKey()).addChildEventListener(new ChildEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getValue() != null) {
                    String email = snapshot.getValue(String.class);
                    if (email != null) {
                        reacts.add(email);
                        ((TextView) holder.itemView.findViewById(R.id.tv_react)).setText(reacts.size() + "");
                        holder.itemView.findViewById(R.id.tv_react).setVisibility(View.VISIBLE);
                        holder.itemView.findViewById(R.id.v_base).setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    String email = snapshot.getValue(String.class);
                    if (email != null) {
                        for (int i = 0; i < reacts.size(); i++) {
                            if (reacts.get(i).equals(email)) {
                                reacts.remove(i);
                                break;
                            }
                        }
                        ((TextView) holder.itemView.findViewById(R.id.tv_react)).setText(reacts.size() + "");
                        if (reacts.size() == 0) {
                            holder.itemView.findViewById(R.id.tv_react).setVisibility(View.GONE);
                            holder.itemView.findViewById(R.id.v_base).setVisibility(View.GONE);
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
    public int getItemCount() {
        if (messages != null) {
            return messages.size();
        }
        return 0;
    }
}
