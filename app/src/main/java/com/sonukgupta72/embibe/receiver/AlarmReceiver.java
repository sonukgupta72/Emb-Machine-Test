package com.sonukgupta72.embibe.receiver;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sonukgupta72.embibe.R;
import com.sonukgupta72.embibe.activity.SplashActivity;
import com.sonukgupta72.embibe.model.MovieDataModel;
import com.sonukgupta72.embibe.sqliteHelper.SQLiteHelperClass;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {
    public static final String LOCAL_NOTIFICATION_BROADCAST = "localBroadcastReceiverNotifications";
    private static final String CHANNEL_ID = "embibe_notification_channel";
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Log.d("RECEIVER", new Date().toString());
        if (!loadJSONFromAssetToDB()) {
            return;
        }

        Intent mIntent = new Intent(context, SplashActivity.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_active_white_24dp)
                .setContentTitle("New Movie available")
                .setContentText("Check this out!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        createNotificationChannel(mNotifyMgr);
        Notification notification = builder.build();
        //Auto cancel on click
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        mNotifyMgr.notify((int) System.currentTimeMillis(), notification);
        //notifying local broadcast
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(LOCAL_NOTIFICATION_BROADCAST));
    }

    private void createNotificationChannel(NotificationManager notificationManager) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "embibe_channel";
            String description = "Notification to notify new movie is added";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager.createNotificationChannel(channel);
        }
    }

    public boolean loadJSONFromAssetToDB() {
        SQLiteHelperClass sqLiteHelperClass = new SQLiteHelperClass(context);
        int dataEntrySize = sqLiteHelperClass.getAllMovieList().size();
        int nextItemIndex = dataEntrySize + 1;

        if (nextItemIndex > 100) {
            //all items are already added
            cancelAlarmManager();
            return false;
        }

        MovieDataModel movieDataModel;
        try {
            InputStream is = context.getAssets().open(nextItemIndex +".json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            movieDataModel = gson.fromJson(json, MovieDataModel.class);
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        if (movieDataModel != null)  {
            sqLiteHelperClass.addMovie(movieDataModel);
        }

        return true;
    }

    private void cancelAlarmManager() {
        if (context == null) return;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        if (alarmManager != null) {
            alarmManager.cancel(alarmIntent);
        }
    }


}
