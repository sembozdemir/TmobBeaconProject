package com.tmobtech.tmobbeaconproject.entity;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.tmobtech.tmobbeaconproject.ParseData.Constants;

/**
 * Created by semih on 13.07.2015.
 */

@ParseClassName("BeaconMap")
public class BeaconMap extends ParseObject {







    public String getName() {

        return getString(Constants.COLUMN_MAP_NAME);
    }

    public void setName(String name) {

        put(Constants.COLUMN_MAP_NAME,name);
    }

    public String getImagePath(){
        return  getString(Constants.COLUMN_MAP_IMAGE_PATH);
    }

    public void setImagePath(String imagePath) {


        put(Constants.COLUMN_MAP_IMAGE_PATH,imagePath);
    }

    public void setUserId(String objectId) {

        put("userId",objectId);
    }

    public String getUserId() {
        return getString("userId");
    }
}
