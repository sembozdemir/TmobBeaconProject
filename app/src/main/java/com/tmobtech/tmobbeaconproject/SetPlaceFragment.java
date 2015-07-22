package com.tmobtech.tmobbeaconproject;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

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
    private float x;
    private float y;
    private Dialog dialog;


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
        mapImageView.setOnTouchListener(this);
    }

    private void initViews(View view) {
        frameLayout = (FrameLayout) view.findViewById(R.id.framePlace);
        mapImageView = (ImageView) view.findViewById(R.id.imageViewPlace);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (v.getId() == mapImageView.getId()) {
            dialogCreate("");
            dialog.show();
            x = event.getX();
            y = event.getY();

        }
        return false;
    }

    private void dialogCreate(String placeName) {
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_place);
        Button saveDialogButton = (Button) dialog.findViewById(R.id.button_save_place);
        Button delDialogButton = (Button) dialog.findViewById(R.id.button_delete_place);
        Button updateDialogButton = (Button) dialog.findViewById(R.id.button_update_place);
        EditText placeNameEditText = (EditText) dialog.findViewById(R.id.editText_place_name);
        ListView beaconPowerListView = (ListView) dialog.findViewById(R.id.listView_beacon);
        BeaconPowerListAdapter beaconPowerListAdapter;
        if (!placeName.equals("")) { // it will be edit place dialog
            dialog.setTitle("Edit Place");
            saveDialogButton.setVisibility(View.GONE);
            beaconPowerListAdapter = new BeaconPowerListAdapter(getActivity(),
                    Utility.getBeaconPowers(parentActivity.getMapID())); // <TODO: yanlış olabilir
            beaconPowerListView.setAdapter(beaconPowerListAdapter);
            placeNameEditText.setText(placeName);
            updateDialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updatePlace();
                    dialog.cancel();
                }
            });

            delDialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deletePlace();
                    dialog.cancel();
                }
            });
        } else {
            dialog.setTitle("New Place"); // it will be new place dialog
            updateDialogButton.setVisibility(View.GONE);
            delDialogButton.setVisibility(View.GONE);

            saveDialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    savePlace();
                    dialog.cancel();
                }
            });
        }
    }

    private void updatePlace() {
        Toast.makeText(getActivity(), "updated", Toast.LENGTH_LONG).show();
    }

    private void deletePlace() {
        Toast.makeText(getActivity(), "deleted", Toast.LENGTH_LONG).show();
    }

    private void savePlace() {
        Toast.makeText(getActivity(), "saved", Toast.LENGTH_LONG).show();
    }
}
