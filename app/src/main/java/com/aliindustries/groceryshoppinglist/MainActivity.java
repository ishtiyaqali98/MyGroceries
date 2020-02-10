package com.aliindustries.groceryshoppinglist;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.SystemClock;
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

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    ListView resultsListView;
    LinearLayout nolistlayout;
    ArrayList<String> userSelection = new ArrayList<String>();

    ArrayList<String> mtitle;
    ArrayList<Integer> progress;
    String olditem = "";
    String newitem = "";
    ArrayList<String> progresstxt;
    DatabaseHelper myDb;
    CustomAdapter2 adapter;
    ArrayList<Integer> posdelete = new ArrayList<>();
    int count = 0;
    ArrayList<Integer> ischeckedcount = new ArrayList<>();
    ArrayList<Integer> totalitemcount = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        resultsListView = (ListView) findViewById(R.id.listview1);
        nolistlayout = (LinearLayout) findViewById(R.id.lin95);
        myDb = DatabaseHelper.getInstance(MainActivity.this);

        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this,createList.class));
                overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);

            }
        });
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


        navigationView.bringToFront();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.nav_share: {
                        try {
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                            String shareMessage= "\nLet me recommend you this application\n\n";
                            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                            startActivity(Intent.createChooser(shareIntent, "choose one"));
                        } catch(Exception e) {
                            //e.toString();
                        }
                        break;
                    }
                }
                return true;

            }
        });


        if(count <= 0) {

            nolistlayout.setVisibility(View.VISIBLE);
            resultsListView.setVisibility(View.GONE);
        }
        else {
            nolistlayout.setVisibility(View.GONE);
            resultsListView.setVisibility(View.VISIBLE);
        }


        Cursor cursor1 = myDb.getTitle();


        if (cursor1.moveToFirst()){
            do{
                String data = cursor1.getString(cursor1.getColumnIndex("TITLE"));
                if(counter < count) {
                    mtitle.add(data);
                    counter++;
                }

                // do what ever you want here
            }while(cursor1.moveToNext());
        }
        cursor1.close();


        Collections.sort(mtitle);


        for(int i = 0; i < mtitle.size();i++) {

            int k = (int) myDb.getIsCheckedCount(mtitle.get(i),1,getResources().getString(R.string.itemidentifier));
            int z = (int) myDb.getItemCount(mtitle.get(i),getResources().getString(R.string.itemidentifier));
            ischeckedcount.add(k);
            totalitemcount.add(z);

        }



        resultsListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        resultsListView.setMultiChoiceModeListener(modeListener);

        for(int i = 0; i < count;i++) {

            if(totalitemcount.get(i) != 0) {
                double zx = ((double)ischeckedcount.get(i) / (double)totalitemcount.get(i)) * (double)100;
                System.out.println("this is zx: " + zx);
                int n = (int) Math.ceil(zx);
                System.out.println("this is n: " + n);

                progress.add(n);
            }
            else {
                progress.add(0);
            }

            progresstxt.add(ischeckedcount.get(i) + " of " + totalitemcount.get(i));

        }

        adapter = new CustomAdapter2(this,  mtitle,progress, progresstxt);
        resultsListView.setAdapter(adapter);

        resultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            private long mLastClickTime = 0;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Intent i = new Intent(MainActivity.this,ItemActivity.class);
                i.putExtra("c_title", mtitle.get(position));

                startActivity(i);
                overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);

            }
        });
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

                                        myDb.updateTitle(Integer.toString(data),newitem);


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
                                int data = cursor2.getInt(cursor2.getColumnIndex("ID"));
                                idarraylist.add(data);

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

}
