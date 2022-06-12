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
import com.project.lovedatingapp.interfaces.IOnClickUserWithImage;
import com.project.lovedatingapp.models.Image;
import com.project.lovedatingapp.models.UserCategory;
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
import java.util.HashMap;
import java.util.List;

public class ChatFragment extends Fragment {

    private ChatViewModel chatViewModel;
    private FragmentChatBinding binding;
    private UserAdapter adapter;
    private List<UserCategory> mUser;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private List<String> userList;
    private List<Image> mListImage;
    private boolean checkSender = false;
    private boolean checkReceiver = false;

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
        mListImage = new ArrayList<>();
        adapter = new UserAdapter(getContext(), mUser, new OnEventShowUser() {
            @Override
            public void onClickShowUser(UserCategory user) {
                Intent intent = new Intent(getContext(), MessageActivity.class);
                intent.putExtra(MessageActivity.KEY, user.getUser().getId());
                intent.putExtra(MessageActivity.KEY_URL, user.getImages().get(0).getUrl());
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
                    checkSender = false;
                    checkReceiver = false;
                    Chat chat = snapshot.getValue(Chat.class);
                    if(userList.size() == 0){
                        if (chat.getSender().equals(firebaseUser.getUid())) {
                            userList.add(chat.getReceiver());
                        }
                        if (chat.getReceiver().equals(firebaseUser.getUid())){
                            userList.add(chat.getSender());
                        }
                    }else{
                        for(int i = 0; i < userList.size(); i++){
                            if(chat.getSender().equals(firebaseUser.getUid())){
                                if(userList.get(i).equals(chat.getReceiver())){
                                    checkReceiver = true;
                                }
                            }
                            if (chat.getReceiver().equals(firebaseUser.getUid())){
                                if(userList.get(i).equals(chat.getSender())){
                                    checkSender = true;
                                }
                            }
                        }
                        if(!checkSender && !chat.getSender().equals(firebaseUser.getUid())){
                            userList.add(chat.getSender());
                        }
                        if(!checkReceiver && !chat.getReceiver().equals(firebaseUser.getUid())){
                            userList.add(chat.getReceiver());
                        }
                    }
                }
                readChats();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });



        binding.edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i == 0 && i2 == 0) {

                } else {
                    searchUser(charSequence.toString().toLowerCase());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return root;
    }

    private boolean checkExist = false;

    private void readChats() {
        mUser = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                mUser.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    checkExist = false;
                    User user = snapshot.getValue(User.class);
                    for (String id : userList) {
                        if (user.getId().equals(id)) {
                            if (mUser.size() != 0) {
                                for (int i = 0; i < mUser.size(); i++) {
                                    User user1 = mUser.get(i).getUser();
                                    if (user1.getId().equals(user.getId())) {
                                        checkExist = true;
                                    }
                                }
                                if(!checkExist){
                                    reference.child(user.getId()).child("images").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot snapshot) {
                                            mListImage = new ArrayList<>();
                                            for (DataSnapshot dsChild : snapshot.getChildren()) {
                                                HashMap<String,Object> map = (HashMap<String, Object>) dsChild.getValue();
                                                String idImg = (String) map.get("id");
                                                String urlImg = (String) map.get("imageURL");
                                                mListImage.add(new Image(idImg, urlImg));
                                            }
                                            mUser.add(new UserCategory(user, mListImage));
                                            adapter.setList(mUser);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                        }
                                    });
                                }
                            } else {
                                Log.d("zzzzzz", "onDataChange: ");
                                reference.child(user.getId()).child("images").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        mListImage = new ArrayList<>();
                                        for (DataSnapshot dsChild : snapshot.getChildren()) {
                                            HashMap<String,Object> map = (HashMap<String, Object>) dsChild.getValue();
                                            String idImg = (String) map.get("id");
                                            String urlImg = (String) map.get("imageURL");
                                            mListImage.add(new Image(idImg, urlImg));
                                        }
                                        mUser.add(new UserCategory(user, mListImage));
                                        adapter.setList(mUser);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void searchUser(String s) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("search")
                .startAt(s).endAt(s + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                mUser.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    assert firebaseUser != null;
                    assert user != null;
                    if (!user.getId().equals(firebaseUser.getUid())) {
                        reference.child(user.getId()).child("images").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                mListImage = new ArrayList<>();
                                for (DataSnapshot dsChild : snapshot.getChildren()) {
                                    HashMap<String,Object> map = (HashMap<String, Object>) dsChild.getValue();
                                    String idImg = (String) map.get("id");
                                    String urlImg = (String) map.get("imageURL");
                                    mListImage.add(new Image(idImg, urlImg));
                                }
                                mUser.add(new UserCategory(user, mListImage));
                                adapter.setList(mUser);
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });
                    }
                }
                adapter.setList(mUser);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}