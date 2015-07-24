package com.tmobtech.tmobbeaconproject;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.tmobtech.tmobbeaconproject.ParseData.Constants;

/**
 * Created by semih on 13.07.2015.
 */
@ParseClassName("BeaconMap")
public class BeaconMap extends ParseObject {
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




    public String getName() {
        return name;

    }

    public void setName(String name) {
        this.name = name;
        put(Constants.COLUMN_MAP_NAME,name);
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
        put(Constants.COLUMN_MAP_IMAGE_PATH,name);
    }
}
