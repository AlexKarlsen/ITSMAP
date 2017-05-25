package com.example.alex.pubgolf.Models;

/**
 * Created by Emil- on 23/05/2017.
 */

public class Score {
    public Player player;
    public long value;

    public Score() {}

    public Score(Player _player, Long _value){
        player = _player;
        value = _value;
    }
}
