package com.tmobtech.tmobbeaconproject;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import com.tmobtech.tmobbeaconproject.ParseData.Constants;
import com.tmobtech.tmobbeaconproject.customviews.BeaconMarkerView;
import com.tmobtech.tmobbeaconproject.data.MyDbHelper;
import com.tmobtech.tmobbeaconproject.entity.Beacon;
import com.tmobtech.tmobbeaconproject.utility.FindBeacon;
import com.tmobtech.tmobbeaconproject.utility.ParseCore;
import com.tmobtech.tmobbeaconproject.utility.UserGuideDialog;
import com.tmobtech.tmobbeaconproject.utility.Utility;

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
    String mapId;
    PlaceBeaconActivity placeBeaconActivity;
    Button silDialogBtn;
    Dialog dialog;
    Button kaydetDialogBtn;
    ImageButton refreshBtn;
    ListView listView;
    EditText markerName;
    List<Beacon> listBeacon;
    FindBeacon findBeacon;
    TextView selectedBeacon;
    SetPlaceFragment setPlaceFragment;
    FragmentTransaction fragmentTransaction;
    private static final int CONTENT_VIEW_ID = 10101010;
    private   BluetoothAdapter mBlue = BluetoothAdapter.getDefaultAdapter();
    static int listPosition;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_set_beacon, null);

        frameLayout=(FrameLayout) view.findViewById(R.id.frameBeacon);

        UserGuideDialog userGuideDialog=new UserGuideDialog(getActivity(),"setBeaconPage");
        
        initialize(view);
        mapImageView = (ImageView) view.findViewById(R.id.imageViewBeacon);


        try {
            imagePath = placeBeaconActivity.getImagePath();
            mapId = placeBeaconActivity.getMapID();

            listBeacon = Utility.getBeaconFromParse(mapId);
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
                   // myDbHelper.deleteBeacon(((BeaconMarkerView) markerViewClass).getBeacon().getId());
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

                list = findBeacon.ls;
                com.tmobtech.tmobbeaconproject.BeaconDialogListAdapter spinnerAdapter = new com.tmobtech.tmobbeaconproject.BeaconDialogListAdapter(getActivity(), list);
                listView.setAdapter(spinnerAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            listPosition = position;
                            selectedBeacon.setText(list.get(position).getBluetoothAddress());
                        } catch (Exception e) {
                        }

                    }
                });
            }


            if (v.getId() == kaydetDialogBtn.getId()) {

                kaydet();
            }


        } catch (Exception e) {
        }
    }

    private void kaydet() {

        boolean isAdded = false;
        try {
            final BeaconMarkerView beaconMarkerView = new BeaconMarkerView(getActivity());

            Beacon beacon = new Beacon();

            if (markerName.getText().toString().trim().length() > 0) {

                final List<Beacon> listFromDb = Utility.getBeaconFromParse(mapId);
                for (int i = 0; i < listFromDb.size(); i++) {
                    if (((org.altbeacon.beacon.Beacon)list.get(listPosition)).getBluetoothAddress().equals(listFromDb.get(i).getMacAddress())) {
                        isAdded = true;
                    }
                }


                if (!isAdded) {
                    beacon.setBeaconName(markerName.getText().toString());


                    beacon.setMacAddress(list.get(listPosition).getBluetoothAddress());

                    beaconMarkerView.setX(x - 64);
                    beaconMarkerView.setY(y - 64);
                    beacon.setApsis(x - 64);
                    beacon.setOrdinat(y - 64);



                    mapId = placeBeaconActivity.getMapID();

                    beacon.setMapId(mapId);

                    beacon.save();
                    Log.e("Username", User.userName);


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

                                    update(beaconMarkerView);


                                   // dialog.cancel();

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
                                ((BeaconMarkerView) markerViewClass).getBeacon().deleteInBackground();
                               // myDbHelper.deleteBeacon(((BeaconMarkerView) markerViewClass).getBeacon().getId());
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


    private void update(final BeaconMarkerView v) {
        boolean macAdressIsUnique = true;


            listBeacon = Utility.getBeaconFromParse(mapId);

        if (!v.getBeacon().getMacAddress().equals(selectedBeacon.getText())) {
            for (int i = 0; i < listBeacon.size(); i++) {
                if (selectedBeacon.getText().equals(listBeacon.get(i).getMacAddress())) {
                    macAdressIsUnique = false;
                }
            }

            if (macAdressIsUnique) {
                try {
                    v.getBeacon().setBeaconName(markerName.getText().toString());
                } catch (Exception e) {

                }
                try {
                    v.getBeacon().setMacAddress(list.get(listPosition).getBluetoothAddress());
                } catch (Exception e) {

                }

                try {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Beacon");

// Retrieve the object by id
                    query.getInBackground(v.getBeacon().getObjectId(), new GetCallback<ParseObject>() {
                        public void done(ParseObject beaconObject, ParseException e) {
                            if (e == null) {
                                beaconObject.put(Constants.COLUMN_BEACON_NAME,markerName.getText().toString());
                                beaconObject.put(Constants.COLUMN_BEACON_MAC_ADDRESS, list.get(listPosition).getBluetoothAddress());
                                try {
                                    beaconObject.save();
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }

                            }
                        }
                    });
                   // myDbHelper.updateBeaconName(v.getBeacon().getId(), markerName.getText().toString());
                 //   myDbHelper.updateBeaconMacAddress(v.getBeacon().getId(), list.get(listPosition).getBluetoothAddress());

                } catch (Exception e) {

                }

                dialog.cancel();
            } else
                Toast.makeText(getActivity(), "Mac Adress must be Unique", Toast.LENGTH_LONG).show();
        }
        else
        {
            try {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Beacon");

// Retrieve the object by id
                query.getInBackground(v.getBeacon().getObjectId(), new GetCallback<ParseObject>() {
                    public void done(ParseObject beaconObject, ParseException e) {
                        if (e == null) {





                          v.getBeacon().setBeaconName(markerName.getText().toString());
                            beaconObject.put(Constants.COLUMN_BEACON_NAME,markerName.getText().toString());
                            beaconObject.saveInBackground();

                        }
                    }
                });
                // myDbHelper.updateBeaconName(v.getBeacon().getId(), markerName.getText().toString());
                //   myDbHelper.updateBeaconMacAddress(v.getBeacon().getId(), list.get(listPosition).getBluetoothAddress());

            } catch (Exception e) {

            }
            dialog.cancel();

        }

    }

    private void dialogCreate()

    {
        dialog = new Dialog(getActivity());
        dialog.setTitle("Beacon Tanimlama");
        dialog.setContentView(R.layout.dialog);
        kaydetDialogBtn = (Button) dialog.findViewById(R.id.button);
        silDialogBtn = (Button) dialog.findViewById(R.id.button2);
        markerName = (EditText) dialog.findViewById(R.id.editText);
        refreshBtn = (ImageButton) dialog.findViewById(R.id.button3);
        listView = (ListView) dialog.findViewById(R.id.listView);

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
}
