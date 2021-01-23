package com.java.tu.app.message.fragment.grid_image;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.java.tu.app.message.R;
import com.java.tu.app.message.adapter.GridImageAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.java.tu.app.message.asset.Const.IMAGE;

public class GridImageFragment extends Fragment {

    private GridImageViewModel mViewModel;
    private View view;

    private final DatabaseReference refDb;
    private final FirebaseUser fUser;
    private GridImageAdapter adapter;
    private final ArrayList<String> data;
    private ChildEventListener imageListener;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_image)
    RecyclerView rv_image;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.layout_relative)
    RelativeLayout layout;

    public GridImageFragment() {
        refDb = FirebaseDatabase.getInstance().getReference();
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        data = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.grid_image_fragment, container, false);
            ButterKnife.bind(this, view);
            if (adapter == null) {
                adapter = new GridImageAdapter(data);
                Init();
                rv_image.setLayoutManager(new GridLayoutManager(this.getContext(), 3));
                rv_image.setAdapter(adapter);
                refDb.child(IMAGE + "/" + fUser.getEmail().hashCode() + "").addChildEventListener(imageListener);
            }
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(GridImageViewModel.class);
    }

    private void Init() {
        imageListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getValue() != null) {
                    String path = snapshot.getValue(String.class);
                    if (path != null) {
                        rv_image.setVisibility(View.VISIBLE);
                        layout.setVisibility(View.GONE);
                        data.add(path);
                        adapter.notifyItemInserted(data.size() - 1);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    String path = snapshot.getValue(String.class);
                    if (path != null) {
                        if (data != null) {
                            for (String image : data) {
                                if (image.equals(path)) {
                                    data.remove(image);
                                    adapter.notifyItemRemoved(data.indexOf(image));
                                    break;
                                }
                            }
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
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        refDb.child(IMAGE).child(fUser.getEmail().hashCode() + "").removeEventListener(imageListener);
        refDb.onDisconnect();
    }
}