package com.example.alex.pubgolf.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alex.pubgolf.Models.Game;
import com.example.alex.pubgolf.R;
import com.facebook.Profile;

import java.util.ArrayList;

/**
 * Created by Emil- on 23/05/2017.
 * Abstract: Custom array adapter for populating a ListView with game info.
 */

public class GameArrayAdapter extends ArrayAdapter {

    private ArrayList<Game> objects;
    private int cellResourceId;

    public GameArrayAdapter(Context context, int cellResourceId, ArrayList<Game> objects) {
        super(context, cellResourceId, objects);
        this.objects = objects;
        this.cellResourceId = cellResourceId;
    }

    // Modified from https://devtut.wordpress.com/2011/06/09/custom-arrayadapter-for-a-listview-android/
    public View getView(int position, View convertView, ViewGroup parent) {

        // Create a local variable for the view to be converted.
        View gameView = convertView;

        // Get the info object at the given position.
        Game info = objects.get(position);

        // Inflate the view if it is null.
        if (gameView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gameView = inflater.inflate(cellResourceId, null);
        }

        // Update the weatherInfoView with the weather info.
        if (info != null) {

            // Create a local reference to the subviews of the weather info view.
            TextView titleTextView = (TextView) gameView.findViewById(R.id.titleTextView);
            TextView timeTextView = (TextView) gameView.findViewById(R.id.startTimeTextView);
            TextView hostingTextView = (TextView) gameView.findViewById(R.id.hostingTextView);
            LinearLayout outerLinearLayout = (LinearLayout) gameView.findViewById(R.id.outerLinearLayout);

            // Set the text of the text views to display the weather info data.
            if (titleTextView != null) {
                titleTextView.setText(info.Title);
            }
            if (timeTextView != null) {
                timeTextView.setText(info.GetStartTimeAsTimestamp().toString());
            }
            // Highlight gameView if the Game is active
            if (info.State == Game.GameState.InProgress) {
                titleTextView.setTypeface(null, Typeface.BOLD);
                timeTextView.setTypeface(null, Typeface.BOLD);
                outerLinearLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorHighlight));
            }
            // Write "Hosting" in hostingTextView if user is host
            if (Profile.getCurrentProfile().getId().equals(info.Owner.UUID)) // if host
                hostingTextView.setText("Hosting");
            else
                hostingTextView.setText("");
        }

        return gameView;
    }
}
