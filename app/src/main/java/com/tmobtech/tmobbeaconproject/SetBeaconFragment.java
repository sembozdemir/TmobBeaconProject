package com.tmobtech.tmobbeaconproject;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

    private String TAG = "SetBeaconFragmentError";
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
    ImageButton refreshBtn;
    Spinner spinner;
    EditText markerName;
    List<Beacon> listBeacon;
    FindBeacon findBeacon;
    TextView selectedBeacon;
    Button intentPlaceBeacon;
    SetPlaceFragment setPlaceFragment;
    FragmentTransaction fragmentTransaction;
    private static final int CONTENT_VIEW_ID = 10101010;
    private   BluetoothAdapter mBlue = BluetoothAdapter.getDefaultAdapter();
    private SpinnerRefreshTimer spinnerRefreshTimer;







    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.setbeaconfragment, null);
        intentPlaceBeacon = (Button) view.findViewById(R.id.button4);

        spinnerRefreshTimer = new SpinnerRefreshTimer(10000,1000);

        intentPlaceBeacon.setOnClickListener(this);
        frameLayout = (FrameLayout) view.findViewById(R.id.frame2);
        mapImageView = (ImageView) view.findViewById(R.id.imageView);

        initialize(view);


        try {
            imagePath = placeBeaconActivity.getImagePath();
            mapId = placeBeaconActivity.getMapID();
            listBeacon = Utility.getBeaconList(mapId, getActivity());
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        try {
            if (listBeacon.size() > 0) {

                for (int i = 0; i < listBeacon.size(); i++)
                    yerlestir(listBeacon.get(i));
            }

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        mapImageView.setOnTouchListener(this);

        try {
            Picasso.with(getActivity())
                    .load(imagePath).fit().centerInside()
                    .into(mapImageView);

        } catch (Exception e) {
            Log.e(TAG, e.toString());
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
                    Log.e(TAG, e.toString());
                }
            }


            if (v.getId() == refreshBtn.getId()) {

                Log.v("BlueTooth",mBlue.toString());
                if(!mBlue.isEnabled()){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

                    alertDialog.setTitle("Confirm Bluetooth");

                    // Setting Dialog Message
                    alertDialog.setMessage("Are you sure you want open Bluetooth?");

                    // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.ic_bluetooth);

                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

//                        setBluetooth(true);
                            mBlue = BluetoothAdapter.getDefaultAdapter();
                            mBlue.enable();
                            Toast.makeText(getActivity(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event
                            Toast.makeText(getActivity(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    });

                    // Showing Alert Message

                    alertDialog.show();
                }


                refreshSpinner();

            }


            if (v.getId() == kaydetDialogBtn.getId()) {

                kaydet();
            }


        } catch (Exception e) {
        }


        try {
            if (v.getId() == intentPlaceBeacon.getId()) {
                setPlaceFragment = new SetPlaceFragment();
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(CONTENT_VIEW_ID, setPlaceFragment,"SetPlaceFragment");


                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        } catch (NullPointerException e) {
            Log.e(TAG, e.toString());
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }


    }

    private void refreshSpinner() {
        list = findBeacon.ls;
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getActivity(), list);
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


    private void kaydet() {

        boolean isAdded = false;
        try {
            final BeaconMarkerView beaconMarkerView = new BeaconMarkerView(getActivity());

            Beacon beacon = new Beacon();

            if (markerName.getText().toString().trim().length() > 0) {

                List<Beacon> list = Utility.getBeaconList(mapId, getActivity());
                for (int i = 0; i < list.size(); i++) {
                    if (((org.altbeacon.beacon.Beacon) spinner.getSelectedItem()).getBluetoothAddress().equals(list.get(i).getMacAddress())) {
                        isAdded = true;
                    }
                }

                if (!isAdded) {
                    beacon.setBeaconName(markerName.getText().toString());


                    beacon.setMacAddress(((org.altbeacon.beacon.Beacon) spinner.getSelectedItem()).getBluetoothAddress());

                    beaconMarkerView.setX(x - 64);
                    beaconMarkerView.setY(y - 64);

                    mapId = placeBeaconActivity.getMapID();


                    beacon.setId(myDbHelper.insertBeacon(markerName.getText().toString(),
                            beacon.getMacAddress(),
                            beaconMarkerView.getX(),
                            beaconMarkerView.getY(),
                            mapId));


                    frameLayout.addView(beaconMarkerView, layoutParams1);


                    beaconMarkerView.setBeacon(beacon);

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

                                    myDbHelper.updateBeaconMacAddress(beaconMarkerView.getBeacon().getId(), beaconMarkerView.getBeacon().getMacAddress());


                                    dialog.cancel();

                                }
                            });

                            dialog.show();

                        }
                    });


                    beaconMarkerView.setBeacon(beacon);
                } else
                    Toast.makeText(getActivity(), "Mac Adress Must be Unique", Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(getActivity(), "Beacon cannot be Null", Toast.LENGTH_LONG).show();


        } catch (SQLiteConstraintException e) {
            Log.e(TAG, e.getMessage());
            Toast.makeText(getActivity(), "MacAdress must be unique", Toast.LENGTH_LONG).show();
        } catch (NullPointerException e) {
            Log.e(TAG, e.getMessage());
            Toast.makeText(getActivity(), "Beacon or MacAdress cannot be Null", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            Toast.makeText(getActivity(), "Mac adress must be unique", Toast.LENGTH_LONG).show();
        }


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
                                Log.e(TAG, e.toString());
                            }
                        }
                    });
                }
            });

        } catch (Exception e) {
            Log.e(TAG, e.toString());
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

        spinnerRefreshTimer.start();
        dialog = new Dialog(getActivity());
        dialog.setTitle("Beacon Tanimlama");
        dialog.setContentView(R.layout.dialog);
        kaydetDialogBtn = (Button) dialog.findViewById(R.id.button);
        silDialogBtn = (Button) dialog.findViewById(R.id.button2);
        markerName = (EditText) dialog.findViewById(R.id.editText);
        refreshBtn = (ImageButton) dialog.findViewById(R.id.button3);
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

    public static boolean setBluetooth(boolean enable) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean isEnabled = bluetoothAdapter.isEnabled();
//        if (isEnabled)
//            return false;
        if (enable && !isEnabled) {

            return bluetoothAdapter.enable();

        } else if (!enable && isEnabled) {
            return bluetoothAdapter.disable();
        }

        return true;
    }
    public class SpinnerRefreshTimer extends CountDownTimer{

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public SpinnerRefreshTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            refreshSpinner();
        }

        @Override
        public void onFinish() {
            spinnerRefreshTimer.cancel();
            spinnerRefreshTimer.start();
        }
    }
}
