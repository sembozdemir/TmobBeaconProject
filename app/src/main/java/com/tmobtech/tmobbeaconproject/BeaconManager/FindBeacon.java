package com.tmobtech.tmobbeaconproject.BeaconManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.util.Log;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

/**
 * Created by ozberkcetin on 20/07/15.
 */
public class FindBeacon implements BeaconConsumer {
    BeaconManager beaconManager;
    Activity activity;
    public FindBeacon(Activity activity)
    {
        this.activity=activity;
        beaconManager=BeaconManager.getInstanceForApplication(activity);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.bind(this);
    }


    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.i("BeaconFindClass", "I just saw an beacon for the first time!");
            }

            @Override
            public void didExitRegion(Region region) {
                Log.i("BeaconFindClass", "I no longer see an beacon");

            }

            @Override
            public void didDetermineStateForRegion(int i, Region region) {
                Log.i("BeaconFindClass", "I have just switched from seeing/not seeing beacons: "+i);

            }
        });
    }

    @Override
    public Context getApplicationContext() {
        return activity;
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {


    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return false;
    }




}
