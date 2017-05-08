package com.example.alex.weatheraarhusgroup03;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by Emil- on 05/05/2017.
 */

// Heavily inspired by
// https://developer.android.com/training/basics/data-storage/databases.html#DbHelper

// getAllWeatherInfo() and get24HoursWeatherInfo() are inspired by Kaspers demo DataStorageDemo
public class DatabaseHelper extends SQLiteOpenHelper{

    // Logging key
    private static final String LOG = "DatabaseHelper";

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "WeatherInfo.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        Log.e(LOG, "Creating database");
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        Log.e(LOG, "Upgrading database");
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e(LOG, "Downgrading database");
        onUpgrade(db, oldVersion, newVersion);
    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + WeatherInfoTableContract.WeatherInfoTable.TABLE_NAME + " (" +
                    WeatherInfoTableContract.WeatherInfoTable._ID + " INTEGER PRIMARY KEY," +
                    WeatherInfoTableContract.WeatherInfoTable.COLUMN_WEATHERDESCRIPTION + " TEXT," +
                    WeatherInfoTableContract.WeatherInfoTable.COLUMN_TEMPERATURE + " DOUBLE," +
                    WeatherInfoTableContract.WeatherInfoTable.COLUMN_TIMESTAMP + " LONG)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + WeatherInfoTableContract.WeatherInfoTable.TABLE_NAME;


    public int insertWeatherInfo(WeatherInfo weatherInfo)
    {
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        //values.put(WeatherInfoTableContract.WeatherInfoTable._ID, title);
        values.put(WeatherInfoTableContract.WeatherInfoTable.COLUMN_WEATHERDESCRIPTION, weatherInfo.weatherDescription);
        values.put(WeatherInfoTableContract.WeatherInfoTable.COLUMN_TEMPERATURE, weatherInfo.temperature);
        values.put(WeatherInfoTableContract.WeatherInfoTable.COLUMN_TIMESTAMP, weatherInfo.timestamp.getTime());

        Log.e(LOG, "Inserting row");

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(WeatherInfoTableContract.WeatherInfoTable.TABLE_NAME, null, values);
        return (int) newRowId;
    }

    public ArrayList<WeatherInfo> getAllWeatherInfo() {
        String selectQuery = "SELECT  * FROM " + WeatherInfoTableContract.WeatherInfoTable.TABLE_NAME;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        ArrayList<WeatherInfo> infos = getWeatherInfoFromCursor(cursor);

        return infos;
    }

    public ArrayList<WeatherInfo> get24HoursWeatherInfo()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Long timestamp24HoursAgoMillis = (System.currentTimeMillis()) - TimeUnit.HOURS.toMillis(24);

        String selectQuery = "SELECT  * FROM " + WeatherInfoTableContract.WeatherInfoTable.TABLE_NAME + " WHERE "
                + WeatherInfoTableContract.WeatherInfoTable.COLUMN_TIMESTAMP + " > " + timestamp24HoursAgoMillis + " ORDER BY " + WeatherInfoTableContract.WeatherInfoTable.COLUMN_TIMESTAMP + " DESC" ;

        Log.e(LOG, selectQuery);

        Cursor cursor = db.rawQuery(selectQuery, null);

        ArrayList<WeatherInfo> infos = getWeatherInfoFromCursor(cursor);

        return infos;
    }

    public WeatherInfo getLatestWeatherInfo()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + WeatherInfoTableContract.WeatherInfoTable.TABLE_NAME + " ORDER BY " + WeatherInfoTableContract.WeatherInfoTable.COLUMN_TIMESTAMP + " DESC LIMIT 1";
        Log.e(LOG, selectQuery);

        Cursor cursor = db.rawQuery(selectQuery, null);

        ArrayList<WeatherInfo> infos = getWeatherInfoFromCursor(cursor);
        WeatherInfo weatherInfo = infos.get(0);

        return weatherInfo;
    }

    // Helper function which retrieves a list of WeatherInfo from a Cursor
    private ArrayList<WeatherInfo> getWeatherInfoFromCursor(Cursor cursor)
    {
        if (cursor == null || cursor.getCount() == 0)
            return null;

        ArrayList<WeatherInfo> infos = new ArrayList<WeatherInfo>();

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                WeatherInfo info = new WeatherInfo();
                info.id = (cursor.getInt((cursor.getColumnIndexOrThrow(WeatherInfoTableContract.WeatherInfoTable._ID))));
                info.weatherDescription = ((cursor.getString(cursor.getColumnIndexOrThrow(WeatherInfoTableContract.WeatherInfoTable.COLUMN_WEATHERDESCRIPTION))));
                info.temperature = (cursor.getDouble(cursor.getColumnIndexOrThrow(WeatherInfoTableContract.WeatherInfoTable.COLUMN_TEMPERATURE)));
                info.timestamp = new Timestamp(cursor.getLong(cursor.getColumnIndexOrThrow(WeatherInfoTableContract.WeatherInfoTable.COLUMN_TIMESTAMP)));

                // adding to list
                infos.add(info);
            } while (cursor.moveToNext());
        }

        return infos;
    }
}
