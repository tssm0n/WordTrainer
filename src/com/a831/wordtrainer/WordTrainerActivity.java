package com.a831.wordtrainer;

import java.io.IOException;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.a831.wordtrainer.service.SyncProcess;
import com.a831.wordtrainer.service.WordService;

public class WordTrainerActivity extends Activity implements
		TextToSpeech.OnInitListener {

	private static final String TAG = "WordTrainerActivity";
	
	private TextToSpeech tts;
	private WordService wordService;
	private SyncProcess syncService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_word_trainer);

		setVolumeControlStream(AudioManager.STREAM_MUSIC);

		wordService = new WordService(this);
		syncService = new SyncProcess(this);
		tts = new TextToSpeech(this, this);
		
		updateText(wordService.getCurrentWord());
		
		Button nextButton = (Button) findViewById(R.id.nextButton);
		nextButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				updateText(wordService.nextWord());
				
			}
		});
		
		TextView textView = (TextView) findViewById(R.id.fullscreen_content);
		textView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
		        tts.speak(wordService.getCurrentWord(), TextToSpeech.QUEUE_FLUSH, null);
			}
		});		
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}
	
	protected void updateText(String text){
		TextView view = (TextView) findViewById(R.id.fullscreen_content);
		view.setText(text.toUpperCase(Locale.US));
	}

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
 
    @Override
    public void onInit(int status) {
 
        if (status == TextToSpeech.SUCCESS) {
 
            int result = tts.setLanguage(Locale.US);
 
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(TAG, "This Language is not supported");
                tts = null;
             // TODO: Display an error
            } 
            Log.d(TAG, "TTS initialized");
 
        } else {
            Log.e(TAG, "Initilization Failed!");
            tts = null;
            // TODO: Display an error
        }
 
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.settingsmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case R.id.settingsItem:
				Intent intent = new Intent(this, SettingsActivity.class);
				startActivity(intent);
				return true;
			case R.id.syncItem:
			try {
				syncService.checkForUpdates(this);
			} catch (IOException e) {
				AlertDialog.Builder alert = new AlertDialog.Builder(this);

				alert.setTitle("Error Syncing");
				alert.setMessage("An Error Occurred While Syncing");
				
				alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
					}

				});
				

			}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	public void wordlistUpdated() {
		wordService = new WordService(this);
		updateText(wordService.getCurrentWord());
	}    
}
