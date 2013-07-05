package com.a831.wordtrainer.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class TrainerDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "trainer.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TAG = "TrainerDatabaseHelper";	
	
	public TrainerDatabaseHelper(Context context, String name,
			CursorFactory factory, int version,
			DatabaseErrorHandler errorHandler) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public TrainerDatabaseHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
    public TrainerDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }	

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TrainerDatabaseConstants.PARAMETERS_TABLE + "(" + 
				TrainerDatabaseConstants._ID + " INTEGER PRIMARY KEY, " +
				TrainerDatabaseConstants.VALUE_COLUMN + " TEXT" +
				")");
				

		db.execSQL("CREATE TABLE " + TrainerDatabaseConstants.WORDS_TABLE + " (" + 
				TrainerDatabaseConstants._ID + " INTEGER PRIMARY KEY autoincrement, " +
				TrainerDatabaseConstants.WORD_COLUMN + " TEXT" +
		")");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if(oldVersion != newVersion){
	    	db.execSQL("DROP TABLE IF EXISTS " + TrainerDatabaseConstants.PARAMETERS_TABLE);
	    	db.execSQL("DROP TABLE IF EXISTS " + TrainerDatabaseConstants.WORDS_TABLE);
	    	onCreate(db);
		}
	}

}
