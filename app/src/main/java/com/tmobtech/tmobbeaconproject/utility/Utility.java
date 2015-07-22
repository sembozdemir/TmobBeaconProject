package com.tmobtech.tmobbeaconproject.utility;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.tmobtech.tmobbeaconproject.Beacon;
import com.tmobtech.tmobbeaconproject.BeaconManager.FindBeacon;
import com.tmobtech.tmobbeaconproject.BeaconPower;
import com.tmobtech.tmobbeaconproject.Place;
import com.tmobtech.tmobbeaconproject.data.MyDbHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ozberk on 21.7.2015.
 */
public class Utility {

    private static String TAG="UtilityError";

    public static List<Beacon> getBeaconList (long mapId,Activity activity)
    {
        List<Beacon> list;
        list=new ArrayList<>();

        MyDbHelper myDbHelper = new MyDbHelper(activity);
        try {

            Cursor cursor = myDbHelper.getBeaconsAtMap(mapId);
            if (cursor.moveToFirst()) {
                do {
                    try {

                        Beacon beacon = new Beacon();
                        beacon.setBeaconName(cursor.getString(cursor.getColumnIndex(MyDbHelper.COLUMN_BEACON_NAME)));
                        beacon.setMacAddress(cursor.getString(cursor.getColumnIndex(MyDbHelper.COLUMN_BEACON_MAC_ADDRESS)));
                        beacon.setApsis(cursor.getFloat(cursor.getColumnIndex(MyDbHelper.COLUMN_BEACON_APSIS)));
                        beacon.setOrdinat(cursor.getFloat(cursor.getColumnIndex(MyDbHelper.COLUMN_BEACON_ORDINAT)));
                        beacon.setId(cursor.getLong(cursor.getColumnIndex(MyDbHelper.COLUMN_BEACON_ID)));
                        beacon.setMacAddress(cursor.getString(cursor.getColumnIndex(MyDbHelper.COLUMN_BEACON_MAC_ADDRESS)));

                        list.add(beacon);

                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }


        return  list;

    }

    public static List<BeaconPower> getBeaconPowers(FindBeacon findBeacon,long mapID,Activity activity) {
        List<Beacon> list = getBeaconList(mapID, activity);
        List<org.altbeacon.beacon.Beacon> ls = findBeacon.ls;
        List<BeaconPower> beaconPowerList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < ls.size(); j++) {
                if (list.get(i).getMacAddress().equals(ls.get(j).getBluetoothAddress())) {
                    BeaconPower beaconPower = new BeaconPower(list.get(i), ls.get(j).getDistance(), false);
                    beaconPowerList.add(beaconPower);
                }
            }
        }
        return beaconPowerList;
    }

    public static List<Place> getPlaceList(Context context, long mapID) {
        List<Place> placeList = new ArrayList<>();
        MyDbHelper myDbHelper = new MyDbHelper(context);
        Cursor cursor = myDbHelper.getPlacesAtMap(mapID);
        if (cursor.moveToFirst()) {
            do {
                long placeId = cursor.getLong(cursor.getColumnIndex(MyDbHelper.COLUMN_PLACE_ID));
                String placeName = cursor.getString(cursor.getColumnIndex(MyDbHelper.COLUMN_PLACE_NAME));
                float apsis = cursor.getFloat(cursor.getColumnIndex(MyDbHelper.COLUMN_PLACE_APSIS));
                float ordinat = cursor.getFloat(cursor.getColumnIndex(MyDbHelper.COLUMN_PLACE_ORDINAT));
                Place place = new Place(placeId, placeName, apsis, ordinat, getBeaconPowersFromDb(context, placeId));
                placeList.add(place);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return placeList;
    }

    private static List<BeaconPower> getBeaconPowersFromDb(Context context, long placeId) {
        List<BeaconPower> beaconPowerList = new ArrayList<>();
        MyDbHelper myDbHelper = new MyDbHelper(context);
        Cursor cursor = myDbHelper.getMeasurePowersForPlace(placeId);
        if (cursor.moveToFirst()) {
            do {
                long beaconId = cursor.getLong(cursor.getColumnIndex(MyDbHelper.COLUMN_BEACON_MEASURE_BEACON_ID));
                double power = cursor.getDouble(cursor.getColumnIndex(MyDbHelper.COLUMN_BEACON_MEASURE_POWER));
                BeaconPower beaconPower = new BeaconPower(getBeaconFromDb(context, beaconId), power, true);
                beaconPowerList.add(beaconPower);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return beaconPowerList;
    }

    private static Beacon getBeaconFromDb(Context context, long beaconId) {
        Beacon beacon = new Beacon(beaconId);
        MyDbHelper myDbHelper = new MyDbHelper(context);
        Cursor cursor = myDbHelper.getBeaconFromId(beaconId);
        if (cursor.moveToFirst()) {
            beacon.setBeaconName(cursor.getString(cursor.getColumnIndex(MyDbHelper.COLUMN_BEACON_NAME)));
            beacon.setMacAddress(cursor.getString(cursor.getColumnIndex(MyDbHelper.COLUMN_BEACON_MAC_ADDRESS)));
            beacon.setApsis(cursor.getFloat(cursor.getColumnIndex(MyDbHelper.COLUMN_BEACON_APSIS)));
            beacon.setOrdinat(cursor.getFloat(cursor.getColumnIndex(MyDbHelper.COLUMN_BEACON_ORDINAT)));
        }
        cursor.close();
        return beacon;
    }

    public static List<BeaconPower> getCheckedBeaconPowers(List<BeaconPower> beaconPowerList, FindBeacon findBeacon, long mapId, Activity context) {
        List<BeaconPower> allBeaconPowers = getBeaconPowers(findBeacon, mapId, context);
        for (int i=0;i<beaconPowerList.size();i++)
        {
            for (int j=0;j<allBeaconPowers.size();j++)
            {
                if (beaconPowerList.get(i).getBeacon().getMacAddress().equals(allBeaconPowers.get(j).getBeacon().getMacAddress()))
                {
                    allBeaconPowers.get(j).setAdded(true);
                }
                else
                    allBeaconPowers.get(j).setAdded(false);
            }
        }
        return allBeaconPowers;
    }
}
