package com.example.alex.pubgolf;

/**
 * Created by Alex on 23/05/2017.
 */

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.alex.pubgolf.Models.Game;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class GameService extends Service {

    //Logging keys
    private static final String LOG = "GameService";

    //Firebase URls
    private static final String Games = "Games";

    private DatabaseReference mDatabase;

    public GameService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG, "Background service onCreate");
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG, "Background service onStartCommand");
        mDatabase.child(Games).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Game newGame = dataSnapshot.getValue(Game.class);
                // Inform activity or list view
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Game changedGame = dataSnapshot.getValue(Game.class);
                // Inform activity or list view
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Game removedGame = dataSnapshot.getValue(Game.class);
                // Inform activity or list view
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return START_STICKY;
    }

    // Creates a new game and saves Firebase generated key to the locale instance of the game
    public void createNewGame(Game newGame){
        DatabaseReference newGameRef = mDatabase.child(Games).push();
        newGame.Key = newGameRef.getKey();
        newGameRef.setValue(newGame);
    }

    public void updateGameTitle(String gameKey, String gameTitle){
        Map<String, Object> gameUpdates = new HashMap<String, Object>();
        gameUpdates.put(gameKey, gameTitle);
        mDatabase.child(Games).updateChildren(gameUpdates);
    }

    public void removeGame(String gameKey){
        mDatabase.child(Games).child(gameKey).removeValue();
    }

    // Binding to Service can be done from activity
    private final IBinder GameServiceBinder = new GameServiceBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return GameServiceBinder;
    }

    public class GameServiceBinder extends Binder
    {
        GameService getService() { return GameService.this; }
    }
}
