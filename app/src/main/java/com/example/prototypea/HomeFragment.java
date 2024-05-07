package com.example.prototypea;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prototypea.Class.Post;
import com.example.prototypea.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment{
    private List<String> xValues ;
    String  uid, day1, email, fullName,image;
    float donePercent;
    float undonePercent;

    DatabaseReference dbRef;
    int sum = 0;
    int done = 0;
    PieChart pieChart;
    BarChart barChartTK;
    Button btnShare;
    ArrayList<PieEntry> visitors = new ArrayList<>();


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        init(view);
        addEvents(view);
        return view;
    }

    private void addEvents(View view) {
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogShare();

            }
            });
    }

    private void init(View view){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("dataLogin", getActivity().MODE_PRIVATE);
        uid = sharedPreferences.getString("uid", "");
        email = sharedPreferences.getString("email", "");
        fullName = sharedPreferences.getString("fullName", "");
        image = sharedPreferences.getString("image", "");
        System.out.println("uid: " + uid+ "email: " + email + "fullName: " + fullName + "image: " + image);
        System.out.println("++++++++++");
        Locale vietnam = new Locale("vi", "VN");
        SimpleDateFormat day = new SimpleDateFormat("ddMMyyyy", vietnam);
        day1 = day.format(new Date());
        dbRef = FirebaseDatabase.getInstance().getReference();

        pieChart = view.findViewById(R.id.pieChart);
        barChartTK = view.findViewById(R.id.barChart);
        btnShare = view.findViewById(R.id.btnShare);

        getDatabaseTagetDay();
        showBarChart();
    }
    private void showDialogShare() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_center_share_taget);

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
        CircleImageView imageViewAvatarDialog = dialog.findViewById(R.id.imageViewAvatarDialog);
        TextView txtNameDialog = dialog.findViewById(R.id.txtNameDialog);
        TextView txtTimeDialog = dialog.findViewById(R.id.txtTimeDialog);
        TextView txtDateDialog = dialog.findViewById(R.id.txtDateDialog);
        TextView txtStatusDialog = dialog.findViewById(R.id.txtStatusDialog);
        TextView txtStatusDialog2 = dialog.findViewById(R.id.txtStatusDialog2);
        EditText txtContentDialog = dialog.findViewById(R.id.txtContentDialog);
        PieChart pieChartDialog = dialog.findViewById(R.id.pieChartDialog);
        Button btnClose = dialog.findViewById(R.id.btnClose);
        Button btnAdd = dialog.findViewById(R.id.btnAdd);

        Locale vietnam = new Locale("vi", "VN");
        SimpleDateFormat day = new SimpleDateFormat("ddMMyyyy", vietnam);
        SimpleDateFormat time = new SimpleDateFormat("HHmmss", vietnam);
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss", vietnam);

        String dayFormat = day.format(new Date());
        String timeFormat = time.format(new Date());
        String currentDateTimeString = sdf.format(new Date());

        String status = "Đã Hoàn thành "+ donePercent+"% mục tiêu";


        //set data
        if (image!=null&&image.length()>0) {
            Picasso.get().load(image).into(imageViewAvatarDialog);
        }else {
            imageViewAvatarDialog.setImageResource(R.drawable.aklogo);
        }
        txtNameDialog.setText(fullName);
        String formattedTimeString = timeFormat.substring(0, 2) + ":" + timeFormat.substring(2, 4)+":" + timeFormat.substring(4);
        txtTimeDialog.setText(formattedTimeString);
        String formattedDateString = dayFormat.substring(0, 2) + "/" + dayFormat.substring(2, 4) + "/" + dayFormat.substring(4);
        txtDateDialog.setText(formattedDateString);
        txtStatusDialog.setText(status);
        String status2 = done + "/" + sum+ " mục tiêu hoàn thành" ;
        txtStatusDialog2.setText(status2);
//        showPieChart(pieChartDialog, done, sum);

        btnClose.setOnClickListener(v -> {
            System.out.println("Đóng dialog");
            dialog.dismiss();
        });
        btnAdd.setOnClickListener(v -> {
            if (txtContentDialog.getText().toString().isEmpty()) {
                txtContentDialog.setError("Hãy nhập nội dung");
                return;
            }
            // Tạo một đối tượng Post
            Post post = new Post(currentDateTimeString, fullName, uid, status, txtContentDialog.getText().toString(), image, dayFormat, timeFormat, 0, done, sum);

            // Lấy một tham chiếu đến cơ sở dữ liệu
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

            // Đẩy đối tượng Post lên Firebase
            dbRef.child("post").child(currentDateTimeString).setValue(post);
            dialog.dismiss();
            Toast.makeText(getActivity(), "Thêm thành công", Toast.LENGTH_SHORT).show();
        });

        dialog.show();
    }

    //get data from database
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
                donePercent = (float) Math.round((done*100.0f/sum)*100)/100;
                //tính % undone
                undonePercent = 100 - donePercent;
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

    public String formatDay(String day) {
//        String[] parts = day.split("_");
//        return parts[0] + "/" + parts[1];
        return day.substring(0, 2) + "/" + day.substring(2, 4);
    }
    void showBarChart() {
// Tắt nhãn trên trục phải
        barChartTK.getAxisRight().setDrawLabels(false);

        // Lấy các ngày trong tuần và định dạng lại
        String dayTam0 = formatDay(GetData.getDayOfWeek(6));
        String dayTam1 = formatDay(GetData.getDayOfWeek(5));
        String dayTam2 = formatDay(GetData.getDayOfWeek(4));
        String dayTam3 = formatDay(GetData.getDayOfWeek(3));
        String dayTam4 = formatDay(GetData.getDayOfWeek(2));
        String dayTam5 = formatDay(GetData.getDayOfWeek(1));
        String dayTam6 = formatDay(GetData.getDayOfWeek(0));
        System.out.println("dayTam0: " + dayTam0 + "dayTam1: " + dayTam1 + "dayTam2: " + dayTam2 + "dayTam3: " + dayTam3 + "dayTam4: " + dayTam4 + "dayTam5: " + dayTam5 + "dayTam6: " + dayTam6);

        // Thêm các ngày vào danh sách xValues
        xValues = Arrays.asList(dayTam0, dayTam1, dayTam2, dayTam3, dayTam4, dayTam5, dayTam6);

        // Khởi tạo danh sách các mục nhập cho biểu đồ
        ArrayList<BarEntry> entries = new ArrayList<>();
        long dayInWeed[]=GetData.getTotalSumWeekArray;
        for  (int i = 0; i<7; i++){
            // Thêm mục nhập mới với giá trị từ mảng dayInWeed
            entries.add(new BarEntry(i, (float) dayInWeed[6-i]));
            System.out.println("dayInWeed: " + dayInWeed[6-i]);
        }

        // Cấu hình trục Y
        YAxis yAxis = barChartTK.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(100);
        yAxis.setAxisLineWidth(1f);
        yAxis.setAxisLineColor(Color.BLACK);
        yAxis.setTextColor(Color.BLACK);
        yAxis.setLabelCount(10);

        // Tạo tập dữ liệu mới và cấu hình màu sắc
        BarDataSet dataSet = new BarDataSet(entries, "VNĐ");
        dataSet.setColor(Color.GREEN); // Set color for bars
        dataSet.setDrawValues(false); // Hide bar values

        // Tạo dữ liệu cho biểu đồ và thiết lập dữ liệu
        BarData barData = new BarData(dataSet);
        barChartTK.setData(barData);
        barChartTK.getBarData().setBarWidth(0.5f);

        // Tắt mô tả và chú thích
        barChartTK.getDescription().setEnabled(false);
        barChartTK.getLegend().setEnabled(false); // Hide legend
        barChartTK.invalidate();

        // Cấu hình trục X
        barChartTK.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xValues));
        barChartTK.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChartTK.getXAxis().setTextColor(Color.BLACK);
        barChartTK.getXAxis().setLabelRotationAngle(0);
        barChartTK.getXAxis().setAxisLineWidth(2f);
        barChartTK.getXAxis().setAxisLineColor(Color.BLACK);
        barChartTK.getXAxis().setLabelCount(7);
        barChartTK.getXAxis().setTextSize(10f);
        barChartTK.getAxisLeft().setAxisLineWidth(2f);
        barChartTK.getAxisLeft().setAxisLineColor(Color.BLACK);

        //tắt lưới trên trục Y
        barChartTK.getAxisLeft().setDrawGridLines(false);
        //tắt lưới trên trục X
        barChartTK.getXAxis().setDrawGridLines(false);
        //tắt tất cả các lưới
        barChartTK.setDrawGridBackground(false);


        // Cấu hình độ nhỏ nhất giữa các giá trị trên trục X
        barChartTK.getXAxis().setGranularity(1f);
        barChartTK.getXAxis().setGranularityEnabled(true);

        // Thêm hiệu ứng hoạt hình cho biểu đồ
        barChartTK.animateXY(1500, 4000); // Add animation
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

        PieDataSet pieDataSet = new PieDataSet(visitors,"");
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

}