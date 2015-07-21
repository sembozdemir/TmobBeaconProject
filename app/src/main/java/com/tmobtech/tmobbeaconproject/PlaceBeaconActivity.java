package com.tmobtech.tmobbeaconproject;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.FrameLayout;

import com.tmobtech.tmobbeaconproject.data.MyDbHelper;

/**
 * Created by Ozberk on 15.7.2015.
 */
public class PlaceBeaconActivity extends ActionBarActivity {
    static long mapID;
    MyDbHelper myDbHelper;
    Cursor cursor;
    static String imagePath;
    PagerAdapter pagerAdapter;
    ViewPager viewPager;
    private static final int CONTENT_VIEW_ID = 10101010;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frame = new FrameLayout(this);
        frame.setId(CONTENT_VIEW_ID);
        setContentView(frame, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        if (savedInstanceState == null) {
            Fragment newFragment = new SetBeaconFragment();
            FragmentTransaction ft =getSupportFragmentManager().beginTransaction();
            ft.add(CONTENT_VIEW_ID, newFragment).commit();
        }
        initialize();

        cursor = myDbHelper.getMapFromId(mapID);
        try {
            if (cursor.moveToFirst())
                do {
                    imagePath = cursor.getString(cursor.getColumnIndex(MyDbHelper.COLUMN_MAP_IMAGE_PATH));
                }
                while (cursor.moveToNext());
        } catch (Exception e) {
            Log.e("DATABASE ERROR", e.toString());
        }



    }


    private void initialize() {
        myDbHelper = new MyDbHelper(PlaceBeaconActivity.this);
        mapID = getIntent().getLongExtra("mapId", 0);
    }

    public String getImagePath() {
        return imagePath;
    }

    public long getMapID() {
        return mapID;
    }

}
