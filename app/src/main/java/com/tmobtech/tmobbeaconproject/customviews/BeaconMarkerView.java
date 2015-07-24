package com.tmobtech.tmobbeaconproject.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.tmobtech.tmobbeaconproject.entity.Beacon;
import com.tmobtech.tmobbeaconproject.R;

/**
 * Created by semih on 15.07.2015.
 */
public class BeaconMarkerView extends ImageView {
    private Beacon beacon;

    public BeaconMarkerView(Context context) {
        super(context);
        setImageResource(R.drawable.ic_beacon_marker);
    }

    public BeaconMarkerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setImageResource(R.drawable.ic_beacon_marker);
    }

    public BeaconMarkerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setImageResource(R.drawable.ic_beacon_marker);
    }

    public Beacon getBeacon() {
        return beacon;
    }

    public void setBeacon(Beacon beacon) {
        this.beacon = beacon;
    }
}
