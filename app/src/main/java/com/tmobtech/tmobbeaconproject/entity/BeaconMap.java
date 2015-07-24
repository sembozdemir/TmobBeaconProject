package com.tmobtech.tmobbeaconproject.entity;

import com.parse.ParseClassName;

/**
 * Created by semih on 13.07.2015.
 */

@ParseClassName("BeaconMap")
public class BeaconMap {
    private long id;
    private String name;
    private String imagePath;

    public BeaconMap(long id, String name, String imagePath) {
        this.id = id;
        this.name = name;
        this.imagePath = imagePath;
    }

    public BeaconMap(String imagePath, String name) {
        this.imagePath = imagePath;
        this.name = name;
    }

    public BeaconMap() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
