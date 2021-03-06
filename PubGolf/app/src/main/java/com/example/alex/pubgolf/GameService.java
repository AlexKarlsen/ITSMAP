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
import com.example.alex.pubgolf.Models.Player;
import com.example.alex.pubgolf.Models.Score;
import com.facebook.Profile;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class GameService extends Service {

    //Logging keys
    private static final String LOG = "GameService";

    //Firebase URl Suffix
    private static final String GAMES_LEVEL = "Games";
    private static final String HOLES_LEVEL = "Holes";
    private static final String PLAYER_LEVEL = "Players";
    private static final String SCORES_LEVEL = "Scores";

    // Broadcasting Tags
    public static final String BROADCAST_GAME_SERVICE_RESULT = "BROADCAST_GAME_SERVICE_RESULT";
    public static final String BROADCAST_ADD_USER = "BROADCAST_ADD_USER";
    public static final String BROADCAST_ADD_SCORE = "BROADCAST_ADD_SCORE";

    // BroadcastTaskResult tags
    public static final String NEW_GAME_ADDED = "NEW_GAME_ADDED";
    public static final String OLD_GAME_CHANGED = "OLD_GAME_CHANGED";
    public static final String OLD_GAME_REMOVED = "OLD_GAME_REMOVED";

    public static final String EXTRA_DESCRIPTION = "EXTRA_DESCRIPTION";
    public static final String EXTRA_GAME = "EXTRA_GAME";

    public static final String EXTRA_ADD_SUCCESS = "EXTRA_ADD_SUCCESS";
    public static final String EXTRA_ADD_SCORE_SUCCESS = "EXTRA_ADD_SCORE_SUCCESS";

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
            // https://firebase.google.com/docs/database/admin/retrieve-data
            mDatabase.child(GAMES_LEVEL).addChildEventListener(new ChildEventListener() {

                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    // Check if user is a joined player of the received game
                    if (dataSnapshot.child(PLAYER_LEVEL).hasChild(Profile.getCurrentProfile().getId())) {
                        Log.d(LOG, "Received new game");
                        Game newGame = dataSnapshot.getValue(Game.class);
                        // Inform activity or list view
                        broadcastTaskResult(NEW_GAME_ADDED, newGame);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    // Check if user is a joined player of the received game
                    if (dataSnapshot.child(PLAYER_LEVEL).hasChild(Profile.getCurrentProfile().getId())) {
                        Log.d(LOG, "Received changed game");
                        Game changedGame = dataSnapshot.getValue(Game.class);
                        // Inform activity or list view
                        broadcastTaskResult(OLD_GAME_CHANGED, changedGame);
                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    // Check if user is a joined player of the received game
                    if (dataSnapshot.child(PLAYER_LEVEL).hasChild(Profile.getCurrentProfile().getId())) {
                        Log.d(LOG, "Received removed game");
                        Game removedGame = dataSnapshot.getValue(Game.class);
                        // Inform activity or list view
                        broadcastTaskResult(OLD_GAME_REMOVED, removedGame);
                    }
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

    // Saving data to firebase is inspired by following link:
    // https://firebase.google.com/docs/database/admin/save-data

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

    // Update game state by passing game key and the new state
    public void updateGameState(String gameKey, Game.GameState state){
        Map<String, Object> gameUpdates = new HashMap<String, Object>();
        gameUpdates.put("State", state);
        mDatabase.child(GAMES_LEVEL).child(gameKey).updateChildren(gameUpdates);
    }

    // Update game hole index by passing game key and the new index
    public void updateGameHoleIndex(String gameKey, long holeIndex){
        Map<String, Object> gameUpdates = new HashMap<String, Object>();
        gameUpdates.put("HoleIndex", holeIndex);
        mDatabase.child(GAMES_LEVEL).child(gameKey).updateChildren(gameUpdates);
    }

    // Remove game by key
    public void removeGame(String gameKey){
        mDatabase.child(GAMES_LEVEL).child(gameKey).removeValue();
    }

    // Add a user to a Game
    public void addPlayerToGame(final String gameKey, final String UUID, String name){
        final Player player = new Player(UUID, name);

        // Check db to see if game exists and if player has already joined it
        mDatabase.child(GAMES_LEVEL).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(BROADCAST_ADD_USER);

                // Check if game exists in firebase
                if (snapshot.hasChild(gameKey)) {
                    // Check if player has already joined game and join
                    if (!snapshot.child(gameKey).child(PLAYER_LEVEL).hasChild(UUID)) {
                        mDatabase.child(GAMES_LEVEL).child(gameKey).child(PLAYER_LEVEL).child(UUID).setValue(player);
                        broadcastIntent.putExtra(EXTRA_ADD_SUCCESS, true);
                    }
                    else    //Add failed; tell user
                        broadcastIntent.putExtra(EXTRA_ADD_SUCCESS, false);


                }
                else {  //Add failed; tell user
                    broadcastIntent.putExtra(EXTRA_ADD_SUCCESS, false);
                }
                Log.d(LOG, "Broadcasting join game result from Game Service");
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Add hole to an existing game
    public void addHoleToExistingGame(String gameKey, Hole hole){
        mDatabase.child(GAMES_LEVEL).child(gameKey).child(HOLES_LEVEL).setValue(hole);
    }

    // Add a score to a hole
    public void addScoreToHole(final String gameKey, final String holeIndex, final Player player, final long value){

        // Check db to see if game exists and if a score for the player has already been added
        mDatabase.child(GAMES_LEVEL).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(BROADCAST_ADD_SCORE);

                // Check if game exists in firebase
                if (snapshot.hasChild(gameKey)) {
                    // Check if player score has already been added
                    if (!snapshot.child(gameKey).child(HOLES_LEVEL).child(holeIndex).child(SCORES_LEVEL).hasChild(player.UUID)) {
                        Score score = new Score(player, value);
                        mDatabase.child(GAMES_LEVEL).child(gameKey).child(HOLES_LEVEL).child(holeIndex).child(SCORES_LEVEL).child(player.UUID).setValue(score);
                        broadcastIntent.putExtra(EXTRA_ADD_SCORE_SUCCESS, true);
                    }
                    else    //Add failed; tell user
                        broadcastIntent.putExtra(EXTRA_ADD_SCORE_SUCCESS, false);
                }
                else {  //Add failed; tell user
                    broadcastIntent.putExtra(EXTRA_ADD_SCORE_SUCCESS, false);
                }
                Log.d(LOG, "Broadcasting join game result from Game Service");
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
