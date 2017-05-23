package com.example.alex.pubgolf.Helpers;

import com.example.alex.pubgolf.Models.Game;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Alex on 23/05/2017.
 */

public class FireBaseHelper {
    private DatabaseReference mDatabase;

    public FireBaseHelper(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void addNewGame(Game game)
    {

    }


}
