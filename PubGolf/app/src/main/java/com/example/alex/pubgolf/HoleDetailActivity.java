package com.example.alex.pubgolf;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.alex.pubgolf.Models.Game;
import com.example.alex.pubgolf.Models.Hole;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class HoleDetailActivity extends AppCompatActivity {

    public static final String EXTRA_HOLE = "EXTRA_HOLE";
    public static final String EXTRA_GAME = "EXTRA_GAME";

    Game game;
    Hole hole;

    TextView nameTextView;
    TextView descriptionTextView;
    TextView timeTextView;
    TextView indexTextView;
    Button addScorebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hole_detail);

        nameTextView = (TextView) findViewById(R.id.holeNameTextView);
        descriptionTextView = (TextView) findViewById(R.id.holeDescriptionTextView);
        timeTextView = (TextView) findViewById(R.id.holeTimeTextView);
        indexTextView = (TextView) findViewById(R.id.holeIndexTextView);
        addScorebutton = (Button) findViewById(R.id.addScoreButton);

        Intent intent = getIntent();
        if (intent != null) {
            handleStartWithIntent(intent);
        }

        addScorebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass the selected hole index and game to the score activity.
                Context context = getApplicationContext();
                Intent addScoreIntent = new Intent(context, AddScoreActivity.class);
                addScoreIntent.putExtra(EXTRA_GAME, game);
                addScoreIntent.putExtra(EXTRA_HOLE, hole);
                startActivity(addScoreIntent);
            }
        });

        // If hole isnt active or passed then disable button.
        if (hole.Index > game.HoleIndex)
            addScorebutton.setEnabled(false);

        // Register broadcast receivers.
        IntentFilter filter = new IntentFilter();
        filter.addAction(GameService.BROADCAST_GAME_SERVICE_RESULT);
        LocalBroadcastManager.getInstance(this).registerReceiver(onGameServiceResult, filter);
    }

    protected void handleStartWithIntent(Intent intent) {
        Hole holeFromIntent = (Hole) intent.getExtras().getSerializable(GameNavigationActivity.EXTRA_HOLE);
        Game gameFromIntent = (Game) intent.getExtras().getSerializable(GameNavigationActivity.EXTRA_GAME);
        hole = holeFromIntent;
        game = gameFromIntent;

        if (hole == null) return;
        updateFields();
    }

    private void updateFields()
    {
        nameTextView.setText(hole.Name);
        descriptionTextView.setText(hole.Description);
        indexTextView.setText(String.valueOf(hole.Index));

        if (hole.Time != 0)
            timeTextView.setText(hole.GetTimeAsTimestamp().toString());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Unregister broadcast receivers.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onGameServiceResult);
    }

    private BroadcastReceiver onGameServiceResult = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String description = intent.getStringExtra(GameService.EXTRA_DESCRIPTION);

            // Only react on game change notifications.

            if (description.equals(GameService.OLD_GAME_CHANGED)) {

                Game receivedGame = (Game) intent.getSerializableExtra(GameService.EXTRA_GAME);

                // Only update if the updated game is the one currently displayed.
                if (game != null && receivedGame != null) {
                    if (game.Key.equals(receivedGame.Key)) {
                        // Finds the updated version of the Hole displayed
                        for (Hole holeIterator: receivedGame.Holes) {
                            if (holeIterator.Name.equals(hole.Name))
                            {
                                hole = holeIterator;
                                break;
                            }
                        }

                        game = receivedGame;
                        updateFields();
                    }
                }
            }
        }
    };
}
