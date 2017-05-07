package com.example.alex.weatheraarhusgroup03;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Timestamp;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    LinearLayout mainLinearLayout;
    TextView currentTemperatureTextView;
    TextView currentDescriptionTextView;
    FrameLayout currentColorFrameLayout;
    ListView historicWeatherInfoListView;
    FloatingActionButton refreshFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the subviews and create references to ui elements.
        initializeSubviews();

        // Update the layout based on the initial orientation.
        int orientation = getResources().getConfiguration().orientation;
        updateLayoutForOrientation(orientation);

        // Start background service with intent
        Intent backgroundServiceIntent = new Intent(MainActivity.this, WeatherInfoService.class);
        startService(backgroundServiceIntent);
    }

    private void initializeSubviews() {

        // Get references to interface elements.
        mainLinearLayout = (LinearLayout) findViewById(R.id.mainLinearLayout);
        currentTemperatureTextView = (TextView) findViewById(R.id.currentTemperatureTextView);
        currentDescriptionTextView = (TextView) findViewById(R.id.currentDescriptionTextView);
        currentColorFrameLayout = (FrameLayout) findViewById(R.id.colorFrameLayout);
        historicWeatherInfoListView = (ListView) findViewById(R.id.historicWeatherInfoListView);
        refreshFab = (FloatingActionButton) findViewById(R.id.refreshFab);

        // Update the historic list view with the latest 24 hr weather info.
        ArrayList<WeatherInfo> historicWeatherInfo = createHistoricTestInfo();
        updateHistoricListView(historicWeatherInfo);
        updateCurrentView(historicWeatherInfo.get(0));

        // Add on click handler to the refresh fab.
        refreshFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Call the service...

            }
        });
    }

    private void updateHistoricListView(ArrayList<WeatherInfo> historicData) {

        // Populate the list view with the weather info using a custom adapter.
        WeatherInfoArrayAdapter adapter = new WeatherInfoArrayAdapter(this, R.layout.weather_info_list_item, historicData);
        historicWeatherInfoListView.setAdapter(adapter);
    }

    private void updateCurrentView(WeatherInfo currentInfo) {

        // Update the 'current' textViews with data from the weather info.
        currentTemperatureTextView.setText(String.format("%.1f°C", currentInfo.temperature));
        currentDescriptionTextView.setText(currentInfo.weatherDescription);

        // Update the color display with a fitting color.
        currentColorFrameLayout.setBackgroundColor(getDisplayColorForTemperature(currentInfo.temperature));
    }

    private int getDisplayColorForTemperature(Double temperature) {

        // Return a fitting color given a temperature.
        if (temperature > 35) {
            return ContextCompat.getColor(getApplicationContext(), R.color.temperatureVeryHigh);
        } else if (temperature > 25) {
            return ContextCompat.getColor(getApplicationContext(), R.color.temperatureHigh);
        } else if (temperature > 15) {
            return ContextCompat.getColor(getApplicationContext(), R.color.temperatureMediumHigh);
        } else if (temperature > 5) {
            return ContextCompat.getColor(getApplicationContext(), R.color.temperatureMedium);
        } else {
            return ContextCompat.getColor(getApplicationContext(), R.color.temperatureLow);
        }
    }

    private ArrayList<WeatherInfo> createHistoricTestInfo() {

        // Initialize the list of historic weather info with adapter.
        ArrayList<WeatherInfo> testWeatherInfo = new ArrayList<WeatherInfo>();

        WeatherInfo w1 = new WeatherInfo();
        w1.id = 1;
        w1.temperature = 24.7;
        w1.weatherDescription = "Cloudy";
        w1.timestamp = new Timestamp(System.currentTimeMillis());

        WeatherInfo w2 = new WeatherInfo();
        w2.id = 2;
        w2.temperature = 25.1;
        w2.weatherDescription = "Cloudy";
        w2.timestamp = new Timestamp(System.currentTimeMillis());

        WeatherInfo w3 = new WeatherInfo();
        w3.id = 3;
        w3.temperature = 28.7;
        w3.weatherDescription = "Sunny";
        w3.timestamp = new Timestamp(System.currentTimeMillis());

        WeatherInfo w4 = new WeatherInfo();
        w4.id = 4;
        w4.temperature = 29.1;
        w4.weatherDescription = "Sunny";
        w4.timestamp = new Timestamp(System.currentTimeMillis());

        WeatherInfo w5 = new WeatherInfo();
        w5.id = 1;
        w5.temperature = 30.4;
        w5.weatherDescription = "Sunny";
        w5.timestamp = new Timestamp(System.currentTimeMillis());

        WeatherInfo w6 = new WeatherInfo();
        w6.id = 1;
        w6.temperature = 28.7;
        w6.weatherDescription = "Sunny with rain";
        w6.timestamp = new Timestamp(System.currentTimeMillis());

        testWeatherInfo.add(w1);
        testWeatherInfo.add(w2);
        testWeatherInfo.add(w3);
        testWeatherInfo.add(w4);
        testWeatherInfo.add(w5);
        testWeatherInfo.add(w6);

        return testWeatherInfo;
    }

    // Orientation-based layout.

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);

        // Handle configuration changes, i.e. orientation change.
        updateLayoutForOrientation(configuration.orientation);
    }

    protected void updateLayoutForOrientation(int orientation) {

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {

            // Arrange the views in the main linear layout horizontally.
            mainLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

        } else {

            // Arrange the views in the main linear layout vertically.
            mainLinearLayout.setOrientation(LinearLayout.VERTICAL);
        }
    }
}
