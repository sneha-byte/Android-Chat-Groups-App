package com.example.interestgroups.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.interestgroups.R;
import com.example.interestgroups.adapter.MessageAdapter;
import com.example.interestgroups.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText editTextMessage;
    private Button btnSend;

    private MessageAdapter messageAdapter;
    private List<Message> messageList;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    private String currentUserId;
    private String otherUserId;
    private String chatId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = findViewById(R.id.recyclerViewMessages);
        editTextMessage = findViewById(R.id.editTextMessage);
        btnSend = findViewById(R.id.btnSend);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList, auth);
        recyclerView.setAdapter(messageAdapter);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUserId = auth.getCurrentUser().getUid();

        // Get the other user's UID from the intent
        otherUserId = getIntent().getStringExtra("otherUserId");
        if (TextUtils.isEmpty(otherUserId)) {
            Toast.makeText(this, "No user selected", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Generate a consistent chatId for two users (alphabetically sorted)
        chatId = generateChatId(currentUserId, otherUserId);

        // Load existing messages
        loadMessages();

        // Send message button
        btnSend.setOnClickListener(v -> sendMessage());
    }

    /** Generates a unique chat ID for two users (consistent order) */
    private String generateChatId(String uid1, String uid2) {
        // Sort UIDs so the order is always the same
        if (uid1.compareTo(uid2) < 0) {
            return uid1 + "_" + uid2;
        } else {
            return uid2 + "_" + uid1;
        }
    }

    /** Load messages from Firestore for this chat */
    private void loadMessages() {
        CollectionReference messagesRef = db.collection("chats")
                .document(chatId)
                .collection("messages");

        messagesRef.orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) return;
                    messageList.clear();
                    if (value != null) {
                        for (QueryDocumentSnapshot doc : value) {
                            Message message = doc.toObject(Message.class);
                            messageList.add(message);
                        }
                        messageAdapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(messageList.size() - 1);
                    }
                });
    }

    /** Send a message and save to Firestore */
    private void sendMessage() {
        String text = editTextMessage.getText().toString().trim();
        if (TextUtils.isEmpty(text)) return;

        Message message = new Message(currentUserId, text, System.currentTimeMillis());

        DocumentReference messageRef = db.collection("chats")
                .document(chatId)
                .collection("messages")
                .document();

        messageRef.set(message).addOnSuccessListener(aVoid -> editTextMessage.setText(""))
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to send", Toast.LENGTH_SHORT).show());
    }
}
