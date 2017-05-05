package com.example.alex.weatheraarhusgroup03;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Priebe on 05/05/2017.
 *
 * Abstract: Custom array adapter for populating a ListView with weather info.
 */

public class WeatherInfoArrayAdapter extends ArrayAdapter<WeatherInfo> {

    private ArrayList<WeatherInfo> objects;

    public WeatherInfoArrayAdapter(Context context, int textViewResourceId, ArrayList<WeatherInfo> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
    }

    // Modified from https://devtut.wordpress.com/2011/06/09/custom-arrayadapter-for-a-listview-android/
    public View getView(int position, View convertView, ViewGroup parent) {

        // Create a local variable for the view to be converted.
        View weatherInfoView = convertView;

        // Get the info object at the given position.
        WeatherInfo info = objects.get(position);

        // Inflate the view if it is null.
        if (weatherInfoView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            weatherInfoView = inflater.inflate(R.layout.weather_info_list_item, null);
        }

        // Update the weatherInfoView with the weather info.
        if (info != null) {

            // Create a local reference to the subviews of the weather info view.
            TextView descriptionTextView = (TextView) weatherInfoView.findViewById(R.id.descriptionTextView);
            TextView temperatureTextView = (TextView) weatherInfoView.findViewById(R.id.temperatureTextView);
            TextView timeTextView = (TextView) weatherInfoView.findViewById(R.id.timeTextView);

            // Set the text of the text views to display the weather info data.
            descriptionTextView.setText(info.weatherDescription);
            temperatureTextView.setText(String.format("%.2f °C", info.temperature));
            timeTextView.setText(info.timestamp.toString());
        }

        return convertView;
    }
}
