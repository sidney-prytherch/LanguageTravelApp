package edu.wit.mobileapp.languagetravelapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class FoodFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.travel_fragment_food, container, false);
        // Inflate the layout for this fragment
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_list_item_1);

        for(int i = 0; i<50; i++){
            adapter.add("Food "+i);
        }
        ListView listView = (ListView)view.findViewById(R.id.ListViewFood);
        listView.setAdapter(adapter);


        return view;
    }


}
