package com.example.alex.pubgolf;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GameService extends Service {

    //Logging keys
    private static final String LOG = "GameService";


    public GameService() {
    }



    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG, "Background service onCreate");

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
