package com.example.prototypea;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
public class RegisterActivity extends AppCompatActivity {
    private EditText registerPhoneEditText;
    private EditText registerPasswordEditText;
    private EditText confirmPasswordEditText;
    private EditText fullNameEditText;
    private EditText birthYearEditText;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        registerPhoneEditText = findViewById(R.id.registerPhoneEditText);
        registerPasswordEditText = findViewById(R.id.registerPasswordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        fullNameEditText = findViewById(R.id.fullNameEditText);
        birthYearEditText = findViewById(R.id.birthYearEditText);
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.registerButton).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String email = registerPhoneEditText.getText().toString();
        String password = registerPasswordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        String fullName = fullNameEditText.getText().toString();
        String birthYear = birthYearEditText.getText().toString();

        if (!password.equals(confirmPassword)) {
            Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
    .addOnCompleteListener(RegisterActivity.this, task -> {
        if (task.isSuccessful()) {
            String uid = mAuth.getCurrentUser().getUid();
            // Write user info to Realtime Database
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
            User user = new User(fullName, birthYear, email);
            dbRef.child(uid).setValue(user); // Use phone number as key

            Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
        } else {
            // Log the exception
            if (task.getException() != null) {
                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                    Toast.makeText(RegisterActivity.this, "The email address is already in use by another account.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
                Log.e("Registration Error", task.getException().getMessage());
            }
        }
    });
    }
});
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}