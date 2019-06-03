package com.example.mysendikapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    /**
     * @param remoteMessage
     */

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage==null) {
            return;
        } else if (remoteMessage.getNotification()==null) {
            if (remoteMessage.getData().size() > 0) {       //Notification sent by YucelYavuz
                HashMap hmp =parseYucelYavuz(remoteMessage.getData().toString());
                String body= hmp.get("\"message\"").toString();
                String title= hmp.get("\"title\"").toString();
                showNotification(title,body);

            } else {
                return;
            }
        } else {    //remoteNotification && .getNotification are not NULL   //Notification sent correctly
            showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
            Log.d(TAG, "From:" + remoteMessage.getFrom());
            Log.d(TAG, "Message Notification body: " + remoteMessage.getNotification().getBody());
        }



    }

    public HashMap parseYucelYavuz(String str){
        ArrayList<String> arrVal = new ArrayList<>();
        ArrayList<String> arrKey = new ArrayList<>();
        HashMap<String,String > hmp = new HashMap<>();

//        String str = "{data={\"image\":\"no\",\"title\":\"titleteti\",\"message\":\"Transmiteti\"}}";
        Pattern patternValue = Pattern.compile("\"(.*?)\"");
        Matcher matcherValue = patternValue.matcher(str);
        int i=0;
        while (matcherValue.find()) {
            if(i%2==0){
//                Log.d("PatternMatchers","Key ->> "+matcherValue.group(0) );
                arrKey.add(matcherValue.group(0));
            }else {
//                Log.d("PatternMatchers","Val->> "+matcherValue.group(0) );
                arrVal.add(matcherValue.group(0));
            }
            i++;
        }

        for (i=0;i<arrKey.size();i++){
            hmp.put(arrKey.get(i),arrVal.get(i));
        }

        return hmp;
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "onRefreshed Token:" + token);
        this.saveToken(token);
    }

    public void showNotification(String title, String body) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "com.example.myfcm.newChannel";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NotificationName",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Test channel");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_notification)
                .setContentTitle(title)
                .setContentText(body)
                .setContentInfo("Info");

        notificationManager.notify(new Random().nextInt(), notificationBuilder.build());


    }

    public void saveToken (String token){
        SharedPreferences xd = this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = xd.edit();

        editor.remove("notificationToken");
        editor.putString("notificationToken",token);
        editor.apply();
    }

}