package com.tmobtech.tmobbeaconproject.entity;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.tmobtech.tmobbeaconproject.ParseData.Constants;

/**
 * Created by semih on 21.07.2015.
 */
@ParseClassName("BeaconPower")
public class BeaconPower extends ParseObject {

    public String getBeaconId() {
        return getString(Constants.COLUMN_BEACON_MEASURE_BEACON_ID);
    }

    public void setBeaconId(String beaconId) {
        put(Constants.COLUMN_BEACON_MEASURE_BEACON_ID, beaconId);
    }

    public String getPlaceId() {
        return getString(Constants.COLUMN_BEACON_MEASURE_PLACE_ID);
    }

    public void setPlaceId(String placeId) {
        put(Constants.COLUMN_BEACON_MEASURE_PLACE_ID, placeId);
    }

    public double getPower() {
        return getDouble(Constants.COLUMN_BEACON_MEASURE_POWER);
    }

    public void setPower(double power) {
        put(Constants.COLUMN_BEACON_MEASURE_POWER, power);
    }

    public LocalBeaconPower toLocalBeaconPower(String beaconName, String beaconMacAddress) {
        LocalBeaconPower localBeaconPower = new LocalBeaconPower();
        localBeaconPower.setBeaconId(getBeaconId());
        localBeaconPower.setBeaconName(beaconName);
        localBeaconPower.setBeaconMacAddress(beaconMacAddress);
        localBeaconPower.setBeaconDistance(getPower());
        localBeaconPower.setBeaconIsAdded(true);

        return localBeaconPower;
    }
}
