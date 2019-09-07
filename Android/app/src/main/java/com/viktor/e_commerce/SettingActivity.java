package com.viktor.e_commerce;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class SettingActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "SettingActivity";

    private Button back;

    private Button addressManage;

    private Button quit;

    private Button quitLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*        if(Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }*/
        setContentView(R.layout.activity_setting);
        //取控件
        back = (Button)findViewById(R.id.back);
        addressManage = (Button)findViewById(R.id.address_manage);
        quit = (Button)findViewById(R.id.quit);
        quitLogin = (Button)findViewById(R.id.quit_login);
        //监听
        back.setOnClickListener(this);
        addressManage.setOnClickListener(this);
        quit.setOnClickListener(this);
        quitLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                Animation animation = AnimationUtils.loadAnimation(SettingActivity.this, R.anim.rotate_forever);
                animation.setRepeatCount(Animation.INFINITE);
                animation.setRepeatMode(Animation.RESTART);
                LinearInterpolator interpolator = new LinearInterpolator();
                animation.setInterpolator(interpolator);
                back.startAnimation(animation);
                finish();
                break;
            case R.id.address_manage:
                if(userId != 0){
                    AddressActivity.actionStart(this);
                }else{
                    Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
                    LoginActivity.actionStart(this);
                }
                break;
            case R.id.quit:
                ActivityCollector.finishAll();
                break;
            case R.id.quit_login:
                userId = 0;
                storeId = 0;
                LoginActivity.actionStart(this);
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 500);
                break;
        }
    }

    public static void actionStart(Context context){
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.activity_right_fade_out, R.anim.activity_no);//第一个参数作用于调用方，第二参数作用于启动方
        //out应理解为出来，in理解为消失
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        back.clearAnimation();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.activity_left_fade_in);
    }
}
