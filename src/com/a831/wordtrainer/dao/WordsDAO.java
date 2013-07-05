package com.a831.wordtrainer.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.a831.wordtrainer.database.TrainerDatabaseConstants;
import com.a831.wordtrainer.database.TrainerDatabaseHelper;

public class WordsDAO {
	private TrainerDatabaseHelper helper;
	
	private static final String TAG = "NotifierDAO";
	
	public WordsDAO(Context context){
		helper = new TrainerDatabaseHelper(context);
	}
	
	public void close(){
		helper.close();
	}
	
	public List<String> loadWords(){
		SQLiteDatabase db = helper.getReadableDatabase();
		
		Cursor cursor = db.rawQuery("select * from " + TrainerDatabaseConstants.WORDS_TABLE, new String[0]);
		
		List<String> words = new ArrayList<String>();
		
		if(cursor.moveToFirst()){
			while(!cursor.isAfterLast()){
				words.add(cursor.getString(1));
				cursor.moveToNext();
			}
		}
		
		cursor.close();
	
		return words;
	}
	
	public boolean replaceWords(List<String> words){
		if(clearWords() == -1){
			return false;
		}
		for(String word : words){
			if(saveWord(word) == -1){
				return false;
			}
		}
		return true;
	}

	private long saveWord(String word) {
		ContentValues values = new ContentValues();
		values.put(TrainerDatabaseConstants.WORD_COLUMN, word);
		
		return helper.getWritableDatabase().insert(TrainerDatabaseConstants.WORDS_TABLE, null, values);
	}

	private int clearWords() {
		return helper.getWritableDatabase().delete(TrainerDatabaseConstants.WORDS_TABLE, "", new String[0]);
	}
}
