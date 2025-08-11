package com.example.interestgroups.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.interestgroups.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    private EditText emailInput, passwordInput, confirmPasswordInput;
    private Button signupButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        // Link XML views
        emailInput = findViewById(R.id.editTextEmail);
        passwordInput = findViewById(R.id.editTextPassword);
        confirmPasswordInput = findViewById(R.id.editTextConfirmPassword);
        signupButton = findViewById(R.id.buttonSignup);

        signupButton.setOnClickListener(view -> registerUser());
    }

    private void registerUser() {
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        String confirmPassword = confirmPasswordInput.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    String uid = mAuth.getCurrentUser().getUid();

                    // Create user data map
                    Map<String, Object> userData = new HashMap<>();
                    userData.put("email", email);
                    userData.put("createdAt", System.currentTimeMillis());
                    userData.put("groups", new ArrayList<>()); // Empty list initially

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("users").document(uid)
                            .set(userData)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                                Intent loginIntent = new Intent(SignupActivity.this, LoginActivity.class);
                                startActivity(loginIntent);
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Log.e("Firestore Error", e.getMessage());
                                Toast.makeText(this, "Failed to save user data.", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(error -> {
                    Log.e("signup error", error.toString());
                    Toast.makeText(this, "Error creating account", Toast.LENGTH_SHORT).show();
                });
    }
}