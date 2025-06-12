package com.sima.smartakuarium;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class AlarmHelper {
    public static void resetAlarms(Context context) {
        Log.d("AlarmHelper", "Resetting alarms...");

        // Mengakses AlarmManager dari sistem
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) {
            // Mengecek versi API untuk memastikan dukungan terhadap canScheduleExactAlarms
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // API level 31 atau lebih tinggi
                if (alarmManager.canScheduleExactAlarms()) {
                    Log.d("AlarmHelper", "App has permission to schedule exact alarms.");

                    // Membuat Intent untuk AlarmReceiver
                    Intent intent = new Intent(context, AlarmReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                            context,
                            0,
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                    );

                    // Contoh logika penjadwalan ulang alarm
                    alarmManager.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            System.currentTimeMillis() + 60000, // Alarm dalam 1 menit
                            pendingIntent
                    );

                    Log.d("AlarmHelper", "Alarm rescheduled successfully.");
                } else {
                    Log.d("AlarmHelper", "App does not have permission to schedule exact alarms.");
                }
            } else {
                Log.d("AlarmHelper", "Exact alarms not supported on API level below 31.");
            }
        } else {
            Log.d("AlarmHelper", "AlarmManager is null. Unable to reset alarms.");
        }
    }
}
