package com.example.alex.pubgolf;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LobbyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        // Initialize the host game button.
        Button hostGameButton = (Button) this.findViewById(R.id.hostGameButton);
        hostGameButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create intent for host game activity.
                Context context = getApplicationContext();
                Intent hostGameIntent = new Intent(context, EditGameActivity.class);
                startActivity(hostGameIntent);
            }
        });

        // Initialize the join game button.
        Button joinGameButton = (Button) this.findViewById(R.id.joinGameButton);
        joinGameButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create intent for join game activity.
            }
        });
    }
}
