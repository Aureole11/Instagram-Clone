package com.example.aprilandroid.Activities;

//import android.Manifest;
import android.content.Context;
import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.net.Uri;
import android.content.SharedPreferences;
import android.os.Bundle;
//import android.os.Handler;
//import android.provider.MediaStore;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.EditText;
import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;

//import androidx.activity.EdgeToEdge;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.aprilandroid.Application.Utils;
import com.example.aprilandroid.Fragments.HomeFragment;
import com.example.aprilandroid.Fragments.ProfileFragment;
import com.example.aprilandroid.Fragments.ReelsFragment;
import com.example.aprilandroid.Fragments.SearchFragment;
import com.example.aprilandroid.Model.Users;
import com.example.aprilandroid.R;
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.google.android.material.card.MaterialCardView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.checkbox.MaterialCheckBox;
//import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

//import com.example.aprilandroid.Adapter.RecyclerViewAdapter;
//import com.example.aprilandroid.Model.Student;


public class MainActivity extends AppCompatActivity {

//    List<Student> studentList = new ArrayList<>();
//    int imgVal = 1;

    //    private static final int REQUEST_IMAGE_CAPTURE = 123;
//    ImageView image;
//    private static final int REQUEST_SEND_SMS = 123;

//    BottomNavigationView bottom_navigation;
    Fragment selectedFragment = null;
    MaterialCheckBox home,search, reels;
    ImageView add;
    MaterialCardView profile;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = auth.getCurrentUser();
    CircleImageView profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        Utils.statusBar(this, R.color.black);
        /*bottom_navigation = findViewById(R.id.bottom_navigation);
        bottom_navigation.setOnItemSelectedListener(item -> {
                if (item.getItemId() == R.id.home){
                    selectedFragment = new HomeFragment();
                } else if (item.getItemId() == R.id.search) {
                    selectedFragment = new SearchFragment();
                } else if (item.getItemId() == R.id.add) {

                } else if (item.getItemId() == R.id.reels) {
                    selectedFragment = new ReelsFragment();
                } else if (item.getItemId() == R.id.profile) {
                    selectedFragment = new ProfileFragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                return true;

        });*/
        home = findViewById(R.id.home);
        search = findViewById(R.id.search);
        reels = findViewById(R.id.reels);
        add = findViewById(R.id.add);
        profile = findViewById(R.id.profile);
        profile_image = findViewById(R.id.profile_image);
        home.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                selectedFragment = new HomeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                search.setChecked(false);
                reels.setChecked(false);
                search.setEnabled(true);
                reels.setEnabled(true);
                home.setEnabled(false);
                home.setChecked(true);
                profile.setStrokeWidth(0);
            }else {
                home.setEnabled(true);
            }
        });
        search.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                selectedFragment = new SearchFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                home.setChecked(false);
                reels.setChecked(false);
                home.setEnabled(true);
                reels.setEnabled(true);
                search.setEnabled(false);
                search.setChecked(true);
                profile.setStrokeWidth(0);
            }else {
                search.setEnabled(true);
            }
        });
            reels.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if(isChecked){
                    selectedFragment = new ReelsFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    home.setChecked(false);
                    search.setChecked(false);
                    home.setEnabled(true);
                    search.setEnabled(true);
                    reels.setEnabled(false);
                    reels.setChecked(true);
                    profile.setStrokeWidth(0);
                }else {
                    reels.setEnabled(true);
                }
        });
            profile.setOnClickListener(v -> {
                SharedPreferences preferences = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("profileId", FirebaseAuth.getInstance().getCurrentUser().getUid());
                editor.apply();
                selectedFragment =new ProfileFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    home.setChecked(false);
                    search.setChecked(false);
                    reels.setChecked(false);
                    home.setEnabled(true);
                    search.setEnabled(true);
                    reels.setEnabled(true);
                    profile.setStrokeWidth(5);
                    profile.setStrokeColor(ContextCompat.getColor(this, R.color.black));;
            });
        if (selectedFragment == null){
            selectedFragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(currentUser == null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }else {
            getCurrentUserData();
        }
    }

    private void getCurrentUserData() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);
                if (user.getImageUrl().equals("default")){
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                }else {
                    Glide.with(MainActivity.this).load(user.getImageUrl()).into(profile_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                if (item.getItemId() == R.id.home){
//                    selectedFragment = new HomeFragment();
//                } else if (item.getItemId() == R.id.search) {
//                    selectedFragment = new SearchFragment();
//                } else if (item.getItemId() == R.id.add) {
//
//                } else if (item.getItemId() == R.id.reels) {
//                    selectedFragment = new ReelsFragment();
//                } else if (item.getItemId() == R.id.profile) {
//                    selectedFragment = new ProfileFragment();
//                }
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
//                return true;
//            }
//        });
//        if (selectedFragment == null){
//            selectedFragment = new HomeFragment();
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
//
//        }
//
//    }
//}


//        EditText editText = findViewById(R.id.name);
//        Button btn = findViewById(R.id.click);
//        btn.setOnClickListener(v -> {
//            Intent secondActivity = new Intent(this, SecondActivity.class);
//            secondActivity.putExtra("name", editText.getText().toString());
//            startActivity(secondActivity);
//            MainActivity.this.finish();
//        });
//    }

//        Button btn = findViewById(R.id.click);
//        btn.setOnClickListener(v -> {
//            startActivity(new Intent(this, SecondActivity.class));
//            MainActivity.this.finish();
//        });
//    }

//        Button btn = findViewById(R.id.click);
//        btn.setOnClickListener(v -> {
//            Intent chromeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"));
//            chromeIntent.setPackage("com.android.chrome");
//            startActivity(chromeIntent);
//        });
//    }


//        image = findViewById(R.id.image);
//        Button btn = findViewById(R.id.click);
//        btn.setOnClickListener(v -> {
//            if(isSMSPermissionGranted()){
//                Intent sendSMSIntent=new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"+ "1234567890"));
//                sendSMSIntent.putExtra("sms_body", "Hello, This is a text message");
//                startActivity(sendSMSIntent);
//            } else {
//                new Handler().postDelayed(() -> {
//                    if(isSMSPermissionGranted()) {
//                    Intent sendSMSIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"+ "1234567890" ));
//                    sendSMSIntent.putExtra("sms_body", "Hello, This is a text message");
//                    startActivity(sendSMSIntent);
//                }
//                }, 5000);
//        }
//    });
//}

//    private boolean isSMSPermissionGranted() {
//        if (checkSelfPermission(Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED){
//            return true;
//        }else{
//            requestPermissions(new String[]{Manifest.permission.SEND_SMS},REQUEST_SEND_SMS);
//            return false;
//        }
//    }

//            if(isPermissionGranted()) {
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
////                startActivity(intent);
//            } else {
//                new Handler().postDelayed(() -> {
//                    if (isPermissionGranted()) {
//                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        startActivity(intent);
//                    } else {
//                        Toast.makeText(this, " " + isPermissionGranted(), Toast.LENGTH_SHORT).show();
//                    }
//
//                }, 5000);
//            }

//        });
//
//    }



//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
//            Toast.makeText(this, "Image Captured", Toast.LENGTH_SHORT).show();
//            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//            image.setImageBitmap(bitmap);
////            image.setImageURI(data.getData());
//        }
//    }
//
//    private boolean isPermissionGranted() {
//        if(checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
//            return true;
//        }else {
//            requestPermissions(new String[] {Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
//            return false;
//        }
//    }



//        TextView textview = findViewById(R.id.textView);
//        MaterialCardView tyre = findViewById(R.id.tyre);
//        Button btn = findViewById(R.id.button);
//                btn.setOnClickListener(v -> {
//                    Animation animation = AnimationUtils.loadAnimation(this, R.anim.move_right);
//                    tyre.startAnimation(animation);
//                    textview.startAnimation(animation);
//                    animation.setAnimationListener(new Animation.AnimationListener() {
//                        @Override
//                        public void onAnimationStart(Animation animation) {
//
//                        }
//
//                        @Override
//                        public void onAnimationEnd(Animation animation) {
//                            textview.setVisibility(View.GONE);
//
//                        }
//
//                        @Override
//                        public void onAnimationRepeat(Animation animation) {
//
//                        }
//                    });
//        });
//        Animation animation = AnimationUtils.loadAnimation(this, R.anim.move_right);
//        textview.startAnimation(animation);

//        studentList.add(new Student("1", "Jasmine", "10", "15", "1"));
//        studentList.add(new Student("2", "Kajal", "10", "15", "2"));
//        studentList.add(new Student("3", "Kunal", "10", "15", "3"));
//        studentList.add(new Student("4", "Kashish", "10", "15", "4"));
//        studentList.add(new Student("5", "Shivani", "10", "15", "5"));
//        studentList.add(new Student("6", "Shivangi", "10", "15", "6"));
//        studentList.add(new Student("7", "Rohit", "10", "15", "7"));
//        studentList.add(new Student("8", "Aditya", "10", "15", "8"));
//        studentList.add(new Student("9", "Nakul", "10", "15", "9"));
//        studentList.add(new Student("10", "Madhuri", "10", "15", "10"));
//        studentList.add(new Student("11", "Gaurav", "10", "15", "11"));
//        studentList.add(new Student("12", "Naina", "10", "15", "12"));
//        studentList.add(new Student("13", "Sahil", "10", "15", "13"));
//        studentList.add(new Student("14", "Purva", "10", "15", "14"));
//        studentList.add(new Student("15", "Chelsi", "10", "15", "15"));
//
//
//        RecyclerView recyclerView = findViewById(R.id.recyclerview);
////        String[] countries= {"India", "USA", "China", "Japan", "UK", "Germany", "France", "Italy", "Brazil", "Canada"};
//        RecyclerViewAdapter adapter = new RecyclerViewAdapter(studentList);
//        recyclerView.setAdapter(adapter);

//        ListView listView = findViewById(R.id.listview);
//        String[] countries= {"India", "USA", "China", "Japan", "UK", "Germany", "France", "Italy", "Brazil", "Canada"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, countries);
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener((parent, view, position, id) -> Toast.makeText(MainActivity.this, countries[position]+" Clicked", Toast.LENGTH_SHORT).show());
//
//            }
//        });

//        WebView webView = findViewById(R.id.webview);
//        webView.loadUrl("https://www.google.com");
//        WebViewClient webViewClient = new WebViewClient(){
//
//            public void OnPageStarted(WebView view, String url){
//                Toast.makeText(MainActivity.this, "Page Loaded", Toast.LENGTH_SHORT).show();
//            }
//        };
//        webView.setWebViewClient(webViewClient);
//        webView.setWebViewClient(new WebViewClient());
//        webView.getSettings().setJavaScriptEnabled(true);


//        CheckBox checkBox = findViewById(R.id.checkbox);
//        checkBox.setChecked(true);
//        checkBox.setOnCheckedChangeListener((buttonView, isChecked) ->  {
//            Toast.makeText(this, isChecked ? "Checked" : "UnChecked", Toast.LENGTH_SHORT).show();
//        });
//        Button changeBtn = findViewById(R.id.changeBtn);
//        ImageView image = findViewById(R.id.image);
//       changeBtn.setOnClickListener(v -> {
//           if(image.getTag().equals("r")) {
//               image.setImageResource(R.drawable.foggy);
//               image.setTag("foggy");
//           } else {
//               image.setImageResource(R.drawable.r);
//               image.setTag("r");
//           if(imgVal == 1){
//               image.setImageResource(R.drawable.foggy);
//               imgVal = 2;
//           }else{
//               image.setImageResource(R.drawable.r);
//               imgVal = 1;
//           }
//       });
//        TextView text = findViewById(R.id.text);
//        EditText name = findViewById(R.id.name);
//        changeBtn.setOnClickListener(v -> {
//            text.setText(name.getText());
//            name.setText("");
//        });
//        changeBtn.setOnClickListener(v -> {
//            text.setText("Pregrad");
//        });
//        text.setOnClickListener(v -> {
//            text.setText("TextView");
//        });


//        changeBtn.setOnClickListener(v -> {
//            if(text.getText().equals("TextView")){
//                text.setText("Pregrad");
//            } else{
//              text.setText("TextView");
//            }
//        });


//        changeBtn.setOnClickListener(v -> {
//            if(text.getText().toString().toUpperCase().equals("TEXTVIEW")){     //TEXTVIEW  OR textview but every time converting it will affect the memory
//                text.setText("Pregrad");
//            } else{
//                text.setText("TextView");
//            }
//        });

//        changeBtn.setOnClickListener(v -> {
//            if(text.getText().toString().equalsIgnoreCase("textview")){
//                text.setText("Pregrad");
//            } else{
//                text.setText("TextView");
//            }
//        });

//}