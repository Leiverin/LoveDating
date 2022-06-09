package com.project.lovedatingapp.ui.home;

import android.util.Log;

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
import com.google.gson.Gson;
import com.project.lovedatingapp.models.Image;
import com.project.lovedatingapp.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeViewModel extends ViewModel {
    private DatabaseReference reference;
    private FirebaseUser firebaseUser;
    private List<Image> mListImage;
    private List<User> mListUser;
    private MutableLiveData<List<User>> mListUserLiveData;

    public HomeViewModel() {
        mListUserLiveData = new MutableLiveData<>();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        mListImage = new ArrayList<>();
        mListUser = new ArrayList<>();
    }
    public void callToGetImage(){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                mListUser.clear();

                for(DataSnapshot ds : snapshot.getChildren()){
                    HashMap<String,Object> map = (HashMap<String, Object>) ds.getValue();
                    String id = (String) map.get("id");
                    if(!id.equals(firebaseUser.getUid())){
                        reference.child(id).child("images").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                mListImage = new ArrayList<>();
                                for (DataSnapshot dsChild : snapshot.getChildren()) {
                                    HashMap<String,Object> map = (HashMap<String, Object>) dsChild.getValue();
                                    String idImg = (String) map.get("id");
                                    String urlImg = (String) map.get("imageURL");
                                    mListImage.add(new Image(idImg, urlImg));
                                }
                                mListUser.add(new User(id, mListImage));
                                mListUserLiveData.postValue(mListUser);
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public LiveData<List<User>> getUser() {
        return mListUserLiveData;
    }
}