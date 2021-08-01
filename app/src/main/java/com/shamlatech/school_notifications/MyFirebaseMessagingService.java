package com.shamlatech.school_notifications;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import com.shamlatech.school_management.Config;
import com.shamlatech.school_management.HomeActivity;
import com.shamlatech.school_management.NotificationView;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.Support;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.shamlatech.utils.Vars.Pref_User_ID;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMessagingServ";
    private final static AtomicInteger c = new AtomicInteger(5);

    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            ///sendNotification(bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if(remoteMessage.getData()!=null)
            getImage(remoteMessage);
    }

    private void handleDataMessage(Map<String, String> data) {

       if (Support.GetPref(getApplicationContext(), Pref_User_ID).equals(data.get("user_id"))) {

            String Sound = data.get("n_sound");
            String Vibrate = data.get("n_vibrate");
            Bundle bundle = new Bundle();
            for (Map.Entry<String, String> entry : data.entrySet()) {
                bundle.putString(entry.getKey(), entry.getValue());
            }

            Intent intent = new Intent(getApplicationContext(), NotificationView.class);
            intent.putExtra("created_on", data.get("created_on"));
            intent.putExtra("title", data.get("title"));
            intent.putExtra("content", data.get("content"));
            intent.putExtra("id", data.get("id"));
            showNotification(data, intent, Sound, Vibrate);
        }

    }

    public static int getID() {
        return c.incrementAndGet();
    }

    private void showNotification(Map<String, String> data, Intent intent, String Sound, String Vibrate) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int requestCode = 0;
        String title = data.get("title");
        String message = data.get("content");

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), requestCode, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        //PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,0);

        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(getApplicationContext(), "1")
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setLights(Color.RED, 1000, 300)
                .setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(getID(), noBuilder.build());

        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.playNotificationSound();
        notificationUtils.playNotificationVibrate();
       /* if (Sound.equals("1")) {
            notificationUtils.playNotificationSound();
        }
        if (Vibrate.equals("1")) {
            notificationUtils.playNotificationVibrate();
        }*/

    }

/*    private void sendNotification(Bitmap bitmap){


        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
        style.bigPicture(bitmap);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(getApplicationContext(), NotificationView.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,0);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "101";

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_MAX);

            //Configure Notification Channel
            notificationChannel.setDescription("Game Notifications");
            notificationChannel.enableLights(true);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);

            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(Config.title)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentText(Config.content)
                .setContentIntent(pendingIntent)
                .setStyle(style)
                .setLargeIcon(bitmap)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_MAX);


        notificationManager.notify(1, notificationBuilder.build());


    }*/

    private void getImage(final RemoteMessage remoteMessage) {

        Map<String, String> data = remoteMessage.getData();
        Config.title = data.get("title");
        Config.content = data.get("content");
        Config.imageUrl = data.get("imageUrl");
        Config.gameUrl = data.get("gameUrl");

        handleDataMessage(data);
        //Create thread to fetch image from notification
        if(remoteMessage.getData()!=null){

            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    // Get image from data Notification
                    /*Picasso.get()
                            .load(Config.imageUrl)
                            .into(target);*/
                }
            }) ;
        }
    }
}














/*
package com.shamlatech.school_notifications;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.shamlatech.activity.AnnouncementViewActivity;
import com.shamlatech.activity.ForumViewActivity;
import com.shamlatech.activity.MessageActivity;
import com.shamlatech.activity.ProfileActivity;
import com.shamlatech.activity.parent.ParentStudentActivity;
import com.shamlatech.activity.parent.ParentsAdditionalActivity;
import com.shamlatech.activity.teacher.TeachersAdditionalActivity;
import com.shamlatech.activity.teacher.TeachersStudentActivity;
import com.shamlatech.school_management.HomeActivity;
import com.shamlatech.school_management.Index;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.Support;
import com.shamlatech.utils.Vars;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.shamlatech.utils.Vars.Pref_User_ID;
import static com.shamlatech.utils.Vars.Role_Principal;


*/
/**
 * Created by  Dharmalingam Sekar  on 02-03-2017.
 *//*


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCM Service";
    private final static AtomicInteger c = new AtomicInteger(5);
    final String CONS_NOTIFICATION_TYPE_UPDATE_TEACHER = "1";
    final String CONS_NOTIFICATION_TYPE_UPDATE_MARKS = "2";
    final String CONS_NOTIFICATION_TYPE_UPDATE_ASSIGNMENT = "3";
    final String CONS_NOTIFICATION_TYPE_UPDATE_BEHAVIOUR = "4";
    final String CONS_NOTIFICATION_TYPE_UPDATE_HEALTH = "5";
    final String CONS_NOTIFICATION_TYPE_UPDATE_ATTENDANCE = "6";
    final String CONS_NOTIFICATION_TYPE_UPDATE_EVENTS = "7";
    final String CONS_NOTIFICATION_TYPE_UPDATE_DOCUMENT = "8";
    final String CONS_NOTIFICATION_TYPE_UPDATE_PHOTO = "9";
    final String CONS_NOTIFICATION_TYPE_UPDATE_VIDEO = "10";
    final String CONS_NOTIFICATION_TYPE_CREATE_FORUMS_TEACHER = "11";
    final String CONS_NOTIFICATION_TYPE_CREATE_FORUMS_PARENT = "12";
    final String CONS_NOTIFICATION_TYPE_REPLY_FORUMS = "13";
    final String CONS_NOTIFICATION_TYPE_NEW_ANNOUNCEMENT = "14";
    final String CONS_NOTIFICATION_TYPE_UPDATE_PROFILE = "15";
    final String CONS_NOTIFICATION_TYPE_MESSAGE = "16";

    public static int getID() {
        return c.incrementAndGet();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage == null)
            return;
        if (remoteMessage.getData().size() > 0) {
            try {
                handleDataMessage(remoteMessage);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
        //Log.d(TAG, "From: " + remoteMessage.getFrom());
       // Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
    }

    private void handleDataMessage(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        handleDataMessage(data);

    }

    private void handleDataMessage(Map<String, String> data) {

        if (Support.GetPref(getApplicationContext(), Pref_User_ID).equals(data.get("user_id"))) {

            String Sound = data.get("n_sound");
            String Vibrate = data.get("n_vibrate");
            Bundle bundle = new Bundle();
            for (Map.Entry<String, String> entry : data.entrySet()) {
                bundle.putString(entry.getKey(), entry.getValue());
            }

            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.putExtra("bundle", bundle);
            showNotification(data, intent, Sound, Vibrate);
        }

    }

    private void showNotification(Map<String, String> data, Intent intent, String Sound, String Vibrate) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int requestCode = 0;
        String title = getResources().getString(R.string.app_name);
        String message = data.get("content");
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), requestCode, intent,
                PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(getApplicationContext(), "1")
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(getID(), noBuilder.build());

        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        if (Sound.equals("1")) {
            notificationUtils.playNotificationSound();
        }
        if (Vibrate.equals("1")) {
            notificationUtils.playNotificationVibrate();
        }

    }

}*/
