package com.example.alex.weatheraarhusgroup03;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
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
    public int onStartCommand(Intent intent, int flags, int startId){
        //in this case we only start the background running loop once
        if(!started && intent!=null) {
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

    @Override
    public IBinder onBind(Intent intent) {
        //this service is not for binding: return null.
        return null;
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
                    result = getWeatherInfo();
                    Log.d(LOG, "Retrieved weather data: " + result);
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
}
