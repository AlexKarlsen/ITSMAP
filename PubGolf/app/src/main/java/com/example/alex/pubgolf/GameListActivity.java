package com.example.alex.pubgolf;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.example.alex.pubgolf.Adapters.GameArrayAdapter;
import com.example.alex.pubgolf.Models.Game;

import java.security.Timestamp;
import java.security.acl.Owner;
import java.util.ArrayList;

public class GameListActivity extends AppCompatActivity {

    // Views
    ListView gameListView;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);

        initializeSubviews();
    }

    public void initializeSubviews()
    {
        gameListView = (ListView) findViewById(R.id.gamesListView);
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        updateGamesListView(makeTestData());

        // Add on click handler to the fab.
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Create intent for game list activity.
                Context context = getApplicationContext();
                Intent lobbyIntent = new Intent(context, LobbyActivity.class);
                startActivity(lobbyIntent);
            }
        });
    }

    private void updateGamesListView(ArrayList<Game> gamesData) {

        if (gamesData == null) { return; }

        // Populate the list view with the weather info using a custom adapter.
        GameArrayAdapter adapter = new GameArrayAdapter(this, R.layout.game_info_list_item, gamesData);
        gameListView.setAdapter(adapter);
    }

    private ArrayList<Game> makeTestData()
    {
        ArrayList<Game> games = new ArrayList<Game>();

        Game game1 = new Game();
        game1.Owner = "Emil";   // ??
        game1.Title = "PGÅ 2017";
        game1.StartTime = System.currentTimeMillis();
        game1.State = Game.GameState.InProgress;

        Game game2 = new Game();
        game2.Owner = "Alex";
        game2.Title = "PGÅ 2018";
        game2.StartTime = (long)1495537461;
        game2.State = Game.GameState.Created;

        games.add(game1);
        games.add(game2);

        return games;
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Register broadcast receivers.
        IntentFilter filter = new IntentFilter();
        filter.addAction(GameService.BROADCAST_GAME_SERVICE_RESULT);
        LocalBroadcastManager.getInstance(this).registerReceiver(onGameServiceResult, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Unbind from the service;
        //if (bound) {
            //unbindService(connection);
          //  bound = false;
        //}

        // Unregister broadcast receiver.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onGameServiceResult);
    }

    private BroadcastReceiver onGameServiceResult = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Check if the service was successful.
            String description = intent.getStringExtra(GameService.EXTRA_DESCRIPTION);
            Log.d("Description", description);

            if (description == "New Game added") {

                // Call the service.
                //getDataFromServiceAndUpdateViews();
            } else {

                // Display a toast.
                //Toast.makeText(MainActivity.this, R.string.update_failed, Toast.LENGTH_SHORT).show();
            }
        }
    };
}
