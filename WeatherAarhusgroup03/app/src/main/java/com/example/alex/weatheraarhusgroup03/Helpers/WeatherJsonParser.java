package com.example.alex.weatheraarhusgroup03.Helpers;

import com.example.alex.weatheraarhusgroup03.WeatherInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;

/**
 * JSON deserializer from class
 * Created by kasper on 01/05/17.
 * Changed by Alex on 07/05/17 to deserialize to weather info object
 */


public class WeatherJsonParser {
    //Constant to convert from Kelvin to Celsius
    private static final double TO_CELCIUS_FROM_KELVIN = -273.15;

    //Static parse JSON to weather info object
    public static WeatherInfo parseCityWeatherJson(String jsonString){
        //The weather info object
        WeatherInfo weatherInfo = new WeatherInfo();

        //Try deserialize
        try {
            JSONObject cityWeatherJson = new JSONObject(jsonString);

            //Set weather info properties from JSON object
            weatherInfo.weatherDescription = cityWeatherJson.getJSONArray("weather").getJSONObject(0).getString("description");
            weatherInfo.temperature = Double.parseDouble(cityWeatherJson.getJSONObject("main").getString("temp")) + TO_CELCIUS_FROM_KELVIN;
            weatherInfo.timestamp = new Timestamp(System.currentTimeMillis());
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return weatherInfo;
    }
}
