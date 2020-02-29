package com.aliindustries.groceryshoppinglist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.aliindustries.groceryshoppinglist.ui.login.loginscreen;

import java.util.Calendar;

public class notificationactivity extends AppCompatActivity {

    Switch aSwitch;
    Switch aSwitch2;

    boolean isswitched = true;
    boolean isswitched2 = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificationactivity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.ic_launcher_background));
        }
        aSwitch = findViewById(R.id.switch1);
        aSwitch2 = findViewById(R.id.switch2);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(notificationactivity.this);
        boolean getbool = prefs.getBoolean("checkswitch", true);
        boolean getbool2 = prefs.getBoolean("checkswitch2", true);

        if(getbool == true) {
            aSwitch.setChecked(true);
        }
        else {
            aSwitch.setChecked(false);
        }
        if(getbool2 == true) {
            aSwitch2.setChecked(true);
        }
        else {
            aSwitch2.setChecked(false);
        }


        if(aSwitch.isChecked() == true) {

            setRepeatedNotification(0,1,0,0);
            isswitched = true;

        }
        else {
            disableAlarmManager(0);
            isswitched  = false;
        }

        if(aSwitch2.isChecked() == true) {

            setRepeatedNotification2(1,2,0,0);
            isswitched2 = true;

        }
        else {
            disableAlarmManager2(1);
            isswitched2  = false;
        }

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true) {
                    setRepeatedNotification(0,1,0,0);
                    isswitched = true;

                }
                else {
                    disableAlarmManager(0);
                    isswitched  = false;

                }
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(notificationactivity.this);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("checkswitch",isswitched);
                editor.apply();

            }
        });


        aSwitch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true) {
                    setRepeatedNotification2(1,2,0,0);
                    isswitched2 = true;

                }
                else {
                    disableAlarmManager2(1);
                    isswitched2  = false;
                }
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(notificationactivity.this);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("checkswitch2",isswitched2);
                editor.apply();

            }
        });

    }


    private void setRepeatedNotification(int ID, int hh, int mm, int ss) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(notificationactivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(notificationactivity.this, ID, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmIntent.setData((Uri.parse("custom://"+System.currentTimeMillis())));
        assert alarmManager != null;
        alarmManager.cancel(pendingIntent);

        Calendar calendar = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hh);
        calendar.set(Calendar.MINUTE, mm);
        calendar.set(Calendar.SECOND, ss);

        //check whether the time is earlier than current time. If so, set it to tomorrow. Otherwise, all alarms for earlier time will fire

        if(calendar.before(now)){
            calendar.add(Calendar.DATE, 1);
        }

        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_HALF_DAY, pendingIntent);
        }

    }
    private void setRepeatedNotification2(int ID, int hh, int mm, int ss) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(notificationactivity.this, SpendReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(notificationactivity.this, ID, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmIntent.setData((Uri.parse("custom://"+System.currentTimeMillis())));
        assert alarmManager != null;
        alarmManager.cancel(pendingIntent);

        Calendar calendar = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hh);
        calendar.set(Calendar.MINUTE, mm);
        calendar.set(Calendar.SECOND, ss);

        //check whether the time is earlier than current time. If so, set it to tomorrow. Otherwise, all alarms for earlier time will fire

        if(calendar.before(now)){
            calendar.add(Calendar.DATE, 1);
        }

        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_HALF_DAY, pendingIntent);
        }

    }
    public void disableAlarmManager(int requestcode){

        Intent intent = new Intent(notificationactivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), requestcode, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.cancel(pendingIntent);
    }
    public void disableAlarmManager2(int requestcode){

        Intent intent = new Intent(notificationactivity.this, SpendReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), requestcode, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.cancel(pendingIntent);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(notificationactivity.this,MainActivity.class));
        finish();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
    }
}
