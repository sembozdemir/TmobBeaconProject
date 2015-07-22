package com.tmobtech.tmobbeaconproject.utility;

import android.app.Activity;
import android.database.Cursor;
import android.util.Log;

import com.tmobtech.tmobbeaconproject.Beacon;
import com.tmobtech.tmobbeaconproject.BeaconPower;
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
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }


        return  list;

    }

    public static BeaconPower[] getBeaconPowers(long mapID) {
        return new BeaconPower[0];
    }
}
