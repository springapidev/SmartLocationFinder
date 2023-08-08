package com.coderbd.smartlocationfinder.category;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

    public class CategoryDatabaseHandler extends SQLiteOpenHelper {
        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "bmi";
        private static final String TABLE_SMF_CATEGORY_DATABASE = "smf_category";
        private static final String KEY_ID = "id";
        private static final String KEY_NAME = "name";


        public CategoryDatabaseHandler(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            //3rd argument to be passed is CursorFactory instance
        }

        // Creating Tables
        @Override
        public void onCreate(SQLiteDatabase db) {
            String CREATE_SMF_CATEGORY_DATA_TABLE = "CREATE TABLE " + TABLE_SMF_CATEGORY_DATABASE + "("
                    + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT )";
            db.execSQL(CREATE_SMF_CATEGORY_DATA_TABLE);
        }

        // Upgrading database
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Drop older table if existed
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SMF_CATEGORY_DATABASE);

            // Create tables again
            onCreate(db);
        }

        // code to add the new contact
        public void addCategory(Category category) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_NAME, category.getName());

            // Inserting Row
            db.insert(TABLE_SMF_CATEGORY_DATABASE, null, values);
            //2nd argument is String containing nullColumnHack
            db.close(); // Closing database connection
        }

        // code to get the single contact


        // code to get all contacts in a list view
        public List<Category> getAllCategories() {
            List<Category> categories = new ArrayList();
            // Select All Query
            String selectQuery = "SELECT  * FROM " + TABLE_SMF_CATEGORY_DATABASE;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Category category = new Category(Integer.parseInt(cursor.getString(0)),
                            cursor.getString(1));          // Adding Data to list
                    categories.add(category);
                } while (cursor.moveToNext());
            }

            // return contact list
            return categories;
        }

        // Deleting single Bmidata
        public void deleteData(Category bmiData) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_SMF_CATEGORY_DATABASE, KEY_ID + " = ?",
                    new String[] { String.valueOf(bmiData.getId()) });
            db.close();
        }

        // Getting Data Count
        public int getDataCount() {
            String countQuery = "SELECT  * FROM " + TABLE_SMF_CATEGORY_DATABASE;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            cursor.close();

            // return count
            return cursor.getCount();
        }

    }
