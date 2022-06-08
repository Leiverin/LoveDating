package com.project.lovedatingapp.ui.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.lovedatingapp.models.Image;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<List<Image>> mListImageLiveData;
    private DatabaseReference reference;
    private FirebaseUser firebaseUser;
    private List<Image> mListImage;

    public HomeViewModel() {
        mListImageLiveData = new MutableLiveData<>();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        mListImage = new ArrayList<>();
    }

    public void callToGetImage(){
        reference.child("images").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                mListImage.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    HashMap<String,Object> map = (HashMap<String, Object>) ds.getValue();
                    String id = (String) map.get("id");
                    String imageURL = (String) map.get("imageURL");
                    mListImage.add(new Image(id, imageURL));
                }
                mListImageLiveData.postValue(mListImage);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public LiveData<List<Image>> getListImage() {
        return mListImageLiveData;
    }
}