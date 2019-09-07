package com.viktor.e_commerce;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.viktor.e_commerce.Util.HttpUtil;
import com.viktor.e_commerce.Util.Utility;
import com.viktor.e_commerce.gson.User;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "LoginActivity";

    private SharedPreferences pref;

    private SharedPreferences.Editor editor;

    private EditText account;

    private EditText password;

    private CheckBox remember;

    private Button login;

    private Button back;

    private TextView register;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //取控件
        account = (EditText)findViewById(R.id.account);
        password = (EditText)findViewById(R.id.password);
        remember = (CheckBox)findViewById(R.id.remember_pass);
        login = (Button)findViewById(R.id.login);
        register = (TextView)findViewById(R.id.register);
        back = (Button)findViewById(R.id.back);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        //设置数据
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember = pref.getBoolean("remember_password", false);
        if(isRemember){
            String ac = pref.getString("account", "");
            String pa = pref.getString("password", "");
            account.setText(ac);
            password.setText(pa);
            remember.setChecked(true);
        }
        //监听
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                String ac = account.getText().toString();
                String pa = password.getText().toString();
                requestLogin(ac, pa);
                swipeRefreshLayout.setRefreshing(true);
                break;
            case R.id.register:
                RegisterActivity.actionStart(this);
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 500);
                break;
            case R.id.back:
                onBackPressed();
                break;
        }
    }

    private void requestLogin(final String account, final String password){
        String url = ROOT + "user/?format=json&user=" + account;
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "获取服务器信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseText = response.body().string();
                List<User> users = Utility.parseJSONForUser(responseText);
                if(users.size() == 1){
                    final User user = users.get(0);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(user.getUserNickname().equals(account) && user.getUserPassword().equals(password)){
                                editor = pref.edit();
                                if(remember.isChecked()){
                                    editor.putBoolean("remember_password", true);
                                    editor.putString("account", account);
                                    editor.putString("password", password);
                                }
                                else{
                                    editor.clear();
                                }
                                editor.apply();
                                userId = user.getUserId();
                                swipeRefreshLayout.setRefreshing(false);
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("user_head", user.getUserHeadImage());
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }
                    });
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "该用户不存在", Toast.LENGTH_SHORT).show();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                }
            }
        });
    }

    public static void actionStart(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.activity_fade_out, R.anim.activity_no);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.activity_fade_in);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }
}
