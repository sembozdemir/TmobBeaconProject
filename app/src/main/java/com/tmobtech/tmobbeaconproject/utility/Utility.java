package com.tmobtech.tmobbeaconproject.utility;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.tmobtech.tmobbeaconproject.ParseData.Constants;
import com.tmobtech.tmobbeaconproject.entity.Beacon;
import com.tmobtech.tmobbeaconproject.entity.BeaconPower;
import com.tmobtech.tmobbeaconproject.entity.LocalBeaconPower;
import com.tmobtech.tmobbeaconproject.entity.Place;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ozberk on 21.7.2015.
 */
public class Utility {

    private static String TAG="UtilityError";


    public static List<LocalBeaconPower> getBeaconPowers(FindBeacon findBeacon,List<Beacon> list) {

        List<org.altbeacon.beacon.Beacon> ls = findBeacon.ls;
        List<LocalBeaconPower> beaconPowerList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < ls.size(); j++) {
                if (list.get(i).getMacAddress().equals(ls.get(j).getBluetoothAddress())) {
                    LocalBeaconPower beaconPower = new LocalBeaconPower();
                    beaconPower.setBeaconId(list.get(i).getObjectId());
                    beaconPower.setBeaconName(list.get(i).getBeaconName());
                    beaconPower.setBeaconMacAddress(list.get(i).getMacAddress());
                    beaconPower.setBeaconDistance(ls.get(j).getDistance());
                    beaconPower.setBeaconIsAdded(false);
                    beaconPowerList.add(beaconPower);
                }
            }
        }
        return beaconPowerList;
    }

    public static List<LocalBeaconPower> getCheckedBeaconPowers(List<LocalBeaconPower> beaconPowerList, FindBeacon findBeacon, List<Beacon> beaconList) {
        List<LocalBeaconPower> allBeaconPowers = getBeaconPowers(findBeacon, beaconList);
        for (int i=0;i<beaconPowerList.size();i++)
        {
            for (int j=0;j<allBeaconPowers.size();j++)
            {
                if (beaconPowerList.get(i).getBeaconMacAddress().equals(allBeaconPowers.get(j).getBeaconMacAddress()))
                {
                    if (beaconPowerList.get(i).isBeaconIsAdded())
                        allBeaconPowers.get(j).setBeaconIsAdded(true);
                }

            }
        }
        return allBeaconPowers;
    }



    public static  List<Beacon> getBeaconFromParse(String mapId) {
        try {
            List<Beacon> beaconList;
            ParseQuery<Beacon> query = ParseQuery.getQuery(Beacon.class);
            query.whereEqualTo(Constants.COLUMN_BEACON_MAP_ID, mapId);
            beaconList=query.find();
            return beaconList;
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }


    public static List<Place> getPlaceFromParse(String mapID) {
        try {
            List<Place> placeList;
            ParseQuery<Place> query = ParseQuery.getQuery(Place.class);
            query.whereEqualTo(Constants.COLUMN_PLACE_MAP_ID, mapID);
            placeList = query.find();
            return placeList;
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    public static List<BeaconPower> getBeaconPowerToSave(List<LocalBeaconPower> beaconPowersListe, String placeId) {
        List<BeaconPower> beaconPowers = new ArrayList<>();

        for (LocalBeaconPower localBeaconPower : beaconPowersListe) {
            if (localBeaconPower.isBeaconIsAdded()) {
                beaconPowers.add(localBeaconPower.toBeaconPower(placeId));
            }
        }
        return beaconPowers;
    }

    public static List<LocalBeaconPower> getBeaconPowersFromParse(String placeId) {
        ParseQuery<BeaconPower> query = ParseQuery.getQuery(BeaconPower.class);
        query.whereEqualTo(Constants.COLUMN_BEACON_MEASURE_PLACE_ID, placeId);
        final List<LocalBeaconPower> localBeaconPowers = new ArrayList<>();
        try {
            List<BeaconPower> beaconPowers =  query.find();
            for (final BeaconPower beaconPower : beaconPowers) {
                ParseQuery<Beacon> beaconParseQuery = ParseQuery.getQuery(Beacon.class);
                Beacon beacon = beaconParseQuery.get(beaconPower.getBeaconId());
                localBeaconPowers.add(beaconPower.toLocalBeaconPower(beacon.getBeaconName(),
                        beacon.getMacAddress()));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return localBeaconPowers;
    }
}
