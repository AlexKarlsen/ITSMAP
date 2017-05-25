package com.example.alex.pubgolf;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.alex.pubgolf.Adapters.GameArrayAdapter;
import com.example.alex.pubgolf.Adapters.HoleArrayAdapter;
import com.example.alex.pubgolf.Models.Game;
import com.example.alex.pubgolf.Models.Hole;

import java.util.ArrayList;
import java.util.List;

public class EditCourseActivity extends AppCompatActivity {

    Game game;
    GameService gameService;
    Boolean bound = false;

    ArrayList<Hole> holesList = new ArrayList<Hole>();

    // Views
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);

        Intent intent = getIntent();
        if (intent != null) {
            handleStartWithIntent(intent);
        }

        Intent gameServiceIntent = new Intent(this, GameService.class);
        startService(gameServiceIntent);
        bindService(gameServiceIntent, connection, Context.BIND_IMPORTANT);

        listView = (ListView) findViewById(R.id.holeListView);
        Button doneButton = (Button) findViewById(R.id.doneButton);

        // Add on click handler
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Save the created game to Firebase
                gameService.createNewGame(game);
            }
        });

        updateListView(holesList);
    }

    protected void handleStartWithIntent(Intent intent) {
        game = (Game) intent.getExtras().getSerializable(EditGameActivity.EXTRA_GAME);
        holesList = (ArrayList<Hole>) game.Holes;
        updateListView(holesList);
    }

    private void updateListView(ArrayList<Hole> data) {

        if (data == null) { return; }

        // Populate the list view with the weather info using a custom adapter.
        HoleArrayAdapter adapter = new HoleArrayAdapter(this, R.layout.hole_info_list_item, data);
        listView.setAdapter(adapter);
    }

    // Modified from: https://developer.android.com/guide/components/bound-services.html#Binder
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {

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

    @Override
    protected void onStop() {
        super.onStop();

        // Unbind from the service;
        if (bound) {
            unbindService(connection);
            bound = false;
        }

        // Unregister broadcast receiver.
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(onWeatherServiceResult);
    }
}
