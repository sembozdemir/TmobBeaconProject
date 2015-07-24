package com.tmobtech.tmobbeaconproject.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.tmobtech.tmobbeaconproject.entity.Place;
import com.tmobtech.tmobbeaconproject.R;

/**
 * Created by semih on 22.07.2015.
 */
public class PlaceMarkerView extends ImageView {
    private Place place;

    public PlaceMarkerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setImageResource(R.drawable.ic_place_marker);
    }

    public PlaceMarkerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setImageResource(R.drawable.ic_place_marker);
    }

    public PlaceMarkerView(Context context) {
        super(context);
        setImageResource(R.drawable.ic_place_marker);
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }
}
