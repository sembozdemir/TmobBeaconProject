package com.tmobtech.tmobbeaconproject.entity;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.tmobtech.tmobbeaconproject.ParseData.Constants;

import java.util.List;

/**
 * Created by semih on 21.07.2015.
 */
@ParseClassName("Place")
public class Place extends ParseObject {

    private long placeId;
    private String placeName;
    private float apsis;
    private float ordinat;
    private String mapId;
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

    public String getPlaceName() {
        return getString(Constants.COLUMN_PLACE_NAME);
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
        put(Constants.COLUMN_PLACE_NAME, placeName);
    }

    public float getApsis() {
        return (float) getDouble(Constants.COLUMN_PLACE_APSIS);
    }

    public void setApsis(float apsis) {
        this.apsis = apsis;
        put(Constants.COLUMN_PLACE_APSIS, apsis);
    }

    public float getOrdinat() {
        return (float) getDouble(Constants.COLUMN_PLACE_ORDINAT);
    }

    public void setOrdinat(float ordinat) {
        this.ordinat = ordinat;
        put(Constants.COLUMN_PLACE_ORDINAT, ordinat);
    }

    public String getMapId() {
        return getString(Constants.COLUMN_PLACE_MAP_ID);
    }

    public void setMapId(String mapId) {
        put(Constants.COLUMN_PLACE_MAP_ID, mapId);
    }

    // Todo degisecek veya silinecek
    public List<BeaconPower> getBeaconPowerList() {
        return beaconPowerList;
    }

    // Todo degisecek veya silinecek
    public void setBeaconPowerList(List<BeaconPower> beaconPowerList) {
        this.beaconPowerList = beaconPowerList;
    }
}
