package com.example.alex.pubgolf.Models;

import java.io.Serializable;

/**
 * Created by Emil- on 23/05/2017.
 */

public class Score implements Serializable {
    public Player player;
    public long value;

    public Score() {}

    public Score(Player _player, Long _value){
        player = _player;
        value = _value;
    }
}
