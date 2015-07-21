package com.tmobtech.tmobbeaconproject.utility;

import android.app.Activity;

/**
 * Created by semih on 21.07.2015.
 */
public class Utility {
    public static int getDisplayWidthPixel(Activity activity) {
        return activity.getWindowManager().getDefaultDisplay().getWidth();
    }

    public static int getDisplayHeightPixel(Activity activity) {
        return activity.getWindowManager().getDefaultDisplay().getHeight();
    }
}
