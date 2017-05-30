package com.example.alex.pubgolf.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.alex.pubgolf.Models.Hole;
import com.example.alex.pubgolf.Models.Score;
import com.example.alex.pubgolf.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Lasse on 30/05/2017.
 * Abstract: Array adapter for populating a list view with scores (for scoreboard).
 */

public class ScoreArrayAdapter extends ArrayAdapter {

    private ArrayList<Score> objects;
    private int cellResourceId;

    public ScoreArrayAdapter(Context context, int cellResourceId, ArrayList<Score> objects) {
        super(context, cellResourceId, objects);

        this.objects = objects;

        // Sort scores by value (descending).
        Collections.sort(this.objects, new Comparator<Score>() {
            @Override
            public int compare(Score lhs, Score rhs) {

                if (lhs.Value < rhs.Value) return -1; // Less than
                if (lhs.Value > rhs.Value) return 1; // Greather than
                return 0; // Equal
            }
        });

        this.cellResourceId = cellResourceId;
    }

    // Modified from https://devtut.wordpress.com/2011/06/09/custom-arrayadapter-for-a-listview-android/
    public View getView(int position, View convertView, ViewGroup parent) {

        // Create a local variable for the view to be converted.
        View scoreView = convertView;

        // Get the info object at the given position.
        Score score = objects.get(position);

        // Inflate the view if it is null.
        if (scoreView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            scoreView = inflater.inflate(cellResourceId, null);
        }

        // Update the view with the score info.
        if (score != null) {

            // Create a local reference to the subviews of the weather info view.
            TextView nameTextView = (TextView) scoreView.findViewById(R.id.nameTextView);
            TextView placementTextView = (TextView) scoreView.findViewById(R.id.placementTextView);
            TextView scoreTextView = (TextView) scoreView.findViewById(R.id.scoreTextView);

            // Set the text of the text views to display the weather info data.
            if (nameTextView != null) {
                nameTextView.setText(score.Player.Name);
            }
            if (placementTextView != null) {
                placementTextView.setText(String.valueOf(position + 1));
            }
            if (scoreTextView != null) {
                scoreTextView.setText("score: " + String.valueOf(score.Value));
            }
        }

        return scoreView;
    }
}
