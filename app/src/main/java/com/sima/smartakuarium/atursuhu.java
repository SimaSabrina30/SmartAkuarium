package com.sima.smartakuarium;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class atursuhu extends AppCompatActivity {

    private TextView currentTemperatureText, savedSettingsText;
    private EditText targetTempInput, minTempInput, maxTempInput;
    private Button saveButton;

    private static final String CHANNEL_ID = "suhu_channel";
    private static final int NOTIFICATION_ID = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_atursuhu);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Init
        currentTemperatureText = findViewById(R.id.currentTemperatureText);
        savedSettingsText = findViewById(R.id.savedSettingsText);
        targetTempInput = findViewById(R.id.targetTempInput);
        minTempInput = findViewById(R.id.minTempInput);
        maxTempInput = findViewById(R.id.maxTempInput);
        saveButton = findViewById(R.id.saveButton);
        ImageButton backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(v -> finish());

        createNotificationChannel();
        currentTemperatureText.setText("Suhu Saat Ini: 28°C"); // Dummy

        loadTemperatureSettings();

        saveButton.setOnClickListener(v -> {
            String target = targetTempInput.getText().toString().trim();
            String min = minTempInput.getText().toString().trim();
            String max = maxTempInput.getText().toString().trim();

            if (target.isEmpty()) {
                Toast.makeText(this, "Masukkan suhu target!", Toast.LENGTH_SHORT).show();
                return;
            }

            double t = Double.parseDouble(target);
            double tMin = min.isEmpty() ? 0 : Double.parseDouble(min);
            double tMax = max.isEmpty() ? 0 : Double.parseDouble(max);

            if (!min.isEmpty() && t < tMin) {
                Toast.makeText(this, "Target < Min!", Toast.LENGTH_SHORT).show(); return;
            }
            if (!max.isEmpty() && t > tMax) {
                Toast.makeText(this, "Target > Max!", Toast.LENGTH_SHORT).show(); return;
            }

            SharedPreferences prefs = getSharedPreferences("AkuariumPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("targetTemp", target);
            editor.putString("minTemp", min);
            editor.putString("maxTemp", max);
            editor.apply();

            String message = "Suhu target: " + target + "°C";
            if (!min.isEmpty() && !max.isEmpty()) {
                message += "\nMin: " + min + "°C, Max: " + max + "°C";
            }

            Toast.makeText(this, "Pengaturan disimpan!", Toast.LENGTH_SHORT).show();
            savedSettingsText.setText(message);
            showTemperatureNotification(message);

            targetTempInput.setText(""); minTempInput.setText(""); maxTempInput.setText("");
        });
    }

    private void loadTemperatureSettings() {
        SharedPreferences prefs = getSharedPreferences("AkuariumPrefs", MODE_PRIVATE);
        String target = prefs.getString("targetTemp", "");
        String min = prefs.getString("minTemp", "");
        String max = prefs.getString("maxTemp", "");

        targetTempInput.setText(target);
        minTempInput.setText(min);
        maxTempInput.setText(max);

        if (!target.isEmpty()) {
            String message = "Suhu target: " + target + "°C";
            if (!min.isEmpty() && !max.isEmpty()) {
                message += "\nMin: " + min + "°C, Max: " + max + "°C";
            }
            savedSettingsText.setText(message);
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Notifikasi Suhu", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Channel untuk notifikasi pengaturan suhu");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private void showTemperatureNotification(String message) {
        Intent intent = new Intent(this, atursuhu.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_temperatur)
                .setContentTitle("Pengaturan Suhu Tersimpan")
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, builder.build());
    }
}
