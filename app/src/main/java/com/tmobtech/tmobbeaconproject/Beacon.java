package com.tmobtech.tmobbeaconproject;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Ozberk on 15.7.2015.
 */
@ParseClassName("Beacon")
public class Beacon extends ParseObject {
    private long id;
    private String beaconName;
    private String macAddress;
    private float apsis;
    private float ordinat;
    private  long mapId;


    public Beacon(String beaconName, String macAddress, float apsis, float ordinat) {
        this.beaconName = beaconName;
        this.macAddress = macAddress;
        this.apsis = apsis;
        this.ordinat = ordinat;
    }

    public Beacon() {
    }

    public Beacon(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
        put("beacon_id",id);
    }

    public String getBeaconName() {
        return beaconName;
    }

    public void setBeaconName(String beaconName) {
        this.beaconName = beaconName;
        put("beacon_name",beaconName);
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
        put("mac_adress",macAddress);
    }

    public float getApsis() {
        return apsis;
    }

    public void setApsis(float apsis) {
        this.apsis = apsis;
        put("apsis",apsis);
    }

    public float getOrdinat() {
        return ordinat;
    }

    public void setOrdinat(float ordinat) {
        this.ordinat = ordinat;
        put("ordinat",ordinat);
    }
}
