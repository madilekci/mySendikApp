package com.example.mysendikapp.bildirimler;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.mysendikapp.R;
import com.example.mysendikapp.SplashActivityMain;
import com.example.mysendikapp.anketler.anketWebViev;
import com.example.mysendikapp.dashboard.ActivityDashboard;
import com.example.mysendikapp.etkinlik.etkinlikDetaylari;
import com.example.mysendikapp.haberler.haberDetaylari;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    private String body = "", title = "", type = "", id = "";
    private Intent myIntent;

    private boolean isClickable = true;       //True if user already logged in
    private boolean isNotifyable = true;      //From settings

    NotificationChannel notificationChannelHigh;
    NotificationCompat.Builder notificationBuilder;
    Uri alarmSound;
    NotificationManager notificationManager;


    /**
     * @param remoteMessage
     */

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "MESSAGE RECEIVED");
        getAbles();

        if (!isNotifyable) {
            return;
        }

        if (remoteMessage == null) {
            return;
        } else if (remoteMessage.getNotification() == null) {
            if (remoteMessage.getData().size() > 0) {       //Notification sent by YucelYavuz
                HashMap hmp = parseYucelYavuz(remoteMessage.getData().toString());
                body = "" + hmp.get("\"message\"").toString().replaceAll("\"", "");
                title = "" + hmp.get("\"title\"").toString().replaceAll("\"", "");
                type = "" + hmp.get("\"type\"").toString().replaceAll("\"", "");
                id = "" + hmp.get("\"id\"").toString().replaceAll("\"", "");


                Log.d(TAG, "HMP.GET ->> "
                        + "\n" + "-----> " + body
                        + "\n" + "-----> " + title
                        + "\n" + "-----> " + type
                        + "\n" + "-----> " + id
                );
                inspectNotificationData();
            } else {
                return;
            }
        } else {
            type = "4";
            body = "" + remoteMessage.getNotification().getBody();
            title = "" + remoteMessage.getNotification().getTitle();
            inspectNotificationData();
            Log.d(TAG, "From:" + remoteMessage.getFrom());
            Log.d(TAG, "Message Notification body: " + remoteMessage.getNotification().getBody());
        }   //remoteNotification && .getNotification are not NULL   //Notification sent correctly       // This executes if notification is coming from Firebase , NOT FROM YUCELYAVUZ

    }

    public HashMap parseYucelYavuz(String str) {
        ArrayList<String> arrVal = new ArrayList<>();
        ArrayList<String> arrKey = new ArrayList<>();
        HashMap<String, String> hmp = new HashMap<>();
        Log.d(TAG, "message -> " + str);
//        String str = "{data={\"image\":\"no\",\"title\":\"titleteti\",\"message\":\"Transmiteti\",}}";
        Pattern patternValue = Pattern.compile("\"(.*?)\"");
        Matcher matcherValue = patternValue.matcher(str);
        int i = 0;
        while (matcherValue.find()) {
            if (i % 2 == 0) {
                Log.d("PatternMatchers", "Key ->> " + matcherValue.group(0));
                arrKey.add(matcherValue.group(0));
            } else {
                Log.d("PatternMatchers", "Val->> " + matcherValue.group(0));
                arrVal.add(matcherValue.group(0));
            }
            i++;
        }

        for (i = 0; i < arrKey.size(); i++) {
            hmp.put(arrKey.get(i), arrVal.get(i));
        }

        return hmp;
    }

    public void inspectNotificationData() {
        if (!isClickable) {       //if user is not logged in
            type = "5";
        }
        this.createChannelHigh();


        switch (Integer.parseInt(type)) {
            case 1:
                Log.d(TAG, "Haber bildirimi");
                myIntent = new Intent(this, haberDetaylari.class);
                myIntent.putExtra("haber_id", "" + id);
                showNotification();
                break;
            case 2:
                Log.d(TAG, "Etkinlik bildirimi");
                myIntent = new Intent(this, etkinlikDetaylari.class);
                myIntent.putExtra("etkinlik_id", "" + id);
                showNotification();
                break;
            case 3:
                Log.d(TAG, "Anket bildirimi");
                myIntent = new Intent(this, anketWebViev.class);
                showNotification();
                break;
            case 4:
                Log.d(TAG, "Normal bildirim");
                myIntent = new Intent(this, bildirimAkisi.class);
                showNotification();
                break;
            case 5:
                Log.d(TAG, "Normal bildirim -- Giriş yapılmamış.");
                myIntent = new Intent(this, SplashActivityMain.class);
                showNotification();
                break;
        }

    }

    public void createChannelHigh() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID_HIGH = "com.example.myfcm.highChannel";
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID_HIGH);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AudioAttributes att = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();

            notificationChannelHigh = new NotificationChannel(NOTIFICATION_CHANNEL_ID_HIGH, "e-STK Bildirim kanalı", NotificationManager.IMPORTANCE_HIGH);
            notificationChannelHigh.setSound(alarmSound, att);
            notificationChannelHigh.setDescription("Test channel");
            notificationChannelHigh.enableLights(true);
            notificationChannelHigh.setShowBadge(true);
            notificationChannelHigh.setLightColor(Color.BLUE);
            notificationChannelHigh.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationChannelHigh.enableVibration(true);
            notificationChannelHigh.setVibrationPattern((new long[]{25, 0, 250, 0, 50,}));
            notificationManager.createNotificationChannel(notificationChannelHigh);

        }
    }

    public void createBuilderHigh(CharSequence csTitle, CharSequence csContent1) {
        notificationBuilder
                .setStyle((new NotificationCompat.InboxStyle()
                        .setBigContentTitle(csTitle)
                        .addLine(csContent1))
                )
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setVibrate((new long[]{50, 0, 0, 50, 0, 0, 0, 200}))
                .setContentInfo("Info")
                .setSound(alarmSound)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true);

    }

    public void showNotification() {

        CharSequence csTitle = "" + title;
        CharSequence csContent1 = "" + body;
        createBuilderHigh(csTitle, csContent1);


        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(contentIntent);

        notificationManager.notify(new Random().nextInt(), notificationBuilder.build());
    }


    public void getAbles() {
        SharedPreferences sp = this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        isClickable = sp.getBoolean("isLogged", false);
        isNotifyable = sp.getBoolean("isNotifyable", true);
    }


    public void saveToken(String token) {
        SharedPreferences xd = this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = xd.edit();

        editor.remove("notificationToken");
        editor.putString("notificationToken", token);
        editor.apply();
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "onRefreshed Token:" + token);
        this.saveToken(token);
    }

}