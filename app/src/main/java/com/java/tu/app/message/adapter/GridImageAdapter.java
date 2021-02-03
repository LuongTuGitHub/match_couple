package com.java.tu.app.message.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.java.tu.app.message.R;
import com.java.tu.app.message.activity.ViewImageActivity;
import com.java.tu.app.message.asset.Image;

import org.jetbrains.annotations.NotNull;

import java.util.Vector;

public class GridImageAdapter extends RecyclerView.Adapter<ViewHolder> {

    private final Vector<String> data;
    private final Context context;

    public GridImageAdapter(Vector<String> data, @NotNull Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String key = data.get(position);
        new Image(holder.itemView.getContext()).getImage(holder.itemView.findViewById(R.id.iv_image), key, Long.MAX_VALUE);
        holder.itemView.findViewById(R.id.layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewImageActivity.class);
                intent.putExtra("image", key);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (data != null) {
            return data.size();
        }
        return 0;
    }
}
