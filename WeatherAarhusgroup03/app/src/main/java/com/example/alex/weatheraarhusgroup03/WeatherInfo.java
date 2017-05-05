package com.example.alex.weatheraarhusgroup03;

import java.sql.Timestamp;

/**
 * Created by Emil- on 05/05/2017.
 */

public class WeatherInfo {
    int id;                     // (corresponding to database entry)
    String weatherDescription;  // (text)
    double temperature;         // (in celcius)
    Timestamp timestamp;        // (when the data is from)
}
