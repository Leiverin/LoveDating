package com.project.lovedatingapp.views;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.lovedatingapp.adapters.PhotoAdapter;
import com.project.lovedatingapp.MainActivity;
import com.project.lovedatingapp.databinding.ActivityPostBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PostActivity extends AppCompatActivity {
    private ActivityPostBinding binding;
    private PhotoAdapter adapter;
    List<Uri> uriList;
    int PICK_IMAGE = 22;
    ArrayList<Uri> mlList = new ArrayList<Uri>();
    private Uri ImageUri;
    private int post_count = 0;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(view);


        binding.toobarTitle.setText("Create Post");
        setSupportActionBar(binding.toobar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        adapter = new PhotoAdapter(this, uriList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        binding.rcv.setAdapter(adapter);
        binding.rcv.setHasFixedSize(true);
        binding.rcv.setLayoutManager(gridLayoutManager);

        binding.imgSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });
        binding.tvPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post();
            }

        });

    }

    private void post() {
        if (ImageUri != null) {

            StorageReference imageFolder = FirebaseStorage.getInstance().getReference().child("ImagePost");
            for (post_count = 0; post_count < mlList.size(); post_count++) {

                Uri InvididualImage = mlList.get(post_count);
                StorageReference ImageName = imageFolder.child("Image" + InvididualImage.getLastPathSegment());

                ImageName.putFile(InvididualImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String url = String.valueOf(uri);
                                StorageLink(url);
                                startActivity(new Intent(PostActivity.this, MainActivity.class));
                                Toast.makeText(getApplication(), "Post Successfully !", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        binding.progressBar.setVisibility(View.VISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        binding.progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplication(), "Post Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        } else {
            Toast.makeText(getApplication(), "Please select image", Toast.LENGTH_SHORT).show();
        }
    }

    private void StorageLink(String url) {
        String caption = binding.tvCaption.getText().toString();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Post");
        HashMap<String, Object> hashMap = new HashMap<>();
        String id = databaseReference.push().getKey();
        hashMap.put("PostID", id);
        hashMap.put("ImageLink", url);
        hashMap.put("Caption", caption);
        hashMap.put("Publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());

        databaseReference.push().setValue(hashMap);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data.getClipData() != null) {
                    Log.d("aaa", String.valueOf(data.getClipData()));
                    int countClipData = data.getClipData().getItemCount();
                    int currentImage = 0;
                    while (currentImage < countClipData) {
                        ImageUri = data.getClipData().getItemAt(currentImage).getUri();
                        mlList.add(ImageUri);
                        currentImage = currentImage + 1;

                    }
                    adapter.setData(mlList);
                } else {
                    Toast.makeText(getApplication(), "Please select multiple image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}