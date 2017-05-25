package com.example.alex.pubgolf.Models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Emil- on 23/05/2017.
 */

public class Game implements Serializable {

    public String Key;      // Database key
    public String Owner;    // Type?
    public String Title;
    public String Description;
    public Long StartTime;  // Date in millis. Optional

    public Long HoleIndex;  // Active Hole
    public GameState State;

    public Game() {}

    public java.sql.Timestamp GetStartTimeAsTimestamp()
    {
        return new java.sql.Timestamp(StartTime);
    }

    public enum GameState
    {
        Created,
        InProgress,
        Completed,
        Cancelled
    }

    public List<Player> Players;
    public List<Hole> Holes;
}
