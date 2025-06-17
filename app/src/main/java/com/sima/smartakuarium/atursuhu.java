package com.sima.smartakuarium;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class atursuhu extends AppCompatActivity {

    private TextView currentTemperatureText, savedSettingsText;
    private EditText targetTempInput, minTempInput, maxTempInput, minTdsInput, maxTdsInput; // Added TDS inputs
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
        // Corrected initializations based on typical UI structure and your XML
        // Assuming minTempInput is also used as the target temperature input based on your XML
        minTempInput = findViewById(R.id.minTempInput);
        maxTempInput = findViewById(R.id.maxTempInput);
        minTdsInput = findViewById(R.id.minTdsInput); // Initialize TDS input
        maxTdsInput = findViewById(R.id.maxTdsInput); // Initialize TDS input

        currentTemperatureText = findViewById(R.id.titleText); // Re-purposing titleText as a dummy for current temp or remove if not needed
        savedSettingsText = findViewById(R.id.savedSettingsText);
        saveButton = findViewById(R.id.saveButton);
        ImageButton backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(v -> finish());

        createNotificationChannel();
        currentTemperatureText.setText("Atur Parameter Akuarium"); // Dummy data for current temperature

        loadTemperatureSettings();

        saveButton.setOnClickListener(v -> {
            String target = minTempInput.getText().toString().trim(); // Using minTempInput as target based on XML
            String min = minTempInput.getText().toString().trim();
            String max = maxTempInput.getText().toString().trim();
            String minTds = minTdsInput.getText().toString().trim(); // Get TDS min
            String maxTds = maxTdsInput.getText().toString().trim(); // Get TDS max

            if (target.isEmpty()) {
                Toast.makeText(this, "Masukkan suhu minimum!", Toast.LENGTH_SHORT).show(); // Changed message
                return;
            }

            double t = Double.parseDouble(target);
            double tMin = min.isEmpty() ? 0 : Double.parseDouble(min);
            double tMax = max.isEmpty() ? 0 : Double.parseDouble(max);

            if (!min.isEmpty() && !max.isEmpty() && tMin >= tMax) { // Added a check for minTemp >= maxTemp
                Toast.makeText(this, "Suhu minimum harus lebih kecil dari suhu maksimum!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!minTds.isEmpty() && !maxTds.isEmpty()) {
                double tdsMin = Double.parseDouble(minTds);
                double tdsMax = Double.parseDouble(maxTds);
                if (tdsMin >= tdsMax) {
                    Toast.makeText(this, "TDS minimum harus lebih kecil dari TDS maksimum!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }


            SharedPreferences prefs = getSharedPreferences("AkuariumPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("minTemp", min); // Changed to minTemp
            editor.putString("maxTemp", max);
            editor.putString("minTds", minTds); // Save TDS min
            editor.putString("maxTds", maxTds); // Save TDS max
            editor.apply();

            String message = "Suhu min: " + min + "°C"; // Changed message
            if (!max.isEmpty()) {
                message += ", Suhu max: " + max + "°C";
            }
            if (!minTds.isEmpty() && !maxTds.isEmpty()) {
                message += "\nTDS min: " + minTds + " ppm, TDS max: " + maxTds + " ppm";
            }

            Toast.makeText(this, "Pengaturan disimpan!", Toast.LENGTH_SHORT).show();
            savedSettingsText.setText(message);
            showTemperatureNotification(message);

            // Pass data to detailakuarium
            Intent intent = new Intent(atursuhu.this, detailakuarium.class);
            intent.putExtra("minTemp", min);
            intent.putExtra("maxTemp", max);
            intent.putExtra("minTds", minTds);
            intent.putExtra("maxTds", maxTds);
            startActivity(intent);

            // Clear inputs after saving (optional, can be removed if you want them to persist)
            minTempInput.setText("");
            maxTempInput.setText("");
            minTdsInput.setText("");
            maxTdsInput.setText("");
        });
    }

    private void loadTemperatureSettings() {
        SharedPreferences prefs = getSharedPreferences("AkuariumPrefs", MODE_PRIVATE);
        String min = prefs.getString("minTemp", ""); // Load minTemp
        String max = prefs.getString("maxTemp", "");
        String minTds = prefs.getString("minTds", ""); // Load minTds
        String maxTds = prefs.getString("maxTds", ""); // Load maxTds

        minTempInput.setText(min); // Set minTemp
        maxTempInput.setText(max);
        minTdsInput.setText(minTds); // Set minTds
        maxTdsInput.setText(maxTds); // Set maxTds

        if (!min.isEmpty() || !minTds.isEmpty()) { // Check if any settings exist
            String message = "Suhu min: " + min + "°C"; // Changed message
            if (!max.isEmpty()) {
                message += ", Suhu max: " + max + "°C";
            }
            if (!minTds.isEmpty() && !maxTds.isEmpty()) {
                message += "\nTDS min: " + minTds + " ppm, TDS max: " + maxTds + " ppm";
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
                .setContentTitle("Pengaturan Tersimpan") // Changed title
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, builder.build());
    }
}