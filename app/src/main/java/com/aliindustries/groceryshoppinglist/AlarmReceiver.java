package com.aliindustries.groceryshoppinglist;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;


import java.util.Calendar;


public class AlarmReceiver extends BroadcastReceiver{
    private static final String CHANNEL_ID = "com.aliindustries.groceryshoppinglist.channelId";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent notificationIntent = new Intent(context, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context);


        Uri soundUri = Uri.parse("android.resource://" + context.getApplicationContext().getPackageName() + "/" + R.raw.deduction);
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            notification = builder.setContentTitle("MyGroceries")
                    .setContentText("Its been a while since you've created a grocery list. Tap to create one now!")
                    .setTicker("New Notification!")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setWhen(Calendar.getInstance().getTimeInMillis())
                    .setShowWhen(true)
                    .setSound(soundUri)
                    .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                    .setLights(Color.MAGENTA, 500, 500)
                    .setPriority(Notification.PRIORITY_MAX) // or NotificationCompat.PRIORITY_MAX
                    .build();
        }
        else {
            notification = builder.setContentTitle("MyGroceries")
                    .setContentText("Its been a while since you've created a grocery list. Tap to create one now!")
                    .setTicker("New Notification!")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setWhen(Calendar.getInstance().getTimeInMillis())
                    .setSound(soundUri)
                    .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                    .setLights(Color.MAGENTA, 500, 500)
                    .setPriority(Notification.PRIORITY_MAX) // or NotificationCompat.PRIORITY_MAX
                    .build();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "NotificationDemo",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.enableLights(true);
            channel.setLightColor(Color.MAGENTA);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();

            channel.setSound(soundUri, audioAttributes);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[] { 1000, 1000, 1000, 1000, 1000 });

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        if (notificationManager != null) {
            notificationManager.notify(0, notification);
        }
    }

}