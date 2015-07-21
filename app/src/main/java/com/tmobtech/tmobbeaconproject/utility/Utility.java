package com.tmobtech.tmobbeaconproject.utility;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.tmobtech.tmobbeaconproject.Beacon;
import com.tmobtech.tmobbeaconproject.views.BeaconMarkerView;

import java.util.List;

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

    public List<Beacon> getBeaconList (long mapId)
    {


    }

}
