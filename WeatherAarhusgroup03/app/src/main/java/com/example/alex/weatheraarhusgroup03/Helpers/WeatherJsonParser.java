package com.example.alex.weatheraarhusgroup03.Helpers;

import com.example.alex.weatheraarhusgroup03.WeatherInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;

/**
 * Created by kasper on 01/05/17.
 */

public class WeatherJsonParser {

    private static final double TO_CELCIOUS_FROM_KELVIN = -273.15;

    //example of simple JSON parsing
    public static WeatherInfo parseCityWeatherJson(String jsonString){
        String weatherString = "could not parse json";
        WeatherInfo weatherInfo = new WeatherInfo();

        try {
            JSONObject cityWeatherJson = new JSONObject(jsonString);

            weatherInfo.weatherDescription = cityWeatherJson.getJSONArray("weather").getJSONObject(0).getString("description");
            weatherInfo.temperature = Double.parseDouble(cityWeatherJson.getJSONObject("main").getString("temp")) + TO_CELCIOUS_FROM_KELVIN;
            weatherInfo.timestamp = new Timestamp(System.currentTimeMillis());

            //weatherString = name + " " + measurements.toString(); // measurements.getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("main") + " : " + measurements.getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("description");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return weatherInfo;
    }
}
