package com.tmobtech.tmobbeaconproject;

/**
 * Created by semih on 13.07.2015.
 */
public class BeaconMap {
    private String name;
    private String imagePath; // TODO: it is for dummy data. Use database later

    public BeaconMap(String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
