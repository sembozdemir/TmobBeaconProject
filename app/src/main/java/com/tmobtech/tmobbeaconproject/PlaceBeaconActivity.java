package com.tmobtech.tmobbeaconproject;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.tmobtech.tmobbeaconproject.entity.BeaconMap;

/**
 * Created by Ozberk on 15.7.2015.
 */
public class PlaceBeaconActivity extends ActionBarActivity implements View.OnClickListener {
    static String mapID;
    static String imagePath;
    private String TAG = "PlaceBeaconActivityError";

    private Button buttonPrev;
    private Button buttonNext;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_beacon);

        initialize();
        initViews();

        buttonPrev.setVisibility(View.GONE);
        buttonNext.setVisibility(View.VISIBLE);
        setTitle("Set your beacons");
        setCurrentFragment(new SetBeaconFragment());
    }

    private void initViews() {
        buttonPrev = (Button) findViewById(R.id.buttonPrev);
        buttonNext = (Button) findViewById(R.id.buttonNext);
        buttonPrev.setOnClickListener(this);
        buttonNext.setOnClickListener(this);
        frameLayout = (FrameLayout) findViewById(R.id.framePlaceBeacon);

    }


    private void initialize() {
        mapID = getIntent().getStringExtra("mapId");
        imagePath=getIntent().getStringExtra("imagePath");
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getMapID() {
        return mapID;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonNext:
                buttonNext.setVisibility(View.GONE);
                buttonPrev.setVisibility(View.VISIBLE);
                setTitle("Set your places");
                setCurrentFragment(new SetPlaceFragment());
                break;
            case R.id.buttonPrev:
                buttonPrev.setVisibility(View.GONE);
                buttonNext.setVisibility(View.VISIBLE);
                setTitle("Set your beacons");
                setCurrentFragment(new SetBeaconFragment());
                break;
        }
    }

    public void setCurrentFragment(Fragment fragment) {


        SetBeaconFragment setBeaconFragment=(SetBeaconFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentBeacon);
        SetPlaceFragment setPlaceFragment=(SetPlaceFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentPlace);
        if (setBeaconFragment==null) {

            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.framePlaceBeacon, fragment, fragment.getClass().getSimpleName())
                    .commit();
        }
        else
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_place_beacon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_edit:
                editMap(mapID);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void editMap(final String mapID) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_edit_map);
        dialog.setTitle("Edit Map");

        final EditText editText = (EditText) dialog.findViewById(R.id.editText_map_name_edit_dialog);
        final Button button = (Button) dialog.findViewById(R.id.button_edit_dialog);

        ParseQuery<BeaconMap> query = ParseQuery.getQuery("BeaconMap");
        query.getInBackground(mapID, new GetCallback<BeaconMap>() {
            @Override
            public void done(final BeaconMap beaconMap, ParseException e) {
                if (e == null) {
                    editText.setText(beaconMap.getName());
                    editText.setSelection(editText.getText().toString().length());

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!editText.getText().toString().equals("")) {
                                beaconMap.setName(editText.getText().toString());
                                beaconMap.saveInBackground();
                                dialog.cancel();
                            }
                        }
                    });
                } else {
                    // something went wrong
                }
            }
        });

        dialog.show();
    }
}
