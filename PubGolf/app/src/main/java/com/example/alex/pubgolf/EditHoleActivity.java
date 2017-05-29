package com.example.alex.pubgolf;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.alex.pubgolf.Models.Hole;
import com.example.alex.pubgolf.Models.TimeContainer;

import java.sql.Timestamp;
import java.util.Calendar;

public class EditHoleActivity extends AppCompatActivity {

    public static final String EXTRA_HOLE = "EXTRA_HOLE";

    EditText nameEditText;
    EditText descriptionEditText;
    EditText dateEditText;
    EditText timeEditText;

    TimeContainer selectedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_hole);

        // Initialize the subviews.
        nameEditText = (EditText) this.findViewById(R.id.nameEditText);
        descriptionEditText = (EditText) this.findViewById(R.id.descriptionEditText);
        dateEditText = (EditText) this.findViewById(R.id.dateEditText);
        setupDateEditText();
        timeEditText = (EditText) this.findViewById(R.id.timeEditText1);
        setupTimeEditText();

        // Initialize the host game button.
        Button doneButton = (Button) this.findViewById(R.id.doneButton);
        doneButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDoneClicked();
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

    protected void setupTimeEditText() {

        // Spawn a date picker when clicking the edit text.
        timeEditText.setKeyListener(null);
        timeEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {


            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {
                    return;
                }

                onTimeEditTextInteraction(timeEditText);
            }
        });
        timeEditText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                onDateEditTextInteraction(timeEditText);
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

                if (selectedTime == null) {
                    selectedTime = new TimeContainer(year, month, dayOfMonth, 0, 0);
                } else {
                    selectedTime.year = year;
                    selectedTime.monthOfYear = month;
                    selectedTime.dayOfMonth = dayOfMonth;
                }
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

        DatePickerDialog d = new DatePickerDialog(EditHoleActivity.this, dpd, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        d.show();
    }

    protected void onTimeEditTextInteraction(final EditText editText) {

        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                String timeString = hourOfDay + ":" + minute;
                editText.setText(timeString);

                if (selectedTime == null) {
                    selectedTime = new TimeContainer(0, 0, 0, hourOfDay, minute);
                } else {
                    selectedTime.hourOfDay = hourOfDay;
                    selectedTime.minute = minute;
                }
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

        TimePickerDialog timePickerDialog = new TimePickerDialog(EditHoleActivity.this, listener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    protected void onDoneClicked() {

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

        // Create the new hole object.
        Hole hole = new Hole();
        hole.Name = name;
        hole.Description = description;
        if (selectedTime != null) {
            hole.Time = selectedTime.toTimestamp().getTime();
        }

        // Return from activity with the hole.
        Intent intent = new Intent();
        intent.putExtra(EXTRA_HOLE, hole);
        setResult(RESULT_OK, intent);
        finish();
    }

    protected boolean inputIsValid() {

        // Actually validate the input.
        if (nameEditText.getText().toString().length() == 0) {
            return false;
        } else if (descriptionEditText.getText().toString().length() == 0) {
            return false;
        }

        return true;
    }
}
