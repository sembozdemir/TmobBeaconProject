package com.tmobtech.tmobbeaconproject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.squareup.picasso.Picasso;
import com.tmobtech.tmobbeaconproject.BeaconManager.FindBeacon;
import com.tmobtech.tmobbeaconproject.data.MyDbHelper;
import com.tmobtech.tmobbeaconproject.views.MarkerView;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Ozberk on 15.7.2015.
 */
public class SetBeaconFragment extends Fragment implements View.OnTouchListener, View.OnClickListener{

    MarkerView markerViewClass;
    FrameLayout frameLayout;
    static float x;
    static float y;
    ImageView mapImageView;
    MyDbHelper myDbHelper;
    List<org.altbeacon.beacon.Beacon> list;


    String imagePath;
    FrameLayout.LayoutParams layoutParams1;
    LayoutInflater inflater;
    long mapId;
    PlaceBeaconActivity placeBeaconActivity;
    Button silDialogBtn;
    Dialog dialog;
    Button kaydetDialogBtn;
    Button refreshBtn;
    Spinner spinner;
    EditText markerName;
    List<Beacon> listBeacon;
    FindBeacon findBeacon;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.setbeaconfragment, null);
        frameLayout = (FrameLayout) view.findViewById(R.id.frame2);
        initialize();
        mapImageView = (ImageView) view.findViewById(R.id.imageView);

        try {
            imagePath = placeBeaconActivity.getImagePath();
            mapId = placeBeaconActivity.getMapID();
            Cursor cursor = myDbHelper.getBeaconsAtMap(mapId);
            if (cursor.moveToFirst()) {
                do {
                    try {

                        Beacon beacon = new Beacon();
                        beacon.setBeaconName(cursor.getString(cursor.getColumnIndex(MyDbHelper.COLUMN_BEACON_NAME)));
                        beacon.setMacAddress(cursor.getString(cursor.getColumnIndex(MyDbHelper.COLUMN_BEACON_MAC_ADDRESS)));
                        beacon.setApsis(cursor.getFloat(cursor.getColumnIndex(MyDbHelper.COLUMN_BEACON_APSIS)));
                        beacon.setOrdinat(cursor.getFloat(cursor.getColumnIndex(MyDbHelper.COLUMN_BEACON_ORDINAT)));
                        beacon.setId(cursor.getLong(cursor.getColumnIndex(MyDbHelper.COLUMN_BEACON_ID)));


                        listBeacon.add(beacon);


                    } catch (Exception e) {
                        Log.e("GetBeaconInDatabaseErr", e.toString());
                    }

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("ImagePathError", e.toString());
        }


        try {
            if (listBeacon.size() > 0) {

                for (int i = 0; i < listBeacon.size(); i++)
                    yerlestir(listBeacon.get(i));

            }


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


        return view;
    }

    private void initialize() {
        layoutParams1 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        placeBeaconActivity = new PlaceBeaconActivity();

        myDbHelper = new MyDbHelper(getActivity());

        listBeacon = new ArrayList<>();

        findBeacon=new FindBeacon(getActivity());



    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {


        if (v.getId() == mapImageView.getId()) {


            dialogCreate();
            dialog.show();

            x = event.getX();
            y = event.getY();
            kaydetDialogBtn.setOnClickListener(this);
            silDialogBtn.setOnClickListener(this);
            refreshBtn.setOnClickListener(this);

        }


        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == silDialogBtn.getId()) {
            try {
                ViewGroup parentView = (ViewGroup) markerViewClass.getParent();
                parentView.removeView(markerViewClass);
                myDbHelper.deleteBeacon(markerViewClass.getBeacon().getId());
                dialog.cancel();
            } catch (Exception e) {
                Log.e("Eror Remove View :", e.toString());
            }
        }

        if (v.getId()==refreshBtn.getId())
        {


            list=findBeacon.ls;
            com.tmobtech.tmobbeaconproject.SpinnerAdapter spinnerAdapter=new com.tmobtech.tmobbeaconproject.SpinnerAdapter(getActivity(),list);
            spinner.setAdapter(spinnerAdapter);



        }

        if (v.getId() == kaydetDialogBtn.getId()) {

            kaydet();
        }

    }


    private void kaydet() {
        final MarkerView markerView = new MarkerView(getActivity());
        markerView.setImageResource(R.drawable.marker);
        Beacon beacon = new Beacon();
        beacon.setBeaconName(markerName.getText().toString());
        markerView.setBeacon(beacon);


        markerView.setX(x - 64);
        markerView.setY(y - 64);
        frameLayout.addView(markerView, layoutParams1);
        try {
            mapId = placeBeaconActivity.getMapID();
        } catch (Exception e) {
            Log.e("MapIdError", "MapIdError");
        }

        try {

            beacon.setId(myDbHelper.insertBeacon(markerName.getText().toString(),
                    "00:11:22",
                    markerView.getX(),
                    markerView.getY(),
                    mapId));

            markerView.setBeacon(beacon);
        } catch (Exception e) {
            Log.e("SQLError", e.toString());
        }

        dialog.cancel();

        markerView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                markerViewClass = (MarkerView) v;
                markerName.setText(markerViewClass.getBeacon().getBeaconName());
                kaydetDialogBtn.setText("Update");
                kaydetDialogBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        markerView.setTag(markerName.getText());
                        dialog.cancel();

                    }
                });

                dialog.show();


            }
        });

    }

    private void yerlestir(final Beacon beacon) {
        try {


            final MarkerView markerView = new MarkerView(getActivity());
            markerView.setImageResource(R.drawable.marker);
            markerView.setX(beacon.getApsis());
            markerView.setY(beacon.getOrdinat());
            markerView.setBeacon(beacon);
            frameLayout.addView(markerView, layoutParams1);

            markerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    markerViewClass = (MarkerView) v;


                    dialogCreate();
                    kaydetDialogBtn.setText("Update");
                    markerName.setText(beacon.getBeaconName());
                    dialog.show();

                    kaydetDialogBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            update(markerView);

                        }
                    });

                    silDialogBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                ViewGroup parentView = (ViewGroup) markerViewClass.getParent();
                                parentView.removeView(markerViewClass);
                                myDbHelper.deleteBeacon(markerViewClass.getBeacon().getId());
                                dialog.cancel();
                            } catch (Exception e) {
                                Log.e("Error Remove View :", e.toString());
                            }
                        }
                    });

                }
            });


        } catch (Exception e) {
            Log.e("YerlestirError", e.toString());
        }


    }


    private void update(MarkerView v) {
        v.getBeacon().setBeaconName(markerName.getText().toString());
        dialog.cancel();


    }

    private void dialogCreate()

    {
        dialog = new Dialog(getActivity());
        dialog.setTitle("Beacon Tanimlama");
        dialog.setContentView(R.layout.dialog);
        kaydetDialogBtn = (Button) dialog.findViewById(R.id.button);
        silDialogBtn = (Button) dialog.findViewById(R.id.button2);
        markerName = (EditText) dialog.findViewById(R.id.editText);
        refreshBtn=(Button)dialog.findViewById(R.id.button3);
        spinner=(Spinner)dialog.findViewById(R.id.spinner);

    }




}
