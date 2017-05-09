package com.example.alex.weatheraarhusgroup03;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.alex.weatheraarhusgroup03.Helpers.WeatherJsonParser;

import java.util.ArrayList;

/**
 * Created by Alex on 05/05/17
 * Edited by Alex on 07/05/17
 */

public class WeatherInfoService extends Service {

    //Intent keys
    public static final String BROADCAST_BACKGROUND_SERVICE_RESULT = "BROADCAST_BACKGROUND_SERVICE_RESULT";
    public static final String EXTRA_STATUS = "EXTRA_STATUS";

    //Logging keys
    private static final String LOG = "WeatherInfoService";

    //Weather Api keys
    public static final String API_KEY = "5cbcdf2987ac637f7ad446132eaee0c8";
    public static final String CITY_NAME = "aarhus";
    public static final String API_CALL = "http://api.openweathermap.org/data/2.5/weather?q=" + CITY_NAME + "&appid=" + API_KEY;

    //Service configuration
    private static final long UPDATE_INTERVAL = 10000; // 1800000 = 30 min.
    private boolean started = false;

    //Database helper
    private static DatabaseHelper dbHelper;

    //Queue for Volley
    RequestQueue queue;

    public WeatherInfoService() { }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG, "Background service onCreate");
        dbHelper = new DatabaseHelper(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //in this case we only start the background running loop once
        if (!started && intent != null) {
            Log.d(LOG, "Background service onStartCommand");
            started = true;
            getWeatherInfoByInterval(UPDATE_INTERVAL);
        } else {
            Log.d(LOG, "Background service onStartCommand - already started.");
        }
        return START_STICKY;
    }

    //Helper method for readability and not editing borrowed method.
    private void getWeatherInfo() {
        sendRequest(API_CALL);
    }

    //The function starts a task that gets the weather information by a given interval.
    //The task recursively calls itself by the interval.
    //Strongly inspired from the service demo from class
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
                    getWeatherInfo();
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
                if (started) { getWeatherInfoByInterval(UPDATE_INTERVAL); }
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

    //Broadcasting completion status to the activity
    private void broadcastTaskResult(Boolean success){
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(BROADCAST_BACKGROUND_SERVICE_RESULT);
        broadcastIntent.putExtra(EXTRA_STATUS, success);
        Log.d(LOG, "Broadcast sent");
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }

    //Send Http request using volley. Inspired by the weather app demo
    public void sendRequest(String callUrl){
        //send request using Volley
        if(queue==null){
            queue = Volley.newRequestQueue(this);
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, callUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Deserialize Json response to a weatherinfo object
                        WeatherInfo weatherinfo = interpretWeatherJSON(response);

                        //Write to database
                        dbHelper.insertWeatherInfo(weatherinfo);

                        //On success broadcast result
                        broadcastTaskResult(true);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                broadcastTaskResult(false);
            }
        });
        queue.add(stringRequest);
    }

    //Attempt to decode the json response from weather server
    public WeatherInfo interpretWeatherJSON(String jsonResponse){ return WeatherJsonParser.parseCityWeatherJson(jsonResponse); }

    //Required functions to call from activity
	public WeatherInfo getCurrentWeather() { return dbHelper.getLatestWeatherInfo();}

    ArrayList<WeatherInfo> getPastWeather() {
        return dbHelper.get24HoursWeatherInfo();
    }

    //Interface.
    private final IBinder weatherInfoServiceBinder = new WeatherInfoServiceBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return weatherInfoServiceBinder;
    }

    //Binding.
    public class WeatherInfoServiceBinder extends Binder { WeatherInfoService getService() {return WeatherInfoService.this;}}
}
