package com.aliindustries.groceryshoppinglist;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import static com.aliindustries.groceryshoppinglist.CustomAdapter4.uv_counter;
import java.util.ArrayList;
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

    ImageView imageView;
    ArrayList<String> arrayList = new ArrayList<String>();
    private OnFragmentInteractionListener mListener;

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


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override

            public boolean onQueryTextSubmit(String text) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                 if(uv_counter == 1) {
                   searchView.clearFocus();
                    uv_counter = 4;
                 }


                if(!arrayList.get(arrayList.size()-1).equals("Yogurt")) {
                    arrayList.remove(arrayList.size()-1);
                }
                adapter.getFilter().filter(newText);

                if(!arrayList.contains(newText)) {
                    arrayList.add(newText);
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




}


