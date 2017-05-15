package com.example.maxhi_000.mumsfit;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class CustomListAdapter extends ArrayAdapter<String> {

    private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();
    private Context mContext;
    private int countChecked;

    public CustomListAdapter(Context context,int resource, int textViewResourceId , List<String> list )
    {
        super(context, resource, textViewResourceId, list);
        mContext = context;
    }

    public void setNewSelection(int position, boolean value) {
        mSelection.put(position, value);
        countChecked = position;
        notifyDataSetChanged();
    }

    public boolean isPositionChecked(int position) {
        Boolean result = mSelection.get(position);
        return result == null ? false : result;
    }

    public Set<Integer> getCurrentCheckedPosition() {
        return mSelection.keySet();
    }

    public void removeSelection(int position) {
        mSelection.remove(position);
        notifyDataSetChanged();
    }

    public void clearSelection() {
        mSelection = new HashMap<Integer, Boolean>();
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = super.getView(position, convertView, parent);//let the adapter handle setting up the row views
        v.setBackgroundColor(mContext.getResources().getColor(android.R.color.transparent)); //default color

        if (mSelection.get(position) != null) {
            v.setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_blue_light));// this is a selected position so make it red
        }

        return v;
    }
}