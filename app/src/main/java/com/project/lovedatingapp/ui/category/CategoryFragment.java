package com.project.lovedatingapp.ui.category;

import android.content.Intent;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.google.gson.Gson;
import com.project.lovedatingapp.interfaces.IOnClickUserWithImage;
import com.project.lovedatingapp.interfaces.OnEventShowUser;
import com.project.lovedatingapp.models.Image;
import com.project.lovedatingapp.models.UserCategory;
import com.project.lovedatingapp.utils.Common;
import com.project.lovedatingapp.adapters.UserAdapterCategory;
import com.project.lovedatingapp.databinding.FragmentCategoryBinding;
import com.project.lovedatingapp.models.User;
import com.project.lovedatingapp.views.DetailActivity;
import com.project.lovedatingapp.views.ShowDetailUserActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CategoryFragment extends Fragment {
    private CategoryViewModel categoryViewModel;
    private FragmentCategoryBinding binding;
    private List<UserCategory> list;
    private UserAdapterCategory adapter;
    private FirebaseUser firebaseUser;
    private List<Image> mListImage;
    private DatabaseReference reference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        categoryViewModel =
                new ViewModelProvider(this).get(CategoryViewModel.class);

        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        categoryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        list = new ArrayList<>();
        mListImage = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users");

        adapter = new UserAdapterCategory(getActivity(), list, new IOnClickUserWithImage() {
            @Override
            public void onClick(UserCategory user, String url) {
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_ID_USER, user.getUser().getId());
                intent.putExtra(DetailActivity.EXTRA_URL_IMAGE, url);
                getActivity().startActivity(intent);
            }
        });
        binding.rvCategory.setAdapter(adapter);
        readUser();

        binding.edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(i == 0 && i2 == 0){

                }else{
                    searchUser(charSequence.toString().toLowerCase());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return root;
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
                        if (user.getAge() <= (Common.user.getAge() + 2) && user.getAge() >= (Common.user.getAge() - 2)){
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
                                    list.add(new UserCategory(user, mListImage));
                                    adapter.setList(list);
                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                }
                            });
                        }
                    }
                }
                adapter.setList(list);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void readUser() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    assert user != null;
                    if (!user.getId().equals(firebaseUser.getUid())) {
                        if (user.getAge() <= (Common.user.getAge() + 2) && user.getAge() >= (Common.user.getAge() - 2)){
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
                                    list.add(new UserCategory(user, mListImage));
                                    adapter.setList(list);
                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                }
                            });
                        }
                    }
                }
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