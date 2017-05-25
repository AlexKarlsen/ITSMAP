package com.example.alex.pubgolf;

/**
 * Created by Alex on 23/05/2017.
 * Edited by Alex on 24/05/2017.
 */

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.alex.pubgolf.Models.Game;
import com.example.alex.pubgolf.Models.Hole;
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

    //Firebase URl Suffix
    private static final String GAMES_LEVEL = "Games";
    private static final String HOLES_LEVEL = "Holes";
    private static final String PLAYER_LEVEL = "Players";

    // Broadcasting Tags
    public static final String BROADCAST_GAME_SERVICE_RESULT = "BROADCAST_GAME_SERVICE_RESULT";
    public static final String EXTRA_DESCRIPTION = "EXTRA_DESCRIPTION";
    public static final String EXTRA_GAME = "EXTRA_GAME";

    // Service state boolean
    private boolean started = false;

    // Firebase reference
    private DatabaseReference mDatabase;

    public GameService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG, "Background service onCreate");

        // Get a reference to Firebase Real-time database at root-level
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG, "Game service onStartCommand");

        // Check if service is already started. If not start service and register Firebase listeners.
        if(!started && intent != null){
            Log.d(LOG, "Start Game service");
            started = true;

            // Listen for changes at Games level in Firebase
            mDatabase.child(GAMES_LEVEL).addChildEventListener(new ChildEventListener() {

                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Log.d(LOG, "Received new game");
                    Game newGame = dataSnapshot.getValue(Game.class);
                    // Inform activity or list view
                    broadcastTaskResult("New Game added", newGame);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Log.d(LOG, "Received changed game");
                    Game changedGame = dataSnapshot.getValue(Game.class);
                    // Inform activity or list view
                    broadcastTaskResult("A Game is changed", changedGame);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Log.d(LOG, "Received removed game");
                    Game removedGame = dataSnapshot.getValue(Game.class);
                    // Inform activity or list view
                    broadcastTaskResult("A Game is removed", removedGame);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else{
            Log.d(LOG, "Game Service already started");
        }
        return START_STICKY;
    }

    // Creates a new game and saves Firebase generated key to the locale instance of the game
    public void createNewGame(Game newGame){
        DatabaseReference newGameRef = mDatabase.child(GAMES_LEVEL).push();
        newGame.Key = newGameRef.getKey();
        newGameRef.setValue(newGame);
    }

    // Update game title by passing game key and the new title
    public void updateGameTitle(String gameKey, String gameTitle){
        Map<String, Object> gameUpdates = new HashMap<String, Object>();
        gameUpdates.put(gameKey, gameTitle);
        mDatabase.child(GAMES_LEVEL).updateChildren(gameUpdates);
    }

    // Remove game by key
    public void removeGame(String gameKey){
        mDatabase.child(GAMES_LEVEL).child(gameKey).removeValue();
    }

    // Add a user to a Game
    public void addUserToGame(String gameKey, String UUID, String name){
        Map<String, Object> gameUpdates = new HashMap<String, Object>();
        gameUpdates.put(PLAYER_LEVEL, UUID);
        gameUpdates.put(PLAYER_LEVEL, name);
        mDatabase.child(GAMES_LEVEL).child(gameKey).updateChildren(gameUpdates);
    }

    // Add hole to an existing game
    public void addHoleToExistingGame(String gameKey, Hole hole){
        mDatabase.child(GAMES_LEVEL).child(gameKey).child(HOLES_LEVEL).setValue(hole);
    }

    // Broadcasting events to the activity, activities need to bind to service and implement onRecieve()
    private void broadcastTaskResult(String changedDescription, Game changedGame){
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(BROADCAST_GAME_SERVICE_RESULT);
        broadcastIntent.putExtra(EXTRA_DESCRIPTION, changedDescription);
        broadcastIntent.putExtra(EXTRA_GAME, changedGame);
        Log.d(LOG, "Broadcasting from Game Service");
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
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
