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
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.List;

public class jadwalmakanikan extends AppCompatActivity {

    private EditText timeInput;
    private LinearLayout savedScheduleLayout;
    private static final String PREFS_NAME = "FeedingPrefs";
    private static final String KEY_SCHEDULES = "schedules";
    private List<String> scheduleList = new ArrayList<>();

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

        timeInput = findViewById(R.id.timeInput);
        savedScheduleLayout = findViewById(R.id.savedScheduleLayout);
        Button saveButton = findViewById(R.id.saveButton);
        ImageButton backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(jadwalmakanikan.this, beranda.class);
            startActivity(intent);
            finish();
        });

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

        saveButton.setOnClickListener(v -> {
            String time = timeInput.getText().toString().trim();

            if (!time.isEmpty()) {
                if (!scheduleList.contains(time)) {
                    scheduleList.add(time);
                    saveSchedules();
                    renderScheduleView(time);
                    setAlarm(time);
                    timeInput.setText("");
                } else {
                    Toast.makeText(this, "Jadwal sudah ada", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loadSchedules();
    }

    private void saveSchedules() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray array = new JSONArray();
        for (String time : scheduleList) {
            array.put(time);
        }
        editor.putString(KEY_SCHEDULES, array.toString());
        editor.apply();
    }

    private void loadSchedules() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String json = prefs.getString(KEY_SCHEDULES, null);
        if (json != null) {
            try {
                JSONArray array = new JSONArray(json);
                for (int i = 0; i < array.length(); i++) {
                    String time = array.getString(i);
                    scheduleList.add(time);
                    renderScheduleView(time);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void renderScheduleView(String time) {
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
        deleteButton.setOnClickListener(view -> {
            savedScheduleLayout.removeView(container);
            scheduleList.remove(time);
            saveSchedules();
        });

        container.addView(textView);
        container.addView(deleteButton);
        savedScheduleLayout.addView(container);
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

            if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                Toast.makeText(this, "Waktu sudah lewat, alarm diatur untuk esok hari", Toast.LENGTH_SHORT).show();
            }

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlarmReceiver.class);
            intent.putExtra("time", time);
            int requestCode = (int) System.currentTimeMillis();
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
