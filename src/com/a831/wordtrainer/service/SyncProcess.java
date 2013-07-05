package com.a831.wordtrainer.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.a831.wordtrainer.WordTrainerActivity;
import com.a831.wordtrainer.dao.WordsDAO;

public class SyncProcess {
	private static final String TAG = "SyncService";
	
	private ProgressDialog progress;
	private WordsDAO dao;
	private WordTrainerActivity activity;
	
	public SyncProcess(WordTrainerActivity activity){
		this.activity = activity;
	}
	
	public void checkForUpdates(WordTrainerActivity activity) throws IOException {
		Log.d(TAG, "Syncing");
		this.activity = activity;
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
		
		String sourceURL = prefs.getString("url", "");
		URL url = new URL(sourceURL);

		progress = new ProgressDialog(activity);
		progress.setTitle("Syncing");
		progress.setMessage("Syncing. Please wait...");
		progress.show();
		
		if(dao == null){
			dao = new WordsDAO(activity);
		}
		
		RetrieveWords asyncWords = new RetrieveWords();
		asyncWords.execute(url);
	}

	private void saveResult(List<String> result) {
		progress.dismiss();
		if(!dao.replaceWords(result)){
			// TODO: Display an error
		}
		activity.wordlistUpdated();
		
	}


	
	private class RetrieveWords extends AsyncTask<URL, Integer, List<String>>{

		@Override
		protected List<String> doInBackground(URL... url) {
			
		   	try {
				InputStream inputStream = url[0].openConnection().getInputStream();
				List<String> result = parse(inputStream);
				inputStream.close();
				return result;
			} catch (Exception e) {
				Log.w(TAG, e.getMessage());
				return null;
			}
		}
		

		@Override
		protected void onPostExecute(List<String> result) {
			saveResult(result);
		}


		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}



		private List<String> parse(InputStream inputStream) throws IOException {
			List<String> result = new LinkedList<String>();
			
			InputStreamReader isr = new InputStreamReader(inputStream);
			BufferedReader reader = new BufferedReader(isr);
			try {
				String next = null;
				do {
					next = reader.readLine();
					if(next != null && !"".equals(next))
					result.add(next);
				} while(next != null);
			} catch (IOException e) {
				Log.e(TAG, "Error parsing inputstream");
				throw e;
			} finally {
				try {
					reader.close();
					isr.close();
				} catch (IOException e) {
					Log.e(TAG, "Error closing reader");
					throw e;
				}
			}
			
			Log.d(TAG, "Found: " + result.size() + " Words");
			return result;
		}		
		
	}
}
