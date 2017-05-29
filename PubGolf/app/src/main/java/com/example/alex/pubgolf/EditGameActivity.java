package com.example.alex.pubgolf;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alex.pubgolf.Models.Game;
import com.example.alex.pubgolf.Models.Player;
import com.example.alex.pubgolf.Models.TimeContainer;
import com.facebook.Profile;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

public class EditGameActivity extends AppCompatActivity {

    public static final String EXTRA_GAME = "EXTRA_GAME";

    EditText titleEditText;
    EditText descriptionEditText;
    EditText dateEditText;

    TimeContainer selectedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_game);

        // Initialize the subviews.
        titleEditText = (EditText) this.findViewById(R.id.titleEditText1);
        descriptionEditText = (EditText) this.findViewById(R.id.descriptionEditText);
        dateEditText = (EditText) this.findViewById(R.id.dateEditText);
        setupDateEditText();

        // Initialize the host game button.
        Button continueButton = (Button) this.findViewById(R.id.continueButton);
        continueButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onContinueClicked();
            }
        });
    }

    protected void setupDateEditText() {

        // Spawn a date picker when clicking the edit text.
        dateEditText.setKeyListener(null);
        dateEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {


            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {
                    return;
                }

                onDateEditTextInteraction(dateEditText);
            }
        });
        dateEditText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                onDateEditTextInteraction(dateEditText);
            }
        });
    }

    protected void onDateEditTextInteraction(final EditText editText) {

        // Modified from: https://stackoverflow.com/questions/36662642/how-to-open-datepicker-dialog-on-click-of-edit-text-android

        DatePickerDialog.OnDateSetListener dpd = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                int month = monthOfYear + 1;
                String dateString = dayOfMonth+"/"+month+"/"+year;
                editText.setText(dateString);

                selectedTime = new TimeContainer(year, month, dayOfMonth, 0, 0);
            }
        };

        // Prepopulate the date picker with today's date.
        Timestamp nowTimestamp = new Timestamp(System.currentTimeMillis());

        Calendar cal = Calendar.getInstance();
        if (selectedTime != null) {
            cal.setTimeInMillis(selectedTime.toTimestamp().getTime());
        } else {
            cal.setTimeInMillis(nowTimestamp.getTime());
        }

        DatePickerDialog d = new DatePickerDialog(EditGameActivity.this, dpd, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        d.show();
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
        String title = titleEditText.getText().toString();
        String description = descriptionEditText.getText().toString();

        // Create the new game object.
        Game game = new Game();
        game.Owner = new Player(Profile.getCurrentProfile().getId(), Profile.getCurrentProfile().getName());
        game.Players = new HashMap<String, Player>();
        game.Players.put(UUID.randomUUID().toString(), game.Owner);
        game.Title = title;
        game.Description = description;
        if (selectedTime != null) {
            game.StartTime = selectedTime.toTimestamp().getTime();
        }
        game.HoleIndex = (long) 0;
        game.State = Game.GameState.Created;

        // Pass the game object on to the course edit activity.
        Context context = getApplicationContext();
        Intent editCourseIntent = new Intent(context, EditCourseActivity.class);
        editCourseIntent.putExtra(EXTRA_GAME, game);
        startActivity(editCourseIntent);
    }

    protected boolean inputIsValid() {

        // Actually validate the input.
        if (titleEditText.getText().toString().length() == 0) {
            return false;
        } else if (descriptionEditText.getText().toString().length() == 0) {
            return false;
        }

        return true;
    }
}
