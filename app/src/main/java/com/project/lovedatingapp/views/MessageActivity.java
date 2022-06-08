package com.project.lovedatingapp.views;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.lovedatingapp.R;
import com.project.lovedatingapp.adapters.MessageAdapter;
import com.project.lovedatingapp.models.Chat;
import com.project.lovedatingapp.models.User;
import com.project.lovedatingapp.utils.Common;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private static final int PICK_IMAGE=1;


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
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               selectImage();
            }
        });

        rvSend.setHasFixedSize(true);

        intent = getIntent();
        String userId = intent.getStringExtra("userId");
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
//                if(user.getImageURL().equals("default")){
//                    circleImage.setImageResource(R.mipmap.ic_launcher);
//                }else {
//                    Gilde.with(MessageActivity.this).load(user.getImageURL()).into(circleImage);
//                }
                readMessage(firebaseUser.getUid(), userId, Common.mListImage.get(0).getUrl());
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
        intent = getIntent();
        String userId = intent.getStringExtra("userId");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", key);
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("isseen", false);
        reference.child("Chats").push().setValue(hashMap);

        DatabaseReference chatRef=FirebaseDatabase.getInstance().getReference("ChatList")
                .child(firebaseUser.getUid())
                .child(userId);
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    chatRef.child("id").setValue(userId);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
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

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select image"), PICK_IMAGE);

    }


    @Override
    protected void onPause() {
        super.onPause();
        databaseReference.removeEventListener(seenListener);
    }
}