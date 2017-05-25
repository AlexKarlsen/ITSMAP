package com.example.alex.pubgolf;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class JoinGameActivity extends AppCompatActivity {

    // Views
    Button joinButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);

        joinButton = (Button) findViewById(R.id.joinButton);

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JoinClicked();
            }
        });

    }

    public void JoinClicked()
    {

        // Return from activity with the hole.
        Intent intent = new Intent();

        setResult(RESULT_OK, intent);
        finish();
    }
}
