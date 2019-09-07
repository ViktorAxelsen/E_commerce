package com.viktor.e_commerce;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.viktor.e_commerce.Adapter.StoreItemAdapter;
import com.viktor.e_commerce.Util.HttpUtil;
import com.viktor.e_commerce.Util.Util;
import com.viktor.e_commerce.Util.Utility;
import com.viktor.e_commerce.gson.Item;
import com.viktor.e_commerce.gson.Store;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StoreActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "StoreActivity";

    private int uploadItemId;

    private int uploadImageType;

    private Uri imageUri;

    private File outputImage;

    private PopupWindow popupWindow;

    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView recyclerView;

    private StoreItemAdapter adapter;

    private Button back;

    private Button settings;

    private Button waitSend;

    private List<Item> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        //取控件
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        recyclerView = (RecyclerView)findViewById(R.id.recycle_view);
        back = (Button)findViewById(R.id.back);
        settings = (Button)findViewById(R.id.settings);
        waitSend = (Button)findViewById(R.id.wait_send);
        //监听
        back.setOnClickListener(this);
        settings.setOnClickListener(this);
        waitSend.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestStoreItem();
            }
        });
        //数据
        swipeRefreshLayout.setRefreshing(true);
        requestStoreItem();
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        adapter = new StoreItemAdapter(items, new StoreItemChangeImageListener() {
            @Override
            public void change(int itemId) {
                uploadItemId = itemId;
                showAlertDialog();
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void showAlertDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("添加商品图片");
        dialog.setMessage("请选择添加详情图还是预览图？");
        dialog.setCancelable(true);
        dialog.setPositiveButton("预览图", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                uploadImageType = 0;
                dialog.dismiss();
                showPopupWindow();
            }
        });
        dialog.setNegativeButton("详情图", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                uploadImageType = 1;
                dialog.dismiss();
                showPopupWindow();
            }
        });
        dialog.show();
    }

    private void requestStoreItem(){
        String url = ROOT + "item/?format=json&item_store=" + storeId;
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(StoreActivity.this, "获取商店信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseText = response.body().string();
                final List<Item> storeItems = Utility.parseJSONForItem(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        items.clear();
                        for(int i = 0; i < storeItems.size(); i++){
                            items.add(storeItems.get(i));
                        }
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                onBackPressed();
                break;
            case R.id.settings:
                StoreSetting.actionStart(this);
                break;
            case R.id.wait_send:
                WaitSendActivity.actionStart(this);
                break;
            case R.id.album:
                if(ContextCompat.checkSelfPermission(StoreActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(StoreActivity.this, new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE }, Util.OPEN_ALBUM);
                }else{
                    Util.openAlbum(this);
                }
                popupWindow.dismiss();
                break;
            case R.id.take_photo:
                outputImage = new File(getExternalCacheDir(), "output_image.jpg");
                try{
                    if(outputImage.exists()){
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                }catch (IOException e){
                    e.printStackTrace();
                }

                if(Build.VERSION.SDK_INT >= 24){
                    imageUri = FileProvider.getUriForFile(StoreActivity.this, "com.viktor.e_commerce.fileprovider", outputImage);
                }else{
                    imageUri = Uri.fromFile(outputImage);
                }
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.CAMERA }, Util.OPEN_CAMERA);
                }else{
                    Util.openCamera(this, imageUri);
                }
                popupWindow.dismiss();
                break;
            case R.id.cancel:
                popupWindow.dismiss();
                break;
        }
    }

    public static void actionStart(Context context){
        Intent intent = new Intent(context, StoreActivity.class);
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.activity_right_fade_out, R.anim.activity_no);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.activity_left_fade_in);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    private void showPopupWindow(){
        if(popupWindow == null){
            View vPopupWindow = LayoutInflater.from(this).inflate(R.layout.popup_window, null, false);//引入弹窗布局
            //menu子项监听
            vPopupWindow.findViewById(R.id.album).setOnClickListener(this);
            vPopupWindow.findViewById(R.id.take_photo).setOnClickListener(this);
            vPopupWindow.findViewById(R.id.cancel).setOnClickListener(this);
            //PopupWindow实例
            popupWindow = new PopupWindow(vPopupWindow, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
            popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    changeBackgroundAlpha(0.5f, 1.0f);
                }
            });
        }
        popupWindow.showAtLocation(findViewById(R.id.store_root_view), Gravity.BOTTOM, 0, 0);
        changeBackgroundAlpha(1.0f, 0.5f);
    }

    private void changeBackgroundAlpha(float start, float end){
        ValueAnimator animator = ValueAnimator.ofFloat(start, end).setDuration(400);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setBackgroudAlpha((Float)animation.getAnimatedValue());
            }
        });
        animator.start();
    }

    private void setBackgroudAlpha(float alpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;//调节透明度
        getWindow().setAttributes(lp);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case Util.OPEN_ALBUM:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Util.openAlbum(this);
                }else{
                    Toast.makeText(StoreActivity.this, "Permission needs to be improved", Toast.LENGTH_SHORT).show();
                }
                break;
            case Util.OPEN_CAMERA:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Util.openCamera(this, imageUri);
                }else{
                    Toast.makeText(this, "Permission needs to be improved", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case Util.CHOOSE_PHOTO:
                if(resultCode == RESULT_OK){
                    if(Build.VERSION.SDK_INT >= 19){
                        handleImageOnKitKat(data);
                    }else{
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            case Util.OPEN_CAMERA:
                if(resultCode == RESULT_OK){
                    String str = outputImage.toString();
                    uploadImage(outputImage, str.substring(str.lastIndexOf("/") + 1, str.length()));
                }
                break;
        }
    }

    @TargetApi(19)
    public void handleImageOnKitKat(Intent data){
        Log.d(TAG, "enter handleN");
        String imagePath = null;
        Uri uri = data.getData();
        Log.d(TAG, uri.toString());
        if(DocumentsContract.isDocumentUri(this, uri)){
            String docId = DocumentsContract.getDocumentId(uri);//这里的Uri对应的是存储图片的数据库的表以及该图片在第几行的信息
            Log.d(TAG, docId);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                Log.d(TAG, "1");
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Log.d(TAG, "2");
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        }else if("com.sec.android.gallery3d.provider".equals(uri.getAuthority())){
            imagePath = getImagePath(uri, null);
        }
        else if("content".equalsIgnoreCase(uri.getScheme())){
            Log.d(TAG, "3");
            imagePath = getImagePath(uri, null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            Log.d(TAG, "4");
            imagePath = uri.getPath();
        }
        uploadImage(new File(imagePath), imagePath.substring(imagePath.lastIndexOf("/") + 1, imagePath.length()));
    }

    public void handleImageBeforeKitKat(Intent data){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        uploadImage(new File(imagePath), imagePath.substring(imagePath.lastIndexOf("/") + 1, imagePath.length()));
    }

    public String getImagePath(Uri uri, String selection){
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }


    public void uploadImage(File file, String fileName){
        String url = "";
        if(uploadImageType == 1){
            url = ROOT + "item_detail_image/" + uploadItemId;
        }else{
            url = ROOT + "upload_item_preview_image/" + uploadItemId;
        }
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("img", fileName, RequestBody.create(MediaType.parse("multipart/form-data"), file))
                .build();

        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + UUID.randomUUID())
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(StoreActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
