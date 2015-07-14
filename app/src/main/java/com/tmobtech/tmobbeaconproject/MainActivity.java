package com.tmobtech.tmobbeaconproject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.tmobtech.tmobbeaconproject.data.MyDbHelper;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private FloatingActionButton mFab;
    private GridView mGridView;
    private MyDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initiliaze View Components
        initViews();

        // Get Database Helper
        mDbHelper = new MyDbHelper(this);

        // set OnClickListener for Floating Action Button
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewMap();
            }
        });

        // Create grid adapter and set it
        ArrayList<BeaconMap> beaconMaps = getBeaconMapsArray();
        MyGridAdapter myGridAdapter = new MyGridAdapter(this, beaconMaps);

        mGridView.setAdapter(myGridAdapter);

        // set OnItemClickListener for grid items
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BeaconMap beaconMap = (BeaconMap) parent.getItemAtPosition(position);
                onMapSelected(beaconMap);
            }
        });
    }

    private ArrayList<BeaconMap> getBeaconMapsArray() {
        ArrayList<BeaconMap> beaconMapArrayList = new ArrayList<BeaconMap>();
        Cursor cursor = mDbHelper.getMaps();
        if (cursor.moveToFirst()) {
            do {
                BeaconMap beaconMap = new BeaconMap(
                        cursor.getString(cursor.getColumnIndex(MyDbHelper.COLUMN_MAP_NAME)),
                        cursor.getString(cursor.getColumnIndex(MyDbHelper.COLUMN_MAP_IMAGE_PATH)));
                beaconMapArrayList.add(beaconMap);
            } while (cursor.moveToNext());
        }

        return beaconMapArrayList;
    }

    private void onMapSelected(BeaconMap beaconMap) {
        Toast.makeText(this, beaconMap.getName() + " selected", Toast.LENGTH_LONG).show();

        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        Log.d(LOG_TAG, path);
    }

    private void addNewMap() {
        Intent cameraIntent = new Intent(MainActivity.this, CameraActivity.class);
        startActivity(cameraIntent);
    }

    private void initViews() {
        mGridView = (GridView) findViewById(R.id.gridview);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
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
