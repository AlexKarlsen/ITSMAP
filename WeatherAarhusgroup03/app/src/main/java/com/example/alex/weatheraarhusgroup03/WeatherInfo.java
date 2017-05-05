package com.example.alex.weatheraarhusgroup03;

import java.sql.Timestamp;

/**
 * Created by Emil- on 05/05/2017.
 */

public class WeatherInfo {
    public int id;                     // (corresponding to database entry)
    public String weatherDescription;  // (text)
    public double temperature;         // (in celcius)
    public Timestamp timestamp;        // (when the data is from)
}
