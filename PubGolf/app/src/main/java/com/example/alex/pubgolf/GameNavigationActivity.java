package com.example.alex.pubgolf;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.alex.pubgolf.Adapters.HoleArrayAdapter;
import com.example.alex.pubgolf.Adapters.ScoreArrayAdapter;
import com.example.alex.pubgolf.Models.Game;
import com.example.alex.pubgolf.Models.Hole;
import com.example.alex.pubgolf.Models.Score;
import com.example.alex.pubgolf.Models.Scoreboard;

import java.util.ArrayList;

public class GameNavigationActivity extends AppCompatActivity {

    public static final String EXTRA_HOLE_INDEX = "EXTRA_HOLE_INDEX";
    Game game;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_navigation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // Handle intent.
        Intent intent = getIntent();
        if (intent != null) {
            handleStartWithIntent(intent);
        }
    }

    protected void handleStartWithIntent(Intent intent) {
        game = (Game) intent.getExtras().getSerializable(GameListActivity.EXTRA_GAME);

        // Show the navigation button and game title in the toolbar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) return;
        actionBar.setTitle(game.Title);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {

        // Go back when the back arrow is pressed.
        onBackPressed();
        return true;
    }

    /**
     * A fragment for displaying different game information.
     */
    public static class GameFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";
        private int sectionNumber;
        private View view;

        Game game;
        ArrayList<Hole> holesList;

        public GameFragment() { }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static GameFragment newInstance(int sectionNumber, Game game) {
            GameFragment fragment = new GameFragment();
            fragment.game = game;
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);

            // Return a different view based on the section number.

            if (sectionNumber == 0) {

                // Game details section.

                View rootView = inflater.inflate(R.layout.fragment_game_details, container, false);
                view = rootView;
                updateView();
                return rootView;

            } else if (sectionNumber == 1) {

                // Course section.

                View rootView = inflater.inflate(R.layout.fragment_game_course, container, false);
                view = rootView;

                ((ListView) view.findViewById(R.id.holesListView)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> list, View v, int pos, long id) {

                        // Pass the selected hole index and game to the score activity.
                        int selectedHoleIndex = pos;
                        if (selectedHoleIndex <= game.HoleIndex) {
                            Intent addScoreIntent = new Intent(getContext(), AddScoreActivity.class);
                            addScoreIntent.putExtra(GameService.EXTRA_GAME, game);
                            addScoreIntent.putExtra(EXTRA_HOLE_INDEX, selectedHoleIndex);
                            startActivity(addScoreIntent);
                        }
                    }
                });

                updateView();
                return rootView;

            } else {

                // Scoreboard section.

                View rootView = inflater.inflate(R.layout.fragment_game_scoreboard, container, false);
                view = rootView;
                updateView();
                return rootView;
            }
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Register broadcast receivers.
            IntentFilter filter = new IntentFilter();
            filter.addAction(GameService.BROADCAST_GAME_SERVICE_RESULT);
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(onGameServiceResult, filter);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();

            // Unregister broadcast receivers.
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(onGameServiceResult);
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
                            game = receivedGame;
                            updateView();
                        }
                    }
                }
            }
        };

        private void updateView() {

            // Updates the state of the view.

            if (view == null) return;

            if (sectionNumber == 0) {

                // Game details section.

                TextView descriptionTextView = (TextView) view.findViewById(R.id.gameDescriptionLabel);
                descriptionTextView.setText(game.Description);

                TextView dateTextView = (TextView) view.findViewById(R.id.gameDateLabel);
                dateTextView.setText(game.GetStartTimeAsTimestamp().toString());

            } else if (sectionNumber == 1) {

                // Course section.

                if (game.Holes != null) {

                    holesList = new ArrayList<Hole>();
                    holesList.addAll(game.Holes);

                    ListView holesListView = (ListView) view.findViewById(R.id.holesListView);
                    HoleArrayAdapter adapter = new HoleArrayAdapter(getActivity(), R.layout.hole_info_list_item, holesList);
                    FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.advanceHoleIndexFAB);

                    holesListView.setAdapter(adapter);
                }

            } else {

                // Scoreboard section.

                if (game != null) {

                    ArrayList<Score> scoreList = Scoreboard.calculateScoresForGame(game);
                    ListView scoreboardListView = (ListView) view.findViewById(R.id.scoreboardListView);
                    ScoreArrayAdapter adapter = new ScoreArrayAdapter(getActivity(), R.layout.scoreboard_list_item, scoreList);
                    scoreboardListView.setAdapter(adapter);
                }
            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return GameFragment.newInstance(position, game);
        }

        @Override
        public int getCount() {

            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "Details";
                case 1:
                    return "Course";
                case 2:
                    return "Scoreboard";
            }

            return null;
        }
    }
}
