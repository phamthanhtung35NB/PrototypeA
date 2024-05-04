package com.example.prototypea;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    static private List<ItemList> itemList = new ArrayList<>();
    private FirebaseAuth mAuth;
    Adapter adapter;
    RecyclerView listItemRecyclerView;
    // Tạo một Dialog mới
    Dialog dialog;
    FloatingActionButton fab;
    DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private FrameLayout fragmentContainer;
    static boolean isCheckQR = false;
    CircleImageView imageLogoAvatar;
    TextView textUsername;
    TextView textEmail;
    String profilePic = "";
    String accountId = "";
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        init(savedInstanceState);
        addEvents();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    void init(Bundle savedInstanceState) {
        fab = findViewById(R.id.fab);
        //cái ảnh nút quét mã QR
//        fab.setImageResource(R.drawable.qrcode);
        drawerLayout = findViewById(R.id.main);
        fragmentContainer = findViewById(R.id.fragment_container);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);
        navigationView = findViewById(R.id.navigationView);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.navHome);
        }
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();



        //thanh bên
        NavigationView navigationView = findViewById(R.id.navigationView);
        View headerView = navigationView.getHeaderView(0);
        imageLogoAvatar = headerView.findViewById(R.id.imageLogoAvatar);
        textUsername = headerView.findViewById(R.id.textUsername);
        textEmail = headerView.findViewById(R.id.textEmail);
//        SharedPreferences sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);
//        String username = sharedPreferences.getString("username", "");
//        String email = sharedPreferences.getString("email", "");
//        accountId = sharedPreferences.getString("uid", "");
//        profilePic= sharedPreferences.getString("profilePic", "");
//        textUsername.setText(username);
//        textEmail.setText(email);
        if (!profilePic.isEmpty()&&profilePic!=null&&profilePic.length()>0){
            Picasso.get().load(profilePic).into(imageLogoAvatar);
        }


        replaceFragment(new HomeFragment(), true);
        Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
    }
    void addEvents(){
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NotNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navHome){
                    System.out.println("navHome");
                    replaceFragment(new HomeFragment(), false);
                    Toast.makeText(MainActivity.this, "navHome", Toast.LENGTH_SHORT).show();
                }else if(itemId==R.id.navTaget){
                    replaceFragment(new TagetFragment(), false);
                    Toast.makeText(MainActivity.this, "navMessage", Toast.LENGTH_SHORT).show();
                }
                else if (itemId == R.id.navMXH){
                    Toast.makeText(MainActivity.this, "navQR", Toast.LENGTH_SHORT).show();

                }
                return true;
            }
        });
        //thanh bên
        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_header_home) {
                Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
            } else if (itemId == R.id.nav_header_settings) {
                Toast.makeText(MainActivity.this, "Settings", Toast.LENGTH_SHORT).show();

            } else if (itemId == R.id.nav_header_share) {
                Toast.makeText(MainActivity.this, "Share", Toast.LENGTH_SHORT).show();

            } else if (itemId == R.id.nav_header_feedback) {
                Toast.makeText(MainActivity.this, "Feedback", Toast.LENGTH_SHORT).show();
                //mở link liên kết github
                Intent intent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse("https://github.com/phamthanhtung35NB/Restaurant-manager"));
                startActivity(intent);
            } else if(itemId == R.id.nav_header_logout){
                Toast.makeText(MainActivity.this, "Logout", Toast.LENGTH_SHORT).show();

                //xóa dữ liệu đăng nhập
//                SharedPreferences sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.clear();
//                editor.apply();
                //chuyển màn hình login
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);

            }
            return true;
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new TagetFragment(), false);

            }
        });
        imageLogoAvatar.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        });
    }


    void replaceFragment(Fragment fragment, boolean isAppInit){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (!isAppInit){
            fragmentTransaction.replace(R.id.fragment_container, fragment);
        }else {
            fragmentTransaction.add(R.id.fragment_container, fragment);
        }

        fragmentTransaction.commit();
    }
//    void init() {
//    //set adapter
//    adapter = new Adapter(itemList, this);
//    listItemRecyclerView = findViewById(R.id.listItemRecyclerView);
//    listItemRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//    listItemRecyclerView.setAdapter(adapter);
//
//    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
//    String uid = mAuth.getCurrentUser().getUid();
//    dbRef.child(uid).child("mission").addValueEventListener(new ValueEventListener() {
//    @Override
//    public void onDataChange(DataSnapshot dataSnapshot) {
//        itemList.clear();
//        for (DataSnapshot itemSnapshot: dataSnapshot.getChildren()) {
//            String key = itemSnapshot.getKey();
//            String content = itemSnapshot.child("content").getValue(String.class);
//            String status = itemSnapshot.child("status").getValue(String.class);
//            itemList.add(new ItemList(content, status, key));
//        }
//        adapter.updateList(itemList);
//        listItemRecyclerView.setAdapter(adapter);
//    }
//
//    @Override
//    public void onCancelled(DatabaseError databaseError) {
//        // Handle possible errors.
//    }
//});
//}
//    void addEvent() {
//        findViewById(R.id.button).setOnClickListener(v -> showDialog());
//    }
//    private void showDialog() {
//        final EditText taskEditText = new EditText(this);
//        AlertDialog dialog = new AlertDialog.Builder(this)
//                .setTitle("Add a new task")
//                .setMessage("What do you want to do next?")
//                .setView(taskEditText)
//                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        String task = String.valueOf(taskEditText.getText());
//                        addItemToFirebase(task);
//                    }
//                })
//                .setNegativeButton("Cancel", null)
//                .create();
//        dialog.show();
//    }
//
//    private void addItemToFirebase(String task) {
//        Locale vietnam = new Locale("vi", "VN");
//        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss", vietnam);
//        String currentDateTimeString = sdf.format(new Date());
//
//        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
//        String uid = mAuth.getCurrentUser().getUid();
//        ItemList item = new ItemList(task, "Không Tốt", currentDateTimeString);
//        dbRef.child(uid).child("mission").child(currentDateTimeString).setValue(item);
//    }
}