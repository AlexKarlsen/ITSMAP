package com.example.alex.pubgolf;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.alex.pubgolf.Adapters.GameArrayAdapter;
import com.example.alex.pubgolf.Models.Game;

import java.security.Timestamp;
import java.security.acl.Owner;
import java.util.ArrayList;

public class GameListActivity extends AppCompatActivity {

    // Views
    ListView gameListView;
    //FloatingActionButton refreshFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);

        initializeSubviews();
    }

    public void initializeSubviews()
    {
        gameListView = (ListView) findViewById(R.id.gamesListView);

        updateGamesListView(makeTestData());
    }

    private void updateGamesListView(ArrayList<Game> gamesData) {

        if (gamesData == null) { return; }

        // Populate the list view with the weather info using a custom adapter.
        GameArrayAdapter adapter = new GameArrayAdapter(this, R.layout.game_info_list_item, gamesData);
        gameListView.setAdapter(adapter);
    }

    private ArrayList<Game> makeTestData()
    {
        ArrayList<Game> games = new ArrayList<Game>();

        Game game1 = new Game();
        game1.Owner = "Emil";
        game1.Title = "PGÅ 2017";
        game1.StartTime = System.currentTimeMillis();
        game1.State = Game.GameState.InProgress;

        Game game2 = new Game();
        game2.Owner = "Alex";
        game2.Title = "PGÅ 2018";
        game2.StartTime = (long)1495537461;
        game2.State = Game.GameState.Created;

        games.add(game1);
        games.add(game2);

        return games;
    }
}
