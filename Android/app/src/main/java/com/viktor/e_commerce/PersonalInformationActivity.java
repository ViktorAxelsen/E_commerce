package com.viktor.e_commerce;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.viktor.e_commerce.Util.HttpUtil;
import com.viktor.e_commerce.Util.Util;
import com.viktor.e_commerce.Util.Utility;
import com.viktor.e_commerce.gson.User;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PersonalInformationActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "PersonalInformationActi";

    private PopupWindow popupWindow;

    private ImageView header;

    private Uri imageUri;

    private File outputImage;

    private TextView userAccount;

    private TextView userNickname;

    private TextView userSex;

    private TextView userTel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        //取控件
        header = (ImageView)findViewById(R.id.head_image_view);
        userAccount = (TextView)findViewById(R.id.user_id);
        userNickname = (TextView)findViewById(R.id.user_nickname);
        userSex = (TextView)findViewById(R.id.user_sex);
        userTel = (TextView)findViewById(R.id.user_tel);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        //设置数据
        if(userId != 0){
            requestUser();
        }else{
            Glide.with(this).load(R.drawable.head_default).into(header);
            userAccount.setText("--");
            userNickname.setText("--");
            userSex.setText("--");
            userTel.setText("--");
        }
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //Util.sendNotification(this, (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE));
    }

    private void requestUser(){
        String url = ROOT + "user/?format=json&user_id=" + userId;
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PersonalInformationActivity.this, "获取服务器信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseText = response.body().string();
                final User user = Utility.parseJSONForUser(responseText).get(0);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        userAccount.setText(user.getUserId() + "");
                        userNickname.setText(user.getUserNickname());
                        userSex.setText(user.getUserSex());
                        userTel.setText(user.getUserTel());
                        try{
                            Glide.with(getApplication()).load(user.getUserHeadImage()).into(header);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public static void actionStart(Context context){
        Intent intent = new Intent(context, PersonalInformationActivity.class);
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.activity_fade_out, R.anim.activity_no);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.personal_information_activity_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.change_header:
                showPopupWindow();
                break;
        }
        return true;
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
        popupWindow.showAtLocation(findViewById(R.id.personal_root_view), Gravity.BOTTOM, 0, 0);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.album:
                if(ContextCompat.checkSelfPermission(PersonalInformationActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(PersonalInformationActivity.this, new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE }, Util.OPEN_ALBUM);
                }else{
                    Util.openAlbum(this);
                }
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
                    imageUri = FileProvider.getUriForFile(PersonalInformationActivity.this, "com.viktor.e_commerce.fileprovider", outputImage);
                }else{
                    imageUri = Uri.fromFile(outputImage);
                }
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.CAMERA }, Util.OPEN_CAMERA);
                }else{
                    Util.openCamera(this, imageUri);
                }
                break;
            case R.id.cancel:
                break;
        }
        popupWindow.dismiss();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.activity_fade_in);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case Util.OPEN_ALBUM:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Util.openAlbum(this);
                }else{
                    Toast.makeText(PersonalInformationActivity.this, "Permission needs to be improved", Toast.LENGTH_SHORT).show();
                }
                break;
            case Util.OPEN_CAMERA:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Util.openCamera(this, imageUri);
                }else{
                    Toast.makeText(PersonalInformationActivity.this, "Permission needs to be improved", Toast.LENGTH_SHORT).show();
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
                    try{
                        String str = outputImage.toString();
                        uploadImage(outputImage, str.substring(str.lastIndexOf("/") + 1, str.length()));
                        Log.d(TAG, imageUri.toString());
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));//这里的uri对应的是图片文件的路径
                        ExifInterface exifInterface = null;
                        try{
                            exifInterface = new ExifInterface(getContentResolver().openInputStream(imageUri));
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                        correctAndSetImage(exifInterface, bitmap);
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
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
        Toast.makeText(PersonalInformationActivity.this, uri.toString(), Toast.LENGTH_LONG).show();
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
        displayImage(imagePath);
        uploadImage(new File(imagePath), imagePath.substring(imagePath.lastIndexOf("/") + 1, imagePath.length()));
    }

    public void handleImageBeforeKitKat(Intent data){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
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

    public void displayImage(String imagePath){
        if(imagePath != null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            ExifInterface exifInterface = null;
            try{
                exifInterface = new ExifInterface(imagePath);
            }catch (IOException e){
                e.printStackTrace();
            }
            correctAndSetImage(exifInterface, bitmap);
        }else{
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    public void correctAndSetImage(ExifInterface exifInterface, Bitmap bitmap){
        int degree = 0;
        if(exifInterface != null){
            Log.d(TAG, "enter not null");
            degree = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            switch (degree){
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
                    degree = 0;
                    break;
            }
            Log.d(TAG, degree + "");
            if(degree != 0){
                Matrix matrix = new Matrix();
                matrix.postRotate(degree);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }
            Glide.with(this).load(bitmap).into(header);
        }
    }

    public void uploadImage(File file, String fileName){
        String url = ROOT + "upload_user_head_image/" + userId;
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
                        Toast.makeText(PersonalInformationActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}
