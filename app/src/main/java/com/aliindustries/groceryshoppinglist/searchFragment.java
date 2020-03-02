package com.aliindustries.groceryshoppinglist;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import static com.aliindustries.groceryshoppinglist.CustomAdapter4.uv_counter;
import static com.aliindustries.groceryshoppinglist.ItemActivity.maintitle;
import static com.aliindustries.groceryshoppinglist.MainActivity.justAlphaChars;
import static com.aliindustries.groceryshoppinglist.createList.getMonthName_Abbr;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;


public class searchFragment extends Fragment implements  backPressed {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ListView listView;
    SearchView searchView;
    CustomAdapter4 adapter;
    DatabaseHelper mydb;
    String myidentifir = "";
    ImageView imageView;
    ArrayList<String> arrayList = new ArrayList<String>();
    private OnFragmentInteractionListener mListener;
    FirebaseAuth firebaseAuth;
    DatabaseReference mref;
    String emailfirebasenode = "";
    String signinmethod = "";
    int count2 = 0;
    ArrayList<addFirebaseList> addFirebaseLists = new ArrayList<>();

    public searchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment searchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static searchFragment newInstance(String param1, String param2) {
        searchFragment fragment = new searchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        firebaseAuth = FirebaseAuth.getInstance();
        mref = FirebaseDatabase.getInstance().getReference();
        mydb = DatabaseHelper.getInstance(getActivity());
        arrayList.add("Rice");
        arrayList.add("Dried pasta");
        arrayList.add("Dried noodles");
        arrayList.add("Beans");
        arrayList.add("Whole grains");
        arrayList.add("Canned tomatoes");
        arrayList.add("Canned tuna");
        arrayList.add("Canned coconut milk");
        arrayList.add("Canned soup");
        arrayList.add("Chicken broth");
        arrayList.add("Bread");
        arrayList.add("Peanut butter");
        arrayList.add("Whole oats");
        arrayList.add("Cereal");
        arrayList.add("Nuts");
        arrayList.add("Dried fruit");
        arrayList.add("Crackers");
        arrayList.add("Cooking oil");
        arrayList.add("Olive oil");
        arrayList.add("Vegetable oil");
        arrayList.add("Canola oil");
        arrayList.add("Coconut oil");
        arrayList.add("Sesame oil");
        arrayList.add("Vinegar");
        arrayList.add("Soy sauce");
        arrayList.add("Honey");
        arrayList.add("Maple syrup");
        arrayList.add("All-purpose flour");
        arrayList.add("Sugar");
        arrayList.add("Coffee");
        arrayList.add("Tea");
        arrayList.add("Milk");
        arrayList.add("Cream");
        arrayList.add("Eggs");
        arrayList.add("Juice");
        arrayList.add("Butter");
        arrayList.add("Yogurt");
        arrayList.add("Cheese");
        arrayList.add("Seeds");

        arrayList.add("Fresh fruit");
        arrayList.add("Fresh vegetables");
        arrayList.add("Tofu");
        arrayList.add("Tortillas");

        arrayList.add("Frozen vegetables");
        arrayList.add("Frozen fruit");
        arrayList.add("Meat");
        arrayList.add("Chicken");
        arrayList.add("Fish");

        arrayList.add("Ice cream");
        arrayList.add("Salt");
        arrayList.add("Black pepper");
        arrayList.add("Spices");
        arrayList.add("Garlic powder");
        arrayList.add("Garlic");
        arrayList.add("Onions");
        arrayList.add("Herbs");
        arrayList.add("Stock cubes");

        arrayList.add("Pulses");

        arrayList.add("Chili powder");
        arrayList.add("Paprika");
        arrayList.add("Cumin");
        arrayList.add("Cinnamon");
        arrayList.add("Nutmeg");
        arrayList.add("Ketchup");
        arrayList.add("Mayonnaise");
        arrayList.add("Mustard");
        arrayList.add("Barbecue sauce");
        arrayList.add("Hot sauce");
        arrayList.add("Jam");
        arrayList.add("Chocolate");
        arrayList.add("Cake");


        Collections.sort(arrayList);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_search, container, false);

        listView = (ListView)rootView.findViewById(R.id.listview20);
        searchView = (SearchView) rootView.findViewById(R.id.searchbar);
        imageView = (ImageView) rootView.findViewById(R.id.imageView862);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
                startActivity(new Intent(getContext(),ItemActivity.class));
                getActivity().finish();
            }
        });


        adapter = new CustomAdapter4(getContext(),getActivity(),arrayList);


        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id444) {

                TextView textView = view.findViewById(R.id.textView50);
                CheckBox checkBox = view.findViewById(R.id.checkBox);
                checkBox.setChecked(true);
                uv_counter = 1;

                String itemdata = textView.getText().toString().trim();
                itemdata = itemdata.replace("'","");
                int qty2 = 1;
                int id2 = 1;
                String pp_title2 = "";

                Cursor cursor = mydb.getQty_ID(maintitle,itemdata);
                if (cursor.moveToFirst()){
                    do{
                        int data = cursor.getInt(cursor.getColumnIndex("QUANTITY"));
                        int data2 = cursor.getInt(cursor.getColumnIndex("ID"));
                        String data3 = cursor.getString(cursor.getColumnIndex("TITLE"));

                        qty2 = data;
                        id2 = data2;
                        pp_title2 = data3;

                    }
                    while(cursor.moveToNext());
                }
                cursor.close();

                if(mydb.itemExists(maintitle,itemdata) == true) {
                    qty2 = qty2 + 1;

                    boolean c = mydb.updateData(Integer.toString(id2),maintitle,itemdata,0,qty2);

                    if(c == true) {
                        Toast.makeText(getContext(),itemdata + " * " + qty2 +" updated to " + maintitle + " shopping list",Toast.LENGTH_LONG).show();
                        try {
                            if (firebaseAuth.getCurrentUser() != null) {
                                if (isNetworkAvailable() == false) {
                                    Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), "Internet connection is required when signing in!", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                } else {
                                    emailfirebasenode = firebaseAuth.getCurrentUser().getEmail();
                                    emailfirebasenode = emailfirebasenode.replace(".", "");
                                    signinmethod = FirebaseAuth.getInstance().getCurrentUser().getIdToken(false).getResult().getSignInProvider().toString().trim();
                                    if (!signinmethod.trim().equals("password")) {

                                    } else {
                                        emailfirebasenode = emailfirebasenode + "10125signincode";
                                    }
                                    FirebaseDatabase.getInstance().getReference(emailfirebasenode).child(id2 + pp_title2).child(id2 + pp_title2 + "item").child("qty").setValue(qty2);
                                }
                            }
                        }
                        catch (Exception e) {

                        }

                    }
                    else {
                        Toast.makeText(getContext(),"Error. Item not updated",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    boolean bc = itemdata.matches(".*[a-zA-Z].*");
                    if(bc == false) {
                        Toast.makeText(getContext(),"Enter a valid item",Toast.LENGTH_LONG).show();
                    }
                    else {
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

                        Boolean a = mydb.insertData(maintitle, itemdata, 0, 1,round(0.00,2),s2,monthnameabbr,yr,calendar.getTimeInMillis(),date_created);
                        if(a == true) {
                            Toast.makeText(getContext(),itemdata + " * 1 added to " + maintitle + " shopping list",Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(getContext(),"Error. Item not added",Toast.LENGTH_LONG).show();

                        }
                        try {
                            if (firebaseAuth.getCurrentUser() != null) {
                                if (isNetworkAvailable() == false) {
                                    Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), "Internet connection is required when signing in!", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                } else {
                                    emailfirebasenode = firebaseAuth.getCurrentUser().getEmail();
                                    emailfirebasenode = emailfirebasenode.replace(".", "");
                                    signinmethod = FirebaseAuth.getInstance().getCurrentUser().getIdToken(false).getResult().getSignInProvider().toString().trim();
                                    if (!signinmethod.trim().equals("password")) {

                                    } else {
                                        emailfirebasenode = emailfirebasenode + "10125signincode";
                                    }
                                    count2 = (int) mydb.getAllCount();
                                    Cursor cursor2 = mydb.getAllData();
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
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override

            public boolean onQueryTextSubmit(String text) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                 if(uv_counter == 1 && newText.length() > 0) {

                     searchView.setQuery("", true);
                     newText = "";
                     uv_counter = 4;
                     myidentifir = "identified";
                 }
                 else {
                     myidentifir = "";
                 }

                if(!arrayList.get(arrayList.size()-1).equals("Yogurt")) {
                    arrayList.remove(arrayList.size()-1);
                }
                adapter.getFilter().filter(newText);

                if(!arrayList.contains(newText)) {
                    arrayList.add(newText);

                    if(myidentifir.equals("identified")) {
                        arrayList.remove(arrayList.size()-1);
                    }
                }


                arrayList.remove("");
                if(newText.trim().equals("")) {
                    listView.setVisibility(View.GONE);
                }
                else {
                    listView.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });

    return rootView;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void on_BackPressed() {

        getActivity().getSupportFragmentManager().popBackStack();

        startActivity(new Intent(getContext(),ItemActivity.class));



    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



}


