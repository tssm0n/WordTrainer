package com.a831.wordtrainer.service;

import java.util.Date;
import java.util.List;
import java.util.Random;

import android.content.Context;

import com.a831.wordtrainer.dao.WordsDAO;

public class WordService {

	private String currentWord;
	
	private List<String> words = null;
	private Random random;
	
	public WordService(Context context){
		WordsDAO dao = new WordsDAO(context);
		words = dao.loadWords();
		
		random = new Random(new Date().getTime());
		
		nextWord();
	}
	
	public String getCurrentWord(){
		return currentWord;
	}
	
	public String nextWord(){
		if(words == null || words.size() == 0){
			currentWord = "...";
			return currentWord;
		}
		int index = random.nextInt(words.size());
		if(index >= words.size()){
			index = 0;
		}
		currentWord = words.get(index);
		return currentWord;
	}
	
	
}
