package com.example.alex.pubgolf.Models;

import java.io.Serializable;

/**
 * Created by Emil- on 23/05/2017.
 */

public class Score implements Serializable {
    public Player Player;
    public long Value;

    public Score() {}

    public Score(Player _player, Long _value){
        Player = _player;
        Value = _value;
    }
}
