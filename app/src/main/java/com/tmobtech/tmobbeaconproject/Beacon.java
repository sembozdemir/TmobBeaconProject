package com.tmobtech.tmobbeaconproject;

/**
 * Created by Ozberk on 15.7.2015.
 */
public class Beacon {
    private long id;
    private String beaconName;
    private String macAddress;
    private float apsis;
    private float ordinat;

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
    }

    public String getBeaconName() {
        return beaconName;
    }

    public void setBeaconName(String beaconName) {
        this.beaconName = beaconName;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
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
}
