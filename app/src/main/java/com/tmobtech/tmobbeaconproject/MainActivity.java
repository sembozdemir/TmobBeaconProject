package com.tmobtech.tmobbeaconproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.github.clans.fab.FloatingActionButton;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.tmobtech.tmobbeaconproject.ParseData.Constants;
import com.tmobtech.tmobbeaconproject.entity.Beacon;
import com.tmobtech.tmobbeaconproject.entity.BeaconMap;
import com.tmobtech.tmobbeaconproject.entity.BeaconPower;
import com.tmobtech.tmobbeaconproject.entity.Place;
import com.tmobtech.tmobbeaconproject.utility.UserGuideDialog;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    public static final String ACTION_CAMERA = "camera";
    public static final String ACTION_GALLERY = "gallery";
    private FloatingActionButton mFabCamera;
    private FloatingActionButton mFabGallery;
    private GridView mGridView;

    private List<ParseObject> todos;
    private MyGridAdapter myGridAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserGuideDialog userGuideDialog=new UserGuideDialog(MainActivity.this,"homePage");

        initViews();

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

        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           final int arg2, long arg3) {


                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

                alertDialog.setTitle("Confirm Delete...");

                // Setting Dialog Message
                alertDialog.setMessage("Are you sure you want delete this?");

                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.delete);

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        deleteMap(arg2);
                        updateGridContent();
                    }
                });

                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();


                return false;
            }
        });


    }

    private void updateGridContent() {
        List<BeaconMap> beaconMaps = getBeaconMapsArray();
        myGridAdapter = new MyGridAdapter(this, beaconMaps);

        mGridView.setAdapter(myGridAdapter);
    }


    @Override
    protected void onResume() {
        super.onResume();

        // Create grid adapter and set it
        updateGridContent();
    }

    public void deleteMap(int position) {
        BeaconMap beaconMap = (BeaconMap) mGridView.getItemAtPosition(position);
        ParseQuery<Place> parseQueryBeacon=ParseQuery.getQuery("Place");
        parseQueryBeacon.whereEqualTo(Constants.COLUMN_PLACE_MAP_ID, beaconMap.getObjectId());

        try {

            ParseQuery<Beacon> beaconParseQuery=ParseQuery.getQuery("Beacon");
            beaconParseQuery.whereEqualTo(Constants.COLUMN_BEACON_MAP_ID,beaconMap.getObjectId());
            List<Beacon> beaconList=beaconParseQuery.find();
            for (Beacon beacon:beaconList)
            {
                beacon.deleteInBackground();
            }


            List<Place>placeList=parseQueryBeacon.find();
            for (Place place : placeList)
            {
                ParseQuery<BeaconPower> beaconPowerParseQuery= ParseQuery.getQuery("BeaconPower");
                beaconPowerParseQuery.whereEqualTo(Constants.COLUMN_BEACON_MEASURE_PLACE_ID,place.getObjectId());
                List<BeaconPower> beaconPowerList=beaconPowerParseQuery.find();
                for (BeaconPower beaconPower:beaconPowerList)
                {
                    beaconPower.delete();
                }





                place.deleteInBackground();

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        beaconMap.deleteInBackground();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    private List<BeaconMap> getBeaconMapsArray() {
       List<BeaconMap> beaconMaps=new ArrayList<>();
        ParseQuery<BeaconMap> query = ParseQuery.getQuery(BeaconMap.class);
        query.whereEqualTo("userId", ParseUser.getCurrentUser().getObjectId());
        Log.e("UserId=",ParseUser.getCurrentUser().getObjectId());
        try {
            beaconMaps=query.find();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return beaconMaps;
    }

    private void onMapSelected(BeaconMap beaconMap) {
        Intent placeBeaconIntent = new Intent(MainActivity.this, PlaceBeaconActivity.class);
        placeBeaconIntent.putExtra("mapId", beaconMap.getObjectId());
        placeBeaconIntent.putExtra("imagePath",beaconMap.getImagePath());
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

}
