package com.example.alex.pubgolf.Models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Emil- on 23/05/2017.
 */

public class Game implements Serializable {

    public String Key;      // Database key
    public Player Owner;    // Type?
    public String Title;
    public String Description;
    public long StartTime;  // Date in millis. Optional

    public long HoleIndex;  // Active Hole
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

    public HashMap<String, Player> Players;
    public List<Hole> Holes;
}
