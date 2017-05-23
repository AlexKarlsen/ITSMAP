package com.example.alex.pubgolf;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alex.pubgolf.Models.Game;

public class EditGameActivity extends AppCompatActivity {

    public static final String EXTRA_GAME = "EXTRA_GAME";

    EditText nameEditText;
    EditText descriptionEditText;
    EditText dateEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_game);

        // Initialize the subviews.
        nameEditText = (EditText) this.findViewById(R.id.nameEditText);
        descriptionEditText = (EditText) this.findViewById(R.id.descriptionEditText);
        dateEditText = (EditText) this.findViewById(R.id.dateEditText);

        // Initialize the host game button.
        Button continueButton = (Button) this.findViewById(R.id.continueButton);
        continueButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onContinueClicked();
            }
        });
    }

    protected void onContinueClicked() {

        // Validate user input.
        if (!inputIsValid()) {

            // Display a toast to the user.
            Context context = getApplicationContext();
            String text = "Input invalid!"; // TODO: Localize text.
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.show();

            return;
        }

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
        Context context = getApplicationContext();
        Intent editCourseIntent = new Intent(context, EditCourseActivity.class);
        editCourseIntent.putExtra(EXTRA_GAME, game);
        startActivity(editCourseIntent);
    }

    protected boolean inputIsValid() {

        // TODO: Actually validate the input.
        return true;
    }
}
