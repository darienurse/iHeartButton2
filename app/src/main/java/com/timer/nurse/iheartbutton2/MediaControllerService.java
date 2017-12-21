package com.timer.nurse.iheartbutton2;

import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.util.Log;
import android.view.KeyEvent;

import com.google.android.things.contrib.driver.button.Button;
import com.google.android.things.contrib.driver.button.ButtonInputDriver;

import java.io.IOException;

/**
 * Created by Darien on 12/20/2017.
 */

public class MediaControllerService extends InputMethodService {
	private static final String TAG = MediaControllerService.class.getSimpleName();
	private static final String GPIO_BUTTON = "BCM21";
	private ButtonInputDriver mButtonInputDriver;

	@Override
	public void onCreate() {
		super.onCreate();
		try {
			mButtonInputDriver = new ButtonInputDriver(
					GPIO_BUTTON,
					Button.LogicState.PRESSED_WHEN_LOW,
					KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
			mButtonInputDriver.register();
		} catch (IOException e) {
			Log.w(TAG, "Could not open GPIO pins", e);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i("EXIT", "ondestroy!");
		final Intent broadcastIntent = new Intent(".RestartSensor");
		sendBroadcast(broadcastIntent);
		try {
			mButtonInputDriver.close();
		} catch (IOException e) {
			Log.e(TAG, "button driver error", e);
		}
	}
}