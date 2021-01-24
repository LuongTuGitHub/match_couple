package com.java.tu.app.message.fragment.chat;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.java.tu.app.message.R;
import com.java.tu.app.message.adapter.ConversationAdapter;
import com.java.tu.app.message.adapter.PersonAdapter;
import com.java.tu.app.message.adapter.event.OnClickItemRecyclerView;
import com.java.tu.app.message.asset.Image;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.java.tu.app.message.asset.Const.AVATAR;
import static com.java.tu.app.message.asset.Const.CHAT;
import static com.java.tu.app.message.asset.Const.CONVERSATION;
import static com.java.tu.app.message.asset.Const.OFFLINE;
import static com.java.tu.app.message.asset.Const.ONLINE;
import static com.java.tu.app.message.asset.Const.PROFILE;
import static com.java.tu.app.message.asset.Const.STATUS;

public class ChatFragment extends Fragment implements OnClickItemRecyclerView {

    private ChatViewModel mViewModel;
    private View view;
    private PersonAdapter personAdapter;
    private FirebaseUser fUser;
    private DatabaseReference refDb;
    private ConversationAdapter conversationAdapter;


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.iv_avatar)
    public ImageView iv_avatar;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_friend)
    public RecyclerView rv_friend;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_chat)
    public RecyclerView rv_chat;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.chat_fragment, container, false);
            ButterKnife.bind(this, view);
            fUser = FirebaseAuth.getInstance().getCurrentUser();
            refDb = FirebaseDatabase.getInstance().getReference();
            rv_friend.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
            rv_chat.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
            if (personAdapter == null) {
                personAdapter = new PersonAdapter(requireContext());
                rv_friend.setAdapter(personAdapter);
            }
            if (conversationAdapter == null) {
                conversationAdapter = new ConversationAdapter(this, requireContext());
                rv_chat.setAdapter(conversationAdapter);
            }
            iv_avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewPager viewPager = requireActivity().findViewById(R.id.vp_home);
                    if (viewPager != null) {
                        if (Objects.requireNonNull(viewPager.getAdapter()).getCount() > 0) {
                            viewPager.setCurrentItem(2);
                        }
                    }
                }
            });
            refDb.child(STATUS).child(Objects.requireNonNull(fUser.getEmail()).hashCode() + "").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue() != null) {
                        String status = snapshot.getValue(String.class);
                        if (status != null) {
                            if (status.equals(ONLINE)) {
                                toolbar.setSubtitle(R.string.active);
                            } else {
                                toolbar.setSubtitle(R.string.inactive);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            refDb.child(PROFILE).child(fUser.getEmail().hashCode() + "").child(AVATAR).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue() != null) {
                        String key = snapshot.getValue(String.class);
                        if (key != null) {
                            new Image(requireContext()).getImage(iv_avatar, key, Long.MAX_VALUE);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            refDb.child(CHAT).child(fUser.getEmail().hashCode() + "").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (snapshot.getValue() != null) {
                        String key = snapshot.getValue(String.class);
                        if (key != null) {
                            ArrayList<String> conversation = conversationAdapter.getConversions();
                            if (conversation == null) {
                                conversation = new ArrayList<>();
                            }
                            conversation.add(key);
                            conversationAdapter.setConversions(conversation);
                            conversationAdapter.notifyItemInserted(conversation.size() - 1);
                        }
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        refDb.onDisconnect();
    }

    @Override
    public void onSelectItem(View view, int position) {

    }
}