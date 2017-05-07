package com.example.alex.weatheraarhusgroup03;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.alex.weatheraarhusgroup03.Helpers.UrlHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;

public class WeatherInfoService extends Service {

    //Logging keys
    private static final String LOG = "WeatherInfoService";

    //Weather Api keys
    public static final String API_KEY = "5cbcdf2987ac637f7ad446132eaee0c8";
    public static final String CITY_NAME = "aarhus";
    public static final String API_CALL = "http://api.openweathermap.org/data/2.5/weather?q=" + CITY_NAME + "&appid=" + API_KEY;

    //Service configuration
    private static final long UPDATE_INTERVAL = 1800000;
    private boolean started = false;


    public WeatherInfoService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG, "Background service onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //in this case we only start the background running loop once
        if (!started && intent != null) {
            Log.d(LOG, "Background service onStartCommand");
            started = true;
            getWeatherInfoByInterval(UPDATE_INTERVAL);
        } else {
            Log.d(LOG, "Background service onStartCommand - already started. Get current weather info");
            getWeatherInfo();
        }
        return START_STICKY;
        //return super.onStartCommand(intent, flags, startId);
    }

    //Helper method for readability and not editing borrowed method.
    private String getWeatherInfo() {
        return UrlHelper.callURL(API_CALL);
    }

    //The function starts a task that gets the weather information by a given interval. The task recursively calls itself by the interval.
    private void getWeatherInfoByInterval(final long interval) {

        AsyncTask<Object, Object, String> task = new AsyncTask<Object, Object, String>(){

            @Override
            protected void onPreExecute(){ super.onPreExecute(); }

            @Override
            protected String doInBackground(Object[] params){
                String result;
                String s = "Background job";
                try {
                    Log.d(LOG, "Task started");
                    //result = getWeatherInfo();
                    Log.d(LOG, "Retrieved weather data: ");
                    Thread.sleep(interval);
                    Log.d(LOG, "Task completed");
                } catch (Exception e) {
                    s+= " did not finish due to error";
                    //e.printStackTrace();
                    return s;
                }
                s += " completed";
                return s;
            }

            @Override
            protected void onPostExecute(String stringResult) {
                super.onPostExecute(stringResult);

                if (started) {
                    getWeatherInfoByInterval(UPDATE_INTERVAL);
                }
            }

        };

        task.execute();
    }

    @Override
    public void onDestroy() {
        started = false;
        Log.d(LOG,"Background service destroyed");
        super.onDestroy();
    }

    // Interface.

    public WeatherInfo getCurrentWeather() {
        return createHistoricTestInfo().get(0);
    }

    ArrayList<WeatherInfo> getPastWeather() {
        return createHistoricTestInfo();
    }

    // Binding.

    private final IBinder weatherInfoServiceBinder = new WeatherInfoServiceBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return weatherInfoServiceBinder;
    }

    public class WeatherInfoServiceBinder extends Binder {

        WeatherInfoService getService() {
            return WeatherInfoService.this;
        }
    }

    // Test data.

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
}
