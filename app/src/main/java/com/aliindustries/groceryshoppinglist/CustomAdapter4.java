package com.aliindustries.groceryshoppinglist;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.aliindustries.groceryshoppinglist.ItemActivity.maintitle;
import static com.aliindustries.groceryshoppinglist.MainActivity.justAlphaChars;
import static com.aliindustries.groceryshoppinglist.createList.getMonthName_Abbr;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;

public class CustomAdapter4 extends BaseAdapter implements Filterable {

    private List<String>originalData;
    private List<String> filteredData;
    private LayoutInflater mInflater;
    private ItemFilter mFilter = new ItemFilter();
    Context context;
    private View view2;
    DatabaseHelper myDb;
    public static int uv_counter = 0;
    Activity activity;
    ArrayList<addFirebaseList> addFirebaseLists = new ArrayList<>();
    int count2 = 0;

    ViewHolder viewHolder;
    boolean[] checkBoxState;
    String[] numberstate;
    FirebaseAuth firebaseAuth;
    String emailfirebasenode = "";
    DatabaseReference mref;
    String signinmethod = "";


    public CustomAdapter4(Context context,Activity activity, List<String> data) {
        this.filteredData = data ;
        this.originalData = data ;
        this.context = context;
        this.activity = activity;
        mInflater = LayoutInflater.from(context);
        checkBoxState=new boolean[data.size()];
        numberstate=new String[data.size()];
        for(int i = 0;i < numberstate.length;i++) {

            numberstate[i] = "1";
        }

    }

    @Override
    public int getCount() {
        try {
            return filteredData.size();
        }
        catch (Exception e) {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return filteredData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    @Override

    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        myDb = DatabaseHelper.getInstance(context);
        firebaseAuth = FirebaseAuth.getInstance();
        mref = FirebaseDatabase.getInstance().getReference();
        if(convertView==null)
        {
            convertView=inflater.inflate(R.layout.list_items3, null);
            viewHolder=new ViewHolder();

            //cache the views
            viewHolder.textView=(TextView) convertView.findViewById(R.id.textView50);
            viewHolder.checkBox=(CheckBox) convertView.findViewById(R.id.checkBox);

            //link the cached views to the convertview
            convertView.setTag( viewHolder);


        }
        else
            viewHolder=(ViewHolder) convertView.getTag();



        viewHolder.textView.setText(filteredData.get(position));

        if(uv_counter == 1 || uv_counter == 0) {
            viewHolder.checkBox.setChecked(checkBoxState[position]);
        }
        viewHolder.checkBox.setTag( position);

        if(uv_counter == 4) {
            viewHolder.checkBox.setChecked(false);
        }

        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                String itemdata = filteredData.get(position).replace("'", "");
                if(((CheckBox)v).isChecked()) {
                    checkBoxState[position] = true;

                    if(myDb.itemExists(maintitle,itemdata) == true) {
                        Cursor cursor = myDb.getQty_ID(maintitle,itemdata);
                        int qty = 1;
                        int id = 1;
                        String pp_title = "";
                        String pp_item = "";
                        int pp_is_checked = 1;
                        double pp_prices = 1;
                        if (cursor.moveToFirst()){
                            do{
                                int data = cursor.getInt(cursor.getColumnIndex("QUANTITY"));
                                int data2 = cursor.getInt(cursor.getColumnIndex("ID"));
                                String data3 = cursor.getString(cursor.getColumnIndex("TITLE"));
                                String data4 = cursor.getString(cursor.getColumnIndex("ITEM"));
                                int data5 = cursor.getInt(cursor.getColumnIndex("ISCHECKED"));
                                double data6 = cursor.getDouble(cursor.getColumnIndex("PRICE"));
                                qty = data;
                                id = data2;
                                pp_title = data3;
                                pp_item = data4;
                                pp_is_checked = data5;
                                pp_prices = data6;
                            }
                            while(cursor.moveToNext());
                        }
                        cursor.close();
                        qty = qty + 1;

                        uv_counter = 1;
                       boolean b =  myDb.updateData(Integer.toString(id),maintitle,itemdata,0,qty);

                        if(b == true) {
                            Toast.makeText(context,itemdata + " * " + qty +" updated to " + maintitle + " shopping list",Toast.LENGTH_LONG).show();
                            try {
                                if (firebaseAuth.getCurrentUser() != null) {
                                    if (isNetworkAvailable() == false) {
                                        Snackbar snackbar = Snackbar.make(activity.findViewById(android.R.id.content), "Internet connection is required when signing in!", Snackbar.LENGTH_LONG);
                                        snackbar.show();
                                    } else {
                                        emailfirebasenode = firebaseAuth.getCurrentUser().getEmail();
                                        emailfirebasenode = emailfirebasenode.replace(".", "");
                                        signinmethod = FirebaseAuth.getInstance().getCurrentUser().getIdToken(false).getResult().getSignInProvider().toString().trim();
                                        if (!signinmethod.trim().equals("password")) {

                                        } else {
                                            emailfirebasenode = emailfirebasenode + "10125signincode";
                                        }

                                        FirebaseDatabase.getInstance().getReference(emailfirebasenode).child(id + pp_title).child(id + pp_title + "item").child("qty").setValue(qty);

                                    }
                                }
                            }
                            catch (Exception e) {

                            }



                        }
                        else {
                            Toast.makeText(context,"Error. Item not updated",Toast.LENGTH_LONG).show();

                        }

                    }
                    else {
                        String s = itemdata.trim();
                        boolean bc = s.matches(".*[a-zA-Z].*");
                        if(bc == false) {
                            Toast.makeText(context,"Enter a valid item",Toast.LENGTH_LONG).show();
                        }
                        else {
                            uv_counter = 1;
                            Calendar calendar = Calendar.getInstance();
                            String date_created = calendar.getTime().toString();
                            calendar.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
                            calendar.set(Calendar.HOUR_OF_DAY,0);
                            calendar.set(Calendar.MINUTE,0);
                            calendar.set(Calendar.SECOND,0);
                            calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)-6);
                            int k2 = calendar.get(Calendar.DAY_OF_MONTH);
                            String k3 = getMonthName_Abbr(calendar.get(Calendar.MONTH));
                            calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)+6);
                            int yr = calendar.get(Calendar.YEAR);
                            String monthnameabbr = getMonthName_Abbr(calendar.get(Calendar.MONTH)) + " " + yr;

                            String s2 = k2 + " " + k3 + " - " + calendar.get(Calendar.DAY_OF_MONTH) + " " + getMonthName_Abbr(calendar.get(Calendar.MONTH)) + " " + yr;


                            Boolean a = myDb.insertData(maintitle, itemdata, 0, 1,round(0.00,2),s2,monthnameabbr,yr,calendar.getTimeInMillis(),date_created);

                            if(a == true) {
                                Toast.makeText(context,itemdata + " * 1 added to " + maintitle + " shopping list",Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(context,"Error. Item not added",Toast.LENGTH_LONG).show();

                            }
                            try {
                                if (firebaseAuth.getCurrentUser() != null) {
                                    if (isNetworkAvailable() == false) {
                                        Snackbar snackbar = Snackbar.make(activity.findViewById(android.R.id.content), "Internet connection is required when signing in!", Snackbar.LENGTH_LONG);
                                        snackbar.show();
                                    } else {
                                        emailfirebasenode = firebaseAuth.getCurrentUser().getEmail();
                                        emailfirebasenode = emailfirebasenode.replace(".", "");
                                        signinmethod = FirebaseAuth.getInstance().getCurrentUser().getIdToken(false).getResult().getSignInProvider().toString().trim();
                                        if (!signinmethod.trim().equals("password")) {

                                        } else {
                                            emailfirebasenode = emailfirebasenode + "10125signincode";
                                        }
                                        count2 = (int) myDb.getAllCount();
                                        Cursor cursor2 = myDb.getAllData();
                                        if (cursor2.moveToFirst()) {
                                            do {
                                                final int data0 = cursor2.getInt(cursor2.getColumnIndex("ID"));
                                                final String data1 = cursor2.getString(cursor2.getColumnIndex("TITLE"));

                                                final String data2 = cursor2.getString(cursor2.getColumnIndex("ITEM"));
                                                final int data3 = cursor2.getInt(cursor2.getColumnIndex("ISCHECKED"));
                                                final int data4 = cursor2.getInt(cursor2.getColumnIndex("QUANTITY"));
                                                final double data5 = cursor2.getDouble(cursor2.getColumnIndex("PRICE"));
                                                final String data6 = cursor2.getString(cursor2.getColumnIndex("WEEK"));
                                                final String data7 = cursor2.getString(cursor2.getColumnIndex("MONTH"));
                                                final int data8 = cursor2.getInt(cursor2.getColumnIndex("YEAR"));
                                                final long data9 = cursor2.getLong(cursor2.getColumnIndex("DATEINMS"));
                                                final String data10 = cursor2.getString(cursor2.getColumnIndex("DATECREATED"));

                                                final String mydata = justAlphaChars(data1);
                                                if (count2 > 0 && addFirebaseLists.size() <= count2) {
                                                    addFirebaseList addFirebaseList = new addFirebaseList(data0, data1, data2, data3, data4, data5,data6,data7,data8,data9,data10);
                                                    addFirebaseLists.add(addFirebaseList);
                                                }
                                                mref = FirebaseDatabase.getInstance().getReference(emailfirebasenode);
                                                mref.addListenerForSingleValueEvent(new ValueEventListener() {

                                                    @Override
                                                    public void onDataChange(DataSnapshot snapshot) {
                                                        addFirebaseList addFirebaseList = new addFirebaseList(data0, data1, data2, data3, data4, data5,data6,data7,data8,data9,data10);
                                                        int identifier1 = addFirebaseList.getID();
                                                        if (snapshot.child(identifier1 + mydata).child(identifier1 + mydata + "item").hasChild(Integer.toString(identifier1))) {
                                                        } else {
                                                            int identifier2 = addFirebaseList.getID();
                                                            mref.child(identifier2 + mydata).child(identifier2 + mydata + "item").setValue(addFirebaseList);

                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });
                                            } while (cursor2.moveToNext());
                                        }
                                        cursor2.close();

                                    }
                                }
                            }
                            catch (Exception e) {

                            }


                        }
                        }
                }
                else {
                    checkBoxState[position] = false;
                    Cursor cursor = myDb.getQty_ID(maintitle,itemdata);
                    int qty = 1;
                    int id = 1;
                    String pp_title = "";
                    String pp_item = "";
                    int pp_is_checked = 1;
                    double pp_prices = 1;

                    if (cursor.moveToFirst()){
                        do{
                            int data = cursor.getInt(cursor.getColumnIndex("QUANTITY"));
                            int data2 = cursor.getInt(cursor.getColumnIndex("ID"));
                            String data3 = cursor.getString(cursor.getColumnIndex("TITLE"));


                            qty = data;
                            id = data2;
                            pp_title = data3;

                        }
                        while(cursor.moveToNext());
                    }
                    cursor.close();
                    qty = qty - 1;

                    if(qty <= 0 ) {
                        uv_counter = 0;
                        myDb.deleteData(Integer.toString(id));
                        Toast.makeText(context, "Item deleted!", Toast.LENGTH_LONG).show();
                        try {
                            if (firebaseAuth.getCurrentUser() != null) {
                                if (isNetworkAvailable() == false) {
                                    Snackbar snackbar = Snackbar.make(activity.findViewById(android.R.id.content), "Internet connection is required when signing in!", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                } else {
                                    emailfirebasenode = firebaseAuth.getCurrentUser().getEmail();
                                    emailfirebasenode = emailfirebasenode.replace(".", "");
                                    signinmethod = FirebaseAuth.getInstance().getCurrentUser().getIdToken(false).getResult().getSignInProvider().toString().trim();
                                    if (!signinmethod.trim().equals("password")) {

                                    } else {
                                        emailfirebasenode = emailfirebasenode + "10125signincode";
                                    }

                                    FirebaseDatabase.getInstance().getReference(emailfirebasenode).child(id + pp_title).removeValue();

                                }
                            }
                        }
                        catch (Exception e ) {
                            e.printStackTrace();
                        }
                    }
                    else  {
                        uv_counter = 1;
                       boolean c =  myDb.updateData(Integer.toString(id),maintitle,itemdata,0,qty);

                        if(c == true) {
                            Toast.makeText(context,itemdata + " * " + qty +" updated to " + maintitle + " shopping list",Toast.LENGTH_LONG).show();
                            try {
                                if (firebaseAuth.getCurrentUser() != null) {
                                    if (isNetworkAvailable() == false) {
                                        Snackbar snackbar = Snackbar.make(activity.findViewById(android.R.id.content), "Internet connection is required when signing in!", Snackbar.LENGTH_LONG);
                                        snackbar.show();
                                    } else {
                                        emailfirebasenode = firebaseAuth.getCurrentUser().getEmail();
                                        emailfirebasenode = emailfirebasenode.replace(".", "");
                                        signinmethod = FirebaseAuth.getInstance().getCurrentUser().getIdToken(false).getResult().getSignInProvider().toString().trim();
                                        if (!signinmethod.trim().equals("password")) {

                                        } else {
                                            emailfirebasenode = emailfirebasenode + "10125signincode";
                                        }
                                        FirebaseDatabase.getInstance().getReference(emailfirebasenode).child(id + pp_title).child(id + pp_title + "item").child("qty").setValue(qty);

                                    }
                                }
                            }
                            catch (Exception e) {

                            }

                        }
                        else {
                            Toast.makeText(context,"Error. Item not updated",Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });


        //return the view to be displayed
        return convertView;
    }


    //class for caching the views in a row
    static  class ViewHolder
    {
        TextView textView;
        CheckBox checkBox;
    }


    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<String> list = originalData;

            int count = list.size();
            final ArrayList<String> nlist = new ArrayList<String>(count);

            String filterableString ;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i);
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(filterableString);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<String>) results.values;
            notifyDataSetChanged();

        }

    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
