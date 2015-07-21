package com.tmobtech.tmobbeaconproject.BeaconManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.tmobtech.tmobbeaconproject.R;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ozberkcetin on 20/07/15.
 */
public class FindBeacon  implements BeaconConsumer {
    protected static final String TAG = "RangingActivity";
   static private BeaconManager beaconManager;
    Activity activity;
    public static List<Beacon> ls;
public  FindBeacon(Activity activity){
    this.activity=activity;
    beaconManager = BeaconManager.getInstanceForApplication(activity);

    // To detect proprietary beacons, you must add a line like below corresponding to your beacon
    // type.  Do a web search for "setBeaconLayout" to get the proper expression.
    beaconManager.getBeaconParsers().add(new BeaconParser().
            setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));







    beaconManager.bind(this);
    }

    @Override
    public void onBeaconServiceConnect() {

        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {

                List<Beacon> list=new ArrayList(collection);
                ls=new ArrayList();

                for (int i=0;i<list.size();i++)
                {
                    if (   (list.get(i).getDistance()<2)   )
                    {
                        ls.add(list.get(i));
                        Collections.sort(ls, new Comparator<Beacon>() {
                            public int compare(Beacon emp1, Beacon emp2) {
                                return Double.compare(emp1.getDistance(), emp2.getDistance());
                            }
                        });

                    }
                }




            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myMonitoringUniqueId", null, null, null));
        } catch (RemoteException e) {    }

    }

    @Override
    public Context getApplicationContext() {
        return activity;
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {

        activity.unbindService(serviceConnection);
    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        activity.bindService(intent,serviceConnection,i);
        return true;
    }

    public List<Beacon> getBeaconList(){


        return  ls;
    }

    public void   stopBeaconBindService ()
    {
        beaconManager.unbind(this);
    }



}