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

    public static final int NEW_HOLE_REQUEST = 10001;

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

        listView = (ListView) findViewById(R.id.holeListView);

        Intent intent = getIntent();
        if (intent != null) {
            handleStartWithIntent(intent);
        }

        Intent gameServiceIntent = new Intent(this, GameService.class);
        startService(gameServiceIntent);
        bindService(gameServiceIntent, connection, Context.BIND_IMPORTANT);

        // Add on click handler
        Button doneButton = (Button) findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                game.Holes = holesList;

                // Save the created game to Firebase.
                gameService.createNewGame(game);
            }
        });

        Button addHoleButton = (Button) findViewById(R.id.addHoleButton);
        addHoleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Create intent for host game activity.
                Context context = getApplicationContext();
                Intent addHoleIntent = new Intent(context, EditHoleActivity.class);
                startActivityForResult(addHoleIntent, NEW_HOLE_REQUEST);
            }
        });

        updateListView(holesList);
    }

    protected void handleStartWithIntent(Intent intent) {
        game = (Game) intent.getExtras().getSerializable(EditGameActivity.EXTRA_GAME);
        if (game == null) return;
        if (game.Holes != null) {
            holesList = new ArrayList<Hole>();
            holesList.addAll(game.Holes);
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == NEW_HOLE_REQUEST && resultCode == RESULT_OK) {

            Hole hole = (Hole) data.getExtras().getSerializable(EditHoleActivity.EXTRA_HOLE);
            if (hole == null) return;
            holesList.add(hole);
            updateListView(holesList);
        }
    }
}
