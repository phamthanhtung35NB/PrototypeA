package com.example.prototypea;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button registerButton;
    // Get a reference to the database
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);
        SharedPreferences sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        String password = sharedPreferences.getString("password", "");
        emailEditText.setText(email);
        passwordEditText.setText(password);
        // Set up the login button to call loginUser(
        if (!email.equals("") || !password.equals("")) {
            loginUser();
        }
        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginUser();
                }
            });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loginUser() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(LoginActivity.this, "Authentication successful.",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                        // Lắng nghe sự kiện thay đổi dữ liệu
                        dbRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                                    // Lấy uid
                                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                    // Lấy thông tin người dùng
                                    String birthYear = userSnapshot.child("birthYear").getValue(String.class);
                                    String fullName = userSnapshot.child("fullName").getValue(String.class);
                                    String image = userSnapshot.child("image").getValue(String.class);


                                    SharedPreferences sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("uid", uid);
                                    editor.putString("email", email);
                                    editor.putString("password", password);
                                    editor.putString("birthYear", birthYear);
                                    editor.putString("fullName", fullName);
                                    editor.putString("image", image);
                                    editor.apply();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // Xử lý lỗi
                                System.out.println("Failed to read value: " + databaseError.toException());
                            }
                        });

                        startActivity(intent);
                        finish();
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}