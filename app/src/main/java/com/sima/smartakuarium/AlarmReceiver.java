package com.sima.smartakuarium;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String type = intent.getStringExtra("type");
        String jenis = intent.getStringExtra("jenis");

        String title;
        String message;

        if ("makan".equals(type)) {
            title = "Waktunya Memberi Makan Ikan!";
            message = "Jangan lupa beri makan ikanmu.";
        } else if ("perawatan".equals(type)) {
            title = "Jadwal Perawatan Akuarium!";
            message = "Saatnya melakukan perawatan: " + jenis;
        } else {
            title = "Pengingat!";
            message = "Ada pengingat untuk ikanmu.";
        }

        // Simpan ke histori
        String waktu = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(new Date());
        notifikasi.simpanNotifikasi(context.getSharedPreferences("notifikasi", Context.MODE_PRIVATE), message, waktu);

        // Tampilkan notifikasi
        showNotification(context, title, message);
    }

    private void showNotification(Context context, String title, String message) {
        String channelId = "jadwal_ikan_channel";
        String channelName = "Pengingat Jadwal Akuarium";

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName,
                    NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
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
