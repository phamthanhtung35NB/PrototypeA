package com.example.prototypea;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.prototypea.Adapter.AdapterTaget;
import com.example.prototypea.Class.ItemList;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class TagetFragment extends Fragment {
    static private List<ItemList> itemList = new ArrayList<>();
    AdapterTaget adapter;
    RecyclerView listItemRecyclerView;
    Button btnAdd;
    // Tạo một Dialog mới
    Dialog dialog;
    String uid;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_taget, container, false);
        init(view);
        addEvents(view);
        return view;
    }

    void init(View view) {
        //set adapter
        adapter = new AdapterTaget(itemList, getActivity());
        btnAdd = view.findViewById(R.id.btnAdd);
        listItemRecyclerView = view.findViewById(R.id.listItemRecyclerView);
        listItemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listItemRecyclerView.setAdapter(adapter);

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("dataLogin", getActivity().MODE_PRIVATE);
        uid = sharedPreferences.getString("uid", "");

        Locale vietnam = new Locale("vi", "VN");
        SimpleDateFormat day = new SimpleDateFormat("ddMMyyyy", vietnam);
        String day1 = day.format(new Date());




                dbRef.child(uid).child("mission").child(day1).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        itemList.clear();
                        int sum = 0;
                        for (DataSnapshot itemSnapshot: dataSnapshot.getChildren()) {
                            if (!itemSnapshot.getKey().equals("sum") && !itemSnapshot.getKey().equals("done")) {
                                String key = itemSnapshot.getKey();
                                String content = itemSnapshot.child("content").getValue(String.class);
                                String status = itemSnapshot.child("status").getValue(String.class);
                                System.out.printf("key: %s, content: %s, status: %s" , key, content, status);
                                if (status != null && status.equals("Đã Thực Hiện Được")) {
                                }
                                itemList.add(new ItemList(content, status, key));
                                sum++;
                            }
                        }
                        dbRef.child(uid).child("mission").child(day1).child("sum").setValue(sum);

                        updateDataSum(uid, day1, sum);
                        adapter.updateList(itemList);
                        listItemRecyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle possible errors.
                    }
                });
//    dbRef.child(uid).child("mission").child(day1).addValueEventListener(new ValueEventListener() {
//    @Override
//    public void onDataChange(DataSnapshot dataSnapshot) {
//        itemList.clear();
//        int sum = 0;
//        int done = 0;
//        if (dataSnapshot.hasChildren()) {
//            for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
//                if (itemSnapshot.getKey() != null) {
//                    String key = itemSnapshot.getKey();
//                    String content = itemSnapshot.child("content").getValue(String.class);
//                    String status = itemSnapshot.child("status").getValue(String.class);
//                    if (status != null && status.equals("Đã Thực Hiện Được")) {
//                        done++;
//                    }
//                    itemList.add(new ItemList(content, status, key));
//                    sum++;
//                }
//
//            }
//        }
//        dbRef.child(uid).child("mission").child(day1).child("sum").setValue(sum);
//        dbRef.child(uid).child("mission").child(day1).child("done").setValue(done);
//        adapter.updateList(itemList);
//        listItemRecyclerView.setAdapter(adapter);
//    }
//
//    @Override
//    public void onCancelled(DatabaseError databaseError) {
//        // Handle possible errors.
//    }
//});
}

    private void addEvents(View view) {
       btnAdd.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                showDialog();
           }
       });
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_center_add_taget);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        if (Gravity.BOTTOM == Gravity.BOTTOM) {
            dialog.setCancelable(true);
        } else {
            dialog.setCancelable(false);
        }
        EditText tvContent = dialog.findViewById(R.id.tvContent);
        Button btnClose = dialog.findViewById(R.id.btnClose);
        Button btnAdd = dialog.findViewById(R.id.btnAdd);
        btnClose.setOnClickListener(v -> {
            System.out.println("Đóng dialog");
            dialog.dismiss();
        });
        btnAdd.setOnClickListener(v -> {
            String content = tvContent.getText().toString();
            if (content.isEmpty()) {
                tvContent.setError("Không được để trống");
                return;
            }
            dialog.dismiss();
            addItemToFirebase(content);
            Toast.makeText(getActivity(), "Thêm thành công", Toast.LENGTH_SHORT).show();
        });

        dialog.show();
    }

    private void addItemToFirebase(String task) {
        Locale vietnam = new Locale("vi", "VN");
        SimpleDateFormat day = new SimpleDateFormat("ddMMyyyy", vietnam);
        String day1 = day.format(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss", vietnam);
        String currentDateTimeString = sdf.format(new Date());

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
//kiểm tra xem có dbRef.child(uid).child("mission").child(day1).child("sum").setValue(sum); chưa

        ItemList item = new ItemList(task, "Chưa Thực Hiện Được", currentDateTimeString);
        dbRef.child(uid).child("mission").child(day1).child(currentDateTimeString).setValue(item);
    }
    public static void updateDataSum(String accountId, String day, long newSum) {
        FirebaseFirestore db3 = FirebaseFirestore.getInstance();
        DocumentReference docRef = db3.collection(accountId).document(day);

        Map<String, Object> updates = new HashMap<>();
        updates.put("sum", newSum);

        docRef.update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }
}