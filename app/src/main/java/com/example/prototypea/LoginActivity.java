package com.example.prototypea;

import static android.content.ContentValues.TAG;
import static com.example.prototypea.SocialNetworkFragment.uid;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prototypea.Class.ItemList;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
//        if (!email.equals("") || !password.equals("")) {
//            loginUser();
//        }
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

                        //khểm tra xem có // thêm thông tin người dùng vào firestore collection là restaurant dựa vào uid


                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(LoginActivity.this, "Authentication successful.",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
// Lấy uid
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        GetData.readAndSaveSumWeek(uid);
                        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
                        Locale vietnam = new Locale("vi", "VN");
                        SimpleDateFormat day = new SimpleDateFormat("ddMMyyyy", vietnam);
                        String day1 = day.format(new Date());

                        DatabaseReference dbRef1 = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference ref = dbRef1.child(uid).child("mission").child(day1);
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                System.out.println("đang kiểm tra mục tiêu mặc định");
                                if(dataSnapshot.hasChild("sum")) {
                                    // child "sum" exists
                                    System.out.println("đa có mục tiêu mặc định");
                                    System.out.println("sum: " + dataSnapshot.child("sum").getValue());
                                } else {
                                    // child "sum" does not exist
                                    System.out.println("chưa có mục tiêu mặc định");
                                    defaultTaget(uid);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // Handle possible errors.
                            }
                        });

                        // Lắng nghe sự kiện thay đổi dữ liệu
                        dbRef.child(uid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {


                                    // Lấy thông tin người dùng
                                    String birthYear = dataSnapshot.child("birthYear").getValue(String.class);
                                    String fullName = dataSnapshot.child("fullName").getValue(String.class);
                                    String image = dataSnapshot.child("image").getValue(String.class);


                                    SharedPreferences sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("uid", uid);
                                    editor.putString("email", email);
                                    editor.putString("password", password);
                                    editor.putString("birthYear", birthYear);
                                    editor.putString("fullName", fullName);
                                    editor.putString("image", image);
                                    System.out.println("dataLogin: " + uid + email + password + birthYear + fullName + image);
                                    editor.apply();
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
    void defaultTaget(String uid) {
    Locale vietnam = new Locale("vi", "VN");
    SimpleDateFormat day = new SimpleDateFormat("ddMMyyyy", vietnam);
    String day1 = day.format(new Date());
    System.out.println(day1);
    System.out.println("đang tạo mục tiêu mặc định");
    //tạo arr mục tiêu mặc định
    String[] arr = {"Ăn sáng đầy đủ", "Uống đủ nước", "Ngủ đủ giấc", "Tập thể dục thường xuyên", "Ăn uống lành mạnh",
    "Hạn chế ăn vặt", "Dành thời gian cho bản thân", "Kết nối với người thân", "Học hỏi những điều mới", "Giúp đỡ người khác"};
        ArrayList<ItemList> list = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss", vietnam);
        String currentDateTimeString = sdf.format(new Date()) + i; // Add the counter to the end of the string
        ItemList item = new ItemList(arr[i], "Chưa Thực Hiện Được", currentDateTimeString);
        list.add(item);
        System.out.println("item: " + item.getContent() + item.getStatus() + item.getKey());
    }

    dbRef.child(uid).child("mission").child(day1).setValue(list);
}
}