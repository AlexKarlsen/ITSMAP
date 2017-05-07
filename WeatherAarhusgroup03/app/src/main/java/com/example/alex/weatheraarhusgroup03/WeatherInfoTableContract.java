package com.example.alex.weatheraarhusgroup03;

import android.provider.BaseColumns;

/**
 * Created by Emil- on 05/05/2017.
 */

// Heavily inspired by
// https://developer.android.com/training/basics/data-storage/databases.html#DbHelper
public final class WeatherInfoTableContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private WeatherInfoTableContract() {}

    /* Inner class that defines the table contents */
    public static class WeatherInfoTable implements BaseColumns {
        public static final String TABLE_NAME = "WeatherInfoTable";
        //public static final String COLUMN_ID = "id";
        public static final String COLUMN_WEATHERDESCRIPTION = "weatherDescription";
        public static final String COLUMN_TEMPERATURE = "temperature";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
