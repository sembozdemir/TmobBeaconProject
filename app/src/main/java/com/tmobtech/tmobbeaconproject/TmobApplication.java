package com.tmobtech.tmobbeaconproject;

import android.app.Application;

import com.parse.ParseObject;
import com.tmobtech.tmobbeaconproject.entity.Beacon;


/**
 * Created by ozberkcetin on 24/07/15.
 */
public class TmobApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        com.parse.Parse.initialize(this, "HXHT2n4P9lx1C4bZ6zPc0YBEcN0lDlMUM3ktpWaf", "M1dwVFvcLMa8uP9tNGtqFF8TZc8CJDfsrEecmnNh");

        ParseObject.registerSubclass(Beacon.class);
        ParseObject.registerSubclass(BeaconMap.class);


    }
}
