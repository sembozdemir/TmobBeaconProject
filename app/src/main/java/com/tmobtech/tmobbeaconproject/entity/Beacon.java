package com.tmobtech.tmobbeaconproject.entity;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.tmobtech.tmobbeaconproject.ParseData.Constants;

/**
 * Created by Ozberk on 15.7.2015.
 */
@ParseClassName("Beacon")
public class Beacon extends ParseObject {



    public String getMapId() {
        return  getString(Constants.COLUMN_BEACON_MAP_ID);
    }

    public void setMapId(String mapId) {
        put(Constants.COLUMN_BEACON_MAP_ID,mapId);
    }



    public String getBeaconName() {
        return getString(Constants.COLUMN_BEACON_NAME);
    }

    public void setBeaconName(String beaconName) {

        put(Constants.COLUMN_BEACON_NAME,beaconName);
    }

    public String getMacAddress() {
        return getString(Constants.COLUMN_BEACON_MAC_ADDRESS);
    }

    public void setMacAddress(String macAddress) {

        put(Constants.COLUMN_BEACON_MAC_ADDRESS,macAddress);
    }

    public float getApsis() {
        return  getNumber(Constants.COLUMN_BEACON_APSIS).floatValue();
    }

    public void setApsis(float apsis) {

        put(Constants.COLUMN_BEACON_APSIS,apsis);
    }

    public float getOrdinat() {
        return  getNumber(Constants.COLUMN_BEACON_ORDINAT).floatValue();
    }

    public void setOrdinat(float ordinat) {

        put(Constants.COLUMN_BEACON_ORDINAT,ordinat);
    }
}
