package com.tmobtech.tmobbeaconproject;

import android.app.Dialog;
import android.database.Cursor;
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

import com.tmobtech.tmobbeaconproject.data.MyDbHelper;

/**
 * Created by Ozberk on 15.7.2015.
 */
public class PlaceBeaconActivity extends ActionBarActivity implements View.OnClickListener {
    static String mapID;
    MyDbHelper myDbHelper;
    Cursor cursor;
    static String imagePath;
    private String TAG = "PlaceBeaconActivityError";

    private static final int CONTENT_VIEW_ID = 10101010;

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
        myDbHelper = new MyDbHelper(PlaceBeaconActivity.this);
        mapID = getIntent().getStringExtra("mapId");
        getImageMap();
    }

    private void getImageMap() {
       // cursor = myDbHelper.getMapFromId(mapID); TODO Degisicicek
        if (cursor.moveToFirst())
            do {
                imagePath = cursor.getString(cursor.getColumnIndex(MyDbHelper.COLUMN_MAP_IMAGE_PATH));
            }
            while (cursor.moveToNext());
        cursor.close();
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
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.framePlaceBeacon, fragment, fragment.getClass().getSimpleName())
                .commit();
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
               // editMap(mapID); TODO DEGISICEK
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void editMap(final long mapID) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_edit_map);
        dialog.setTitle("Edit Map");
        String mapName = "";
        Cursor cursor = myDbHelper.getMapFromId(mapID);
        if (cursor.moveToFirst()) {
            mapName = cursor.getString(cursor.getColumnIndex(MyDbHelper.COLUMN_MAP_NAME));
        }
        final EditText editText = (EditText) dialog.findViewById(R.id.editText_map_name_edit_dialog);
        editText.setText(mapName);
        editText.setSelection(editText.getText().toString().length());

        Button button = (Button) dialog.findViewById(R.id.button_edit_dialog);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText.getText().toString().equals("")){
                    myDbHelper.updateMapName(mapID, editText.getText().toString());
                    dialog.cancel();
                }
            }
        });

        dialog.show();
    }
}
