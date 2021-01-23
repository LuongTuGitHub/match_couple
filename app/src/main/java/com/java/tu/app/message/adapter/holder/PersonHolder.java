package com.java.tu.app.message.adapter.holder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PersonHolder extends RecyclerView.ViewHolder {
    public PersonHolder(@NonNull View itemView) {
        super(itemView);
        setIsRecyclable(true);
    }
}
