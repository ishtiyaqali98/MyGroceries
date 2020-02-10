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
import java.util.Set;

import static com.aliindustries.groceryshoppinglist.ItemActivity.maintitle;

public class CustomAdapter3 extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> Title;
    private ArrayList<Integer>  subtitle;
    private ArrayList<Double>  price;
    public static String mc_currentcurrency = "£";
    DecimalFormat decim = new DecimalFormat("0.00");

    private View view2;
    DatabaseHelper myDb;
    private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();
    LayoutInflater inflater;
    public CustomAdapter3(Context context, ArrayList<String> text1,ArrayList<Integer>  subtext2,ArrayList<Double>  price) {
        mContext = context;
        Title = text1;
        subtitle = subtext2;
        this.price = price;

    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return Title.size();
    }

    @Override

    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
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

        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            view2 = new View(mContext);

        } else {
            view2 = (View) convertView;
            //re-using if already here
        }

        view2 = inflater.inflate(R.layout.list_items2, parent, false);
        title = (TextView) view2.findViewById(R.id.textView3);
        sub_title = (TextView) view2.findViewById(R.id.textView4);
        subprice = (TextView) view2.findViewById(R.id.price);

        title.setText(Title.get(position));


        sub_title.setText("Qty: " + subtitle.get(position));

        String s2 = decim.format(price.get(position));
        subprice.setText("Price: " + mc_currentcurrency + s2);


        String s = sub_title.getText().toString();

        s = s.replaceAll("[^\\d.]", "");


        Cursor cursor1 = myDb.getIsChecked(maintitle,title.getText().toString(),Integer.parseInt(s));
        int k = 0;
        int tempid = 0;

        if (cursor1.moveToFirst()){
            do{
                int data = cursor1.getInt(cursor1.getColumnIndex("ISCHECKED"));
                int data2 = cursor1.getInt(cursor1.getColumnIndex("ID"));

                k = data;
                tempid = data2;
                // do what ever you want here
            }while(cursor1.moveToNext());
        }
        cursor1.close();


        if(k == 0) {
            title.setPaintFlags( title.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            sub_title.setPaintFlags( sub_title.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            subprice.setPaintFlags( subprice.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));

            view2.setBackgroundColor(mContext.getResources().getColor(android.R.color.transparent));


        }
        else if(k == 1) {
            title.setPaintFlags(title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            sub_title.setPaintFlags(sub_title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            subprice.setPaintFlags(subprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            view2.setBackgroundColor(mContext.getResources().getColor(R.color.lightgray));

        }

        if (mSelection.get(position) != null)
        {
            if(k == 1 || k == 0)  {
                view2.setBackgroundColor(mContext.getResources().getColor(R.color.lightergreen));
            }

        }

        return view2;
    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public void setNewSelection(int position, boolean value)
    {
        mSelection.put(position, value);
    }

    public boolean isPositionChecked(int position)
    {
        Boolean result = mSelection.get(position);
        return result == null ? false : result;
    }

    public Set<Integer> getCurrentCheckedPosition() {
        return mSelection.keySet();
    }

    public void removeSelection(int position) {
        mSelection.remove(position);
    }

    public void clearSelection() {
        mSelection = new HashMap<Integer, Boolean>();
    }

}