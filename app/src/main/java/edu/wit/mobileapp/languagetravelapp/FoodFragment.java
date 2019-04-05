package edu.wit.mobileapp.languagetravelapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class FoodFragment extends Fragment {
    private static String TAG = "myApp";
    private ListView listView;
    private TravelListItemAdapter adapter;
    private List<TravelListItem> list = new ArrayList<TravelListItem>();
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.travel_fragment_food, container, false);
        // Inflate the layout for this fragment

        FoodFragment.GetData getData = new GetData(this);
        getData.execute();

        listView = (ListView)view.findViewById(R.id.ListViewFood);



        return view;
    }
    class GetData extends AsyncTask<String, Void, String> {
        private FoodFragment mContext;
        GetData(FoodFragment context){
            this.mContext = context;
        }
        @Override
        protected String doInBackground(String... params) {
            String result = "";
            try {
                String apiKey = "AIzaSyBk-wLeE9bFj6eaCbmZ0Q7krZrMtBu2rJA";
                String searchQuery = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=resturant+in+Portugal&fields=formatted_address,name,rating,opening_hours&key="+apiKey;
                URL url = new URL(searchQuery);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                StringBuffer sb = new StringBuffer();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String read;
                while((read = br.readLine())!=null){
                    sb.append(read);
                }
                br.close();
                result = sb.toString();

            } catch (IOException e) {
                Log.d(TAG, "Error: " + e.toString());
            }
            return result;
        }
        @Override
        protected void onPostExecute(String data){
            String json = data;
            Log.v(TAG, data);

            try {

                JSONObject obj = new JSONObject(json);
                JSONArray values = obj.getJSONArray("results");
                Log.v(TAG, obj.toString());

                for(int i = 0; i < values.length(); i++){
                    TravelListItem item1 = new TravelListItem();
                    JSONObject names = values.getJSONObject(i);


                    String val1 = names.getString("name");
                    String val2 = names.getString("formatted_address");
                    String val3 = names.getString("rating");
                    Log.v(TAG, val1);

                    item1.name = val1;
                    item1.address = val2;
                    item1.rating = val3;
                    list.add(item1);
                }

            } catch (Throwable tx) {
                Log.v(TAG, "Could not parse malformed JSON");
            }
            adapter = new TravelListItemAdapter(getActivity(), 0, list);
            // Assign ListItemAdapter to ListView

            listView.setAdapter(adapter);

        }
    }


}
