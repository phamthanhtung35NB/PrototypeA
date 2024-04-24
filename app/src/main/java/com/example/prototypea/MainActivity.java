package com.example.prototypea;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
public class MainActivity extends AppCompatActivity {
    static private List<ItemList> itemList = new ArrayList<>();
    private FirebaseAuth mAuth;
    Adapter adapter;
    RecyclerView listItemRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        init();
        addEvent();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    void init() {
    //set adapter
    adapter = new Adapter(itemList, this);
    listItemRecyclerView = findViewById(R.id.listItemRecyclerView);
    listItemRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    listItemRecyclerView.setAdapter(adapter);

    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    String uid = mAuth.getCurrentUser().getUid();
    dbRef.child(uid).child("mission").addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        itemList.clear();
        for (DataSnapshot itemSnapshot: dataSnapshot.getChildren()) {
            String key = itemSnapshot.getKey();
            String content = itemSnapshot.child("content").getValue(String.class);
            String status = itemSnapshot.child("status").getValue(String.class);
            itemList.add(new ItemList(content, status, key));
        }
        adapter.updateList(itemList);
        listItemRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        // Handle possible errors.
    }
});
}
    void addEvent() {
        findViewById(R.id.button).setOnClickListener(v -> showDialog());
    }
    private void showDialog() {
        final EditText taskEditText = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add a new task")
                .setMessage("What do you want to do next?")
                .setView(taskEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText());
                        addItemToFirebase(task);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    private void addItemToFirebase(String task) {
        Locale vietnam = new Locale("vi", "VN");
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss", vietnam);
        String currentDateTimeString = sdf.format(new Date());

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        String uid = mAuth.getCurrentUser().getUid();
        ItemList item = new ItemList(task, "Không Tốt", currentDateTimeString);
        dbRef.child(uid).child("mission").child(currentDateTimeString).setValue(item);
    }
}