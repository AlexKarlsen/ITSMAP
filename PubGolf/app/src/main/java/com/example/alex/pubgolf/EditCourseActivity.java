package com.example.alex.pubgolf;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.alex.pubgolf.Models.Game;

public class EditCourseActivity extends AppCompatActivity {

    Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);

        Intent intent = getIntent();
        if (intent != null) {
            handleStartWithIntent(intent);
        }
    }

    protected void handleStartWithIntent(Intent intent) {
        game = (Game) intent.getExtras().getSerializable(EditGameActivity.EXTRA_GAME);
    }
}
