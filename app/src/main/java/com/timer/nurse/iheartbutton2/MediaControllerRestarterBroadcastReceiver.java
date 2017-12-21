package com.timer.nurse.iheartbutton2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Darien on 12/20/2017.
 */

public class MediaControllerRestarterBroadcastReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(MediaControllerRestarterBroadcastReceiver.class.getSimpleName(), "Service Stops!");
		context.startService(new Intent(context, MediaControllerService.class));;
	}
}
