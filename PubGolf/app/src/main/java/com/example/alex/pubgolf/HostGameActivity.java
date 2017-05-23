package com.example.alex.pubgolf;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.alex.pubgolf.Models.Game;

public class HostGameActivity extends AppCompatActivity {

    EditText nameEditText;
    EditText descriptionEditText;
    EditText dateEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_game);

        // Initialize the subviews.
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        dateEditText = (EditText) findViewById(R.id.dateEditText);

        // Initialize the host game button.
        Button continueButton = (Button) findViewById(R.id.continueButton);
        continueButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onContinueClicked();
            }
        });
    }

    void onContinueClicked() {

        // Validate user input?

        // Get the values input by the user.
        String name = nameEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String date = dateEditText.getText().toString();

        // Create the new game object.
        Game game = new Game();
        game.Title = name;
        game.Description = description;
        game.StartTime = (long) 0; // Get the actual time instead.
        game.HoleIndex = (long) 0;
        game.State = Game.GameState.Created;

        // Pass the game object on to the course edit activity.
    }
}
