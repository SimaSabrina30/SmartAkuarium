package com.sima.smartakuarium;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.d("BootReceiver", "Device restarted. Resetting alarms...");
            AlarmHelper.resetAlarms(context); // Panggil resetAlarms dari AlarmHelper
        }
    }
}
