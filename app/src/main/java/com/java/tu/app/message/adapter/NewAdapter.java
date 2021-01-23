package com.java.tu.app.message.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.java.tu.app.message.R;
import com.java.tu.app.message.adapter.holder.NewHolder;

import java.util.ArrayList;

public class NewAdapter extends RecyclerView.Adapter<NewHolder> {

    private final ArrayList<String> data;
    private final DatabaseReference refDb;

    public NewAdapter() {
        data = new ArrayList<>();
        data.add(null);
        refDb = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public NewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new NewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.add_new_view, parent, false));
        }
        return new NewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.news_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if (data != null) {
            return data.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}
