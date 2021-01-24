package com.java.tu.app.message.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.java.tu.app.message.R;
import com.java.tu.app.message.adapter.holder.MessageHolder;
import com.java.tu.app.message.asset.Const;
import com.java.tu.app.message.object.Message;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageHolder> {

    private final Context context;
    private final int type;
    private final ArrayList<Message> messages;

    public MessageAdapter(Context context, int type, ArrayList<Message> messages) {
        this.context = context;
        this.type = type;
        this.messages = messages;
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (type == Const.Conversation.NORMAl) {
            return new MessageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.message_text_right, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        Message message = messages.get(position);
        if (type == Const.Conversation.NORMAl) {
            ((TextView) holder.itemView.findViewById(R.id.tv_body)).setText(message.getBody());
        }
    }

    @Override
    public int getItemCount() {
        if (messages != null) {
            return messages.size();
        }
        return 0;
    }
}
