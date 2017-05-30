package com.example.alex.pubgolf;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.alex.pubgolf.Models.Game;
import com.example.alex.pubgolf.Models.Hole;
import com.example.alex.pubgolf.Models.Player;
import com.example.alex.pubgolf.Models.Score;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddScoreActivity extends AppCompatActivity {

    // Limits for score values
    static final int MinimumScore = 1;
    static final int MaximumScore = 10;

    // UI widgets
    Spinner playerSpinner;
    NumberPicker scorePicker;
    TextView activityTitleTextView;
    TextView enterScoreTextView;
    Button doneButton;

    // BL globals
    GameService gameService;
    Boolean bound = false;
    Game game;
    Player selectedPlayer = null;
    Map<String, Player> playerMap;
    Hole hole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_score);

        // Get Intent
        Intent intent = getIntent();
        if (intent != null) {
            handleStartWithIntent(intent);
        }

        //Initialize all sub-modules
        initializeSubViews();
        initializeScorePicker();
        initializeSpinner();

    }

    // https://developer.android.com/reference/android/widget/NumberPicker.html#NumberPicker(android.content.Context,%20android.util.AttributeSet)
    private void initializeScorePicker() {
        scorePicker = (NumberPicker) findViewById(R.id.scorePicker);
        scorePicker.setMinValue(MinimumScore);
        scorePicker.setMaxValue(MaximumScore);
    }

    // Initializes spinner. It disables position 0 and greys it out to make a hint text.
    // https://android--code.blogspot.dk/2015/08/android-spinner-hint.html
    private void initializeSpinner() {
        ArrayList<String> playerNames = new ArrayList<>();
        //Hint text
        playerNames.add(getString(R.string.select_a_player));
        for (Player player : playerMap.values()) playerNames.add(player.Name);

        // Create an ArrayAdapter using the string array playerNames
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, playerNames){

            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }


        };
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        playerSpinner.setAdapter(adapter);

        // Saves the selected player to enable adding score
        playerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Position 0 is a hint - return so no player is being set
                if(position == 0) return;
                selectedPlayer = new Player();
                for (Player player: playerMap.values()) {
                    if (player.Name == (String) parent.getItemAtPosition(position))
                    {
                        selectedPlayer = player;
                        doneButton.setEnabled(true);
                        break;
                    }
                }

                // If user change the default selection
                // First item is disable and it is used for hint
                if(position > 0){
                    // Notify the selected item text
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void initializeSubViews(){
        activityTitleTextView = (TextView) findViewById(R.id.addScoreActivityTitleTextView);
        activityTitleTextView.setText(getString(R.string.add_score_to_hole)+ " " + (hole.Index+1) + " " + "'" + hole.Name + "'");

        Intent gameServiceIntent = new Intent(this, GameService.class);
        startService(gameServiceIntent);
        bindService(gameServiceIntent, connection, Context.BIND_IMPORTANT);

        enterScoreTextView = (TextView) findViewById(R.id.enterScoreTextView);
        doneButton = (Button) findViewById(R.id.doneButton);
        playerSpinner = (Spinner) findViewById(R.id.playersSpinner);

        if(selectedPlayer == null){
            doneButton.setEnabled(false);
        }

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameService.addScoreToHole(game.Key, String.valueOf(hole.Index), selectedPlayer, scorePicker.getValue());
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    protected void handleStartWithIntent(Intent intent) {
        game = (Game) intent.getExtras().getSerializable(EditGameActivity.EXTRA_GAME);
        hole = (Hole) intent.getExtras().getSerializable(GameNavigationActivity.EXTRA_HOLE);
        if (game == null) return;
        if (game.Players != null) {
            playerMap = new HashMap<>();
            playerMap.putAll(game.Players);

            // If the player already has a score for the particular hole it is removed from the map
            if (hole.Scores != null) {
                for (Score score : hole.Scores.values()) {

                    String playerId = score.Player.UUID;
                    playerMap.remove(playerId);
                }
            }
        }
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
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };
}

