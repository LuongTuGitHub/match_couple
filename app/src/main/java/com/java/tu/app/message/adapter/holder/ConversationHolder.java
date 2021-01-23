package com.java.tu.app.message.adapter.holder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ConversationHolder extends RecyclerView.ViewHolder {
    public ConversationHolder(@NonNull View itemView) {
        super(itemView);
        setIsRecyclable(false);
    }
}
