package com.tmobtech.tmobbeaconproject.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by semih on 14.07.2015.
 */
public class MyDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "beaconmap.db";
    public static final String TABLE_MAPS_NAME = "maps";
    public static final String TABLE_BEACONS_NAME = "beacons";
    public static final String TABLE_PLACES_NAME = "places";
    public static final String TABLE_BEACON_MEASURE_NAME = "beacon_measure";

    public static final String COLUMN_MAPS_ID = "map_id";
    public static final String COLUMN_MAPS_NAME = "map_name";
    public static final String COLUMN_MAPS_IMAGE_PATH = "img_path";

    public static final String COLUMN_BEACONS_ID = "beacon_id";
    public static final String COLUMN_BEACONS_NAME = "beacon_name";
    public static final String COLUMN_BEACONS_MAC_ADDRESS = "mac_address";
    public static final String COLUMN_BEACONS_APSIS = "apsis";
    public static final String COLUMN_BEACONS_ORDINAT = "ordinat";
    public static final String COLUMN_BEACONS_MAP_ID = "beacons_map_id";

    public static final String COLUMN_PLACE_ID = "place_id";
    public static final String COLUMN_PLACE_NAME = "place_name";
    public static final String COLUMN_PLACE_APSIS = "apsis";
    public static final String COLUMN_PLACE_ORDINAT = "ordinat";
    public static final String COLUMN_PLACE_MAP_ID = "place_map_id";

    public static final String COLUMN_BEACON_MEASURE_BEACON_ID = "beacon_id";
    public static final String COLUMN_BEACON_MEASURE_PLACE_ID = "place_id";
    public static final String COLUMN_BEACON_MEASURE_POWER = "power";

    public MyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TABLE_MAPS = "CREATE TABLE " + TABLE_MAPS_NAME + " (" +
                COLUMN_MAPS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MAPS_NAME + " TEXT, " +
                COLUMN_MAPS_IMAGE_PATH + " TEXT," +
                ");";

        db.execSQL(SQL_CREATE_TABLE_MAPS);
        Log.d("MAPS IS CREATED", SQL_CREATE_TABLE_MAPS);

        final String SQL_CREATE_TABLE_BEACONS = "CREATE TABLE" + TABLE_BEACONS_NAME + " (" +
                COLUMN_BEACONS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_BEACONS_NAME + " TEXT, " +
                COLUMN_BEACONS_MAC_ADDRESS + " TEXT, " +
                COLUMN_BEACONS_APSIS + " REAL, " +
                COLUMN_BEACONS_ORDINAT + " REAL, " +
                COLUMN_BEACONS_MAP_ID + " INTEGER, " +
                // foreign key beacons_map_id references map_id
                "FOREIGN KEY(" + COLUMN_BEACONS_MAP_ID + ") " + "REFERENCES " +
                    TABLE_MAPS_NAME + "(" + COLUMN_MAPS_ID + ")" +
                ");";

        db.execSQL(SQL_CREATE_TABLE_BEACONS);
        Log.d("BEACONS IS CREATED", SQL_CREATE_TABLE_BEACONS);

        final String SQL_CREATE_TABLE_PLACES = "CREATE TABLE" + TABLE_PLACES_NAME + " (" +
                COLUMN_PLACE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PLACE_NAME + " TEXT, " +
                COLUMN_PLACE_APSIS + " REAL, " +
                COLUMN_PLACE_ORDINAT + " REAL, " +
                COLUMN_PLACE_MAP_ID + " INTEGER, " +
                // foreign key beacons_map_id references map_id
                "FOREIGN KEY(" + COLUMN_PLACE_MAP_ID + ") " + "REFERENCES " +
                    TABLE_MAPS_NAME + "(" + COLUMN_MAPS_ID + ")" +
                ");";

        db.execSQL(SQL_CREATE_TABLE_PLACES);
        Log.d("PLACES IS CREATED", SQL_CREATE_TABLE_PLACES);

        final String SQL_CREATE_TABLE_BEACON_MEASURE = "CREATE TABLE" + TABLE_BEACON_MEASURE_NAME + " (" +
                COLUMN_BEACON_MEASURE_BEACON_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_BEACON_MEASURE_PLACE_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_BEACON_MEASURE_POWER + " REAL, " +
                // foreign key beacons_id references map_id
                "FOREIGN KEY(" + COLUMN_BEACON_MEASURE_BEACON_ID + ") " + "REFERENCES " +
                    TABLE_BEACONS_NAME + "(" + COLUMN_BEACONS_ID + "), " +
                "FOREIGN KEY(" + COLUMN_BEACON_MEASURE_PLACE_ID + ") " + "REFERENCES " +
                TABLE_PLACES_NAME + "(" + COLUMN_PLACE_ID + ")" +
                ");";

        db.execSQL(SQL_CREATE_TABLE_BEACON_MEASURE);
        Log.d("MEASURE IS CREATED", SQL_CREATE_TABLE_BEACON_MEASURE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MAPS_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BEACONS_NAME);
        onCreate(db);
    }



    public long insertMap(String name, String imgPath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MAPS_NAME, name);
        values.put(COLUMN_MAPS_IMAGE_PATH, imgPath);
        long id = db.insert(TABLE_MAPS_NAME, null, values);
        db.close();
        return id;
    }

    public long insertBeacon(String name, String macAddress, float apsis, float ordinat, long mapId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BEACONS_NAME, name);
        values.put(COLUMN_BEACONS_MAC_ADDRESS, macAddress);
        values.put(COLUMN_BEACONS_APSIS, apsis);
        values.put(COLUMN_BEACONS_ORDINAT, ordinat);
        values.put(COLUMN_BEACONS_MAP_ID, mapId);
        long id = db.insert(TABLE_BEACONS_NAME, null, values);
        db.close();
        return id;
    }

    public long insertPlace(String name, float apsis, float ordinat, long mapId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLACE_NAME, name);
        values.put(COLUMN_PLACE_APSIS, apsis);
        values.put(COLUMN_PLACE_ORDINAT, ordinat);
        values.put(COLUMN_PLACE_MAP_ID, mapId);
        long id = db.insert(TABLE_PLACES_NAME, null, values);
        db.close();
        return id;
    }


}
