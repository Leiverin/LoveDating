package com.project.lovedatingapp.ui.chat;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.google.firebase.database.Query;

import com.project.lovedatingapp.adapters.UserAdapter;
import com.project.lovedatingapp.databinding.FragmentChatBinding;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.project.lovedatingapp.models.ChatList;
import com.project.lovedatingapp.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    private ChatViewModel chatViewModel;
    private FragmentChatBinding binding;
    private UserAdapter adapter;
    private List<User> list;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    private List<ChatList> userList;


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


        reference=FirebaseDatabase.getInstance().getReference("ChatList").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    ChatList chatList=snapshot.getValue(ChatList.class);
                    userList.add(chatList);
                }
                chatList();
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
                searchUser(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return root;
    }
    private void chatList() {
        list=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    User user=snapshot.getValue(User.class);
                    for (ChatList chatList:userList){
                        if(user.getId().equals(chatList.getId())){
                            list.add(user);
                        }
                    }
                }
                adapter=new UserAdapter(getContext(), list);
                binding.rvChat.setAdapter(adapter);
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
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    assert firebaseUser != null;
                    assert user != null;
                    if (!user.getId().equals(firebaseUser.getUid())) {
                        list.add(user);
                    }
                }
                adapter=new UserAdapter(getContext(),list);
                binding.rvChat.setAdapter(adapter);
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