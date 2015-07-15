package com.tmobtech.tmobbeaconproject;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
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

import com.squareup.picasso.Picasso;
import com.tmobtech.tmobbeaconproject.data.MyDbHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kerim on 15.7.2015.
 */
public class SetBeaconFragment extends Fragment implements View.OnTouchListener, View.OnClickListener {

    View markerViewClass;
    FrameLayout frameLayout;
    static float x;
    static float y;
    ImageView mapImageView;
    MyDbHelper myDbHelper;
    Cursor cursor;
    String imagePath;
    FrameLayout.LayoutParams layoutParams1;
    LayoutInflater inflater;
    long mapId;
    PlaceBeaconActivity placeBeaconActivity;
    Button silDialogBtn;
    Dialog dialog;
    Button kaydetDialogBtn;
    EditText markerName;
    List<Beacon> listBeacon;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.setbeaconfragment, null);
        initialize();
        mapImageView = (ImageView) view.findViewById(R.id.imageView);

        try {
            imagePath = placeBeaconActivity.getImagePath();
            mapId = placeBeaconActivity.getMapID();
            Cursor cursor = myDbHelper.getBeaconsAtMap(22);
            if (cursor.moveToFirst()) {
                do {
                    try {

                        Beacon beacon = new Beacon();
                        beacon.beaconName = cursor.getString(cursor.getColumnIndex(MyDbHelper.COLUMN_BEACON_NAME));
                        beacon.macAdress = cursor.getString(cursor.getColumnIndex(MyDbHelper.COLUMN_BEACON_MAC_ADDRESS));
                        beacon.apsis = cursor.getFloat(cursor.getColumnIndex(MyDbHelper.COLUMN_BEACON_APSIS));
                        beacon.ordinat = cursor.getFloat(cursor.getColumnIndex(MyDbHelper.COLUMN_BEACON_ORDINAT));
                       // beacon.mapId = cursor.getLong(cursor.getColumnIndex(MyDbHelper.COLUMN_BEACON_MAP_ID));

                        listBeacon.add(beacon);
                        Log.e("Eklendi","Eklendi");

                    } catch (Exception e) {
                        Log.e("GetBeaconInDatabaseErr", e.toString());
                    }

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("ImagePathError", e.toString());
        }


        try {


        } catch (Exception e) {
            Log.e("BeaconSetMapError", e.toString());
        }


        mapImageView.setOnTouchListener(this);


        try {
            Picasso.with(getActivity())
                    .load(imagePath).fit().centerCrop()
                    .into(mapImageView);

        } catch (Exception e) {
            Log.e("BeaconFragmenError", e.toString());
        }


        Log.e("ImagePath=", imagePath);


        return view;
    }

    private void initialize() {
        layoutParams1 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        placeBeaconActivity = new PlaceBeaconActivity();

        myDbHelper = new MyDbHelper(getActivity());

        listBeacon = new ArrayList<>();


    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {


        if (v.getId() == mapImageView.getId()) {


            dialog = new Dialog(getActivity());
            dialog.setTitle("Beacon Tanimlama");
            dialog.setContentView(R.layout.dialog);
            dialog.show();
            kaydetDialogBtn = (Button) dialog.findViewById(R.id.button);
            silDialogBtn = (Button) dialog.findViewById(R.id.button2);


            markerName = (EditText) dialog.findViewById(R.id.editText);
            x = event.getX();
            y = event.getY();
            kaydetDialogBtn.setOnClickListener(this);


            silDialogBtn.setOnClickListener(this);


        }


        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == silDialogBtn.getId()) {
            try {
                ViewGroup parentView = (ViewGroup) markerViewClass.getParent();
                parentView.removeView(markerViewClass);
                dialog.cancel();
            } catch (Exception e) {
                Log.e("Eror Remove View :", e.toString());
            }
        }

        if (v.getId() == kaydetDialogBtn.getId()) {

            frameLayout = (FrameLayout) getActivity().findViewById(R.id.frame2);

            View markerView = inflater.inflate(R.layout.markerframelayout, null);
            markerView.setTag(markerName.getText());


            Log.e("X=", x + "");


            markerView.setX(x - 64);
            markerView.setY(y - 64);
            frameLayout.addView(markerView, layoutParams1);
            try {
                mapId = placeBeaconActivity.getMapID();
            } catch (Exception e) {
                Log.e("MapIdError", "MapIdError");
            }

            try {
                Log.e("MapID=", mapId + "");
                myDbHelper.insertBeacon(markerName.getText().toString(), "asd", 2, 2, mapId);
            } catch (Exception e) {
                Log.e("SQLError", e.toString());
            }

            dialog.cancel();

            markerView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    markerViewClass = v;
                    kaydetDialogBtn.setText("Update");
                    kaydetDialogBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            v.setTag(markerName.getText());
                            dialog.cancel();

                        }
                    });

                    dialog.show();


                }
            });

        }

    }
}
