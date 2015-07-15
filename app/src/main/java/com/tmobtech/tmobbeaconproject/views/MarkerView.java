package com.tmobtech.tmobbeaconproject.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.tmobtech.tmobbeaconproject.Beacon;

/**
 * Created by semih on 15.07.2015.
 */
public class MarkerView extends ImageView {
    private Beacon beacon;

    public MarkerView(Context context) {
        super(context);
    }

    public MarkerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MarkerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Beacon getBeacon() {
        return beacon;
    }

    public void setBeacon(Beacon beacon) {
        this.beacon = beacon;
    }
}
