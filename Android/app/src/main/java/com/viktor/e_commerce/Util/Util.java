package com.viktor.e_commerce.Util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.viktor.e_commerce.MainActivity;
import com.viktor.e_commerce.PersonalInformationActivity;
import com.viktor.e_commerce.R;
import com.yzq.zxinglibrary.android.CaptureActivity;

public class Util {

    private static final String TAG = "Util";

    public static final int OPEN_CAMERA = 1;

    public static final int START_QRCODE_SCAN = 2;

    public static final int OPEN_PHONE = 3;

    public static final int CALL = 4;

    public static final int OPEN_BROWSER = 5;

    public static final int OPEN_ALBUM = 6;

    public static final int CHOOSE_PHOTO = 7;

    public static final int SEND_NOTIFICATION = 8;

    public static final int GET_LOCATION = 9;

    public static void sendNotification(Context context, NotificationManager manager){
        createChannel(manager);
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(context, "TEST1")
                .setContentTitle("大降价！")
                .setContentText("笔记本电脑、固态硬盘、手机、内存条通通1折！！！！")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setContentIntent(pi)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(context.getResources(), R.drawable.notification_image)))
                .build();
        //.setStyle(new NotificationCompat.BigTextStyle().bigText("This is content textXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"))
        manager.notify(1, notification);
    }

    public static void openBrowser(Context context, String address){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(address));
        context.startActivity(intent);
    }

    public static void openCamera(Context context, Uri imageUri){
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        ((Activity)context).startActivityForResult(intent, OPEN_CAMERA);
    }

    public static void openAlbum(Context context){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        ((Activity)context).startActivityForResult(intent, CHOOSE_PHOTO);
        Log.d(TAG, "open album");
    }

    public static void startQRCodeScan(Context context){
        Intent intent = new Intent(context, CaptureActivity.class);
        ((Activity)context).startActivityForResult(intent, START_QRCODE_SCAN);
    }

    public static void openPhone(Context context){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"));
        context.startActivity(intent);
    }

    public static void call(Context context){
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:2333333"));
        context.startActivity(intent);
    }

    public static void createChannel(NotificationManager manager){
        if(manager.getNotificationChannel("TEST1") != null){
            return;
        }

        NotificationChannel notificationChannel = new NotificationChannel("TEST1", "TEST1", NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        notificationChannel.setLightColor(Color.BLUE);
        notificationChannel.setVibrationPattern(new long[]{ 0, 100, 100, 100 });
        manager.createNotificationChannel(notificationChannel);//必须重装程序生效、channel先设置，notification设置的不生效、
    }

}
