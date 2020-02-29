package com.aliindustries.groceryshoppinglist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aliindustries.groceryshoppinglist.ui.login.loginscreen;
import com.google.firebase.auth.FirebaseAuth;

import static com.aliindustries.groceryshoppinglist.createList.getMonthName_Abbr;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class moneyspentactivity extends AppCompatActivity {

    HashMap<String,Integer> monthtoInt = new HashMap<>();
    DatabaseHelper myDb;

    ArrayList<String> myweek = new ArrayList<>();
    ArrayList<String> mymonth = new ArrayList<>();
    ArrayList<Integer> myyr = new ArrayList<>();
    ArrayList<String> addallweeks = new ArrayList<>();
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    Calendar start;
    Calendar end;
    int checkedItem = 4;

    int datecounter = 0;

    TextView dateview;
    TextView spent_title;
    TextView already_spent;
    String spentdatetitles = "";

    TextView needed_tospend;
    TextView new_total;
    TextView activitylog;
    Button buttonpaid;
    Button button2unpaid;
    ImageView leftimg;
    ImageView rightimg;
    DecimalFormat decim = new DecimalFormat("0.00");

    String totalspent = "";
    String spent_ischecked1 = "";
    String spent_ischecked0 = "";
    long itemremaining = 0;
    String currencysign = "£";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moneyspentactivity);
        myDb = DatabaseHelper.getInstance(moneyspentactivity.this);
        firebaseAuth = FirebaseAuth.getInstance();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.ic_launcher_background));
        }
        monthtoInt.put("Jan",0);
        monthtoInt.put("Feb",1);
        monthtoInt.put("Mar",2);
        monthtoInt.put("Apr",3);
        monthtoInt.put("May",4);
        monthtoInt.put("Jun",5);
        monthtoInt.put("Jul",6);
        monthtoInt.put("Aug",7);
        monthtoInt.put("Sep",8);
        monthtoInt.put("Oct",9);
        monthtoInt.put("Nov",10);
        monthtoInt.put("Dec",11);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dateview = findViewById(R.id.dateformat);
        leftimg = findViewById(R.id.leftimg);
        rightimg = findViewById(R.id.rightimg);
        spent_title = findViewById(R.id.earnings);
        already_spent = findViewById(R.id.alreadyspent);
        needed_tospend = findViewById(R.id.needspend);
        new_total = findViewById(R.id.newtotal);
        activitylog = findViewById(R.id.activitycheck);
        buttonpaid = findViewById(R.id.paidbtn);
        button2unpaid = findViewById(R.id.unpaidbtn);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() != null &&  firebaseAuth.getCurrentUser().isEmailVerified()) {
                }
                else {

                }

            }
        };
        Cursor cursor1 = myDb.getWeekOrder();

        if (cursor1.moveToFirst()) {
            do {
                 String data2 = cursor1.getString(cursor1.getColumnIndex("WEEK"));
                 myweek.add(data2);
            } while (cursor1.moveToNext());
        }
        cursor1.close();

        Cursor cursor2 = myDb.getMonthOrder();
        if (cursor2.moveToFirst()) {
            do {

                 String data2 = cursor2.getString(cursor2.getColumnIndex("MONTH"));
                 mymonth.add(data2);

            } while (cursor2.moveToNext());
        }
        cursor2.close();

        Cursor cursor3 = myDb.getYrOrder();

        if (cursor3.moveToFirst()) {
            do {

                 int data2 = cursor3.getInt(cursor3.getColumnIndex("YEAR"));
                myyr.add(data2);


            } while (cursor3.moveToNext());
        }
        cursor3.close();


        try {

            String extraction1 = myweek.get(0);
            String extraction2 = myweek.get(0);
            String extraction3 = mymonth.get(0);


            extraction1 = extraction1.substring(0, extraction1.indexOf(" "));
            extraction2 = extraction2.substring(extraction2.indexOf("-") + 2);
            extraction3 = extraction3.substring(0, extraction3.indexOf(" "));

            //24 Feb - 1 Mar 2020

            int first = extraction2.indexOf(" ");

            extraction2 = extraction2.substring(0, first);


            extraction1 = extraction1.trim();
            extraction2 = extraction2.trim();
            extraction3 = extraction3.trim();

            int startday = 0;
            int endday = 0;
            String monthnameabbr = "";
            try {
                startday = Integer.parseInt(extraction1);
                endday = Integer.parseInt(extraction2);
                monthnameabbr = extraction3;
            } catch (Exception e) {
                e.printStackTrace();

            }

            start = Calendar.getInstance();
            end = Calendar.getInstance();


            try {
                int monthnameabbr_toint = monthtoInt.get(monthnameabbr);
                int yr = myyr.get(0);
                end.set(Calendar.DAY_OF_MONTH, endday);
                end.set(Calendar.MONTH, monthnameabbr_toint);
                end.set(Calendar.YEAR, yr);
                end.set(Calendar.HOUR_OF_DAY, 0);
                end.set(Calendar.MINUTE, 0);
                end.set(Calendar.SECOND, 0);
                int checker1 = monthnameabbr_toint;

                if (endday < startday) {
                    if (monthnameabbr_toint == 0) {
                        monthnameabbr_toint = 12;
                    }
                    monthnameabbr_toint = monthnameabbr_toint - 1;
                }

                int checker2 = monthnameabbr_toint;

                if (checker1 == 0 && checker2 == 11) {
                    yr = yr - 1;
                }

                start.set(Calendar.DAY_OF_MONTH, startday);
                start.set(Calendar.MONTH, monthnameabbr_toint);
                start.set(Calendar.YEAR, yr);
                start.set(Calendar.HOUR_OF_DAY, 0);
                start.set(Calendar.MINUTE, 0);
                start.set(Calendar.SECOND, 0);
                System.out.println("this is the starttime: " + start.getTime().toString());
                System.out.println("this is the endttime: " + end.getTime().toString());


                Calendar now = Calendar.getInstance();

                now.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                now.set(Calendar.HOUR_OF_DAY, 23);
                now.set(Calendar.MINUTE, 59);
                now.set(Calendar.SECOND, 59);

                int counter = end.get(Calendar.DAY_OF_MONTH);

                while (end.before(now)) {
                    end.set(Calendar.DAY_OF_MONTH, end.get(Calendar.DAY_OF_MONTH) - 6); // monday
                    int k2 = end.get(Calendar.DAY_OF_MONTH);
                    String k3 = getMonthName_Abbr(end.get(Calendar.MONTH));
                    end.set(Calendar.DAY_OF_MONTH, end.get(Calendar.DAY_OF_MONTH) + 6);  //sunday
                    String s = k2 + " " + k3 + " - " + end.get(Calendar.DAY_OF_MONTH) + " " + getMonthName_Abbr(end.get(Calendar.MONTH)) + " " + end.get(Calendar.YEAR);
                    System.out.println("this is the string: " + s);
                    addallweeks.add(s);

                    counter = end.get(Calendar.DAY_OF_MONTH);
                    counter = counter + 7;
                    end.set(Calendar.DAY_OF_MONTH, counter);
                    end.set(Calendar.HOUR_OF_DAY, 0);
                    end.set(Calendar.MINUTE, 0);
                    end.set(Calendar.SECOND, 0);

                }

            } catch (Exception e) {
                e.printStackTrace();

            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }




        datecounter = addallweeks.size()-1;

        if(addallweeks.size() > 0) {

            dateview.setText(addallweeks.get(addallweeks.size()-1));
            rightimg.setVisibility(View.INVISIBLE);

            if(addallweeks.size() == 1) {
                leftimg.setVisibility(View.INVISIBLE);
            }
        }
        else {
            leftimg.setVisibility(View.INVISIBLE);
            rightimg.setVisibility(View.INVISIBLE);
        }

       leftimg.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               if(datecounter >0) {
                   datecounter = datecounter - 1;
                       dateview.setText(addallweeks.get(datecounter));
                   leftimg.setVisibility(View.VISIBLE);
                   rightimg.setVisibility(View.VISIBLE);
                   }
               if(datecounter == 0) {
                   leftimg.setVisibility(View.INVISIBLE);
                   rightimg.setVisibility(View.VISIBLE);
               }
               if(datecounter == 0 && addallweeks.size() == 1) {
                   leftimg.setVisibility(View.INVISIBLE);
                   rightimg.setVisibility(View.INVISIBLE);

               }
               totalspent = myDb.getTotalSumSpent(dateview.getText().toString().trim());
               spent_ischecked0 = myDb.getTotalSumSpent_ischecked(dateview.getText().toString().trim(),0);
               spent_ischecked1 = myDb.getTotalSumSpent_ischecked(dateview.getText().toString().trim(),1);
               itemremaining = myDb.getItemRemainingCount(dateview.getText().toString().trim(),0,getResources().getString(R.string.itemidentifier));

               spent_title.setText(currencysign + totalspent);
               already_spent.setText(currencysign + spent_ischecked1);
               needed_tospend.setText(currencysign + spent_ischecked0);
               new_total.setText(Long.toString(itemremaining));

               if(myDb.getTotalSumSpent(dateview.getText().toString().trim()).equals("0.00")) {
                   activitylog.setText("No activity this week");
               }
               else {
                   activitylog.setText("Your activity this week");
               }

           }
       });

        rightimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(datecounter < addallweeks.size()-1) {
                    datecounter = datecounter + 1;
                        dateview.setText(addallweeks.get(datecounter));
                    leftimg.setVisibility(View.VISIBLE);
                    rightimg.setVisibility(View.VISIBLE);
                }
                if(datecounter == addallweeks.size()-1) {
                    rightimg.setVisibility(View.INVISIBLE);
                    leftimg.setVisibility(View.VISIBLE);
                }
                if(datecounter == addallweeks.size()-1 && addallweeks.size() == 1) {
                    leftimg.setVisibility(View.INVISIBLE);
                    rightimg.setVisibility(View.INVISIBLE);

                }

                totalspent = myDb.getTotalSumSpent(dateview.getText().toString().trim());
                spent_ischecked0 = myDb.getTotalSumSpent_ischecked(dateview.getText().toString().trim(),0);
                spent_ischecked1 = myDb.getTotalSumSpent_ischecked(dateview.getText().toString().trim(),1);
                itemremaining = myDb.getItemRemainingCount(dateview.getText().toString().trim(),0,getResources().getString(R.string.itemidentifier));

                spent_title.setText(currencysign + totalspent);
                already_spent.setText(currencysign + spent_ischecked1);
                needed_tospend.setText(currencysign + spent_ischecked0);
                new_total.setText(Long.toString(itemremaining));

                if(myDb.getTotalSumSpent(dateview.getText().toString().trim()).equals("0.00")) {
                    activitylog.setText("No activity this week");
                }
                else {
                    activitylog.setText("Your activity this week");
                }

            }
        });

        totalspent = myDb.getTotalSumSpent(dateview.getText().toString().trim());
        spent_ischecked0 = myDb.getTotalSumSpent_ischecked(dateview.getText().toString().trim(),0);
        spent_ischecked1 = myDb.getTotalSumSpent_ischecked(dateview.getText().toString().trim(),1);
        itemremaining = myDb.getItemRemainingCount(dateview.getText().toString().trim(),0,getResources().getString(R.string.itemidentifier));

        spent_title.setText(currencysign + totalspent);
        already_spent.setText(currencysign + spent_ischecked1);
        needed_tospend.setText(currencysign + spent_ischecked0);
        new_total.setText(Long.toString(itemremaining));

        if(myDb.getTotalSumSpent(dateview.getText().toString().trim()).equals("0.00")) {
            activitylog.setText("No activity this week");
        }
        else {
            activitylog.setText("Your activity this week");
        }



        buttonpaid.setOnClickListener(new View.OnClickListener() {
            private long mLastClickTime = 0;

            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                spentdatetitles = dateview.getText().toString().trim();
                Intent i = new Intent(moneyspentactivity.this,itempaid.class);
                i.putExtra("paiditems",spentdatetitles);

                startActivity(i);
                overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);


            }
        });

        button2unpaid.setOnClickListener(new View.OnClickListener() {
            private long mLastClickTime = 0;

            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                spentdatetitles = dateview.getText().toString().trim();
                Intent i = new Intent(moneyspentactivity.this,itemunpaid.class);
                i.putExtra("paiditems",spentdatetitles);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);

            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.moneyactivity_menu, menu);
        MenuItem item = menu.findItem(R.id.signout2);

        if(firebaseAuth.getCurrentUser() != null &&  firebaseAuth.getCurrentUser().isEmailVerified()) {
            item.setTitle("Sign out");
        }
        else {
            item.setTitle("Sign in");

        }


        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.changecurrency25:
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(moneyspentactivity.this);
                alertDialog.setTitle("Change currency");
                String[] items = {"$ U.S. dollar (USD)","¥ Japanese Yen (JPY)","Fr Swiss Franc (CHF)","€ European Euro (EUR)","£ British Pound (GBP)","C$ Canadian Dollar (CAD)","R South African Rand (ZAR)","₺ Turkish Lira (TL)","₹ Indian rupee (INR)","RM Malaysian ringgit (MYR)"};

                alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String s1 = totalspent;
                        String s2 = spent_ischecked0; //needed
                        String s3 = spent_ischecked1;

                        switch (which) {
                            case 0:
                                currencysign = "$";
                                checkedItem = 0;
                                spent_title.setText(currencysign + s1);
                                needed_tospend.setText(currencysign + s2);
                                already_spent.setText(currencysign + s3);

                                dialog.dismiss();
                                break;
                            case 1:
                                currencysign = "¥";
                                checkedItem = 1;
                                spent_title.setText(currencysign + s1);
                                needed_tospend.setText(currencysign + s2);
                                already_spent.setText(currencysign + s3);
                                dialog.dismiss();
                                break;
                            case 2:
                                currencysign = "Fr";
                                checkedItem = 2;
                                spent_title.setText(currencysign + s1);
                                needed_tospend.setText(currencysign + s2);
                                already_spent.setText(currencysign + s3);
                                dialog.dismiss();
                                break;
                            case 3:
                                currencysign = "€";
                                checkedItem = 3;
                                spent_title.setText(currencysign + s1);
                                needed_tospend.setText(currencysign + s2);
                                already_spent.setText(currencysign + s3);
                                dialog.dismiss();
                                break;
                            case 4:
                                currencysign = "£";
                                checkedItem = 4;
                                spent_title.setText(currencysign + s1);
                                needed_tospend.setText(currencysign + s2);
                                already_spent.setText(currencysign + s3);
                                dialog.dismiss();
                                break;
                            case 5:
                                currencysign = "C$";
                                checkedItem = 5;
                                spent_title.setText(currencysign + s1);
                                needed_tospend.setText(currencysign + s2);
                                already_spent.setText(currencysign + s3);
                                dialog.dismiss();
                                break;
                            case 6:
                                currencysign = "R";
                                checkedItem = 6;
                                spent_title.setText(currencysign + s1);
                                needed_tospend.setText(currencysign + s2);
                                already_spent.setText(currencysign + s3);
                                dialog.dismiss();
                                break;
                            case 7:
                                currencysign = "₺";
                                checkedItem = 7;
                                spent_title.setText(currencysign + s1);
                                needed_tospend.setText(currencysign + s2);
                                already_spent.setText(currencysign + s3);
                                dialog.dismiss();
                                break;
                            case 8:
                                currencysign = "₹";
                                checkedItem = 8;
                                spent_title.setText(currencysign + s1);
                                needed_tospend.setText(currencysign + s2);
                                already_spent.setText(currencysign + s3);
                                dialog.dismiss();
                                break;
                            case 9:
                                currencysign = "RM";
                                checkedItem = 9;
                                spent_title.setText(currencysign + s1);
                                needed_tospend.setText(currencysign + s2);
                                already_spent.setText(currencysign + s3);
                                dialog.dismiss();
                                break;


                        }
                    }
                });
                AlertDialog alert = alertDialog.create();
                alert.setCanceledOnTouchOutside(true);

                alert.show();
                break;
            case R.id.signout2:

                if(item.getTitle().toString().trim().equals("Sign in")) {
                    startActivity(new Intent(moneyspentactivity.this, loginscreen.class));
                    finish();
                    overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);

                }
                else {
                    FirebaseAuth.getInstance().signOut();
                    item.setTitle("Sign in");

                }
                break;
            default:
                return false;
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(moneyspentactivity.this,MainActivity.class));
        finish();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);



    }


}
