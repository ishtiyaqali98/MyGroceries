package com.aliindustries.groceryshoppinglist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.aliindustries.groceryshoppinglist.ui.login.loginscreen;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    ListView resultsListView;
    LinearLayout nolistlayout;
    ArrayList<String> userSelection = new ArrayList<String>();
    FirebaseAuth firebaseAuth;
    DatabaseReference mref;
    ArrayList<addFirebaseList> addFirebaseLists = new ArrayList<>();


    ArrayList<String> datecreated;

    ArrayList<String> mtitle;
    ArrayList<Integer> progress;
    String olditem = "";
    String newitem = "";
    Button login;
    ArrayList<String> progresstxt;
    DatabaseHelper myDb;
    CustomAdapter2 adapter;
    ArrayList<Integer> posdelete = new ArrayList<>();
    int count = 0;
    int count2 = 0;
    String emailfirebasenode = "";

    ArrayList<Integer> ischeckedcount = new ArrayList<>();
    ArrayList<Integer> totalitemcount = new ArrayList<>();
    private FirebaseAuth.AuthStateListener mAuthListener;

    String signinmethod = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        resultsListView = (ListView) findViewById(R.id.listview1);
        nolistlayout = (LinearLayout) findViewById(R.id.lin95);
        myDb = DatabaseHelper.getInstance(MainActivity.this);
        firebaseAuth = FirebaseAuth.getInstance();
        mref = FirebaseDatabase.getInstance().getReference();

        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, createList.class));
                overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
            }
        });
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        boolean getbool = prefs.getBoolean("checkswitch", true);
        boolean getbool2 = prefs.getBoolean("checkswitch2", true);

        if(getbool == true) {
            setRepeatedNotification(0,1,0,0);
        }
        else {
            disableAlarmManager(0);
        }
        if(getbool2 == true) {
            setRepeatedNotification2(1,2,0,0);
        }
        else {
            disableAlarmManager2(1);
        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.ic_launcher_background));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_share)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        count = (int) myDb.getTitleCount();
        int counter = 0;
        mtitle = new ArrayList<String>();
        progress = new ArrayList<Integer>();
        progresstxt = new ArrayList<String>();
        datecreated = new ArrayList<>();

        navigationView.bringToFront();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.nav_notify: {

                        startActivity(new Intent(MainActivity.this, notificationactivity.class));
                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                        break;
                    }
                    case R.id.spent: {
                        int counts = (int) myDb.getTitleCount();

                        if (counts > 0) {
                            startActivity(new Intent(MainActivity.this, moneyspentactivity.class));
                            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                        } else {
                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Create a list!", Snackbar.LENGTH_LONG);
                            snackbar.show();

                        }
                        break;
                    }
                    case R.id.nav_share: {
                        try {
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                            String shareMessage = "\nLet me recommend you this application\n\n";
                            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                            startActivity(Intent.createChooser(shareIntent, "choose one"));
                        } catch (Exception e) {
                            //e.toString();
                        }
                        break;
                    }
                }
                return true;

            }
        });

        View header = navigationView.getHeaderView(0);
        login = header.findViewById(R.id.loginbutton);


        login.setOnClickListener(new View.OnClickListener() {
            private long mLastClickTime = 0;

            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                if (login.getText().toString().trim().equals("Sign in")) {
                    startActivity(new Intent(MainActivity.this, loginscreen.class));
                } else {
                    FirebaseAuth.getInstance().signOut();
                }


            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {


                if (firebaseAuth.getCurrentUser() != null && firebaseAuth.getCurrentUser().isEmailVerified()) {
                    login.setText("Sign in");

                    login.setText("Sign out");
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Signed in!", Snackbar.LENGTH_LONG);
                    snackbar.show();

                } else {
                    login.setText("Sign in");

                }

            }
        };


        if (count <= 0) {

            nolistlayout.setVisibility(View.VISIBLE);
            resultsListView.setVisibility(View.GONE);
        } else {
            nolistlayout.setVisibility(View.GONE);
            resultsListView.setVisibility(View.VISIBLE);
        }


        String identifier = getResources().getString(R.string.itemidentifier);

        Cursor cursor1 = myDb.getTitle(identifier);


        if (cursor1.moveToFirst()) {
            do {
                String data = cursor1.getString(cursor1.getColumnIndex("TITLE"));
                String data2 = cursor1.getString(cursor1.getColumnIndex("DATECREATED"));

                if (counter < count) {
                    mtitle.add(data);
                    datecreated.add(data2);
                    counter++;
                }

                // do what ever you want here
            } while (cursor1.moveToNext());
        }
        cursor1.close();


        Collections.sort(mtitle);


        for (int i = 0; i < mtitle.size(); i++) {

            int k = (int) myDb.getIsCheckedCount(mtitle.get(i), 1, getResources().getString(R.string.itemidentifier));
            int z = (int) myDb.getItemCount(mtitle.get(i), getResources().getString(R.string.itemidentifier));
            ischeckedcount.add(k);
            totalitemcount.add(z);

        }


        resultsListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        resultsListView.setMultiChoiceModeListener(modeListener);

        for (int i = 0; i < count; i++) {

            if (totalitemcount.get(i) != 0) {
                double zx = ((double) ischeckedcount.get(i) / (double) totalitemcount.get(i)) * (double) 100;
                int n = (int) Math.ceil(zx);

                progress.add(n);
            } else {
                progress.add(0);
            }

            progresstxt.add(ischeckedcount.get(i) + " of " + totalitemcount.get(i));

        }

        adapter = new CustomAdapter2(this, mtitle, progress, progresstxt, datecreated);
        resultsListView.setAdapter(adapter);

        resultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            private long mLastClickTime = 0;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Intent i = new Intent(MainActivity.this, ItemActivity.class);
                i.putExtra("c_title", mtitle.get(position));

                startActivity(i);
                overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);

            }
        });


        if (firebaseAuth.getCurrentUser() != null && firebaseAuth.getCurrentUser().isEmailVerified()) {
            try {
                emailfirebasenode = firebaseAuth.getCurrentUser().getEmail();
                emailfirebasenode = emailfirebasenode.replace(".", "");
                signinmethod = FirebaseAuth.getInstance().getCurrentUser().getIdToken(false).getResult().getSignInProvider().toString().trim();
                if (!signinmethod.trim().equals("password")) {

                } else {
                    emailfirebasenode = emailfirebasenode + "10125signincode";
                }
                String emaileditextstring = emailfirebasenode;
                String signedinemailaddress = emaileditextstring;
                signinmethod = FirebaseAuth.getInstance().getCurrentUser().getIdToken(false).getResult().getSignInProvider().toString().trim();


                count2 = (int) myDb.getAllCount();
                Cursor cursor2 = myDb.getAllData();
                if (cursor2.moveToFirst()) {
                    do {
                        int data0 = cursor2.getInt(cursor2.getColumnIndex("ID"));
                        final String data1 = cursor2.getString(cursor2.getColumnIndex("TITLE"));
                        String data2 = cursor2.getString(cursor2.getColumnIndex("ITEM"));
                        int data3 = cursor2.getInt(cursor2.getColumnIndex("ISCHECKED"));
                        int data4 = cursor2.getInt(cursor2.getColumnIndex("QUANTITY"));
                        double data5 = cursor2.getDouble(cursor2.getColumnIndex("PRICE"));
                        String data6 = cursor2.getString(cursor2.getColumnIndex("WEEK"));
                        String data7 = cursor2.getString(cursor2.getColumnIndex("MONTH"));
                        int data8 = cursor2.getInt(cursor2.getColumnIndex("YEAR"));
                        long data9 = cursor2.getLong(cursor2.getColumnIndex("DATEINMS"));
                        String data10 = cursor2.getString(cursor2.getColumnIndex("DATECREATED"));


                        if (count2 > 0 && addFirebaseLists.size() <= count2) {
                            addFirebaseList addFirebaseList = new addFirebaseList(data0, data1, data2, data3, data4, data5, data6, data7, data8, data9, data10);

                            addFirebaseLists.add(addFirebaseList);
                        }

                        mref = FirebaseDatabase.getInstance().getReference(signedinemailaddress);
                        mref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    String uid = ds.getKey();
                                    if(!uid.trim().equals("hashpassword") && !uid.trim().equals("id") && !uid.trim().equals("email")) {
                                        snapshot.child(uid).getRef().removeValue();
                                    }
                                }
                                if (addFirebaseLists.size() > 0) {
                                    for (int i = 0; i < addFirebaseLists.size(); i++) {
                                        int identifier1 = addFirebaseLists.get(i).getID();
                                        String thedata1 = addFirebaseLists.get(i).getTitle();

                                        if (snapshot.child(identifier1 + thedata1).child(identifier1 + thedata1 + "item").hasChild(Integer.toString(addFirebaseLists.get(i).getID()))) {
                                        } else {




                                            int identifier2 = addFirebaseLists.get(i).getID();
                                            mref.child(identifier2 + thedata1).child(identifier2 + thedata1 + "item").setValue(addFirebaseLists.get(i));
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        // do what ever you want here
                    } while (cursor2.moveToNext());
                }
                cursor2.close();

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
    public static String justAlphaChars(String text) {

        text = text.replaceAll("[^A-Za-z0-9]","");

        return text;
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        startActivity(getIntent());
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.exitapp:
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
                finish();
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthListener);

    }

    AbsListView.MultiChoiceModeListener modeListener  = new AbsListView.MultiChoiceModeListener() {
        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            try
            {
                if (checked)
                {
                    adapter.setNewSelection(position, checked);
                }
                else
                {
                    adapter.removeSelection(position);
                }
                adapter.notifyDataSetChanged();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            if(userSelection.contains(mtitle.get(position))) {

                userSelection.remove(mtitle.get(position));
                posdelete.remove(Integer.valueOf(position));

            }
            else {
                userSelection.add(mtitle.get(position));
                posdelete.add(position);

            }
            mode.setTitle(userSelection.size() + " selected");


            if(posdelete.size() == 1) {
                olditem = mtitle.get(posdelete.get(0));
            }
            else {
                olditem = "";
            }

            mode.invalidate();

        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater menuInflater = mode.getMenuInflater();
            menuInflater.inflate(R.menu.contextmenu,menu);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            MenuItem item = menu.findItem(R.id.edit);

            if(posdelete.size() > 1) {
                item.setVisible(false);
                item.setEnabled(false);
            }
            else {
                item.setVisible(true);
                item.setEnabled(true);
            }

            return true;
        }



        @Override
        public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {

            switch (item.getItemId()) {

                case R.id.edit:

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Set shopping list");
                    View viewInflated = LayoutInflater.from(MainActivity.this).inflate(R.layout.edittextdialog, (ViewGroup) findViewById(android.R.id.content), false);
                    final EditText input = (EditText) viewInflated.findViewById(R.id.editText55);
                    builder.setView(viewInflated);

                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            newitem = input.getText().toString().trim();
                            newitem = newitem.replace("'", "");

                            boolean bc = newitem.matches(".*[a-zA-Z].*");
                            if (bc == false && !myDb.titleExists(newitem)) {
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Enter item name", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                            else if (bc == true && myDb.titleExists(newitem)) {
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "This item already exists!", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                            else {

                                Cursor cursor1 = myDb.getAllTitleRows(olditem);

                                if (cursor1.moveToFirst()) {
                                    do {
                                        int data = cursor1.getInt(cursor1.getColumnIndex("ID"));
                                        final String data2 = cursor1.getString(cursor1.getColumnIndex("ITEM"));
                                        final int data3 = cursor1.getInt(cursor1.getColumnIndex("ISCHECKED"));
                                        final double data4 = cursor1.getDouble(cursor1.getColumnIndex("PRICE"));
                                        final int data5 = cursor1.getInt(cursor1.getColumnIndex("QUANTITY"));
                                        final String data6 = cursor1.getString(cursor1.getColumnIndex("WEEK"));
                                        final String data7 = cursor1.getString(cursor1.getColumnIndex("MONTH"));
                                        final int data8 = cursor1.getInt(cursor1.getColumnIndex("YEAR"));
                                        final long data9 = cursor1.getLong(cursor1.getColumnIndex("DATEINMS"));
                                        final String data10 = cursor1.getString(cursor1.getColumnIndex("DATECREATED"));
                                        myDb.updateTitle(Integer.toString(data),newitem);

                                        try {
                                            if (firebaseAuth.getCurrentUser() != null) {
                                                if (isNetworkAvailable() == false) {
                                                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Internet connection is required when signing in!", Snackbar.LENGTH_LONG);
                                                    snackbar.show();
                                                } else {
                                                    emailfirebasenode = firebaseAuth.getCurrentUser().getEmail();
                                                    emailfirebasenode = emailfirebasenode.replace(".", "");
                                                    signinmethod = FirebaseAuth.getInstance().getCurrentUser().getIdToken(false).getResult().getSignInProvider().toString().trim();
                                                    if (!signinmethod.trim().equals("password")) {

                                                    } else {
                                                        emailfirebasenode = emailfirebasenode + "10125signincode";
                                                    }

                                                    final String mydata = data + justAlphaChars(olditem);
                                                    System.out.println("all the itemidnames: " + mydata);

                                                    mref = FirebaseDatabase.getInstance().getReference(emailfirebasenode).child(mydata);
                                                    final int finalTempid = data;

                                                    mref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot snapshot) {
                                                            ArrayList<String> itemkey = new ArrayList<String>();
                                                            for (DataSnapshot ds : snapshot.getChildren()) {
                                                                String uid = ds.getKey();
                                                                itemkey.add(uid);
                                                            }
                                                            String myitemkey = "";
                                                            for (int i = 0; i < itemkey.size(); i++) {
                                                                Object obj1 = snapshot.child(itemkey.get(i)).child("id").getValue();
                                                                int id = -1;
                                                                try {
                                                                    id = Integer.parseInt(obj1.toString().trim());
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }
                                                                if (id == finalTempid) {
                                                                    myitemkey = itemkey.get(i);
                                                                    break;
                                                                }
                                                            }
                                                            //find better/faster solution
                                                            if (!myitemkey.equals("")) {
                                                                String thenewitem = finalTempid + justAlphaChars(newitem);
                                                                String myitemkey2 = thenewitem + "item";
                                                                System.out.println("the newitem is: " + thenewitem);
                                                                DatabaseReference newmref = FirebaseDatabase.getInstance().getReference(emailfirebasenode).child(thenewitem);
                                                                DatabaseReference newmref2 = FirebaseDatabase.getInstance().getReference(emailfirebasenode).child(mydata);

                                                                newmref.child(myitemkey2).setValue(new addFirebaseList(finalTempid, newitem, data2, data3, data5, data4,data6,data7,data8,data9,data10));
                                                                newmref2.removeValue();

                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });
                                                }
                                            }
                                        }
                                        catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        // do what ever you want here
                                    } while (cursor1.moveToNext());
                                }
                                cursor1.close();

                                int retval = mtitle.indexOf(olditem);

                                if (retval >= 0) {
                                    mtitle.set(retval, newitem);
                                    //qty.set
                                }

                                if (mode != null) {
                                    mode.finish();
                                }
                                adapter.notifyDataSetChanged();

                                dialog.dismiss();



                            }
                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();

                        }
                    });

                    builder.show();

                    break;


                case R.id.delete:
                    Cursor cursor2 = null;
                    ArrayList<Integer> idarraylist = new ArrayList<>();
                    for(int i = 0; i < posdelete.size();i++) {
                        cursor2 = myDb.getAllTitleRows(mtitle.get(posdelete.get(i)));
                        if (cursor2.moveToFirst()) {
                            do {
                                int data0 = cursor2.getInt(cursor2.getColumnIndex("ID"));
                                final String data1 = cursor2.getString(cursor2.getColumnIndex("TITLE"));

                                System.out.println("the ids are: " + data0);
                                idarraylist.add(data0);


                                try {
                                    if (firebaseAuth.getCurrentUser() != null) {
                                        if (isNetworkAvailable() == false) {
                                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Internet connection is required when signing in!", Snackbar.LENGTH_LONG);
                                            snackbar.show();
                                        } else {
                                            emailfirebasenode = firebaseAuth.getCurrentUser().getEmail();
                                            emailfirebasenode = emailfirebasenode.replace(".", "");
                                            signinmethod = FirebaseAuth.getInstance().getCurrentUser().getIdToken(false).getResult().getSignInProvider().toString().trim();
                                            if (!signinmethod.trim().equals("password")) {

                                            } else {
                                                emailfirebasenode = emailfirebasenode + "10125signincode";
                                            }
                                            String mydata = data0 + justAlphaChars(data1);
                                            mref = FirebaseDatabase.getInstance().getReference(emailfirebasenode).child(mydata);
                                            mref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot snapshot) {

                                                    snapshot.getRef().removeValue();
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });

                                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(emailfirebasenode);
                                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot snapshot) {
                                                    ArrayList<String> itemkey = new ArrayList<String>();
                                                    for (DataSnapshot ds : snapshot.getChildren()) {
                                                        String uid = ds.getKey();
                                                        if(!uid.trim().equals("email") && !uid.trim().equals("hashpassword") && !uid.trim().equals("id")) {
                                                            itemkey.add(uid);
                                                        }
                                                        System.out.println("The whole uid: " + itemkey);

                                                    }

                                                    for(int i = 0; i < itemkey.size();i++) {
                                                        Object obj1 = snapshot.child(itemkey.get(i)).child(itemkey.get(i)+"item").child("title").getValue();
                                                        System.out.println("this is the val of obj1: " + obj1);
                                                        String strobj = "";
                                                        try {
                                                            strobj = (String) obj1;


                                                            if(strobj.equals(data1)) {
                                                                DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference(emailfirebasenode).child(itemkey.get(i));
                                                                databaseReference3.removeValue();

                                                            }
                                                        }
                                                        catch (Exception e) {
                                                            e.printStackTrace();
                                                        }



                                                    }

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                        }
                                    }
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                }


                                // do what ever you want here
                            } while (cursor2.moveToNext());
                        }
                    }
                    if (cursor2 != null) {
                        cursor2.close();
                    }


                    for(int i = 0; i < idarraylist.size();i++) {

                        myDb.deleteData(Integer.toString(idarraylist.get(i)));

                    }

                    count = (int) myDb.getTitleCount();
                    if(count<=0) {
                        nolistlayout.setVisibility(View.VISIBLE);
                        resultsListView.setVisibility(View.GONE);
                    }
                    else {
                        nolistlayout.setVisibility(View.GONE);
                        resultsListView.setVisibility(View.VISIBLE);
                    }

                    Collections.sort(posdelete);

                    for(int i = 0; i <= posdelete.size();i++) {
                        i = 0;
                        mtitle.remove((int) posdelete.get(i));
                        posdelete.remove(i);
                        for(int j = 0; j < posdelete.size();j++) {
                            posdelete.set(j, posdelete.get(j) - 1);
                        }
                    }

                    if (mode != null)
                    {
                        mode.finish();
                    }
                    adapter.notifyDataSetChanged();





                    break;
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            userSelection = new ArrayList<>();
            posdelete = new ArrayList<>();
            adapter.clearSelection();
            adapter.notifyDataSetChanged();
        }
    };
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    private void setRepeatedNotification(int ID, int hh, int mm, int ss) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, ID, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
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
        Intent alarmIntent = new Intent(MainActivity.this, SpendReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, ID, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
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

        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), requestcode, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.cancel(pendingIntent);
    }
    public void disableAlarmManager2(int requestcode){

        Intent intent = new Intent(MainActivity.this, SpendReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), requestcode, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.cancel(pendingIntent);
    }
}
