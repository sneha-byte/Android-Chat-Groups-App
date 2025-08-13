package com.example.interestgroups.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.interestgroups.R;
import com.example.interestgroups.adapter.MessagesAdapter;
import com.example.interestgroups.model.MessageModel;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerMessages;
    private EditText editTextMessage;
    private Button buttonSend;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    private String currentUserId;
    private String otherUserId;
    private String chatId;

    private List<MessageModel> messageList = new ArrayList<>();
    private MessagesAdapter messagesAdapter;

    private ListenerRegistration messagesListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerMessages = findViewById(R.id.recyclerMessages);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUserId = auth.getCurrentUser().getUid();

        // Get otherUserId from Intent extras
        otherUserId = getIntent().getStringExtra("otherUserId");

        // Initialize RecyclerView
        recyclerMessages.setLayoutManager(new LinearLayoutManager(this));
        messagesAdapter = new MessagesAdapter(messageList, currentUserId);
        recyclerMessages.setAdapter(messagesAdapter);

        findOrCreateChat();

        buttonSend.setOnClickListener(v -> sendMessage());
    }

    private void findOrCreateChat() {
        // Chat ID could be stable like: uid1_uid2 sorted lexicographically for uniqueness
        chatId = generateChatId(currentUserId, otherUserId);

        // Listen for messages in real-time
        messagesListener = db.collection("chats")
                .document(chatId)
                .collection("messages")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((queryDocumentSnapshots, error) -> {
                    if (error != null) {
                        return;
                    }
                    if (queryDocumentSnapshots != null) {
                        messageList.clear();
                        for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                            MessageModel msg = doc.toObject(MessageModel.class);
                            messageList.add(msg);
                        }
                        messagesAdapter.notifyDataSetChanged();
                        recyclerMessages.scrollToPosition(messageList.size() - 1);
                    }
                });
    }

    private String generateChatId(String uid1, String uid2) {
        if (uid1.compareTo(uid2) < 0) {
            return uid1 + "_" + uid2;
        } else {
            return uid2 + "_" + uid1;
        }
    }

    private void sendMessage() {
        String text = editTextMessage.getText().toString().trim();
        if (TextUtils.isEmpty(text)) return;

        MessageModel message = new MessageModel(currentUserId, text, Timestamp.now());

        db.collection("chats")
                .document(chatId)
                .collection("messages")
                .add(message)
                .addOnSuccessListener(documentReference -> {
                    editTextMessage.setText("");
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (messagesListener != null) {
            messagesListener.remove();
        }
    }
}
