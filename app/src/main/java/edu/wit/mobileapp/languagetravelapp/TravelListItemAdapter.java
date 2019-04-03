package edu.wit.mobileapp.languagetravelapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TravelListItemAdapter  extends ArrayAdapter<TravelListItem> {
    private LayoutInflater mInflater;

    public TravelListItemAdapter(Context context, int rid, List<TravelListItem> list) {
        super(context, rid, list);
        mInflater =
                (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Retrieve data
        TravelListItem item = (TravelListItem) getItem(position);
        // Use layout file to generate View
        View view = mInflater.inflate(R.layout.travel_list_item, null);
        // Set image
        TextView name;
        name = (TextView) view.findViewById(R.id.name);
        name.setText(item.name);
        // Set user name
        TextView address;
        address = (TextView) view.findViewById(R.id.address);
        address.setText(item.address);
        // Set comment
        TextView rating;
        rating = (TextView) view.findViewById(R.id.rating);
        rating.setText(item.rating);
        return view;
    }
}