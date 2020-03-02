package com.aliindustries.groceryshoppinglist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.aliindustries.groceryshoppinglist.ui.login.loginscreen;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.aliindustries.groceryshoppinglist.CustomAdapter5.mc_currentcurrency2;

public class itemunpaid extends AppCompatActivity {
    DatabaseHelper myDb;
    ListView listView;
    int count = 0;
    LinearLayout noitemlayout;
    TextView textView;
    ArrayList<String> o_item;
    ArrayList<String> listtitle;
    CustomAdapter5 customAdapter5;
    ArrayList<Integer> qty;
    DecimalFormat decim = new DecimalFormat("0.00");
    String spenttitle = "";
    ArrayList<Double> price;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    int checkedItem = 4;
    double total = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itempaid);
        listView = (ListView) findViewById(R.id.listview392);
        noitemlayout = (LinearLayout) findViewById(R.id.lin2500);
        textView = findViewById(R.id.textView756);
        myDb = DatabaseHelper.getInstance(itemunpaid.this);
        firebaseAuth = FirebaseAuth.getInstance();
        Bundle extras = getIntent().getExtras();
        o_item = new ArrayList<>();
        listtitle = new ArrayList<>();
        qty = new ArrayList<>();
        price = new ArrayList<>();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.ic_launcher_background));
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (extras != null) {
            spenttitle = extras.getString("paiditems");
        }

        setTitle("Unpaid: " + spenttitle);


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() != null &&  firebaseAuth.getCurrentUser().isEmailVerified()) {
                }
                else {

                }

            }
        };

        count = (int) myDb.getItemWeekIsCheckedCount(spenttitle,0,getResources().getString(R.string.itemidentifier));
        if(count <= 0) {

            noitemlayout.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
        else {
            noitemlayout.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }

        Cursor cursor1 = myDb.getItemWeekIsChecked(spenttitle,0, getResources().getString(R.string.itemidentifier));

        if (cursor1.moveToFirst()) {
            do {
                String data = cursor1.getString(cursor1.getColumnIndex("ITEM"));
                int data2 = cursor1.getInt(cursor1.getColumnIndex("QUANTITY"));
                double data3 = cursor1.getDouble(cursor1.getColumnIndex("PRICE"));
                String data4 = cursor1.getString(cursor1.getColumnIndex("TITLE"));

                o_item.add(data);
                qty.add(data2);
                price.add(data3);
                listtitle.add(data4);

                // do what ever you want here
            } while (cursor1.moveToNext());
        }
        cursor1.close();


        if(qty.size() > 0) {

            for(int i = 0; i < qty.size();i++) {

                total = total + (qty.get(i) * price.get(i));

            }
            total = round(total,2);
            String s = decim.format(total);

            textView.setText("Total: " + mc_currentcurrency2 + s);

        }

        else {

            textView.setText("Total: " + mc_currentcurrency2 + "0.00");

        }
        customAdapter5 = new CustomAdapter5(itemunpaid.this,o_item,qty,price,listtitle);

        listView.setAdapter(customAdapter5);

        TextView empty = new TextView(this);
        empty.setHeight(90);
        listView.addFooterView(empty);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.itempaidmenu, menu);
        MenuItem item = menu.findItem(R.id.signout3);

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
            case R.id.changecurrency1125:
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(itemunpaid.this);
                alertDialog.setTitle("Change currency");
                String[] items = {"$ U.S. dollar (USD)","¥ Japanese Yen (JPY)","Fr Swiss Franc (CHF)","€ European Euro (EUR)","£ British Pound (GBP)","C$ Canadian Dollar (CAD)","R South African Rand (ZAR)","₺ Turkish Lira (TL)","₹ Indian rupee (INR)","RM Malaysian ringgit (MYR)"};

                alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String s = decim.format(total);
                        switch (which) {
                            case 0:
                                mc_currentcurrency2 = "$";
                                checkedItem = 0;
                                textView.setText("Total: " + mc_currentcurrency2 + s);
                                dialog.dismiss();
                                customAdapter5.notifyDataSetChanged();
                                break;
                            case 1:
                                mc_currentcurrency2 = "¥";
                                checkedItem = 1;
                                textView.setText("Total: " + mc_currentcurrency2 + s);
                                dialog.dismiss();
                                customAdapter5.notifyDataSetChanged();
                                break;
                            case 2:
                                mc_currentcurrency2 = "Fr";
                                checkedItem = 2;
                                textView.setText("Total: " + mc_currentcurrency2 + s);
                                dialog.dismiss();
                                customAdapter5.notifyDataSetChanged();
                                break;
                            case 3:
                                mc_currentcurrency2 = "€";
                                checkedItem = 3;
                                textView.setText("Total: " + mc_currentcurrency2 + s);
                                dialog.dismiss();
                                customAdapter5.notifyDataSetChanged();
                                break;
                            case 4:
                                mc_currentcurrency2 = "£";
                                checkedItem = 4;
                                textView.setText("Total: " + mc_currentcurrency2 + s);
                                dialog.dismiss();
                                customAdapter5.notifyDataSetChanged();
                                break;
                            case 5:
                                mc_currentcurrency2 = "C$";
                                checkedItem = 5;
                                textView.setText("Total: " + mc_currentcurrency2 + s);
                                dialog.dismiss();
                                customAdapter5.notifyDataSetChanged();
                                break;
                            case 6:
                                mc_currentcurrency2 = "R";
                                checkedItem = 6;
                                textView.setText("Total: " + mc_currentcurrency2 + s);
                                dialog.dismiss();
                                customAdapter5.notifyDataSetChanged();
                                break;
                            case 7:
                                mc_currentcurrency2 = "₺";
                                checkedItem = 7;
                                textView.setText("Total: " + mc_currentcurrency2 + s);
                                dialog.dismiss();
                                customAdapter5.notifyDataSetChanged();
                                break;
                            case 8:
                                mc_currentcurrency2 = "₹";
                                checkedItem = 8;
                                textView.setText("Total: " + mc_currentcurrency2 + s);
                                dialog.dismiss();
                                customAdapter5.notifyDataSetChanged();
                                break;
                            case 9:
                                mc_currentcurrency2 = "RM";
                                checkedItem = 9;
                                textView.setText("Total: " + mc_currentcurrency2 + s);
                                dialog.dismiss();
                                customAdapter5.notifyDataSetChanged();
                                break;


                        }
                    }
                });
                AlertDialog alert = alertDialog.create();
                alert.setCanceledOnTouchOutside(true);

                alert.show();
                break;
            case R.id.signout3:

                if(item.getTitle().toString().trim().equals("Sign in")) {
                    startActivity(new Intent(itemunpaid.this, loginscreen.class));
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

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(itemunpaid.this,moneyspentactivity.class));
        finish();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);

    }
}
