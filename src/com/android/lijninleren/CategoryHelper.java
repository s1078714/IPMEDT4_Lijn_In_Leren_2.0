package com.android.lijninleren;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CategoryHelper {
	private static final int DATABASE_VERSION = 2;
	private String DATABASE_NAME = "lijninleren.db";
	private String TABLE_NAME = "lijninleren_tabel";
	private String CATEGORY_COLUMN_ID = "id";
	private String CATEGORY_COLUMN_LEERLIJN = "leerlijn";
	private String CATEGORY_COLUMN_VAK = "vak";
	private String CATEGORY_COLUMN_ONDERDEEL = "onderdeel";
	private String CATEGORY_COLUMN_KERNDOEL = "kerndoel";
	private String CATEGORY_COLUMN_GROEP = "groep";
	private String CATEGORY_COLUMN_INFORMATIE_KIND = "informatie_kind";
	private String CATEGORY_COLUMN_INFORMATIE_LERAAR = "informatie_leraar";
	
    Category openHelper;
    private SQLiteDatabase database;

    public CategoryHelper(Context context){
        openHelper = new Category(context);
        database = openHelper.getWritableDatabase();
    }
    public void saveCategoryRecord(String id, String leerlijn, String vak, String onderdeel, String kerndoel, String groep, String informatie_kind, String informatie_leraar) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORY_COLUMN_ID, id);
        contentValues.put(CATEGORY_COLUMN_LEERLIJN, leerlijn);
        contentValues.put(CATEGORY_COLUMN_VAK, vak);
        contentValues.put(CATEGORY_COLUMN_ONDERDEEL, onderdeel);
        contentValues.put(CATEGORY_COLUMN_KERNDOEL, kerndoel);
        contentValues.put(CATEGORY_COLUMN_GROEP, groep);
        contentValues.put(CATEGORY_COLUMN_INFORMATIE_KIND, informatie_kind);
        contentValues.put(CATEGORY_COLUMN_INFORMATIE_LERAAR, informatie_leraar);
        database.insert(TABLE_NAME, null, contentValues);
        }
    public Cursor getTimeRecordList() {
        return database.rawQuery("select " + CATEGORY_COLUMN_LEERLIJN + " from " + TABLE_NAME, null);
        }
    private class Category extends SQLiteOpenHelper {

        public Category(Context context) {
            // TODO Auto-generated constructor stub
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            db.execSQL("CREATE TABLE " + TABLE_NAME + "( "
                    + CATEGORY_COLUMN_ID + " INTEGER PRIMARY KEY, "
                    + CATEGORY_COLUMN_LEERLIJN + " TEXT, "
                    + CATEGORY_COLUMN_VAK + " TEXT, "
                    + CATEGORY_COLUMN_ONDERDEEL + " TEXT, "
                    + CATEGORY_COLUMN_KERNDOEL + " TEXT, "
                    + CATEGORY_COLUMN_GROEP + " TEXT, "
                    + CATEGORY_COLUMN_INFORMATIE_KIND + " TEXT, "
                    + CATEGORY_COLUMN_INFORMATIE_LERAAR + " TEXT)" );

            Log.d("Create table: ", "aangemaakt!");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            db.execSQL("DROP TABLE IF EXISTS"+ TABLE_NAME);
            onCreate(db);
        }

    }
}