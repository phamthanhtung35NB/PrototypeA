package com.example.prototypea;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class GetData {
    public static long getTotalSumWeekArray[] = new long[7];
    public static int i = 0;
    public static void readAndSaveSumWeek(String accountId) {
        FirebaseFirestore db3 = FirebaseFirestore.getInstance();
        for (i = 0; i < 7; i++) {
            String day = getDayOfWeek(i);
            final int finalI = i;
            System.out.println(day);
            DocumentReference docRef = db3.collection(accountId).document(day);
            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        Log.w(TAG, "Listen failed.", error);
                        return;
                    }
                    if (value != null && value.exists()) {
                        long done = value.getLong("done");
                        long sum =value.getLong("sum");
                        // Tính % done so vs sum làm tròn 1 số sau dấu phẩy
                        long donePercent   = (long) Math.round((done*100.0f/sum)*100)/100;
                        getTotalSumWeekArray[finalI] = donePercent;
                        System.out.println("Sum: " + donePercent);
                    } else {
                        System.out.print("No such document");
                        // Tạo một bản ghi mới với giá trị mặc định
                        Map<String, Object> data = new HashMap<>();
                        data.put("done", 0);
                        data.put("sum", 0);
                        docRef.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                getTotalSumWeekArray[finalI] = 0;
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });
                    }
                }
            });
        }
}
    public static String getDayOfWeek(int daysAgo){
        long currentTime = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(daysAgo);
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy", Locale.getDefault());
        String timestamp = sdf.format(new Date(currentTime));
        return timestamp;
    }
}
