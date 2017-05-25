package com.example.alex.pubgolf;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Paint;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.Profile;

public class JoinGameActivity extends AppCompatActivity {

    GameService gameService;
    Boolean bound = false;
    // Views
    Button joinButton;
    EditText gameIdEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);

        joinButton = (Button) findViewById(R.id.joinButton);
        gameIdEditText = (EditText) findViewById(R.id.gameIdEditText);

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JoinClicked();
            }
        });

        Intent gameServiceIntent = new Intent(this, GameService.class);
        startService(gameServiceIntent);
        bindService(gameServiceIntent, connection, Context.BIND_IMPORTANT);
    }

    public void JoinClicked()
    {
        boolean success =
        gameService.addPlayerToGame(gameIdEditText.getText().toString(), Profile.getCurrentProfile().getId(), Profile.getCurrentProfile().getName());

        if (success != true)
        {
            // Display a toast to the user.
            Context context = getApplicationContext();
            String text = "Game id invalid"; // TODO: Localize text.
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.show();

            return;
        }

        // Return from activity with the hole.
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        //finish();
    }

    // Modified from: https://developer.android.com/guide/components/bound-services.html#Binder
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {

            // Cast the IBinder and get WeatherInfoService instance.
            GameService.GameServiceBinder binder = (GameService.GameServiceBinder) service;
            gameService = binder.getService();
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };

    @Override
    protected void onStop() {
        super.onStop();

        // Unbind from the service;
        if (bound) {
            unbindService(connection);
            bound = false;
        }
    }
}
