package com.example.alex.weatheraarhusgroup03;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    WeatherInfoService weatherInfoService;
    Boolean bound = false;

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
        Intent backgroundServiceIntent = new Intent(this, WeatherInfoService.class);
        startService(backgroundServiceIntent);
        bindService(backgroundServiceIntent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Register broadcast receivers.
        IntentFilter filter = new IntentFilter();
        filter.addAction(WeatherInfoService.BROADCAST_BACKGROUND_SERVICE_RESULT);
        LocalBroadcastManager.getInstance(this).registerReceiver(onWeatherServiceResult, filter);
    }

    // Modified from: https://developer.android.com/guide/components/bound-services.html#Binder
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {

            // Cast the IBinder and get WeatherInfoService instance.
            WeatherInfoService.WeatherInfoServiceBinder binder = (WeatherInfoService.WeatherInfoServiceBinder) service;
            weatherInfoService = binder.getService();
            bound = true;

            // Update the views with data from the service.
            if (weatherInfoService != null) {
                updateCurrentView(weatherInfoService.getCurrentWeather());
                updateHistoricListView(weatherInfoService.getPastWeather());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };

    @Override
    protected void onStop() {
        super.onStop();

        // Unbind from the service;
        if (bound) {
            unbindService(connection);
            bound = false;
        }

        // Unregister broadcast receiver.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onWeatherServiceResult);
    }

    private void initializeSubviews() {

        // Get references to interface elements.
        mainLinearLayout = (LinearLayout) findViewById(R.id.mainLinearLayout);
        currentTemperatureTextView = (TextView) findViewById(R.id.currentTemperatureTextView);
        currentDescriptionTextView = (TextView) findViewById(R.id.currentDescriptionTextView);
        currentColorFrameLayout = (FrameLayout) findViewById(R.id.colorFrameLayout);
        historicWeatherInfoListView = (ListView) findViewById(R.id.historicWeatherInfoListView);
        refreshFab = (FloatingActionButton) findViewById(R.id.refreshFab);

        // Clear the current textViews.
        currentColorFrameLayout.setBackgroundColor(Color.WHITE);
        currentDescriptionTextView.setText("");
        currentTemperatureTextView.setText("");

        // Add on click handler to the refresh fab.
        refreshFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Call the service.
                getDataFromServiceAndUpdateViews();
            }
        });
    }

    private void getDataFromServiceAndUpdateViews() {
        updateCurrentView(weatherInfoService.getCurrentWeather());
        updateHistoricListView(weatherInfoService.getPastWeather());
        Toast.makeText(MainActivity.this, R.string.update_success, Toast.LENGTH_SHORT).show();
    }

    // MARK: - Updating the ui elements with data.

    private void updateHistoricListView(ArrayList<WeatherInfo> historicData) {

        if (historicData == null) { return; }

        // Populate the list view with the weather info using a custom adapter.
        WeatherInfoArrayAdapter adapter = new WeatherInfoArrayAdapter(this, R.layout.weather_info_list_item, historicData);
        historicWeatherInfoListView.setAdapter(adapter);
    }

    private void updateCurrentView(WeatherInfo currentInfo) {

        if (currentInfo == null) { return; }

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

    // MARK: - Orientation-based layout.

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

    private BroadcastReceiver onWeatherServiceResult = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Check if the service was successful.
            Boolean success = intent.getBooleanExtra(WeatherInfoService.EXTRA_STATUS, false);

            if (success) {

                // Call the service.
                getDataFromServiceAndUpdateViews();
            } else {

                // Display a toast.
                Toast.makeText(MainActivity.this, R.string.update_failed, Toast.LENGTH_SHORT).show();
            }
        }
    };
}
