package com.example.maxhi_000.mumsfit;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class CustomListAdapter extends ArrayAdapter<String> {

    private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();
    private Context mContext;

    public CustomListAdapter(Context context,int resource, int textViewResourceId , List<String> list )
    {
        super(context, resource, textViewResourceId, list);
        mContext = context;
    }

    public void setNewSelection(int position, boolean value) {
        mSelection.put(position, value);
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
        v.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.transparent));

        if (mSelection.get(position) != null) {
            v.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.holo_blue_light));
        }

        return v;
    }
}