package com.example.alex.pubgolf;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;
import android.widget.TextView;

import com.example.alex.pubgolf.Adapters.HoleArrayAdapter;
import com.example.alex.pubgolf.Adapters.ScoreArrayAdapter;
import com.example.alex.pubgolf.Models.Game;
import com.example.alex.pubgolf.Models.Hole;
import com.example.alex.pubgolf.Models.Player;
import com.example.alex.pubgolf.Models.Score;

import java.util.ArrayList;

public class GameNavigationActivity extends AppCompatActivity {

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

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
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

        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) return;
        actionBar.setTitle(game.Title);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class GameFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        Game game;
        ArrayList<Hole> holesList;

        public GameFragment() {
        }

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

            int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);

            if (sectionNumber == 0) {

                // Game details section.

                View rootView = inflater.inflate(R.layout.fragment_game_details, container, false);

                TextView descriptionTextView = (TextView) rootView.findViewById(R.id.gameDescriptionLabel);
                descriptionTextView.setText(game.Description);

                TextView dateTextView = (TextView) rootView.findViewById(R.id.gameDateLabel);
                dateTextView.setText(game.GetStartTimeAsTimestamp().toString());

                return rootView;

            } else if (sectionNumber == 1) {

                // Course section.

                View rootView = inflater.inflate(R.layout.fragment_game_course, container, false);

                if (game.Holes != null) {

                    holesList = new ArrayList<Hole>();
                    holesList.addAll(game.Holes);

                    ListView holesListView = (ListView) rootView.findViewById(R.id.holesListView);
                    HoleArrayAdapter adapter = new HoleArrayAdapter(getActivity(), R.layout.hole_info_list_item, holesList);
                    holesListView.setAdapter(adapter);
                }

                return rootView;

            } else {

                // Scoreboard section.

                View rootView = inflater.inflate(R.layout.fragment_game_scoreboard, container, false);

                if (game.Holes != null) {

                    Player p1 = new Player("", "Paul Denino");
                    Score s1 = new Score(p1, (long) 1);
                    Player p2 = new Player("", "Enza Denino");
                    Score s2 = new Score(p2, (long) 4);
                    Player p3 = new Player("", "M. Andy");
                    Score s3 = new Score(p3, (long) 9);

                    ArrayList<Score> scoreList = new ArrayList<Score>();
                    scoreList.add(s1);
                    scoreList.add(s2);
                    scoreList.add(s3);

                    ListView scoreboardListView = (ListView) rootView.findViewById(R.id.scoreboardListView);
                    ScoreArrayAdapter adapter = new ScoreArrayAdapter(getActivity(), R.layout.scoreboard_list_item, scoreList);
                    scoreboardListView.setAdapter(adapter);
                }

                return rootView;
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
