package com.sima.smartakuarium;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String time = intent.getStringExtra("time");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "jadwal_channel")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Pengingat Jadwal Makan")
                .setContentText("Saatnya memberi makan ikan pada pukul: " + time)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(time.hashCode(), builder.build());
    }
}
