package com.example.alex.pubgolf;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    List<Player> playerList;
    Map<String, Player> playerMap;

    Hole hole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_score);

        activityTitleTextView = (TextView) findViewById(R.id.addScoreActivityTitleTextView);


        enterScoreTextView = (TextView) findViewById(R.id.enterScoreTextView);
        enterScoreTextView.setText("Select Score");

        doneButton = (Button) findViewById(R.id.doneButton);
        doneButton.setText("Done");

        playerSpinner = (Spinner) findViewById(R.id.playersSpinner);

        Intent intent = getIntent();
        if (intent != null) {
            handleStartWithIntent(intent);
        }

        activityTitleTextView.setText("Add a score for Hole " + (hole.Index+1));
        
        ArrayList<String> playerNames = new ArrayList<>();
        //Hint text
        playerNames.add("Select A Player");
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

        playerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
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

        scorePicker = (NumberPicker) findViewById(R.id.scorePicker);
        scorePicker.setMinValue(MinimumScore);
        scorePicker.setMaxValue(MaximumScore);
    }

    protected void handleStartWithIntent(Intent intent) {
        Game game = (Game) intent.getExtras().getSerializable(EditGameActivity.EXTRA_GAME);
        hole = (Hole) intent.getExtras().getSerializable(GameNavigationActivity.EXTRA_HOLE);
        if (game == null) return;
        if (game.Players != null) {
            playerMap = new HashMap<>();
            playerMap.putAll(game.Players);
        }
    }
}

