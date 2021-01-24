package com.java.tu.app.message.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.java.tu.app.message.R;
import com.java.tu.app.message.activity.ConversationActivity;
import com.java.tu.app.message.adapter.holder.PersonHolder;
import com.java.tu.app.message.asset.Const;
import com.java.tu.app.message.asset.Image;
import com.java.tu.app.message.asset.Key;
import com.java.tu.app.message.object.Profile;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import static com.java.tu.app.message.asset.Const.ONLINE;
import static com.java.tu.app.message.asset.Const.PROFILE;
import static com.java.tu.app.message.asset.Const.STATUS;

public class PersonAdapter extends RecyclerView.Adapter<PersonHolder> {

    private final Context context;
    private final DatabaseReference refDb;
    private final ArrayList<String> emails;


    public PersonAdapter(@NotNull Context context) {
        this.context = context;
        refDb = FirebaseDatabase.getInstance().getReference();
        emails = new ArrayList<>();
        emails.add(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());
    }

    @NonNull
    @Override
    public PersonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_view_top, parent, false);
        return new PersonHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonHolder holder, int position) {
        Intent intent = new Intent(context, ConversationActivity.class);
        intent.putExtra("key", new Key().getKey(Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()), emails.get(position)));
        if (position == 0) {
            intent.putExtra("type", Const.Conversation.NORMAl);
        } else {
            intent.putExtra("type", Const.Conversation.COUPLE);
            intent.putExtra("email", emails.get(position));
        }
        refDb.child(STATUS).child(emails.get(position).hashCode() + "").addValueEventListener(new ValueEventListener() {
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
        refDb.child(PROFILE).child(emails.get(position).hashCode() + "").addValueEventListener(new ValueEventListener() {
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
                    } else {
                        ((ImageView) holder.itemView.findViewById(R.id.iv_avatar)).setImageDrawable(context.getResources().getDrawable(R.color.white));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.itemView.findViewById(R.id.iv_avatar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (emails != null) {
            return emails.size();
        }
        return 0;
    }

    public void add(@NotNull String email) {
        emails.add(email);
        notifyItemInserted(emails.size() - 1);
    }

}
