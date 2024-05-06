package com.example.prototypea.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prototypea.Class.ItemList;
import com.example.prototypea.Class.Post;
import com.example.prototypea.R;
import com.example.prototypea.SocialNetworkFragment;
import com.github.mikephil.charting.charts.PieChart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterPost extends RecyclerView.Adapter<AdapterPost.MyViewHolder> {
    private List<Post> itemListsPost;
    private final Context context;

    public AdapterPost(List<Post> messagesList, Context context) {
        this.itemListsPost = messagesList;
        this.context = context;
        System.out.println("MessagesAdapter.MessagesAdapter");
    }

    @NonNull
    @Override
    public AdapterPost.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, null));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPost.MyViewHolder holder, int position) {

        String myUid = SocialNetworkFragment.uid;
        Post item = itemListsPost.get(position);
        if (myUid.equals(item.getUid())) {
            System.out.println("của mình");
            System.out.println("-------------------------");
            holder.myLayout.setVisibility(View.VISIBLE);
            holder.layout.setVisibility(View.GONE);

            holder.myTxtName.setText(item.getName());
            holder.myTxtTime.setText(item.getTime());
            holder.myTxtDate.setText(item.getDate());
            holder.myTxtStatus.setText(item.getStatus());
            holder.myTxtStatus2.setText("test");
            holder.myTxtContent.setText(item.getContent());

            holder.myTextViewLike.setText(String.valueOf(item.getLikeCount()));
            holder.button1.setText(String.valueOf(item.getLikeCount())+"Like");

            String imageString = item.getImage();
            if (imageString!=null&&imageString.length()>0) {
                Picasso.get().load(imageString).into(holder.myImageViewAvatar);
            }else {
                holder.myImageViewAvatar.setImageResource(R.drawable.aklogo);
            }

            holder.myBtnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


        }else {
            holder.layout.setVisibility(View.VISIBLE);
            holder.myLayout.setVisibility(View.GONE);

            holder.txtName.setText(item.getName());
            holder.txtTime.setText(item.getTime());
            holder.txtDate.setText(item.getDate());
            holder.txtStatus.setText(item.getStatus());
            holder.txtStatus2.setText("test");
            holder.txtContent.setText(item.getContent());

        }

    }

    public void updateList(List<Post> list) {

        this.itemListsPost = list;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return itemListsPost.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout layout,myLayout;
        private TextView txtName,myTxtName;
        private TextView txtTime,myTxtTime;
        private TextView txtDate,myTxtDate;
        private TextView txtStatus,myTxtStatus;
        private TextView txtStatus2,myTxtStatus2;
        private TextView txtContent,myTxtContent;
        private PieChart pieChart,myPieChart;
        private ImageView btnLike,myBtnLike;
        private TextView textViewLike,myTextViewLike;
        Button button1;

        private CircleImageView imageViewAvatar,myImageViewAvatar;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            button1 = itemView.findViewById(R.id.button1);
            layout = itemView.findViewById(R.id.layout);
            imageViewAvatar = itemView.findViewById(R.id.imageViewAvatar);
            txtName = itemView.findViewById(R.id.txtName);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtStatus2 = itemView.findViewById(R.id.txtStatus2);
            txtContent = itemView.findViewById(R.id.txtContent);
            pieChart = itemView.findViewById(R.id.pieChart);

            textViewLike = itemView.findViewById(R.id.textViewLike);
            myLayout = itemView.findViewById(R.id.myLayout);
            myImageViewAvatar = itemView.findViewById(R.id.myImageViewAvatar);
            myTxtName = itemView.findViewById(R.id.myTxtName);
            myTxtTime = itemView.findViewById(R.id.myTxtTime);
            myTxtDate = itemView.findViewById(R.id.myTxtDate);
            myTxtStatus = itemView.findViewById(R.id.myTxtStatus);
            myTxtStatus2 = itemView.findViewById(R.id.myTxtStatus2);
            myTxtContent = itemView.findViewById(R.id.myTxtContent);
            myPieChart = itemView.findViewById(R.id.myPieChart);
            myBtnLike = itemView.findViewById(R.id.myBtnLike);
            myTextViewLike = itemView.findViewById(R.id.myTextViewLike);
        }

    }
}