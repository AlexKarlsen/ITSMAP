package com.example.alex.pubgolf.Models;

import android.location.Address;

import java.util.List;

/**
 * Created by Emil- on 23/05/2017.
 */

public class Hole {
    public String Name;
    public Address HoleAddress;
    public Long Time;   // Time in millis. Optional
    public String Description;
    public Long Index;  // Index representing the hole order in a Game.

    public List<Score> Scores;

    public Hole() {}

    public java.sql.Timestamp GetTimeAsTimestamp()
    {
        return new java.sql.Timestamp(Time);
    }
}