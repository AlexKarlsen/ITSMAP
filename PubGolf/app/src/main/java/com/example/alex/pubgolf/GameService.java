package com.example.alex.pubgolf;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class GameService extends Service {
    public GameService() {
    }

    private DatabaseReference mDatabase;
// ...
    mDatabase = FirebaseDatabase.getInstance().getReference();


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
