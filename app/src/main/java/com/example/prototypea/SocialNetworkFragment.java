package com.example.prototypea;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.prototypea.Adapter.AdapterPost;
import com.example.prototypea.Adapter.AdapterTaget;
import com.example.prototypea.Class.ItemList;
import com.example.prototypea.Class.Post;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SocialNetworkFragment extends Fragment {
    private DatabaseReference dbRef;
    static private List<Post> postList = new ArrayList<>();
    AdapterPost adapterPost = new AdapterPost(postList, getActivity());
    RecyclerView postsRecyclerView;
    ImageButton imageButtonExit;
    public static String uid;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_social_network, container, false);
        init(view);
        addEvents(view);
        getPosts();
        return view;
    }

    private void init(View view){
        imageButtonExit = view.findViewById(R.id.imageButtonExit);
        postsRecyclerView = view.findViewById(R.id.postsRecyclerView);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        postsRecyclerView.setAdapter(adapterPost) ;
        //hiêểu thị thông báo cuối
        postsRecyclerView.scrollToPosition(postList.size() - 1);
        Toast.makeText(getActivity(), "navMessage", Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("dataLogin", getActivity().MODE_PRIVATE);
        uid = sharedPreferences.getString("uid", "");

    }
    private void addEvents(View view){
        imageButtonExit.setOnClickListener(v -> {
//            quay về màn hình chính
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        });
    }
    private void getPosts(){
        // Lấy một tham chiếu đến cơ sở dữ liệu
        dbRef = FirebaseDatabase.getInstance().getReference();

        // Lắng nghe sự kiện thay đổi dữ liệu
        dbRef.child("post").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // Lấy key (day1)
                    String key = postSnapshot.getKey();

                    // Lấy dữ liệu từng biến một
                    String name = postSnapshot.child("name").getValue(String.class);
                    String uid = postSnapshot.child("uid").getValue(String.class);
                    String status = postSnapshot.child("status").getValue(String.class);
                    String content = postSnapshot.child("content").getValue(String.class);
                    String image = postSnapshot.child("image").getValue(String.class);
                    String date = postSnapshot.child("date").getValue(String.class);
                    String time = postSnapshot.child("time").getValue(String.class);
                    Integer likeCount = postSnapshot.child("likeCount").getValue(Integer.class);
                    Integer tagetCount = postSnapshot.child("tagetCount").getValue(Integer.class);
                    Integer tagetSum = postSnapshot.child("tagetSum").getValue(Integer.class);

                    Post post = new Post(key, name, uid, status, content, image, date, time, likeCount, tagetCount, tagetSum);
                    System.out.println("name: " + name );
                    postList.add(post);
                }
                adapterPost.updateList(postList);
                postsRecyclerView.setAdapter(adapterPost);
                //hiêểu thị thông báo cuối
                postsRecyclerView.scrollToPosition(postList.size() - 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi
                System.out.println("Failed to read value: " + databaseError.toException());
            }
        });
    }


}