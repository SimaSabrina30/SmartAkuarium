package com.sima.smartakuarium;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.util.Locale;

import android.widget.Toast;


public class jadwalmakanikan extends AppCompatActivity {

    private EditText timeInput;
    private LinearLayout savedScheduleLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_jadwalmakanikan);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inisialisasi komponen UI
        timeInput = findViewById(R.id.timeInput);
        savedScheduleLayout = findViewById(R.id.savedScheduleLayout);
        Button saveButton = findViewById(R.id.saveButton);
        ImageButton backButton = findViewById(R.id.backButton);

        // Fungsi tombol kembali
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(jadwalmakanikan.this, beranda.class);
            startActivity(intent);
            finish();
        });

        // Time Picker
        timeInput.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            new TimePickerDialog(jadwalmakanikan.this,
                    (view, hourOfDay, minute1) -> {
                        String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute1);
                        timeInput.setText(selectedTime);
                    }, hour, minute, true).show();
        });

        // Tombol simpan jadwal
        saveButton.setOnClickListener(v -> {
            String time = timeInput.getText().toString().trim();

            if (!time.isEmpty()) {
                String schedule = "Jam: " + time;

                LinearLayout container = new LinearLayout(this);
                container.setOrientation(LinearLayout.HORIZONTAL);
                container.setBackgroundResource(R.drawable.backgroundputih);
                container.setPadding(16, 16, 16, 16);

                LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                containerParams.setMargins(0, 8, 0, 0);
                container.setLayoutParams(containerParams);

                TextView textView = new TextView(this);
                textView.setText(schedule);
                textView.setTextSize(16f);
                textView.setTextColor(ContextCompat.getColor(this, android.R.color.black));
                textView.setLayoutParams(new LinearLayout.LayoutParams(
                        0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

                ImageButton deleteButton = new ImageButton(this);
                deleteButton.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
                deleteButton.setBackgroundColor(android.graphics.Color.TRANSPARENT);
                deleteButton.setOnClickListener(view -> savedScheduleLayout.removeView(container));

                container.addView(textView);
                container.addView(deleteButton);
                savedScheduleLayout.addView(container);

                setAlarm(time); // Atur alarm
                timeInput.setText(""); // Reset input
            }
        });
    }

    private void setAlarm(String time) {
        try {
            String[] timeParts = time.split(":");
            int hour = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);

            // Tambahkan satu hari jika waktu yang diatur sudah lewat
            if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                Toast.makeText(this, "Waktu sudah lewat, alarm diatur untuk esok hari", Toast.LENGTH_SHORT).show();
            }

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlarmReceiver.class);
            int requestCode = (int) System.currentTimeMillis(); // ID unik untuk setiap alarm
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    this,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            if (alarmManager != null) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                Toast.makeText(this, "Alarm diatur untuk pukul " + time, Toast.LENGTH_SHORT).show();
                Log.d("AlarmManager", "Alarm diatur untuk: " + calendar.getTime());
            } else {
                Toast.makeText(this, "Gagal mengatur alarm", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Terjadi kesalahan: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
