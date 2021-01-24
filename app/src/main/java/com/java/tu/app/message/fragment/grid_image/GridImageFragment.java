package com.java.tu.app.message.fragment.grid_image;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.java.tu.app.message.asset.Const.IMAGE;
import static com.java.tu.app.message.asset.Const.TIME_SLASH;

public class GridImageFragment extends Fragment {

    private GridImageViewModel mViewModel;
    private View view;

    private final DatabaseReference refDb;
    private final FirebaseUser fUser;
    private GridImageAdapter adapter;
    private final Vector<String> data;
    private ChildEventListener imageListener;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_image)
    RecyclerView rv_image;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.layout_relative)
    RelativeLayout layout;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.layout)
    FrameLayout layout_pro;

    public GridImageFragment() {
        refDb = FirebaseDatabase.getInstance().getReference();
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        data = new Vector<>();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.grid_image_fragment, container, false);
            ButterKnife.bind(this, view);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (checkNetWork()) {
                        layout_pro.setVisibility(View.GONE);
                    } else {
                        new Handler().postDelayed(this, TIME_SLASH);
                    }
                }
            }, TIME_SLASH);
            if (adapter == null) {
                adapter = new GridImageAdapter(data, requireContext());
                Init();
                rv_image.setLayoutManager(new GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false));
                rv_image.setHasFixedSize(true);
                rv_image.setAdapter(adapter);
                refDb.child(IMAGE).child(Objects.requireNonNull(fUser.getEmail()).hashCode() + "").addChildEventListener(imageListener);
            }
            rv_image.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                }
            });
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
                        data.insertElementAt(path, 0);
                        adapter.notifyItemInserted(0);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    String path = snapshot.getKey();
                    if (path != null) {
                        if (data != null) {
                            for (int i = 0; i < data.size(); i++) {
                                if (data.get(i).equals(path)) {
                                    data.removeElementAt(i);
                                    adapter.notifyItemRemoved(i);
                                    if (data.size() == 0) {
                                        layout.setVisibility(View.VISIBLE);
                                    }
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
        refDb.child(IMAGE).child(Objects.requireNonNull(fUser.getEmail()).hashCode() + "").removeEventListener(imageListener);
        refDb.onDisconnect();
    }

    public boolean checkNetWork() {
        ConnectivityManager manager = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager.getAllNetworkInfo() == null) {
            return false;
        }
        return manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }
}