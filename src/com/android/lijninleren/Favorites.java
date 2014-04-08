package com.android.lijninleren;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Favorites extends Activity
{
	List<Map<String,String>> planetsList = new ArrayList<Map<String,String>>();

	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initList();
		
		ListView lv = (ListView) findViewById(R.id.listView);
		SimpleAdapter simpleAdpt = new SimpleAdapter(this, planetsList, android.R.layout.simple_list_item_1, new String[] {"planet"}, new int[] {android.R.id.text1});
		
		lv.setAdapter(simpleAdpt);
	}
		
	private void initList()
		{
			planetsList.add(createPlanet("planet", "Mercury"));
			planetsList.add(createPlanet("planet", "Jupiter"));
		}
		
	private HashMap<String, String> createPlanet(String key, String name)
	{
		HashMap<String, String> planet = new HashMap<String, String>();
		planet.put(key,name);
		return planet;
	}
	
	
	public class DatabaseHandler extends SQLiteOpenHelper
	{
		private static final int DATABASE_VERSION = 2;
		private static final String DATABASE_NAME = "ContactsManager";
		private static final String TABLE_CONTACTS = "contacts";
		private static final String KEY_ID ="id";
		private static final String KEY_NAME = "name";
		private static final String KEY_PH_NO="phone_number";
		private static final String KEY_EMAIL="email";
		
		public DatabaseHandler(Context context)
		{
			super( context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
	public void onCreate(SQLiteDatabase db)
		{
			String CREATE_CONTACTS_TABLE= "CREATE TABLE	" + TABLE_CONTACTS + "( "
			+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"

        	+ KEY_PH_NO + " TEXT," + KEY_EMAIL + " TEXT" + ")";

			db.execSQL(CREATE_CONTACTS_TABLE);
		}
		
		@Override
	 public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	        // TODO Auto-generated method stub

	        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

	        onCreate(db);

	    }
	}
	/*
	public ArrayList<String> FavoritesList() {

	    ArrayList<String> list1 = new ArrayList<String>();
	    Cursor cursor = this.db.query(TABLE_NAME, null, null, null, null, null,
	            "duration desc");

	    if (cursor.moveToFirst()) {
	        do {                
	            //if (cursor.getString(2) != "") {

	                cdObj = new CallData();

	                list1.add(cursor.getString(2));
	                list1.add(cursor.getString(4));
	                list1.add(cursor.getString(5));
	                list1.add(cursor.getString(6));                     

	        } while (cursor.moveToNext());
	    }
	    if (cursor != null && !cursor.isClosed()) {
	        cursor.close();
	    }
	    return list1;
	    */
}



