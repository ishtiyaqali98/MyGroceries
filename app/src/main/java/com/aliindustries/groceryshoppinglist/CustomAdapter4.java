package com.aliindustries.groceryshoppinglist;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;
import static com.aliindustries.groceryshoppinglist.ItemActivity.maintitle;


import java.util.ArrayList;
import java.util.List;

public class CustomAdapter4 extends BaseAdapter implements Filterable {

    private List<String>originalData;
    private List<String> filteredData;
    private LayoutInflater mInflater;
    private ItemFilter mFilter = new ItemFilter();
    Context context;
    private View view2;
    DatabaseHelper myDb;
    public static int uv_counter = 0;

    ViewHolder viewHolder;
    boolean[] checkBoxState;
    String[] numberstate;


    public CustomAdapter4(Context context, List<String> data) {
        this.filteredData = data ;
        this.originalData = data ;
        this.context = context;
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
                        if (cursor.moveToFirst()){
                            do{
                                int data = cursor.getInt(cursor.getColumnIndex("QUANTITY"));
                                int data2 = cursor.getInt(cursor.getColumnIndex("ID"));
                                qty = data;
                                id = data2;
                            }
                            while(cursor.moveToNext());
                        }
                        cursor.close();
                        qty = qty + 1;

                        uv_counter = 1;
                       boolean b =  myDb.updateData(Integer.toString(id),maintitle,itemdata,0,qty);

                        if(b == true) {
                            Toast.makeText(context,itemdata + " * " + qty +" updated to " + maintitle + " shopping list",Toast.LENGTH_LONG).show();
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
                            Boolean a = myDb.insertData(maintitle, itemdata, 0, 1,round(0.00,2));

                            if(a == true) {
                                Toast.makeText(context,itemdata + " * 1 added to " + maintitle + " shopping list",Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(context,"Error. Item not added",Toast.LENGTH_LONG).show();

                            }

                        }
                        }
                }
                else {
                    checkBoxState[position] = false;
                    Cursor cursor = myDb.getQty_ID(maintitle,itemdata);
                    int qty = 1;
                    int id = 1;
                    if (cursor.moveToFirst()){
                        do{
                            int data = cursor.getInt(cursor.getColumnIndex("QUANTITY"));
                            int data2 = cursor.getInt(cursor.getColumnIndex("ID"));
                            qty = data;
                            id = data2;
                        }
                        while(cursor.moveToNext());
                    }
                    cursor.close();
                    qty = qty - 1;

                    if(qty <= 0 ) {
                        uv_counter = 0;
                        myDb.deleteData(Integer.toString(id));
                        Toast.makeText(context,"Item deleted!",Toast.LENGTH_LONG).show();

                    }
                    else  {
                        uv_counter = 1;
                       boolean c =  myDb.updateData(Integer.toString(id),maintitle,itemdata,0,qty);

                        if(c == true) {
                            Toast.makeText(context,itemdata + " * " + qty +" updated to " + maintitle + " shopping list",Toast.LENGTH_LONG).show();
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
}
