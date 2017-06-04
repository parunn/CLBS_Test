package test.clbs.android.example.com.clbsandroidtest.model;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

/**
 * Created by parunpichaiwong on 5/27/2017 AD.
 */

public class LocationDBAdapter {

    //For logging:
    private static final String TAG = "LocationDatabaseBAdapter";

    //DB Fields
    public static final String LOCATION_ROW_ID = "_id";
    public static final int COL_L_rowId = 0;

    //Setup Database fields
    public static final String LOCATION_ID = "location_id";
    public static final String LOCATION_NAME = "location_name";
    public static final String LOCATION_ICON = "location_icon";
    public static final String LOCATION_PHOTO = "location_photo";
    public static final String LOCATION_REFERENCE = "location_reference";
    public static final String LOCATION_SCOPE = "location_scope";
    public static final String LOCATION_VICINITY = "location_vicinity";
    public static final String LOCATION_LATITUDE = "location_latitude";
    public static final String LOCATION_LONGITUDE = "location_longitude";
    public static final String LOCATION_DISTANCE = "location_distance";

    //Setup Column field numbers (0 = LOCATION_ROW_ID, 1 = LOCATION_ID, 2 = LOCATION_NAME...)
    public static final int COL_LOCATION_ID = 1;
    public static final int COL_LOCATION_NAME = 2;
    public static final int COL_LOCATION_ICON = 3;
    public static final int COL_LOCATION_PHOTO = 4;
    public static final int COL_LOCATION_REFERENCE = 5;
    public static final int COL_LOCATION_SCOPE = 6;
    public static final int COL_LOCATION_VICINITY = 7;
    public static final int COL_LOCATION_LATITUDE = 8;
    public static final int COL_LOCATION_LONGITUDE = 9;
    public static final int COL_LOCATION_DISTANCE = 10;

    //Set Key
    public static final String[] ALL_KEYS = new String[] {
            LOCATION_ROW_ID,
            LOCATION_ID,
            LOCATION_NAME,
            LOCATION_ICON,
            LOCATION_PHOTO,
            LOCATION_REFERENCE,
            LOCATION_SCOPE,
            LOCATION_VICINITY,
            LOCATION_LATITUDE,
            LOCATION_LONGITUDE,
            LOCATION_DISTANCE
    };

    //DB name
    public static final String DATABASE_NAME = "locationDatabase";

    //Table name
    public static final String DATABASE_TABLE = "locationTable";

    //Track DB version
    public static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE_SQL =
            "create table " + DATABASE_TABLE
                    + " (" + LOCATION_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "

                    + LOCATION_ID	+ " VARCHAR(30) NOT NULL , "	//Set Length to 30 and UNIQUE for prevent recursive
                    + LOCATION_NAME + " VARCHAR(50) NOT NULL , "		//Set Length to 50
                    + LOCATION_ICON + " VARCHAR(50) NOT NULL, "		//Set Length to 50
                    + LOCATION_PHOTO + " VARCHAR(50) NOT NULL, "		//Set Length to 50
                    + LOCATION_REFERENCE + " VARCHAR(30) NOT NULL, "		//Set Length to 50
                    + LOCATION_SCOPE + " VARCHAR(30) NOT NULL, "		//Set Length to 30
                    + LOCATION_VICINITY + " VARCHAR(30) NOT NULL, "		//Set Length to 30
                    + LOCATION_LATITUDE + " DOUBLE NOT NULL, "
                    + LOCATION_LONGITUDE + " DOUBLE NOT NULL, "
                    + LOCATION_DISTANCE 		+ " DOUBLE NOT NULL "
                    + ");";


    // Context of application who uses us.
    private final Context context;

    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;

    //Application Context
    public LocationDBAdapter(Context ctx) {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }

    // Open Connection.
    public LocationDBAdapter open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }

    // Close Connection.
    public void close() {
        myDBHelper.close();
    }


    //Insertion
    public long insertLocationData(String location_id, String location_name, String location_icon, String location_photo,
                                   String location_reference, String location_scope, String location_vicinity,
                                   double location_latitude, double location_longitude, double location_distance) {

        // Create row's data:
        ContentValues initialValues = new ContentValues();

        initialValues.put(LOCATION_ID, location_id);
        initialValues.put(LOCATION_NAME, location_name);
        initialValues.put(LOCATION_ICON, location_icon);
        initialValues.put(LOCATION_PHOTO, location_photo);
        initialValues.put(LOCATION_REFERENCE, location_reference);
        initialValues.put(LOCATION_SCOPE, location_scope);
        initialValues.put(LOCATION_VICINITY, location_vicinity);
        initialValues.put(LOCATION_LATITUDE, location_latitude);
        initialValues.put(LOCATION_LONGITUDE, location_longitude);
        initialValues.put(LOCATION_DISTANCE, location_distance);

        // Insert it into the database.
        return db.insert(DATABASE_TABLE, null, initialValues);
    }


    //Select specific Location information by Latitude
    public Cursor getLocationDataByLatitude(String location_latitude) {
        // TODO Auto-generated method stub

        try {

            //Use cursor to get UserID as index
            Cursor cursor = db.query(DATABASE_TABLE, new String[] { "*" },"location_latitude=?",
                    new String[] { String.valueOf(location_latitude) }, null, null, null, null);

            return cursor;

        } catch (Exception e) {
            return null;
        }

    }

    //Select specific Location information by Longitude
    public Cursor getLocationDataByLongitude(String location_longitude) {
        // TODO Auto-generated method stub

        try {

            //Use cursor to get UserID as index
            Cursor cursor = db.query(DATABASE_TABLE, new String[] { "*" },"location_longitude=?",
                    new String[] { String.valueOf(location_longitude) }, null, null, null, null);

            return cursor;

        } catch (Exception e) {
            return null;
        }

    }



    //Checking specific Location by Location Lat, Lng
    public Cursor checkLocationDataByLatLng(String location_latitude, String location_longitude) {
        // TODO Auto-generated method stub

        try {

            Cursor cursor = db.query(DATABASE_TABLE, new String[] { "*" },
                    "location_latitude=? AND location_longitude=?",new String[] { String.valueOf(location_latitude), String.valueOf(location_longitude)}
                    , null, null, null, null);

            return cursor;

        } catch (Exception e) {
            return null;
        }

    }


    //Private Helper Classes
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {
            _db.execSQL(DATABASE_CREATE_SQL);
            Log.e(TAG, "Filter table was created");
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Update database to"+newVersion);

            // Destroy old database:
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);

            // Recreate new database:
            onCreate(_db);
        }
    }
}
