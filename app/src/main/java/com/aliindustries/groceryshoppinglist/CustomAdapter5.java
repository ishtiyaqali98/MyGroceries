package com.aliindustries.groceryshoppinglist;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;


public class CustomAdapter5 extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> Title;
    private ArrayList<String> listTitle;

    private ArrayList<Integer> subtitle;
    private ArrayList<Double> price;
    public static String mc_currentcurrency2 = "£";
    DecimalFormat decim = new DecimalFormat("0.00");

    private View view2;
    DatabaseHelper myDb;
    private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();
    LayoutInflater inflater;

    public CustomAdapter5(Context context, ArrayList<String> text1, ArrayList<Integer> subtext2, ArrayList<Double> price,ArrayList<String> listtitle) {
        mContext = context;
        Title = text1;
        subtitle = subtext2;
        this.price = price;
        this.listTitle = listtitle;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return Title.size();
    }

    @Override

    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return Title.get(arg0);
    }

    @Override

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView title;
        myDb = DatabaseHelper.getInstance(mContext);

        TextView sub_title;
        TextView subprice;
        TextView mylisttitle;

        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            view2 = new View(mContext);

        } else {
            view2 = (View) convertView;
            //re-using if already here
        }

        view2 = inflater.inflate(R.layout.list_items1_2, parent, false);
        title = (TextView) view2.findViewById(R.id.textView3);
        sub_title = (TextView) view2.findViewById(R.id.textView4);
        subprice = (TextView) view2.findViewById(R.id.price);
        mylisttitle = view2.findViewById(R.id.listtitle);

        title.setText(Title.get(position));


        sub_title.setText("Qty: " + subtitle.get(position));

        String s2 = decim.format(price.get(position));
        subprice.setText("Price: " + mc_currentcurrency2 + s2);
        mylisttitle.setText("Grocery list: " + listTitle.get(position));



        return view2;
    }

}
