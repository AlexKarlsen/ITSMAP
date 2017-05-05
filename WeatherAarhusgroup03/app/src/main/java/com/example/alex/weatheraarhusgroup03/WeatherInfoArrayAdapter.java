package com.example.alex.weatheraarhusgroup03;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by Priebe on 05/05/2017.
 */

public class WeatherInfoArrayAdapter extends ArrayAdapter<WeatherInfo> {

    private ArrayList<WeatherInfo> objects;

    public WeatherInfoArrayAdapter(Context context, int textViewResourceId, ArrayList<WeatherInfo> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Must return a beatutiful weatherView for the weatherInfo.
        return convertView;
    }
}
