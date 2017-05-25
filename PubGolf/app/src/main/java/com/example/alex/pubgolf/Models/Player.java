package com.example.alex.pubgolf.Models;

/**
 * Created by Alex on 25/05/2017.
 */

public class Player {
    public Player(){}
    public Player(String _uuid, String _name) {
        UUID = _uuid;
        Name = _name;
    }
    public String UUID;
    public String Name;
}
