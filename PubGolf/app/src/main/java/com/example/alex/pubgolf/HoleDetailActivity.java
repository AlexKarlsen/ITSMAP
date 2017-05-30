package com.example.alex.pubgolf;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.alex.pubgolf.Models.Game;
import com.example.alex.pubgolf.Models.Hole;

import java.util.ArrayList;

public class HoleDetailActivity extends AppCompatActivity {

    TextView nameTextView;
    TextView descriptionTextView;
    TextView timeTextView;
    TextView indexTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hole_detail);

        nameTextView = (TextView) findViewById(R.id.holeNameTextView);
        descriptionTextView = (TextView) findViewById(R.id.holeDescriptionTextView);
        timeTextView = (TextView) findViewById(R.id.holeTimeTextView);
        indexTextView = (TextView) findViewById(R.id.holeIndexTextView);

        Intent intent = getIntent();
        if (intent != null) {
            handleStartWithIntent(intent);
        }
    }

    protected void handleStartWithIntent(Intent intent) {
        //Hole hole = (Hole) intent.getExtras().getSerializable();
        //if (hole == null) return;
        //updateFields(hole);
    }

    private void updateFields(Hole hole)
    {
        nameTextView.setText(hole.Name);
        descriptionTextView.setText(hole.Description);
        indexTextView.setText(String.valueOf(hole.Index));

        if (hole.Time != 0)
            timeTextView.setText(hole.GetTimeAsTimestamp().toString());
    }
}
