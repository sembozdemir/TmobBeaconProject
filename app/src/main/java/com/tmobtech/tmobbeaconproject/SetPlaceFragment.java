package com.tmobtech.tmobbeaconproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tmobtech.tmobbeaconproject.data.MyDbHelper;
import com.tmobtech.tmobbeaconproject.utility.Utility;
import com.tmobtech.tmobbeaconproject.views.BeaconMarkerView;

import java.util.List;

/**
 * Created by Ozberk on 15.7.2015.
 */
public class SetPlaceFragment extends Fragment implements View.OnTouchListener {
    private static final String LOG_TAG = SetPlaceFragment.class.getSimpleName();

    private FrameLayout frameLayout;
    private ImageView mapImageView;
    private MyDbHelper myDbHelper;
    private PlaceBeaconActivity parentActivity;
    private List<Beacon> beaconList;
    private FrameLayout.LayoutParams layoutParams1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.setplacefragment, null);

        initViews(view);
        layoutParams1 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        myDbHelper = new MyDbHelper(getActivity());
        parentActivity = (PlaceBeaconActivity) getActivity();
        setImageView(parentActivity.getImagePath());
        beaconList = Utility.getBeaconList(parentActivity.getMapID(), getActivity()) ;
        placeBeacons(beaconList);

        return view;
    }

    private void placeBeacons(List<Beacon> beaconList) {
        try {
            if (beaconList.size() > 0) {
                for(Beacon beacon : beaconList) {
                    final BeaconMarkerView beaconMarkerView = new BeaconMarkerView(getActivity());
                    beaconMarkerView.setX(beacon.getApsis());
                    beaconMarkerView.setY(beacon.getOrdinat());
                    beaconMarkerView.setBeacon(beacon);

                    frameLayout.addView(beaconMarkerView, layoutParams1);
                }
            }

        } catch (Exception e) {
            Log.e(LOG_TAG, "placeBeacons Error: " + e.toString());
        }
    }

    private void setImageView(String imagePath) {
        Picasso.with(getActivity())
                .load(imagePath)
                .fit()
                .centerInside()
                .into(mapImageView);
    }

    private void initViews(View view) {
        frameLayout = (FrameLayout) view.findViewById(R.id.framePlace);
        mapImageView = (ImageView) view.findViewById(R.id.imageViewPlace);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        return false;
    }
}
