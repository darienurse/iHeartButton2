package com.timer.nurse.iheartbutton2;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

	private static final String TAG = MainActivity.class.getSimpleName();
	private static final String TARGET_MEDIA_APPLICATION = "com.clearchannel.iheartradio.controller";
	private TextView mStatus;
	private AudioManager mAudioManager;
	private Intent mServiceIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final MediaControllerService mediaControllerService = new MediaControllerService();
		mServiceIntent = new Intent(this, MediaControllerService.class);
		if (!isMyServiceRunning(mediaControllerService.getClass())) {
			startService(mServiceIntent);
		}

		mStatus = findViewById(R.id.status);
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		final ImageView play = findViewById(R.id.play);
		final ImageView pause = findViewById(R.id.pause);
		play.setOnClickListener(v -> {
			sendMediaCommand(KeyEvent.KEYCODE_MEDIA_PLAY);
		});

		pause.setOnClickListener(v -> {
			sendMediaCommand(KeyEvent.KEYCODE_MEDIA_PAUSE);
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		final Intent launchIntent = getPackageManager().getLaunchIntentForPackage(TARGET_MEDIA_APPLICATION);
		if (launchIntent != null) {
			//null pointer check in case package name was not found
			startActivity(launchIntent);
		} else {
			Toast.makeText(this, String.format("Target Application \"%s\" not found", TARGET_MEDIA_APPLICATION), Toast.LENGTH_LONG).show();
		}
	}

	private void sendMediaCommand(final int keyEventKeyCode) {
		final KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, keyEventKeyCode);
		mAudioManager.dispatchMediaKeyEvent(event);
		displayMessage(keyEventKeyCode);
	}

	private void displayMessage(final int keyEventKeyCode) {
		mStatus.setText(String.format("%s KeyCode sent %s", KeyEvent.keyCodeToString(keyEventKeyCode), getTimestamp()));
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopService(mServiceIntent);
	}

	private boolean isMyServiceRunning(Class<?> serviceClass) {
		final ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		if (manager != null) {
			for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
				if (serviceClass.getName().equals(service.service.getClassName())) {
					Log.i("isMyServiceRunning?", true + "");
					return true;
				}
			}
		}
		Log.i("isMyServiceRunning?", false + "");
		return false;
	}

	public String getTimestamp() {
		final Date currentLocalTime = Calendar.getInstance().getTime();
		final DateFormat date = new SimpleDateFormat("dd-MM-yyy HH:mm:ss z");
		return date.format(currentLocalTime);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE) {
			Log.d(TAG, "button pressed");
			displayMessage(keyCode);
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
}
