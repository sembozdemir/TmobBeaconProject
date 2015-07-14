package com.tmobtech.tmobbeaconproject;

/**
 * Created by semih on 13.07.2015.
 */
public class BeaconMap {
    private String name;
    private Integer imageId; // TODO: it is for dummy data. Use database later

    public BeaconMap(String name, Integer imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }
}
