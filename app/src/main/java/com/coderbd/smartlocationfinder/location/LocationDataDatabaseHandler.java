package com.coderbd.smartlocationfinder.location;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.coderbd.smartlocationfinder.category.Category;

import java.util.ArrayList;
import java.util.List;

public class LocationDataDatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "slf";
    private static final String TABLE_SLF_LOCATION_DATABASE = "slf_location";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_CATEGORY_NAME = "c_name";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LON = "lon";


    public LocationDataDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SMF_CATEGORY_DATA_TABLE = "CREATE TABLE " + TABLE_SLF_LOCATION_DATABASE + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_CATEGORY_NAME + " TEXT," +  KEY_LAT + " TEXT,"+  KEY_LON + " TEXT" + ")";
        db.execSQL(CREATE_SMF_CATEGORY_DATA_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SLF_LOCATION_DATABASE);

        // Create tables again
        onCreate(db);
    }

    // code to add the new contact
    public void addCategory(LocationData locationData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, locationData.getName());
        values.put(KEY_CATEGORY_NAME, locationData.getCname());
        values.put(KEY_LAT, locationData.getLat());
        values.put(KEY_LON, locationData.getLon());


        // Inserting Row
        db.insert(TABLE_SLF_LOCATION_DATABASE, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    // code to get the single contact


    // code to get all contacts in a list view
    public List<LocationData> getAllLocations() {
        List<LocationData> locationDataList = new ArrayList();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SLF_LOCATION_DATABASE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                LocationData locationData = new LocationData(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4));          // Adding Data to list
                locationDataList.add(locationData);
            } while (cursor.moveToNext());
        }

        // return contact list
        return locationDataList;
    }

    // Deleting single Bmidata
    public void deleteData(Category bmiData) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SLF_LOCATION_DATABASE, KEY_ID + " = ?",
                new String[] { String.valueOf(bmiData.getId()) });
        db.close();
    }

    // Getting Data Count
    public int getDataCount() {
        String countQuery = "SELECT  * FROM " + TABLE_SLF_LOCATION_DATABASE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}
