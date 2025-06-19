package com.example.interestgroups.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.interestgroups.R;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button loginButton;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // Saves application state like portrait or landscape
        super.onCreate(savedInstanceState);
        // Shows the activity_login page
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Link UI elements from login_activity.xml using their ids
        emailInput = findViewById(R.id.editTextEmail);
        passwordInput = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogin);
        signUpButton = findViewById(R.id.buttonSignUp);

        // Set login button click to go to loginUser function
        loginButton.setOnClickListener(view -> loginUser());
    }

    private void loginUser() {
        // Gets the text from the user inputs
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        // If the fields are empty sends a short message using a toast
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Email and password are required.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Calls firebase to sign in listens for when it finishes and if successful or not
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Go to MainActivity after successful login
                        // Start activity (source -> destination)
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        // closes login page
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
