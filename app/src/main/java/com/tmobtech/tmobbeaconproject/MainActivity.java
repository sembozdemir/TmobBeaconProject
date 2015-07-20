package com.tmobtech.tmobbeaconproject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.github.clans.fab.FloatingActionButton;
import com.parse.ParseObject;
import com.tmobtech.tmobbeaconproject.BeaconManager.FindBeacon;
import com.tmobtech.tmobbeaconproject.data.MyDbHelper;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    public static final String ACTION_CAMERA = "camera";
    public static final String ACTION_GALLERY = "gallery";
    private FloatingActionButton mFabCamera;
    private FloatingActionButton mFabGallery;
    private GridView mGridView;
    private MyDbHelper mDbHelper;
    private List<ParseObject> todos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);










        initViews();

        // Get Database Helper
        mDbHelper = new MyDbHelper(this);

        // set OnClickListener for Floating Action Button
        mFabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFromCamera();
            }
        });

        mFabGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFromGallery();
            }
        });

        // set OnItemClickListener for grid items
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BeaconMap beaconMap = (BeaconMap) parent.getItemAtPosition(position);
                onMapSelected(beaconMap);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Create grid adapter and set it
        ArrayList<BeaconMap> beaconMaps = getBeaconMapsArray();
        MyGridAdapter myGridAdapter = new MyGridAdapter(this, beaconMaps);

        mGridView.setAdapter(myGridAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDbHelper.close();
    }

    private ArrayList<BeaconMap> getBeaconMapsArray() {
        ArrayList<BeaconMap> beaconMapArrayList = new ArrayList<BeaconMap>();
        Cursor cursor = mDbHelper.getMaps();
        if (cursor.moveToFirst()) {
            do {
                BeaconMap beaconMap = new BeaconMap(
                        cursor.getLong(cursor.getColumnIndex(MyDbHelper.COLUMN_MAP_ID)),
                        cursor.getString(cursor.getColumnIndex(MyDbHelper.COLUMN_MAP_NAME)),
                        cursor.getString(cursor.getColumnIndex(MyDbHelper.COLUMN_MAP_IMAGE_PATH)));
                beaconMapArrayList.add(beaconMap);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return beaconMapArrayList;
    }

    private void onMapSelected(BeaconMap beaconMap) {
        Intent placeBeaconIntent = new Intent(MainActivity.this, PlaceBeaconActivity.class);
        placeBeaconIntent.putExtra("mapId", beaconMap.getId());
        startActivity(placeBeaconIntent);
    }

    private void addFromCamera() {
        Intent cameraIntent = new Intent(MainActivity.this, CameraActivity.class);
        cameraIntent.putExtra(Intent.EXTRA_TEXT, ACTION_CAMERA);
        startActivity(cameraIntent);
    }

    private void addFromGallery() {
        Intent galleryIntent = new Intent(MainActivity.this, CameraActivity.class);
        galleryIntent.putExtra(Intent.EXTRA_TEXT, ACTION_GALLERY);
        startActivity(galleryIntent);
    }

    private void initViews() {
        mGridView = (GridView) findViewById(R.id.gridview);
        mFabCamera = (FloatingActionButton) findViewById(R.id.fab_camera);
        mFabGallery = (FloatingActionButton) findViewById(R.id.fab_gallery);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
