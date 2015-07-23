package com.tmobtech.tmobbeaconproject;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.tmobtech.tmobbeaconproject.data.MyDbHelper;

/**
 * Created by Ozberk on 15.7.2015.
 */
public class PlaceBeaconActivity extends ActionBarActivity implements View.OnClickListener {
    static long mapID;
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
        mapID = getIntent().getLongExtra("mapId", 0);
        getImageMap();
    }

    private void getImageMap() {
        cursor = myDbHelper.getMapFromId(mapID);
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

    public long getMapID() {
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
}
