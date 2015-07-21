package com.tmobtech.tmobbeaconproject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tmobtech.tmobbeaconproject.BeaconManager.FindBeacon;
import com.tmobtech.tmobbeaconproject.data.MyDbHelper;
import com.tmobtech.tmobbeaconproject.utility.Utility;
import com.tmobtech.tmobbeaconproject.views.BeaconMarkerView;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ozberk on 15.7.2015.
 */
public class SetBeaconFragment extends Fragment implements View.OnTouchListener, View.OnClickListener {

    View markerViewClass;
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
    TextView selectedBeacon;
    Button intentPlaceBeacon;
    SetPlaceFragment setPlaceFragment;
    FragmentTransaction fragmentTransaction;
    private static final int CONTENT_VIEW_ID = 10101010;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.setbeaconfragment, null);
        intentPlaceBeacon=(Button)view.findViewById(R.id.button4);


        intentPlaceBeacon.setOnClickListener(this);
        frameLayout = (FrameLayout) view.findViewById(R.id.frame2);
        mapImageView = (ImageView) view.findViewById(R.id.imageView);

        initialize(view);


        try {
            imagePath = placeBeaconActivity.getImagePath();
            mapId = placeBeaconActivity.getMapID();
           listBeacon= Utility.getBeaconList(mapId,getActivity());
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
                    .load(imagePath).fit().centerInside()
                    .into(mapImageView);

        } catch (Exception e) {
            Log.e("BeaconFragmenError", e.toString());
        }

        return view;
    }

    private void initialize(View v) {
        layoutParams1 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        placeBeaconActivity = new PlaceBeaconActivity();

        myDbHelper = new MyDbHelper(getActivity());

        listBeacon = new ArrayList<>();

        findBeacon = new FindBeacon(getActivity());


    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (v.getId() == mapImageView.getId()) {

            dialogCreate();
            dialog.show();
            x = event.getX();
            y = event.getY();
            
        }

        return false;
    }

            @Override
        public void onClick(View v) {
                try {
                    if (v.getId() == silDialogBtn.getId()) {
                        try {
                            ViewGroup parentView = (ViewGroup) markerViewClass.getParent();
                            parentView.removeView(markerViewClass);
                            myDbHelper.deleteBeacon(((BeaconMarkerView) markerViewClass).getBeacon().getId());
                            dialog.cancel();
                        } catch (Exception e) {
                            Log.e("Eror Remove View :", e.toString());
                        }
                    }


                    if (v.getId() == refreshBtn.getId()) {

                        list = findBeacon.ls;
                        com.tmobtech.tmobbeaconproject.SpinnerAdapter spinnerAdapter = new com.tmobtech.tmobbeaconproject.SpinnerAdapter(getActivity(), list);
                        spinner.setAdapter(spinnerAdapter);
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                selectedBeacon.setText(((org.altbeacon.beacon.Beacon) spinner.getSelectedItem()).getBluetoothAddress());
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    }



                    if (v.getId() == kaydetDialogBtn.getId()) {

                        kaydet();
                    }


                }
                catch (Exception e)
                {}



                try {
                    if (v.getId()==intentPlaceBeacon.getId())
                    {
                        setPlaceFragment=new SetPlaceFragment();
                        fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(CONTENT_VIEW_ID,setPlaceFragment);


                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                    }
                }
                catch (Exception e)
                {
                    Log.e("FragmentReplaceError",e.toString());
                }


            }

    private void kaydet() {
        final BeaconMarkerView beaconMarkerView = new BeaconMarkerView(getActivity());

        Beacon beacon = new Beacon();
        beacon.setBeaconName(markerName.getText().toString());
        beaconMarkerView.setBeacon(beacon);

        try {
            beacon.setMacAddress(((org.altbeacon.beacon.Beacon) spinner.getSelectedItem()).getBluetoothAddress());
        } catch (Exception e) {

        }

        beaconMarkerView.setX(x - 64);
        beaconMarkerView.setY(y - 64);
        frameLayout.addView(beaconMarkerView, layoutParams1);
        try {
            mapId = placeBeaconActivity.getMapID();
        } catch (Exception e) {
            Log.e("MapIdError", "MapIdError");
        }

        try {

            beacon.setId(myDbHelper.insertBeacon(markerName.getText().toString(),
                    beacon.getMacAddress(),
                    beaconMarkerView.getX(),
                    beaconMarkerView.getY(),
                    mapId));

            beaconMarkerView.setBeacon(beacon);
        } catch (Exception e) {
            Log.e("SQLError", e.toString());
        }

        dialog.cancel();

        beaconMarkerView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                markerViewClass = (BeaconMarkerView) v;
                markerName.setText(((BeaconMarkerView) markerViewClass).getBeacon().getBeaconName());
                selectedBeacon.setText(((BeaconMarkerView) markerViewClass).getBeacon().getMacAddress());
                kaydetDialogBtn.setText("Update");
                kaydetDialogBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        beaconMarkerView.getBeacon().setBeaconName(markerName.getText().toString());
                        beaconMarkerView.getBeacon().setMacAddress(((org.altbeacon.beacon.Beacon) spinner.getSelectedItem()).getBluetoothAddress());
                        myDbHelper.updateBeaconName(beaconMarkerView.getBeacon().getId(), beaconMarkerView.getBeacon().getBeaconName());
                        try {
                            myDbHelper.updateBeaconMacAddress(beaconMarkerView.getBeacon().getId(), beaconMarkerView.getBeacon().getMacAddress());

                        }
                        catch (Exception e)
                        {}
                         dialog.cancel();

                    }
                });

                dialog.show();

            }
        });

    }

    private void yerlestir(final Beacon beacon) {
        try {

            final BeaconMarkerView beaconMarkerView = new BeaconMarkerView(getActivity());

            beaconMarkerView.setX(beacon.getApsis());
            beaconMarkerView.setY(beacon.getOrdinat());
            beaconMarkerView.setBeacon(beacon);

            frameLayout.addView(beaconMarkerView, layoutParams1);

            beaconMarkerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    markerViewClass = v;

                    dialogCreate();
                    kaydetDialogBtn.setText("Update");
                    try {
                        markerName.setText(beacon.getBeaconName());
                        selectedBeacon.setText(beacon.getMacAddress());
                    } catch (Exception e) {
                    }

                    dialog.show();

                    kaydetDialogBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            update(beaconMarkerView);

                        }
                    });

                    silDialogBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                ViewGroup parentView = (ViewGroup) markerViewClass.getParent();
                                parentView.removeView(markerViewClass);
                                myDbHelper.deleteBeacon(((BeaconMarkerView) markerViewClass).getBeacon().getId());
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


    private void update(BeaconMarkerView v) {
        try {
            v.getBeacon().setBeaconName(markerName.getText().toString());
        } catch (Exception e) {

        }
        try {
            v.getBeacon().setMacAddress(((org.altbeacon.beacon.Beacon) spinner.getSelectedItem()).getBluetoothAddress());
        } catch (Exception e) {

        }

        try {
            myDbHelper.updateBeaconName(v.getBeacon().getId(), markerName.getText().toString());
            myDbHelper.updateBeaconMacAddress(v.getBeacon().getId(), ((org.altbeacon.beacon.Beacon) spinner.getSelectedItem()).getBluetoothAddress());
        } catch (Exception e) {

        }

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
        refreshBtn = (Button) dialog.findViewById(R.id.button3);
        spinner = (Spinner) dialog.findViewById(R.id.spinner);

        kaydetDialogBtn.setOnClickListener(this);
        silDialogBtn.setOnClickListener(this);
        refreshBtn.setOnClickListener(this);
        selectedBeacon = (TextView) dialog.findViewById(R.id.textView5);

    }

    @Override
    public void onPause() {
        super.onPause();
        findBeacon.stopBeaconBindService();
    }
}
