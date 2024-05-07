package com.example.prototypea.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterPost extends RecyclerView.Adapter<AdapterPost.MyViewHolder> {
    private List<Post> itemListsPost;
    private final Context context;

    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

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
            String timeString = item.getTime();
            String formattedTimeString = timeString.substring(0, 2) + ":" + timeString.substring(2, 4)+":" + timeString.substring(4);
            holder.myTxtTime.setText(formattedTimeString);
            String dateString = item.getDate();
            String formattedDateString = dateString.substring(0, 2) + "/" + dateString.substring(2, 4) + "/" + dateString.substring(4);
            holder.myTxtDate.setText(formattedDateString);
            holder.myTxtStatus.setText(item.getStatus());
            String status2 = item.getTagetCount() + "/" + item.getTagetSum()+ " mục tiêu hoàn thành" ;
            holder.myTxtStatus2.setText(status2);
            holder.myTxtContent.setText(item.getContent());
//pie chart
            showPieChart(holder.myPieChart, item.getTagetCount(), item.getTagetSum());


            holder.myBtnLike.setText(String.valueOf(item.getLikeCount()));

            String imageString = item.getImage();
            if (imageString!=null&&imageString.length()>0) {
                Picasso.get().load(imageString).into(holder.myImageViewAvatar);
            }else {
                holder.myImageViewAvatar.setImageResource(R.drawable.aklogo);
            }

            holder.myBtnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //get likeCount
                    int likeCountTmp = item.getLikeCount();
                    likeCountTmp++;
                    //update likeCount
                    dbRef.child("post").child(item.getKey()).child("likeCount").setValue(likeCountTmp);
                }
            });


        }else {
            holder.layout.setVisibility(View.VISIBLE);
            holder.myLayout.setVisibility(View.GONE);

            holder.txtName.setText(item.getName());
            String timeString = item.getTime();
            String formattedTimeString = timeString.substring(0, 2) + ":" + timeString.substring(2, 4)+":" + timeString.substring(4);
            holder.txtTime.setText(formattedTimeString);
            String dateString = item.getDate();
            String formattedDateString = dateString.substring(0, 2) + "/" + dateString.substring(2, 4) + "/" + dateString.substring(4);
            holder.txtDate.setText(formattedDateString);
            holder.txtStatus.setText(item.getStatus());
            String status2 = item.getTagetCount() + "/" + item.getTagetSum()+ " mục tiêu hoàn thành" ;
            holder.txtStatus2.setText(status2);
            holder.txtContent.setText(item.getContent());
            holder.btnLike.setText(String.valueOf(item.getLikeCount()));


            showPieChart(holder.pieChart, item.getTagetCount(), item.getTagetSum());
            String imageString = item.getImage();
            if (imageString!=null&&imageString.length()>0) {
                Picasso.get().load(imageString).into(holder.myImageViewAvatar);
            }else {
                holder.imageViewAvatar.setImageResource(R.drawable.aklogo);
            }

            holder.btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int likeCountTmp = item.getLikeCount();
                    likeCountTmp++;
                    //update likeCount
                    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
                    dbRef.child("post").child(item.getKey()).child("likeCount").setValue(likeCountTmp);

                }
            });

        }

    }
    void showPieChart(PieChart pieChart, int done, int sum){
        //tính % done, làm tròn 2 chữ số
        Float donePercent = (float) Math.round((done*100.0f/sum)*100)/100;

        //tính % undone
        Float undonePercent = 100 - donePercent;
        System.out.println("donePercent: " + donePercent);
        System.out.println("undonePercent: " + undonePercent);

        ArrayList<PieEntry> visitors = new ArrayList<>();
        visitors.add(new PieEntry(  donePercent, ""));
        visitors.add(new PieEntry(undonePercent,""));

        PieDataSet pieDataSet = new PieDataSet(visitors,"Biểu Đồ Mục Tiêu");
        //set color done is green, undone is red
//                pieDataSet.setColors (new int[] {Color. GREEN, Color. YELLOW}) ;
        pieDataSet.setColors ( ColorTemplate.COLORFUL_COLORS) ;

// Create a custom ValueFormatter
        pieDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return super.getFormattedValue(value) + "%";
            }
        });
        pieDataSet.setValueTextSize( 12f) ;
        pieDataSet.setSliceSpace( 7f) ;
        //set color text
        pieDataSet.setValueTextColor(Color.WHITE);

        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false) ;
        pieChart.setCenterText( "Trạng thái" ) ;
        pieChart.setCenterTextSize( 10f) ;
        pieChart.animate();
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
        private Button btnLike,myBtnLike;

        private CircleImageView imageViewAvatar,myImageViewAvatar;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.layout);
            imageViewAvatar = itemView.findViewById(R.id.imageViewAvatar);
            txtName = itemView.findViewById(R.id.txtName);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtStatus2 = itemView.findViewById(R.id.txtStatus2);
            txtContent = itemView.findViewById(R.id.txtContent);
            pieChart = itemView.findViewById(R.id.pieChart);
            btnLike = itemView.findViewById(R.id.btnLike);

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
        }

    }
}