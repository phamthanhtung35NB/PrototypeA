package com.example.prototypea;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    private List<ItemList> itemLists;
    private final Context context;

    public Adapter(List<ItemList> messagesList, Context context) {
        this.itemLists = messagesList;
        this.context = context;
        System.out.println("MessagesAdapter.MessagesAdapter");
    }

    @NonNull
    @Override
    public Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tasks, null));
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.MyViewHolder holder, int position) {
    ItemList item = itemLists.get(position);
    holder.textContent.setText(item.getContent());
    holder.radio1.setChecked(false);
    holder.radio2.setChecked(false);
    holder.radio3.setChecked(false);
    if (item.getStatus().equals("Tốt")) {
        holder.radio1.setChecked(true);
    } else if (item.getStatus().equals("Tạm ổn")) {
        holder.radio2.setChecked(true);
    } else if (item.getStatus().equals("Không Tốt")) {
        holder.radio3.setChecked(true);
    }

    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String key = item.getKey();

    holder.radio1.setOnClickListener(v -> {
//        item.setStatus("Tốt");
        dbRef.child(uid).child("mission").child(key).child("status").setValue("Tốt");
//        notifyDataSetChanged();
    });
    holder.radio2.setOnClickListener(v -> {
//        item.setStatus("Tạm ổn");
        dbRef.child(uid).child("mission").child(key).child("status").setValue("Tạm ổn");
//        notifyDataSetChanged();
    });
    holder.radio3.setOnClickListener(v -> {
//        item.setStatus("Không Tốt");
        dbRef.child(uid).child("mission").child(key).child("status").setValue("Không Tốt");
//        notifyDataSetChanged();
    });
}

    public void updateList(List<ItemList> list){
        this.itemLists=list;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return itemLists.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textContent;
        private RadioButton radio1, radio2, radio3;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textContent = itemView.findViewById(R.id.textContent);
            radio1 = itemView.findViewById(R.id.radio1);
            radio2 = itemView.findViewById(R.id.radio2);
            radio3 = itemView.findViewById(R.id.radio3);
        }
    }
}