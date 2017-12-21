package com.timer.nurse.iheartbutton2;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.things.contrib.driver.button.Button;
import com.google.android.things.contrib.driver.button.ButtonInputDriver;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

	private static final String GPIO_BUTTON = "BCM21";
	private static final String TAG = MainActivity.class.getSimpleName();
	private TextView mStatus;
	private AudioManager mAudioManager;
	private ButtonInputDriver mButtonInputDriver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mStatus = findViewById(R.id.status);
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		final ImageView play = findViewById(R.id.play);
		final ImageView pause = findViewById(R.id.pause);
		try {
			mButtonInputDriver = new ButtonInputDriver(
					GPIO_BUTTON,
					Button.LogicState.PRESSED_WHEN_LOW,
					KeyEvent.KEYCODE_ENTER);
			mButtonInputDriver.register();
		} catch (IOException e) {
			mButtonInputDriver = null;
			Log.w(TAG, "Could not open GPIO pins", e);
		}


		play.setOnClickListener(v -> {
			sendMusicCommand(KeyEvent.KEYCODE_MEDIA_PLAY);
		});

		pause.setOnClickListener(v -> {
			sendMusicCommand(KeyEvent.KEYCODE_MEDIA_PAUSE);
		});
	}

	private void sendMusicCommand(final int command) {
		final KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, command);
		mAudioManager.dispatchMediaKeyEvent(event);
		mStatus.setText(KeyEvent.keyCodeToString(command) + " command sent " + getTimestamp());
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_ENTER) {
			Log.d(TAG, "button pressed");
			sendMusicCommand(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			mButtonInputDriver.close();
		} catch (IOException e) {
			Log.e(TAG, "button driver error", e);
		}
	}

	public String getTimestamp() {
		Calendar cal = Calendar.getInstance();
		Date currentLocalTime = cal.getTime();

		DateFormat date = new SimpleDateFormat("dd-MM-yyy HH:mm:ss z");

		return date.format(currentLocalTime);
	}
}
