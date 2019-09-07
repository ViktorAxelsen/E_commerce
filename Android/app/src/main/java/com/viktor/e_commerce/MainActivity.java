package com.viktor.e_commerce;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.viktor.e_commerce.Util.HttpUtil;
import com.viktor.e_commerce.Util.Util;

import com.yzq.zxinglibrary.common.Constant;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    public LocationClient mLocationClient = null;

    private MyLocationListener myListener = new MyLocationListener();

    private String myPosition;

    private FragmentManager fragmentManager;

    private FragmentTransaction fragmentTransaction;

    private MainFragment mainFragment;

    private CartFragment cartFragment;

    private CategoryFragment categoryFragment;

    private PersonFragment personFragment;

    public DrawerLayout drawerLayout;

    private static boolean isDrawerOpen = false;

    private NavigationView navigationView;

    private RadioButton main;

    private RadioButton people;

    private RadioButton social;

    private RadioButton person;

    private ImageView circleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*        if(Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }*/
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myListener);
        setContentView(R.layout.activity_main);
        main = (RadioButton)findViewById(R.id.main);
        people = (RadioButton)findViewById(R.id.people);
        social = (RadioButton)findViewById(R.id.social);
        person = (RadioButton)findViewById(R.id.person);
        initView();
        main.setChecked(true);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.bottom_toolbar_radio_group);
        navigationView = (NavigationView)findViewById(R.id.nav_view);
        View header = navigationView.inflateHeaderView(R.layout.nav_header);//动态加载头部布局
        circleImageView = (CircleImageView)header.findViewById(R.id.nav_header_imgae);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersonalInformationActivity.actionStart(MainActivity.this);
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                fragmentTransaction = fragmentManager.beginTransaction();
                switch (checkedId){
                    case R.id.main:
                        if(mainFragment == null){
                            mainFragment = new MainFragment();
                        }
                        fragmentTransaction.replace(R.id.body_fragment, mainFragment);
                        break;
                    case R.id.people:
                        if(cartFragment == null){
                            cartFragment = new CartFragment();
                            Bundle argument = new Bundle();
                            argument.putInt("current_id", 3);
                            cartFragment.setArguments(argument);
                        }
                        fragmentTransaction.replace(R.id.body_fragment, cartFragment);
                        break;
                    case R.id.social:
                        if(categoryFragment == null){
                            categoryFragment = new CategoryFragment();
                        }
                        fragmentTransaction.replace(R.id.body_fragment, categoryFragment);
                        break;
                    case R.id.person:
                        if(personFragment == null){
                            personFragment = new PersonFragment();
                        }
                        fragmentTransaction.replace(R.id.body_fragment, personFragment);
                        break;
                }
                fragmentTransaction.commit();
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()){
                    case R.id.nav_menu_call:
                        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{ Manifest.permission.CALL_PHONE }, Util.OPEN_PHONE);
                        }else{
                            Util.openPhone(MainActivity.this);
                        }
                        break;
                    case R.id.nav_menu_friends:
                        break;
                    case R.id.nav_menu_location:
                        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                        dialog.setTitle("我的位置");
                        dialog.setMessage(myPosition);
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        dialog.show();
                        break;
                    case R.id.nav_menu_mail:
                        break;
                    case R.id.nav_menu_settings:
                        SettingActivity.actionStart(MainActivity.this);
                        break;
                    case R.id.nav_menu_task:
                        break;
                    case R.id.nav_menu_share:
                        break;
                    case R.id.nav_menu_feedback:
                        break;
                    default:
                        Log.e(TAG, TAG);
                }
                return true;
            }
        });

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {
                if(v < 0.1){
                    getWindow().setStatusBarColor(Color.rgb(238, 238, 238));
                }else{
                    getWindow().setStatusBarColor(Color.argb((int)(255 * v), 97, 97, 97));
                }
            }

            @Override
            public void onDrawerOpened(@NonNull View view) {
                isDrawerOpen = true;
            }

            @Override
            public void onDrawerClosed(@NonNull View view) {
                isDrawerOpen = false;
            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });
        fragmentManager = getSupportFragmentManager();
        mainFragment = new MainFragment();
        fragmentManager.beginTransaction().replace(R.id.body_fragment, mainFragment).commit();

        List<String> permissionList = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!permissionList.isEmpty()){
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, Util.GET_LOCATION);
        }else{
            requestLocation();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if(intent.getIntExtra("cartFragment", 0) == 1){
            if(cartFragment == null){
                cartFragment = new CartFragment();
                Bundle argument = new Bundle();
                argument.putInt("current_id", 3);
                cartFragment.setArguments(argument);
            }
            fragmentManager.beginTransaction().replace(R.id.body_fragment, cartFragment).commit();
            people.setChecked(true);
        }else if(intent.getStringExtra("user_head") != null){
            Glide.with(this).load(intent.getStringExtra("user_head")).into(circleImageView);
        }

        if(userId == 0){
            Glide.with(this).load(R.drawable.head_default).into(circleImageView);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
    }

    private void requestLocation(){
        initLocation();
        mLocationClient.start();
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);
        option.setOpenGps(true);
        //option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
        option.setIgnoreKillProcess(false);
        option.setIsNeedAddress(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        mLocationClient.setLocOption(option);
    }

    private void initView(){
        Drawable drawable_main = getResources().getDrawable(R.drawable.bottom_toolbar_main_image_selector, null);
        drawable_main.setBounds(0, 0, 70, 70);
        main.setCompoundDrawables(null, drawable_main, null, null);

        Drawable drawable_people = getResources().getDrawable(R.drawable.bottom_toolbar_people_image_selector, null);
        drawable_people.setBounds(0, 0, 70, 70);
        people.setCompoundDrawables(null, drawable_people, null, null);

        Drawable drawable_social = getResources().getDrawable(R.drawable.bottom_toolbar_social_image_selector, null);
        drawable_social.setBounds(0, 0, 70, 70);
        social.setCompoundDrawables(null, drawable_social, null, null);

        Drawable drawable_person = getResources().getDrawable(R.drawable.bottom_toolbar_person_image_selector, null);
        drawable_person.setBounds(0, 0, 70, 70);
        person.setCompoundDrawables(null, drawable_person, null, null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case Util.OPEN_CAMERA:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //Util.openCamera(this);
                }else{
                    Toast.makeText(MainActivity.this, "Permission needs to be improved", Toast.LENGTH_SHORT).show();
                }
                break;
            case Util.OPEN_PHONE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Util.openPhone(MainActivity.this);
                }else{
                    Toast.makeText(MainActivity.this, "Permission needs to be improved", Toast.LENGTH_SHORT).show();
                }
                break;
            case Util.START_QRCODE_SCAN:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Util.startQRCodeScan(this);
                }else{
                    Toast.makeText(MainActivity.this, "Permission needs to be improved", Toast.LENGTH_SHORT).show();
                }
                break;
            case Util.GET_LOCATION:
                if(grantResults.length > 0){
                    for(int result : grantResults){
                        if(result != PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(MainActivity.this, "Permission needs to be improved", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                }else{
                    Toast.makeText(MainActivity.this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case Util.START_QRCODE_SCAN:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        String content = data.getStringExtra(Constant.CODED_CONTENT);
                        Util.openBrowser(this, content);
                    }
                }
                break;
        }
    }

    public static void actionStart(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.activity_scale_in);
    }

    @Override
    public void onBackPressed() {
        if(isDrawerOpen){
            drawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }

    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {

            if(bdLocation == null){
                return;
            }
            StringBuilder currentPosition = new StringBuilder();
            currentPosition.append("纬度：").append(bdLocation.getLatitude()).append("\n");
            currentPosition.append("经度：").append(bdLocation.getLongitude()).append("\n");
            currentPosition.append("国家：").append(bdLocation.getCountry()).append("\n");
            currentPosition.append("省：").append(bdLocation.getProvince()).append("\n");
            currentPosition.append("市：").append(bdLocation.getCity()).append("\n");
            currentPosition.append("区：").append(bdLocation.getDistrict()).append("\n");
            currentPosition.append("街道：").append(bdLocation.getStreet()).append("\n");
            currentPosition.append("定位方式：");
            if(bdLocation.getLocType() == BDLocation.TypeGpsLocation){
                currentPosition.append("GPS");
            }else if(bdLocation.getLocType() == BDLocation.TypeNetWorkLocation){
                currentPosition.append("网络");
            }else if(bdLocation.getLocType() == BDLocation.TypeCacheLocation){
                currentPosition.append("缓存");
            }else if(bdLocation.getLocType() == BDLocation.TypeOffLineLocation){
                currentPosition.append("离线");
            }
            myPosition = currentPosition.toString();
            mLocationClient.requestHotSpotState();
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
            super.onConnectHotSpotMessage(s, i);
            StringBuilder stringBuilder = new StringBuilder(myPosition);
            stringBuilder.append("\n").append("WIFI状态：");
            if(i == LocationClient.CONNECT_HOT_SPOT_FALSE){
                stringBuilder.append("非热点");
            }else if(i == LocationClient.CONNECT_HOT_SPOT_TRUE){
                stringBuilder.append("热点");
            }else if(i == LocationClient.CONNECT_HOT_SPOT_UNKNOWN){
                stringBuilder.append("未知");
            }
            myPosition = stringBuilder.toString();
        }
    }
}
