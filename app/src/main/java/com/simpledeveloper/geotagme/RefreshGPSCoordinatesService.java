package com.simpledeveloper.geotagme;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

public class RefreshGPSCoordinatesService extends Service {

    private final long TEN_MINUTES_INTERVAL = 10000L;

    public RefreshGPSCoordinatesService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                App app = (App) getApplication();
                app.acquireCustomerCoordinates();
            }
        }, TEN_MINUTES_INTERVAL);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

