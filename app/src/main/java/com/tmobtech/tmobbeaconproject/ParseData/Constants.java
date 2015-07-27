package com.tmobtech.tmobbeaconproject.ParseData;

/**
 * Created by ozberkcetin on 24/07/15.
 */
public class Constants {
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
}
