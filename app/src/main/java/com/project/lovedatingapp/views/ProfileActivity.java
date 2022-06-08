package com.project.lovedatingapp.views;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.project.lovedatingapp.MainActivity;
import com.project.lovedatingapp.R;
import com.project.lovedatingapp.adapters.ImageProfileAdapter;
import com.project.lovedatingapp.databinding.ActivityProfileBinding;
import com.project.lovedatingapp.interfaces.IOnClickImageProfile;
import com.project.lovedatingapp.models.Image;
import com.project.lovedatingapp.models.User;
import com.project.lovedatingapp.utils.Common;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ProfileActivity extends AppCompatActivity {
    private ActivityProfileBinding binding;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private ProgressBar progressBar;
    private List<Image> mListImages;
    private List<Image> mListImageDTB;
    private ImageProfileAdapter adapter;
    private Image imageSelected;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private boolean isAlreadyExist;

    final private ActivityResultLauncher<String> launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
            if(result != null){
                uploadImage(result);
            }else{
                isAlreadyExist = false;
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        progressBar = binding.prgLoadImg;
        progressBar.setVisibility(View.GONE);

        mListImageDTB = new ArrayList<>();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        // Get information user
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    Common.user = user;
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        LinearLayoutManager layoutManager = new GridLayoutManager(this, 3);
        binding.rvImageProfile.setLayoutManager(layoutManager);

        mListImageDTB = getListImages(tmpListImages());
        mListImages = new ArrayList<>();
        adapter = new ImageProfileAdapter(this, mListImages, new IOnClickImageProfile() {
            @Override
            public void OnClickImage(int position, Image image) {
                for(Image img : mListImages){
                    if(image.getId() == img.getId()){
                        imageSelected = image;
                        isAlreadyExist = true;
                        break;
                    }
                }
                selectImage();
            }
        });
        binding.rvImageProfile.setAdapter(adapter);

        binding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                finish();
            }
        });

        binding.viewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickEditName();
            }
        });

        // Get image already exist
        reference.child("images").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                mListImages.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    HashMap<String,Object> map = (HashMap<String, Object>) ds.getValue();
                    String id = (String) map.get("id");
                    String imageURL = (String) map.get("imageURL");
                    Image image = new Image(id, imageURL);
                    mListImages.add(0, image);
                }
                mListImages.addAll(mListImageDTB);
                Common.mListImage = mListImages;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void onClickEditName() {
        
    }

    public void selectImage() {
        launcher.launch("image/*");
    }

    private void uploadImage(Uri uri){
        StorageReference storageReference = storageRef.child("images/"+ UUID.randomUUID().toString());
        storageReference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            if(isAlreadyExist){
                                HashMap map = new HashMap();
                                map.put("id", imageSelected.getId());
                                map.put("imageURL", uri.toString());
                                reference.child("images").child(imageSelected.getId()).setValue(map);
                            }else{
                                String key = reference.child("images").push().getKey();
                                HashMap map = new HashMap();
                                map.put("id", key);
                                map.put("imageURL", uri.toString());
                                reference.child("images").child(key).setValue(map);
                            }
                        }
                    });
                }else{
                    Log.d("Error in profile", task.getException().getMessage());
                }
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    private List<Image> tmpListImages(){
        List<Image> imageList = new ArrayList<>();

        imageList.add(new Image(1+"","https://i.ibb.co/TgR2BQz/2gefnr1.jpg"));
        imageList.add(new Image(2+"","https://i.ibb.co/TgR2BQz/2gefnr1.jpg"));
        imageList.add(new Image(3+"","https://i.ibb.co/TgR2BQz/2gefnr1.jpg"));
        imageList.add(new Image(4+"","https://i.ibb.co/TgR2BQz/2gefnr1.jpg"));
        imageList.add(new Image(5+"","https://i.ibb.co/TgR2BQz/2gefnr1.jpg"));
        imageList.add(new Image(6+"","https://i.ibb.co/TgR2BQz/2gefnr1.jpg"));

        return imageList;
    }

    private List<Image> getListImages(List<Image> images){
        if(images.size() > 6){
            images.remove(images.size() - 1);
            return getListImages(images);
        }else{
            return images;
        }
    }

}