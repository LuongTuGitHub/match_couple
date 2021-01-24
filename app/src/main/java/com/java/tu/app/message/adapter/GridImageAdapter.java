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
import com.java.tu.app.message.adapter.holder.GridImageHolder;
import com.java.tu.app.message.asset.Image;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class GridImageAdapter extends RecyclerView.Adapter<GridImageHolder> {

    private final ArrayList<String> data;
    private final Context context;

    public GridImageAdapter(ArrayList<String> data, @NotNull Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public GridImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_view, parent, false);
        return new GridImageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GridImageHolder holder, int position) {
        new Image(holder.itemView.getContext()).getImage(holder.itemView.findViewById(R.id.iv_image), data.get(position), Long.MAX_VALUE);
        holder.itemView.findViewById(R.id.layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewImageActivity.class);
                intent.putExtra("image", data.get(position));
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
