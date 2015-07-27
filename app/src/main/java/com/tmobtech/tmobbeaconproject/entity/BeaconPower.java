package com.tmobtech.tmobbeaconproject.entity;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.tmobtech.tmobbeaconproject.ParseData.Constants;

/**
 * Created by semih on 21.07.2015.
 */
@ParseClassName("BeaconPower")
public class BeaconPower extends ParseObject {
    private Beacon beacon;
    private double distance;
    private boolean isAdded;

    public BeaconPower() {
    }

    public BeaconPower(Beacon beacon, double distance, boolean isAdded) {
        this.beacon = beacon;
        this.distance = distance;
        this.isAdded = isAdded;
    }

    public Beacon getBeacon() {
        return (Beacon) get(Constants.COLUMN_BEACON_MEASURE_BEACON_ID);
    }

    public void setBeacon(Beacon beacon) {
        this.beacon = beacon;
        put(Constants.COLUMN_BEACON_MEASURE_BEACON_ID, beacon.getObjectId());
    }

    public double getDistance() {
        return getDouble(Constants.COLUMN_BEACON_MEASURE_POWER);
    }

    public void setDistance(float distance) {
        this.distance = distance;
        put(Constants.COLUMN_BEACON_MEASURE_POWER, distance);
    }

    public boolean isAdded() {
        return isAdded;
    }

    public void setAdded(boolean isAdded) {
        this.isAdded = isAdded;
    }
}
