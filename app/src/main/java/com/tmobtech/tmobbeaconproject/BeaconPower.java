package com.tmobtech.tmobbeaconproject;

/**
 * Created by semih on 21.07.2015.
 */
public class BeaconPower {
    private Beacon beacon;
    private float distance;
    private boolean isAdded;

    public BeaconPower() {
    }

    public BeaconPower(Beacon beacon, float distance, boolean isAdded) {
        this.beacon = beacon;
        this.distance = distance;
        this.isAdded = isAdded;
    }

    public Beacon getBeacon() {
        return beacon;
    }

    public void setBeacon(Beacon beacon) {
        this.beacon = beacon;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public boolean isAdded() {
        return isAdded;
    }

    public void setAdded(boolean isAdded) {
        this.isAdded = isAdded;
    }
}
