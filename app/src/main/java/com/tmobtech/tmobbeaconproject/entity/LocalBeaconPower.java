package com.tmobtech.tmobbeaconproject.entity;

/**
 * Created by semih on 27.07.2015.
 */
public class LocalBeaconPower {

    private String beaconId;
    private String beaconName;
    private String beaconMacAddress;
    private double beaconDistance;
    private boolean beaconIsAdded;

    public String getBeaconId() {
        return beaconId;
    }

    public void setBeaconId(String beaconId) {
        this.beaconId = beaconId;
    }

    public String getBeaconName() {
        return beaconName;
    }

    public String getBeaconMacAddress() {
        return beaconMacAddress;
    }

    public void setBeaconMacAddress(String beaconMacAddress) {
        this.beaconMacAddress = beaconMacAddress;
    }

    public void setBeaconName(String beaconName) {
        this.beaconName = beaconName;
    }

    public double getBeaconDistance() {
        return beaconDistance;
    }

    public void setBeaconDistance(double beaconDistance) {
        this.beaconDistance = beaconDistance;
    }

    public boolean isBeaconIsAdded() {
        return beaconIsAdded;
    }

    public void setBeaconIsAdded(boolean beaconIsAdded) {
        this.beaconIsAdded = beaconIsAdded;
    }

    public BeaconPower toBeaconPower(String placeId) {
        BeaconPower beaconPower = new BeaconPower();
        beaconPower.setBeaconId(beaconId);
        beaconPower.setPlaceId(placeId);
        beaconPower.setPower(beaconDistance);

        return beaconPower;
    }
}
