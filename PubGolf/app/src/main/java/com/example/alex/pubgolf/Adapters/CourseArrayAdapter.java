package com.example.alex.pubgolf.Adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.alex.pubgolf.Models.Game;
import com.example.alex.pubgolf.Models.Hole;
import com.example.alex.pubgolf.R;

import java.util.ArrayList;

/**
 * Created by Lasse on 30/05/2017.
 * Abstract: Array adapter for displaying holes with active hole info.
 */

public class CourseArrayAdapter extends ArrayAdapter {

    private ArrayList<Hole> objects;
    private long activeHole;
    private Game.GameState gameState;
    private int cellResourceId;

    public CourseArrayAdapter(Context context, int cellResourceId, ArrayList<Hole> objects, long activeHole, Game.GameState gameState) {
        super(context, cellResourceId, objects);
        this.objects = objects;
        this.activeHole = activeHole;
        this.gameState = gameState;
        this.cellResourceId = cellResourceId;
    }

    // Modified from https://devtut.wordpress.com/2011/06/09/custom-arrayadapter-for-a-listview-android/
    public View getView(int position, View convertView, ViewGroup parent) {

        // Create a local variable for the view to be converted.
        View holeView = convertView;

        // Get the info object at the given position.
        Hole info = objects.get(position);

        // Inflate the view if it is null.
        if (holeView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            holeView = inflater.inflate(cellResourceId, null);
        }

        // Update the weatherInfoView with the weather info.
        if (info != null) {

            // Create a local reference to the subviews of the weather info view.
            TextView nameTextView = (TextView) holeView.findViewById(R.id.nameTextView);
            TextView descTextView = (TextView) holeView.findViewById(R.id.descriptionTextView);

            // Set the text of the text views to display the weather info data.
            if (nameTextView != null) {
                nameTextView.setText(info.Name);
            }
            if (descTextView != null) {
                descTextView.setText(info.Description);
            }
        }

        if (gameState == Game.GameState.InProgress && position == activeHole) {
            holeView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorHighlight));
        }

        return holeView;
    }
}
