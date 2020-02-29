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


import java.text.SimpleDateFormat;
import java.util.Calendar;



public class SpendReceiver extends BroadcastReceiver{
    private static final String CHANNEL_ID2 = "com.aliindustries.groceryshoppinglist.channelId2";
    DatabaseHelper myDb;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent notificationIntent = new Intent(context, moneyspentactivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(moneyspentactivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
        myDb = DatabaseHelper.getInstance(context);

        Notification.Builder builder = new Notification.Builder(context);

        Calendar calendarsunday = Calendar.getInstance();
        Calendar calendarmonday = Calendar.getInstance();

        calendarmonday.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        calendarmonday.set(Calendar.HOUR_OF_DAY, 0);
        calendarmonday.set(Calendar.MINUTE, 0);
        calendarmonday.set(Calendar.SECOND, 0);

        calendarsunday.set(Calendar.DAY_OF_MONTH,calendarmonday.get(Calendar.DAY_OF_MONTH)+6);
        calendarsunday.set(Calendar.HOUR_OF_DAY, 23);
        calendarsunday.set(Calendar.MINUTE, 59);
        calendarsunday.set(Calendar.SECOND, 59);

        int k2 = calendarmonday.get(Calendar.DAY_OF_MONTH);
        String k3 = getMonthName_Abbr(calendarmonday.get(Calendar.MONTH));
        String s = k2 + " " + k3 + " - " + calendarsunday.get(Calendar.DAY_OF_MONTH) + " " + getMonthName_Abbr(calendarsunday.get(Calendar.MONTH)) + " " + calendarsunday.get(Calendar.YEAR);

        String s2 = "£0.00";

        long count = myDb.getAllCount();

        if(count > 0) {

            s2 = "£"+myDb.getTotalSumSpent(s);
        }



        Uri soundUri = Uri.parse("android.resource://" + context.getApplicationContext().getPackageName() + "/" + R.raw.deduction);
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            notification = builder.setContentTitle(s)
                    .setContentText("Total spent this week: " + s2)
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
            notification = builder.setContentTitle(s)
                    .setContentText("Total spent this week: " + s2)
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
            builder.setChannelId(CHANNEL_ID2);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID2,
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
            notificationManager.notify(1, notification);
        }
    }

    public static String getMonthName_Abbr(int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, month);
        SimpleDateFormat month_date = new SimpleDateFormat("MMM");
        String month_name = month_date.format(cal.getTime());


        return month_name;
    }




}