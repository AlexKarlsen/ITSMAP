package com.example.alex.pubgolf;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.example.alex.pubgolf.Adapters.GameArrayAdapter;
import com.example.alex.pubgolf.Models.Game;

import java.util.ArrayList;

public class GameListActivity extends AppCompatActivity {

    ArrayList<Game> gamesList = new ArrayList<Game>();

    // Views
    ListView gameListView;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);

        Intent gameServiceIntent = new Intent(this, GameService.class);
        startService(gameServiceIntent);
        //bindService(gameServiceIntent, connection, Context.BIND_IMPORTANT);

        initializeSubviews();
    }

    public void initializeSubviews()
    {
        gameListView = (ListView) findViewById(R.id.gamesListView);
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);

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
            Game game = (Game)intent.getSerializableExtra(GameService.EXTRA_GAME);

            if (description == GameService.NEW_GAME_ADDED) {
                gamesList.add(game);
                updateGamesListView(gamesList);
            } else {

                // Display a toast.
                //Toast.makeText(MainActivity.this, R.string.update_failed, Toast.LENGTH_SHORT).show();
            }
        }
    };
}
