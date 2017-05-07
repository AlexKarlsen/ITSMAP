package com.example.alex.weatheraarhusgroup03.Helpers;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kasper on 01/05/17.
 */

public class WeatherJsonParser {

    private static final double TO_CELCIOUS_FROM_KELVIN = -273.15;

    //example of simple JSON parsing
    public static String parseCityWeatherJson(String jsonString){
        String weatherString = "could not parse json";
        try {
            JSONObject cityWeatherJson = new JSONObject(jsonString);
            String name = cityWeatherJson.getString("name");
            JSONObject measurements = cityWeatherJson.getJSONObject("main");
            weatherString = name + " " + measurements.toString(); // measurements.getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("main") + " : " + measurements.getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("description");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return weatherString;
    }
}
