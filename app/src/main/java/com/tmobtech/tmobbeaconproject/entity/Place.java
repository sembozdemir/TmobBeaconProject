package com.tmobtech.tmobbeaconproject.entity;

import java.util.List;

/**
 * Created by semih on 21.07.2015.
 */
public class Place {

    private long placeId;
    private String placeName;
    private float apsis;
    private float ordinat;
    private List<BeaconPower> beaconPowerList;

    public Place(long placeId, String placeName, float apsis, float ordinat, List<BeaconPower> beaconPowerList) {
        this.placeId = placeId;
        this.placeName = placeName;
        this.apsis = apsis;
        this.ordinat = ordinat;
        this.beaconPowerList = beaconPowerList;
    }

    public Place() {
    }

    public Place(String placeName, float apsis, float ordinat, List<BeaconPower> beaconPowerList) {
        this.placeName = placeName;
        this.apsis = apsis;
        this.ordinat = ordinat;
        this.beaconPowerList = beaconPowerList;
    }

    public long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(long placeId) {
        this.placeId = placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public float getApsis() {
        return apsis;
    }

    public void setApsis(float apsis) {
        this.apsis = apsis;
    }

    public float getOrdinat() {
        return ordinat;
    }

    public void setOrdinat(float ordinat) {
        this.ordinat = ordinat;
    }

    public List<BeaconPower> getBeaconPowerList() {
        return beaconPowerList;
    }

    public void setBeaconPowerList(List<BeaconPower> beaconPowerList) {
        this.beaconPowerList = beaconPowerList;
    }
}
