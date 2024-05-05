package com.example.prototypea;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.prototypea.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment{
    String  uid, day1;
    DatabaseReference dbRef;
    int sum = 0;
    int done = 0;
    PieChart pieChart;
    ArrayList<PieEntry> visitors = new ArrayList<>();
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        init(view);
        addEvents(view);
        return view;
    }

    private void addEvents(View view) {
    }

    private void init(View view){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("dataLogin", getActivity().MODE_PRIVATE);
        uid = sharedPreferences.getString("uid", "");
        Locale vietnam = new Locale("vi", "VN");
        SimpleDateFormat day = new SimpleDateFormat("ddMMyyyy", vietnam);
        day1 = day.format(new Date());
        dbRef = FirebaseDatabase.getInstance().getReference();

        pieChart = view.findViewById(R.id.pieChart);


        getDatabaseTagetDay();
    }
    private void getDatabaseTagetDay() {
        // Read from the database done and sum

        dbRef.child(uid).child("mission").child(day1).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sum = 0;
                done = 0;
                Integer doneINT = dataSnapshot.child("done").getValue(Integer.class);
                Integer sumINT = dataSnapshot.child("sum").getValue(Integer.class);

                if (doneINT != null) {
                    done = doneINT;
                }
                if (sumINT != null) {
                    sum = sumINT;
                }

                System.out.println("done: " + done);
                System.out.println("sum: " + sum);
                //tính % done
                float donePercent = (float) ((float)done / (float)sum)*100;
                //tính % undone
                float undonePercent = 100 - donePercent;
                System.out.println("donePercent: " + donePercent);
                System.out.println("undonePercent: " + undonePercent);
                visitors.add(new PieEntry(  donePercent, "Đã Thực Hiện Được"));
                visitors.add(new PieEntry(undonePercent,"Chưa Thực Hiện Được"));

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
                pieDataSet.setValueTextSize( 16f) ;
                pieDataSet.setSliceSpace( 5f) ;
                //set color text
                pieDataSet.setValueTextColor(Color.BLACK);

                PieData pieData = new PieData(pieDataSet);

                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false) ;
                pieChart.setCenterText( "Mục Tiêu" ) ;
                pieChart.setCenterTextSize( 20f) ;
                pieChart.animate();
                // Lấy sự kiện khi click vào biểu đồ
pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
    @Override
    public void onValueSelected(Entry e, Highlight h) {
        // e là đối tượng Entry được chọn
        float value = e.getY();
        String label = ((PieEntry) e).getLabel();
//        if (value == donePercent){
//            e.setY(done);
//        }else {
//            e.setY(sum-done);
//        }
        // h là đối tượng Highlight mô tả thông tin chi tiết về lựa chọn
        //lấy giá trị value và label đối tượng Highlight
        float highlightValue = h.getY(); // Lấy giá trị từ Highlight
        int dataIndex = h.getDataIndex(); // Lấy chỉ số dữ liệu từ Highlight


Toast.makeText(getActivity(), "Selected H: " + highlightValue + ", data index: " + dataIndex, Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), "Selected: " + label + ", value: " + value, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected() {
        // Thực hiện hành động khi không có giá trị nào được chọn
    }
});
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }
}