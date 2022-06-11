package com.project.lovedatingapp.ui.chat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.google.firebase.database.Query;


import com.google.gson.Gson;
import com.project.lovedatingapp.views.MessageActivity;
import com.project.lovedatingapp.views.ShowDetailUserActivity;
import com.project.lovedatingapp.utils.Common;
import com.project.lovedatingapp.adapters.UserAdapter;
import com.project.lovedatingapp.databinding.FragmentChatBinding;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import com.project.lovedatingapp.interfaces.OnEventShowUser;
import com.project.lovedatingapp.models.Chat;
import com.project.lovedatingapp.models.ChatList;
import com.project.lovedatingapp.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    private ChatViewModel chatViewModel;
    private FragmentChatBinding binding;
    private UserAdapter adapter;
    private List<User> mUser;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    private List<String> userList;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        chatViewModel =
                new ViewModelProvider(this).get(ChatViewModel.class);

        binding = FragmentChatBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        chatViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userList = new ArrayList<>();

        adapter = new UserAdapter(getContext(), mUser, new OnEventShowUser() {
            @Override
            public void onClickShowUser(User user) {
                Intent intent = new Intent(getContext(), MessageActivity.class);
                intent.putExtra(MessageActivity.KEY, user.getId());
                getActivity().startActivity(intent);
            }
        });

        binding.rvChat.setAdapter(adapter);

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getSender().equals(firebaseUser.getUid())) {
                        userList.add(chat.getReceiver());
                    }
                    if (chat.getReceiver().equals(firebaseUser.getUid())){
                        userList.add(chat.getSender());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        readChats();

        binding.edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i == 0 && i2 == 0) {

                } else {
//                    searchUser(charSequence.toString().toLowerCase());
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return root;
    }
    int j = 1;
    private void readChats() {
        mUser = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                mUser.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    for (String id : userList) {
                        if (user.getId().equals(id)) {
                            if (mUser.size() != 0) {
                                for (int i = 0; i < mUser.size(); i++) {
                                    User user1 = mUser.get(i);
                                    if (!user1.getId().equals(user.getId())) {
                                        j++;
                                    }
                                }
                                if(j == mUser.size()){
                                    mUser.add(user);
                                    j = 0;
                                }
                            } else {
                                mUser.add(user);
                            }

                        }
                    }
                }
                adapter.setList(mUser);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

//    private void searchUser(String s) {
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("search")
//                .startAt(s).endAt(s + "\uf8ff");
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
//                mUser.clear();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    User user = snapshot.getValue(User.class);
//                    assert firebaseUser != null;
//                    assert user != null;
//                    if (!user.getId().equals(firebaseUser.getUid())) {
//                        mUser.add(user);
//                    }
//                }
//                adapter.setList(mUser);
//            }
//
//            @Override
//            public void onCancelled(@NonNull @NotNull DatabaseError error) {
//
//            }
//        });
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}