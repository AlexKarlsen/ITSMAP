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
    private int cellResourceId;

    public WeatherInfoArrayAdapter(Context context, int cellResourceId, ArrayList<WeatherInfo> objects) {
        super(context, cellResourceId, objects);
        this.objects = objects;
        this.cellResourceId = cellResourceId;
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
            weatherInfoView = inflater.inflate(cellResourceId, null);
        }

        // Update the weatherInfoView with the weather info.
        if (info != null) {

            // Create a local reference to the subviews of the weather info view.
            TextView descriptionTextView = (TextView) weatherInfoView.findViewById(R.id.descriptionTextView);
            TextView temperatureTextView = (TextView) weatherInfoView.findViewById(R.id.temperatureTextView);
            TextView timeTextView = (TextView) weatherInfoView.findViewById(R.id.timeTextView);

            // Set the text of the text views to display the weather info data.
            if (descriptionTextView != null) {
                descriptionTextView.setText(info.weatherDescription);
            }
            if (temperatureTextView != null) {
                temperatureTextView.setText(String.format("%.1f°C", info.temperature));
            }
            if (timeTextView != null) {
                timeTextView.setText(info.timestamp.toString());
            }
        }

        return weatherInfoView;
    }
}
