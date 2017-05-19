package com.example.alex.pubgolf;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class HostGameActivity extends AppCompatActivity {

    EditText nameEditText;
    EditText dateEditText;
    EditText capacityEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_game);

        // Initialize the subviews.
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        dateEditText = (EditText) findViewById(R.id.dateEditText);
        capacityEditText = (EditText) findViewById(R.id.capacityEditText);

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

        String name = nameEditText.getText().toString();
        String date = dateEditText.getText().toString();
        String capacity = capacityEditText.getText().toString();
    }
}
