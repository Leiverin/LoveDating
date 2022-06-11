package com.project.lovedatingapp.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.lovedatingapp.R;
import com.project.lovedatingapp.adapters.MessageAdapter;
import com.project.lovedatingapp.models.Chat;
import com.project.lovedatingapp.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    private AppBarLayout appBarLayout;
    private Toolbar toobar;
    private CircleImageView circleImage;
    private TextView txtUsername;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    Intent intent;
    private RecyclerView rvSend;
    private RelativeLayout bottom;
    private EditText textSend;
    private ImageButton btnSend;
    private ImageButton btnImage;
    private List<Chat> list;
    private MessageAdapter adapter;
    ValueEventListener seenListener;
    String userId;
    public static final String KEY="GET";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        appBarLayout = findViewById(R.id.appBarLayout);
        toobar = findViewById(R.id.toobar);
        circleImage = findViewById(R.id.circleImage);
        txtUsername = findViewById(R.id.txtUsername);
        rvSend = findViewById(R.id.rv_send);
        bottom = findViewById(R.id.bottom);
        textSend = findViewById(R.id.text_send);
        btnSend = findViewById(R.id.btn_send);
        btnImage = findViewById(R.id.btn_image);
        setSupportActionBar(toobar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toobar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//               startActivity(new Intent(MessageActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });


        rvSend.setHasFixedSize(true);
        intent = getIntent();
        userId= intent.getStringExtra(KEY);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = textSend.getText().toString();
                if (msg.trim().length() == 0) {
                    Toast.makeText(MessageActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                } else {
                    sendMessage(firebaseUser.getUid(), userId, msg);
                }

                textSend.setText("");
            }
        });



        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                txtUsername.setText(user.getUsername());
                if(user.getListImage().get(0).getUrl().equals("default")){
                    circleImage.setImageResource(R.mipmap.ic_launcher);
                }else {
                    Glide.with(MessageActivity.this).load(user.getListImage().get(0).getUrl()).into(circleImage);
                }
                readMessage(firebaseUser.getUid(), userId, user.getListImage().get(0).getUrl());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        seenMessage(userId);
    }

    private void seenMessage(String userid) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid)) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isseen", true);
                        snapshot.getRef().updateChildren(hashMap);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void sendMessage(String sender, String receiver, String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference refKey = reference.push();

        String key = refKey.getKey();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", key);
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("isseen", false);
        reference.child("Chats").push().setValue(hashMap);

//        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("ChatList")
//                .child(sender)
//                .child(receiver);
//        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
//                if (!dataSnapshot.exists()) {
//                    chatRef.child("id").setValue(receiver);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull @NotNull DatabaseError error) {
//
//            }
//        });
    }

    private void readMessage(String myId, String userId, String imageURL) {
        list = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(myId) && chat.getSender().equals(userId) ||
                            chat.getReceiver().equals(userId) && chat.getSender().equals(myId)) {
                        list.add(chat);
                    }
                    adapter = new MessageAdapter(MessageActivity.this, list, imageURL);
                    rvSend.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }




    @Override
    protected void onPause() {
        super.onPause();
        databaseReference.removeEventListener(seenListener);
    }
}