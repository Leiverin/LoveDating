package com.project.lovedatingapp.ui.me.view_pager2;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.lovedatingapp.models.Post;
import com.project.lovedatingapp.databinding.PhotoFragmentBinding;
import com.project.lovedatingapp.ui.me.adapter.PhotoFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

public class PhotoFragment extends Fragment {
    private List<Post> mListPost;
    private PhotoFragmentBinding binding;
    private PhotoFragmentAdapter adapter;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Post");

    public static PhotoFragment newInstance() {
        return new PhotoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = PhotoFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mListPost = new ArrayList<>();
        adapter = new PhotoFragmentAdapter(getContext(), mListPost);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setAdapter(adapter);
        getPhoto();
        return root;

    }

    private void getPhoto() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Post post = dataSnapshot.getValue(Post.class);
                    mListPost.add(post);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}