package com.example.alex.pubgolf.Models;

import java.sql.Timestamp;

/**
 * Created by Priebe on 25/05/2017.
 */

public class TimeContainer {

    public int year;
    public int monthOfYear;
    public int dayOfMonth;
    public int hourOfDay;
    public int minute;

    public TimeContainer() {
        this.year = 0;
        this.monthOfYear = 0;
        this.dayOfMonth = 0;
        this.hourOfDay = 0;
        this.minute = 0;
    }

    public TimeContainer(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute) {
        this.year = year;
        this.monthOfYear = monthOfYear;
        this.dayOfMonth = dayOfMonth;
        this.hourOfDay = hourOfDay;
        this.minute = minute;
    }

    public Timestamp toTimestamp() {
        return Timestamp.valueOf(year + "-" + monthOfYear + "-" + dayOfMonth + " " + hourOfDay + ":" + minute);
    }
}
