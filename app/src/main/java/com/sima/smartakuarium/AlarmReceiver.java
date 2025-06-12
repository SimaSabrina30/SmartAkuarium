package com.sima.smartakuarium;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String type = intent.getStringExtra("type");
        String jenis = intent.getStringExtra("jenis");

        showNotification(context, type, jenis);
    }

    private void showNotification(Context context, String type, String jenis) {
        String channelId = "jadwal_ikan_channel";
        String channelName = "Pengingat Jadwal Akuarium";

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName,
                    NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }

        String title;
        String message;

        if ("makan".equals(type)) {
            title = "Waktunya Memberi Makan Ikan!";
            message = "Jangan lupa beri makan ikanmu.";
        } else if ("perawatan".equals(type)) {
            title = "Jadwal Perawatan Akuarium!";
            message = "Saatnya melakukan perawatan: " + jenis;
        } else {
            title = "Pengingat Waktunya Memberi Makan Ikan!";
            message = "Jangan lupa beri makan ikanmu.";
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_popup_reminder)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true);

        manager.notify((int) System.currentTimeMillis(), builder.build());
    }
}
