package com.simpledeveloper.geotagme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import de.greenrobot.event.EventBus;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EventBus.getDefault().register(this);

        mRealm = Realm.getInstance(this);

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        Intent intent  = new Intent(this, RefreshGPSCoordinatesService.class);
        startService(intent);
    }

    public void onEvent(UserLocationAcquiredEvent event){
        if (event.isCompleted()){

            Log.d("COORDINATESFOUND", "The user coordinates have been acquired!");

            int lastId;
            RealmResults<UserLocation> coordinates = mRealm.where(UserLocation.class).findAll();

            UserLocation location = new UserLocation();

            if (coordinates.isEmpty()){
                location.setId(0);
            }else{
                lastId = coordinates.last().getId();
                location.setId(lastId + 1);
            }

            long millis = System.currentTimeMillis() % 1000;

            location.setLatitude(event.getLatitude());
            location.setLongitude(event.getLongitude());
            location.setCreatedAt(""+millis);

            mRealm.beginTransaction();
            mRealm.copyToRealm(location);
            mRealm.commitTransaction();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }
}
