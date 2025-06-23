package com.example.interestgroups.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.interestgroups.R;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    Button btnChat, btnGroups, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Link buttons
        btnChat = findViewById(R.id.btnChat);
        btnGroups = findViewById(R.id.btnGroups);
        btnLogout = findViewById(R.id.btnLogout);

        // Navigate to Chat screen
        btnChat.setOnClickListener(view -> {
            Intent chatIntent = new Intent(HomeActivity.this, PostActivity.class);
            startActivity(chatIntent);
        });

        // Navigate to Group List screen
        btnGroups.setOnClickListener(view -> {
           // Intent groupIntent = new Intent(HomeActivity.this, GroupListActivity.class);
            //startActivity(groupIntent);
        });

        // Log out and go back to login screen
        btnLogout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent logOutIntent = new Intent(HomeActivity.this, LoginActivity.class);
            logOutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(logOutIntent);
        });
    }
}
