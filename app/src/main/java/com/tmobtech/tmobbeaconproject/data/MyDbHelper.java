package com.tmobtech.tmobbeaconproject.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by semih on 14.07.2015.
 */
public class MyDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 7;

    public static final String DATABASE_NAME = "beaconmap.db";
    public static final String TABLE_MAPS_NAME = "maps";
    public static final String TABLE_BEACONS_NAME = "beacons";
    public static final String TABLE_PLACES_NAME = "places";
    public static final String TABLE_BEACON_MEASURE_NAME = "beacon_measure";

    public static final String COLUMN_MAP_ID = "map_id";
    public static final String COLUMN_MAP_NAME = "map_name";
    public static final String COLUMN_MAP_IMAGE_PATH = "img_path";

    public static final String COLUMN_BEACON_ID = "beacon_id";
    public static final String COLUMN_BEACON_NAME = "beacon_name";
    public static final String COLUMN_BEACON_MAC_ADDRESS = "mac_address";
    public static final String COLUMN_BEACON_APSIS = "apsis";
    public static final String COLUMN_BEACON_ORDINAT = "ordinat";
    public static final String COLUMN_BEACON_MAP_ID = "beacons_map_id";

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
                COLUMN_MAP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MAP_NAME + " TEXT, " +
                COLUMN_MAP_IMAGE_PATH + " TEXT" +
                ");";

        db.execSQL(SQL_CREATE_TABLE_MAPS);
        Log.d("MAPS IS CREATED", SQL_CREATE_TABLE_MAPS);

        final String SQL_CREATE_TABLE_BEACONS = "CREATE TABLE " + TABLE_BEACONS_NAME + "(" +
                COLUMN_BEACON_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_BEACON_NAME + " TEXT, " +
                COLUMN_BEACON_MAC_ADDRESS + " TEXT, " +
                COLUMN_BEACON_APSIS + " REAL, " +
                COLUMN_BEACON_ORDINAT + " REAL, " +
                COLUMN_BEACON_MAP_ID + " INTEGER, " +
                // foreign key beacons_map_id references map_id
                "FOREIGN KEY(" + COLUMN_BEACON_MAP_ID + ") " + "REFERENCES " +
                TABLE_MAPS_NAME + "(" + COLUMN_MAP_ID + ")" + " ON DELETE CASCADE" +
                ");";

        db.execSQL(SQL_CREATE_TABLE_BEACONS);
        Log.d("BEACONS IS CREATED", SQL_CREATE_TABLE_BEACONS);

        final String SQL_CREATE_TABLE_PLACES = "CREATE TABLE " + TABLE_PLACES_NAME + "(" +
                COLUMN_PLACE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PLACE_NAME + " TEXT, " +
                COLUMN_PLACE_APSIS + " REAL, " +
                COLUMN_PLACE_ORDINAT + " REAL, " +
                COLUMN_PLACE_MAP_ID + " INTEGER, " +
                // foreign key beacons_map_id references map_id
                "FOREIGN KEY(" + COLUMN_PLACE_MAP_ID + ") " + "REFERENCES " +
                TABLE_MAPS_NAME + "(" + COLUMN_MAP_ID + ")" + " ON DELETE CASCADE" +
                ");";

        db.execSQL(SQL_CREATE_TABLE_PLACES);
        Log.d("PLACES IS CREATED", SQL_CREATE_TABLE_PLACES);

        final String SQL_CREATE_TABLE_BEACON_MEASURE = "CREATE TABLE " + TABLE_BEACON_MEASURE_NAME + "(" +
                COLUMN_BEACON_MEASURE_BEACON_ID + " INTEGER, " +
                COLUMN_BEACON_MEASURE_PLACE_ID + " INTEGER, " +
                COLUMN_BEACON_MEASURE_POWER + " REAL, " +
                // composite primary key
                "PRIMARY KEY(" + COLUMN_BEACON_MEASURE_BEACON_ID + ", " + COLUMN_BEACON_MEASURE_PLACE_ID + "), " +
                // foreign key beacons_id references map_id
                "FOREIGN KEY(" + COLUMN_BEACON_MEASURE_BEACON_ID + ") " + "REFERENCES " +
                TABLE_BEACONS_NAME + "(" + COLUMN_BEACON_ID + ") " + " ON DELETE CASCADE, " +
                "FOREIGN KEY(" + COLUMN_BEACON_MEASURE_PLACE_ID + ") " + "REFERENCES " +
                TABLE_PLACES_NAME + "(" + COLUMN_PLACE_ID + ")" + " ON DELETE CASCADE" +
                ");";

        db.execSQL(SQL_CREATE_TABLE_BEACON_MEASURE);
        Log.d("MEASURE IS CREATED", SQL_CREATE_TABLE_BEACON_MEASURE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MAPS_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BEACONS_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACES_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BEACON_MEASURE_NAME);
        onCreate(db);
    }


    public long insertMap(String name, String imgPath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MAP_NAME, name);
        values.put(COLUMN_MAP_IMAGE_PATH, imgPath);
        return db.insert(TABLE_MAPS_NAME, null, values);
    }

    public int deleteMap(long mapId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_MAP_ID + " = ?";
        String[] whereArgs = new String[]{
                "" + mapId
        };
        return db.delete(TABLE_MAPS_NAME, whereClause, whereArgs);
    }

    public long insertBeacon(String name, String macAddress, float apsis, float ordinat, long mapId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BEACON_NAME, name);
        values.put(COLUMN_BEACON_MAC_ADDRESS, macAddress);
        values.put(COLUMN_BEACON_APSIS, apsis);
        values.put(COLUMN_BEACON_ORDINAT, ordinat);
        values.put(COLUMN_BEACON_MAP_ID, mapId);
        return db.insert(TABLE_BEACONS_NAME, null, values);
    }

    public int deleteBeacon(long beaconId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_BEACON_ID + " = ?";
        String[] whereArgs = new String[]{
                "" + beaconId
        };
        return db.delete(TABLE_BEACONS_NAME, whereClause, whereArgs);
    }

    public long insertPlace(String name, float apsis, float ordinat, long mapId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLACE_NAME, name);
        values.put(COLUMN_PLACE_APSIS, apsis);
        values.put(COLUMN_PLACE_ORDINAT, ordinat);
        values.put(COLUMN_PLACE_MAP_ID, mapId);
        return db.insert(TABLE_PLACES_NAME, null, values);
    }

    public int deletePlace(String placeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_PLACE_ID + " = ?";
        String[] whereArgs = new String[]{
                "" + placeId
        };
        return db.delete(TABLE_PLACES_NAME, whereClause, whereArgs);
    }

    public long insertBeaconMeasure(long beaconId, String placeId, double power) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BEACON_MEASURE_BEACON_ID, beaconId);
        values.put(COLUMN_BEACON_MEASURE_PLACE_ID, placeId);
        values.put(COLUMN_BEACON_MEASURE_POWER, power);
        return db.insert(TABLE_BEACON_MEASURE_NAME, null, values);
    }

    public Cursor getMaps() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_MAPS_NAME, null, null, null, null, null, null, null);
    }

    public Cursor getBeaconFromId(long beaconId) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = new String[]{
                COLUMN_BEACON_NAME,
                COLUMN_BEACON_MAC_ADDRESS,
                COLUMN_BEACON_APSIS,
                COLUMN_BEACON_ORDINAT,
                COLUMN_BEACON_MAP_ID
        };
        String selection = COLUMN_BEACON_ID + "= ?";
        String[] selectionArgs = new String[]{
                "" + beaconId
        };

        return db.query(TABLE_BEACONS_NAME, columns, selection, selectionArgs, null, null, null);
    }

    public Cursor getMapFromId(long mapId) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = new String[]{
                COLUMN_MAP_NAME,
                COLUMN_MAP_IMAGE_PATH
        };
        String selection = COLUMN_MAP_ID + " = ?";
        String[] selectionArgs = new String[]{
                "" + mapId
        };
        return db.query(TABLE_MAPS_NAME,
                columns,
                selection,
                selectionArgs,
                null, null, null, null);
    }

    public Cursor getBeaconsAtMap(long mapId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_BEACON_MAP_ID + "=?";
        String[] selectionArgs = new String[]{"" + mapId};
        String[] columns = new String[]{
                COLUMN_BEACON_ID,
                COLUMN_BEACON_NAME,
                COLUMN_BEACON_MAC_ADDRESS,
                COLUMN_BEACON_APSIS,
                COLUMN_BEACON_ORDINAT
        };
        return db.query(TABLE_BEACONS_NAME,
                columns,
                selection,
                selectionArgs, null, null, null, null);
    }

    public Cursor getPlacesAtMap(long mapId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_PLACE_MAP_ID + "=?";
        String[] selectionArgs = new String[]{"" + mapId};
        String[] columns = new String[]{
                COLUMN_PLACE_ID,
                COLUMN_PLACE_NAME,
                COLUMN_PLACE_APSIS,
                COLUMN_PLACE_ORDINAT
        };
        return db.query(TABLE_PLACES_NAME,
                columns,
                selection,
                selectionArgs, null, null, null, null);
    }

    public Cursor getMeasurePowersForPlace(String placeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_BEACON_MEASURE_PLACE_ID + "=?";
        String[] selectionArgs = new String[]{"" + placeId};
        String[] columns = new String[]{
                COLUMN_BEACON_MEASURE_BEACON_ID,
                COLUMN_BEACON_MEASURE_POWER
        };
        return db.query(TABLE_BEACON_MEASURE_NAME,
                columns,
                selection,
                selectionArgs, null, null, null, null);
    }

    public int updateMapName(long mapId, String newMapName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_MAP_ID + " = ?";
        String[] whereArgs = new String[]{
                "" + mapId
        };
        ContentValues values = new ContentValues();
        values.put(COLUMN_MAP_NAME, newMapName);
        return db.update(TABLE_MAPS_NAME, values, whereClause, whereArgs);
    }

    public int updateBeaconName(long beaconId, String newBeaconName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_BEACON_ID + " = ?";
        String[] whereArgs = new String[]{
                "" + beaconId
        };
        ContentValues values = new ContentValues();
        values.put(COLUMN_BEACON_NAME, newBeaconName);
        return db.update(TABLE_BEACONS_NAME, values, whereClause, whereArgs);
    }

    public int updateBeaconMacAddress(long beaconId, String newBeaconMacAddress) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_BEACON_ID + " = ?";
        String[] whereArgs = new String[]{
                "" + beaconId
        };
        ContentValues values = new ContentValues();
        values.put(COLUMN_BEACON_MAC_ADDRESS, newBeaconMacAddress);
        return db.update(TABLE_BEACONS_NAME, values, whereClause, whereArgs);
    }

    public int updateBeaconCoordinate(long beaconId, float newApsis, float newOrdinat) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_BEACON_ID + " = ?";
        String[] whereArgs = new String[]{
                "" + beaconId
        };
        ContentValues values = new ContentValues();
        values.put(COLUMN_BEACON_APSIS, newApsis);
        values.put(COLUMN_BEACON_ORDINAT, newOrdinat);
        return db.update(TABLE_BEACONS_NAME, values, whereClause, whereArgs);
    }

    public int updatePlaceName(String placeId, String newPlaceName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_PLACE_ID + " = ?";
        String[] whereArgs = new String[]{
                "" + placeId
        };
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLACE_NAME, newPlaceName);
        return db.update(TABLE_PLACES_NAME, values, whereClause, whereArgs);
    }

    public int updatePlaceCoordinate(long placeId, float newApsis, float newOrdinat) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_BEACON_ID + " = ?";
        String[] whereArgs = new String[]{
                "" + placeId
        };
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLACE_APSIS, newApsis);
        values.put(COLUMN_PLACE_ORDINAT, newOrdinat);
        return db.update(TABLE_PLACES_NAME, values, whereClause, whereArgs);
    }

    public int updateBeaconMeasured(long beaconId, String placeId, double power) {

        //String query="Update from "+TABLE_BEACON_MEASURE_NAME +"Set";
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_BEACON_MEASURE_BEACON_ID + "=? AND " +
                COLUMN_BEACON_MEASURE_PLACE_ID + "=?";
        String[] whereArgs = new String[]{
                "" + beaconId, "" + placeId
        };
        ContentValues values = new ContentValues();
        values.put(COLUMN_BEACON_MEASURE_POWER, power);
        return db.update(TABLE_BEACON_MEASURE_NAME, values, whereClause, whereArgs);
    }

    public int deleteBeaconMeasure(long beaconId, String placeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_BEACON_MEASURE_BEACON_ID + "=? AND " +
                COLUMN_BEACON_MEASURE_PLACE_ID + "=?";
        String[] whereArgs = new String[]{
                "" + beaconId, "" + placeId
        };
        return db.delete(TABLE_BEACON_MEASURE_NAME, whereClause, whereArgs);
    }
}
