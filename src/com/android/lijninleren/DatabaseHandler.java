package com.android.lijninleren;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {
	 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    public static final String DATABASE_NAME = "favoritesManager";
 
    // Contacts table name
    private static final String TABLE_FAVORITES = "favorites";
 
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITEL = "titel";
 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_FAVORITES + "("
                + KEY_ID + " TEXT PRIMARY KEY," + KEY_TITEL + " TEXT);";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
 
        // Create tables again
        onCreate(db);
    }
    
    // Adding new contact
    void addFavoriet(Favoriet favoriet) {
    	SQLiteDatabase db = this.getWritableDatabase();

    	ContentValues values = new ContentValues();

    	String FAV_CHECKER = "SELECT * FROM " + TABLE_FAVORITES + " WHERE " + KEY_ID + " = " + favoriet.getID().toString() + ";";
    	Cursor cursor = db.rawQuery(FAV_CHECKER, null);
//    	Log.d("database query addContact", cursor.toString() );
    	Log.d("Favoriet", "toevoegen");
    	values.put(KEY_ID, favoriet.getID()); // Favoriet id
    	values.put(KEY_TITEL, favoriet.getTitel()); // Favoriet titel/naam

    	// Inserting Row
    	try{
    	db.insert(TABLE_FAVORITES, null, values);
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    		deleteFavoriet(favoriet);
    	}
    	db.close(); // Closing database connection

    }
    
    

 // Getting All Contacts
    public List<Favoriet> getAllFavorites() {
        List<Favoriet> favoriteList = new ArrayList<Favoriet>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FAVORITES;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Favoriet favoriet = new Favoriet();
                favoriet.setID(cursor.getString(0));
                favoriet.setTitel(cursor.getString(1));
                // Adding contact to list
                favoriteList.add(favoriet);
            } while (cursor.moveToNext());
        }
        Log.d("favoriteList (DatabaseHandler)", favoriteList.toString() );
        // return contact list
        return favoriteList;
    }
    
    // Deleting single contact
    public void deleteFavoriet(Favoriet favoriet) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITES, KEY_ID + " = ?",
                new String[] { String.valueOf(favoriet.getID()) });
        db.close();
    }
}