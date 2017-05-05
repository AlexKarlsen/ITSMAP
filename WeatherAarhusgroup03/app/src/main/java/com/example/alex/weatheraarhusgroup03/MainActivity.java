package com.example.alex.weatheraarhusgroup03;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import org.w3c.dom.Text;

import java.sql.Timestamp;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeSubviews();
    }

    void initializeSubviews() {

        // Initialize the list of historic weather info with adapter.
        ArrayList<WeatherInfo> testWeatherInfo = new ArrayList<WeatherInfo>();

        WeatherInfo w1 = new WeatherInfo();
        w1.id = 1;
        w1.temperature = 24.7;
        w1.weatherDescription = "Cloudy";
        w1.timestamp = new Timestamp(System.currentTimeMillis());

        WeatherInfo w2 = new WeatherInfo();
        w2.id = 2;
        w2.temperature = 30.4;
        w2.weatherDescription = "Sunny";
        w2.timestamp = new Timestamp(System.currentTimeMillis());

        testWeatherInfo.add(w1);
        testWeatherInfo.add(w2);

        WeatherInfoArrayAdapter adapter = new WeatherInfoArrayAdapter(this, R.layout.weather_info_list_item, testWeatherInfo);
        ListView historicWeatherInfoListView = (ListView) findViewById(R.id.historicWeatherInfoListView);
        historicWeatherInfoListView.setAdapter(adapter);
    }
}
