package com.viktor.e_commerce;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.viktor.e_commerce.Util.Util;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "MainFragment";

    private CheckBox add;

    private SwipeRefreshLayout swipeRefreshLayout;

    private FloatingActionButton floatingActionButton;

    private PopupMenu popupMenu;

    private Banner banner;

    private List<String> bannerImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        //取控件
        add = (CheckBox) view.findViewById(R.id.add);
        Button home = (Button)view.findViewById(R.id.home);
        Button scan = (Button)view.findViewById(R.id.scan);
        Button search = (Button)view.findViewById(R.id.search);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh);
        floatingActionButton = (FloatingActionButton)view.findViewById(R.id.float_button);
        banner = (Banner)view.findViewById(R.id.banner);
        //设置数据
        bannerImage = new ArrayList<>();
        bannerImage.add("http://www.mallproject.cn:8000/media/item/previewImage/%E6%A0%A1%E5%9B%AD%E6%95%85%E4%BA%8B%E9%A6%86-33%E5%95%86%E5%8A%A1%E9%BB%91%E8%89%B2_pLjcEaH.jpg");
        bannerImage.add("http://www.mallproject.cn:8000/media/item/previewImage/itemDefault.jpg");
        bannerImage.add("http://www.mallproject.cn:8000/media/item/previewImage/%E6%A0%A1%E5%9B%AD%E6%95%85%E4%BA%8B%E9%A6%86-33%E5%95%86%E5%8A%A1%E9%BB%91%E8%89%B2_pLjcEaH.jpg");
        banner.setImageLoader(new GlideImageLoader());
        banner.setImages(bannerImage);
        banner.start();
        //监听
        home.setOnClickListener(this);
        scan.setOnClickListener(this);
        search.setOnClickListener(this);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        add.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch (buttonView.getId()){
                    case R.id.add:
                        if(isChecked){
//                            RotateAnimation rotateAnimation = new RotateAnimation(0.0f, 45.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//                            rotateAnimation.setDuration(500);
//                            rotateAnimation.setFillAfter(true);
//                            add.startAnimation(rotateAnimation);

                            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.add_check_box_rotate_start);
                            animation.setFillAfter(true);
                            add.startAnimation(animation);
                            showPopupMenu(add);
                        }else{
                            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.add_check_box_rotate_recover);
                            animation.setFillAfter(true);
                            add.startAnimation(animation);
                        }
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Thread.sleep(1000);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                swipeRefreshLayout.setRefreshing(false);
                                Snackbar.make(swipeRefreshLayout, "Refresh", Snackbar.LENGTH_SHORT)
                                        .setActionTextColor(Color.WHITE)
                                        .setAction("OK", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                //Logic
                                            }
                                        }).show();
                            }
                        });
                    }
                }).start();
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "TEST", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home:
                if(getActivity() instanceof MainActivity){
                    ((MainActivity)getActivity()).drawerLayout.openDrawer(GravityCompat.START);
                }
                break;
            case R.id.search:
                SearchActivity.actionStart(getContext());
                break;
            case R.id.scan:
                if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(), new String[]{ Manifest.permission.CAMERA }, Util.START_QRCODE_SCAN);
                }else{
                    Util.startQRCodeScan(getActivity());
                }
                break;
        }
    }

    private void showPopupMenu(View view){
        if(popupMenu == null){
            popupMenu = new PopupMenu(getContext(), view);
            popupMenu.getMenuInflater().inflate(R.menu.main_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.settings:
                            SettingActivity.actionStart(getContext());
                            break;
                    }
                    return true;
                }
            });
            popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
                @Override
                public void onDismiss(PopupMenu popupMenu) {
                    add.setChecked(false);
                }
            });
        }
        popupMenu.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(popupMenu != null){
            popupMenu.dismiss();
            popupMenu = null;
        }
    }


    private String temp(){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i <= 100; i++){
            stringBuilder.append("XXXXXXXXXXXXXXXXXXXXXXXXX");
        }
        return stringBuilder.toString();
    }
}
