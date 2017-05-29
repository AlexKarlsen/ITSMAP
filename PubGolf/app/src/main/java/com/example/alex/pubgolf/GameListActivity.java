package com.example.alex.pubgolf;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.alex.pubgolf.Adapters.GameArrayAdapter;
import com.example.alex.pubgolf.Models.Game;

import java.util.ArrayList;

public class GameListActivity extends AppCompatActivity {

    public static final String EXTRA_GAME = "EXTRA_GAME";

    ArrayList<Game> gamesList = new ArrayList<Game>();
    //Boolean bound = false;
    //GameService gameService;

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
        gameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> list, View v, int pos, long id) {

                // Pass the selected game to the game activity, through
                Game selectedGame = gamesList.get(pos);
                Context context = getApplicationContext();
                Intent gameIntent = new Intent(context, GameNavigationActivity.class);
                gameIntent.putExtra(EXTRA_GAME, selectedGame);
                startActivity(gameIntent);
            }
        });
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

    /*
    // Modified from: https://developer.android.com/guide/components/bound-services.html#Binder
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {

            // Cast the IBinder and get WeatherInfoService instance.
            GameService.GameServiceBinder binder = (GameService.GameServiceBinder) service;
            gameService = binder.getService();
            bound = true;

            // Update the views with data from the service.
            if (gameService != null) {
                //updateCurrentView(weatherInfoService.getCurrentWeather());
                //updateHistoricListView(weatherInfoService.getPastWeather());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };
*/

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

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        stopService(new Intent(this, GameService.class));
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
