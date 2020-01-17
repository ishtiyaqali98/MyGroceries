package com.aliindustries.groceryshoppinglist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import static java.security.AccessController.getContext;

public class ItemActivity extends AppCompatActivity implements searchFragment.OnFragmentInteractionListener {

    public static String maintitle = "";
    ListView listView;
    ArrayList<String> o_item;
    ArrayList<Integer> qty;
    int count = 0;
    DatabaseHelper myDb;
    String newitem = "";
    String olditem = "";
    int newquantity = 0;
    ArrayList<Integer> posdelete = new ArrayList<>();
    LinearLayout noitemlayout;

    ArrayList<String> userSelection = new ArrayList<String>();

    CustomAdapter3 customAdapter3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Bundle extras = getIntent().getExtras();
        listView = (ListView) findViewById(R.id.listview2);
        noitemlayout = (LinearLayout) findViewById(R.id.lin5);
        myDb = DatabaseHelper.getInstance(ItemActivity.this);

        setTitle("My new title");
        if (extras != null) {
            maintitle = extras.getString("c_title");
        }
        assert maintitle != null;
        if (!maintitle.equals("")) {
            setTitle(maintitle);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(modeListener);

        count = (int) myDb.getItemCount(maintitle, getResources().getString(R.string.itemidentifier));
        int counter = 0;
        o_item = new ArrayList<>();
        qty = new ArrayList<>();


        if(count <= 0) {

            noitemlayout.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
        else {
            noitemlayout.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }


        Cursor cursor1 = myDb.getItem(maintitle, getResources().getString(R.string.itemidentifier));

        if (cursor1.moveToFirst()) {
            do {
                String data = cursor1.getString(cursor1.getColumnIndex("ITEM"));
                if (counter < count) {
                    o_item.add(data);
                    counter++;
                }
                // do what ever you want here
            } while (cursor1.moveToNext());
        }
        cursor1.close();


        Collections.sort(o_item);

        Cursor cursor2 = null;
        for (int i = 0; i < count; i++) {
            cursor2 = myDb.getQty_ID(maintitle, o_item.get(i));
            if (cursor2.moveToFirst()) {
                do {
                    int data = cursor2.getInt(cursor2.getColumnIndex("QUANTITY"));
                    qty.add(data);

                    // do what ever you want here
                } while (cursor2.moveToNext());
            }
        }
        if (cursor2 != null) {
            cursor2.close();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor1 = myDb.getIsChecked(maintitle, o_item.get(position), qty.get(position));
                TextView titletextview = view.findViewById(R.id.textView3);
                TextView subtitletextview = view.findViewById(R.id.textView4);

                int k = 0;
                int tempid = 0;

                if (cursor1.moveToFirst()) {
                    do {
                        int data = cursor1.getInt(cursor1.getColumnIndex("ISCHECKED"));
                        int data2 = cursor1.getInt(cursor1.getColumnIndex("ID"));

                        k = data;
                        tempid = data2;
                        // do what ever you want here
                    } while (cursor1.moveToNext());
                }
                cursor1.close();

                if (k == 0) {

                    k = k + 1;
                    myDb.updateIsChecked(Integer.toString(tempid), k);
                    titletextview.setPaintFlags(titletextview.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    subtitletextview.setPaintFlags(subtitletextview.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    view.setBackgroundResource(R.color.lightgray);


                } else if (k == 1) {
                    k = k - 1;
                    myDb.updateIsChecked(Integer.toString(tempid), k);
                    titletextview.setPaintFlags(titletextview.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    subtitletextview.setPaintFlags(subtitletextview.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    view.setBackgroundResource(android.R.color.transparent);

                }


            }
        });


        customAdapter3 = new CustomAdapter3(ItemActivity.this, o_item, qty);
        listView.setAdapter(customAdapter3);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                listView.setVisibility(View.GONE);
                noitemlayout.setVisibility(View.GONE);
                getSupportActionBar().hide();
                getSupportFragmentManager().beginTransaction().add(R.id.searchfragment, new searchFragment()).commit();
                break;
            default:
                return false;
        }
        return true;
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    AbsListView.MultiChoiceModeListener modeListener = new AbsListView.MultiChoiceModeListener() {
        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

            try {
                if (checked) {
                    customAdapter3.setNewSelection(position, checked);
                } else {
                    customAdapter3.removeSelection(position);
                }
                customAdapter3.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (userSelection.contains(o_item.get(position))) {

                userSelection.remove(o_item.get(position));
                posdelete.remove(Integer.valueOf(position));

            } else {
                userSelection.add(o_item.get(position));
                posdelete.add(position);

            }
            mode.setTitle(userSelection.size() + " selected");


            if (posdelete.size() == 1) {
                olditem = o_item.get(posdelete.get(0));
            } else {
                olditem = "";
            }

            mode.invalidate();

        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {

            MenuInflater menuInflater = mode.getMenuInflater();
            menuInflater.inflate(R.menu.contextmenu, menu);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            MenuItem item = menu.findItem(R.id.edit);

            if (posdelete.size() > 1) {
                item.setVisible(false);
                item.setEnabled(false);
            } else {
                item.setVisible(true);
                item.setEnabled(true);
            }

            return true;
        }

        @Override
        public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {

            switch (item.getItemId()) {

                case R.id.edit:

                    AlertDialog.Builder builder = new AlertDialog.Builder(ItemActivity.this);
                    builder.setTitle("Set item");
                    View viewInflated = LayoutInflater.from(ItemActivity.this).inflate(R.layout.edittextdialog, (ViewGroup) findViewById(android.R.id.content), false);
                    final EditText input = (EditText) viewInflated.findViewById(R.id.editText55);
                    builder.setView(viewInflated);

                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            newitem = input.getText().toString().trim();
                            newitem = newitem.replace("'", "");

                            boolean bc = newitem.matches(".*[a-zA-Z].*");
                            if (bc == false && !myDb.itemExists(maintitle, newitem)) {
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Enter item name", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            } else if (bc == true && myDb.itemExists(maintitle, newitem)) {
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "This item already exists!", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            } else {
                                dialog.dismiss();
                                final NumberPicker numberPicker = new NumberPicker(ItemActivity.this);
                                numberPicker.setMaxValue(100);
                                numberPicker.setMinValue(1);
                                AlertDialog.Builder builder = new AlertDialog.Builder(ItemActivity.this);
                                builder.setView(numberPicker);
                                builder.setTitle("Set quantity");
                                builder.setMessage("Choose a value :");
                                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        newquantity = numberPicker.getValue();

                                        Cursor cursor1 = myDb.getQty_ID(maintitle, olditem);


                                        if (cursor1.moveToFirst()) {
                                            do {
                                                int data = cursor1.getInt(cursor1.getColumnIndex("ID"));
                                                myDb.updateData(Integer.toString(data), maintitle, newitem, 0, newquantity);

                                                // do what ever you want here
                                            } while (cursor1.moveToNext());
                                        }
                                        cursor1.close();


                                        int retval = o_item.indexOf(olditem);

                                        if (retval >= 0) {
                                            o_item.set(retval, newitem);
                                            qty.set(retval, newquantity);
                                        }

                                        if (mode != null) {
                                            mode.finish();
                                        }
                                        customAdapter3.notifyDataSetChanged();

                                        dialog.dismiss();
                                    }
                                });
                                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.create();
                                builder.show();
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
                    for (int i = 0; i < posdelete.size(); i++) {
                        cursor2 = myDb.getQty_ID(maintitle, o_item.get(posdelete.get(i)));
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

                    for (int i = 0; i < idarraylist.size(); i++) {

                        myDb.deleteData(Integer.toString(idarraylist.get(i)));

                    }
                    count = (int) myDb.getItemCount(maintitle, getResources().getString(R.string.itemidentifier));

                    if(count<=0) {
                        System.out.println("this is thee truth");
                        noitemlayout.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                    }
                    else {
                        noitemlayout.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                    }


                    Collections.sort(posdelete);

                    for (int i = 0; i <= posdelete.size(); i++) {
                        i = 0;
                        o_item.remove((int) posdelete.get(i));
                        posdelete.remove(i);
                        for (int j = 0; j < posdelete.size(); j++) {
                            posdelete.set(j, posdelete.get(j) - 1);
                        }
                    }

                    if (mode != null) {
                        mode.finish();
                    }
                    customAdapter3.notifyDataSetChanged();


                    break;
                default:
                    return false;
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

            userSelection = new ArrayList<>();
            posdelete = new ArrayList<>();
            customAdapter3.clearSelection();
            customAdapter3.notifyDataSetChanged();
        }
    };

    private void tellFragments() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment f : fragments) {
            if (f != null && f instanceof searchFragment)
                ((searchFragment) f).on_BackPressed();
        }
    }



    @Override
    public void onBackPressed() {
        tellFragments();
        getSupportFragmentManager().popBackStackImmediate();
        finish();

    }
}