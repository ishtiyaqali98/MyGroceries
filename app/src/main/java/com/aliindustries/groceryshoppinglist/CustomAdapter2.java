package com.aliindustries.groceryshoppinglist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class CustomAdapter2 extends BaseAdapter {

    private Context mContext;
    private ArrayList<String>  Title;
    private ArrayList<Integer>  progressbar1;
    private ArrayList<String>  progressbartxt;
    private View view2;
    private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();
    LayoutInflater inflater;
    public CustomAdapter2(Context context, ArrayList<String> text1,ArrayList<Integer> subtext1,ArrayList<String>subtext2) {
        mContext = context;
        Title = text1;
        progressbar1 = subtext1;
        progressbartxt = subtext2;

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
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView title;
        ProgressBar progressBar;

        TextView progressbartxtviw;

        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            view2 = new View(mContext);

        } else {
            view2 = (View) convertView;
            //re-using if already here
        }

        view2 = inflater.inflate(R.layout.list_items1, parent, false);
        title = (TextView) view2.findViewById(R.id.title1);
        progressBar = (ProgressBar) view2.findViewById(R.id.progressBar4);
        progressbartxtviw = (TextView) view2.findViewById(R.id.progressbartxtview);

        title.setText(Title.get(position));

        progressBar.setProgress(progressbar1.get(position));

        progressbartxtviw.setText(progressbartxt.get(position));

        view2.setBackgroundColor(mContext.getResources().getColor(android.R.color.transparent));
        if (mSelection.get(position) != null)
        {
            view2.setBackgroundColor(mContext.getResources().getColor(R.color.lightergreen));
        }

        return view2;
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