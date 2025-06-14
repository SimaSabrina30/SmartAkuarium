package com.sima.smartakuarium;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class jadwalperawatanikan extends AppCompatActivity {

    private EditText timeInput;
    private Spinner perawatanSpinner, repeatSpinner;
    private LinearLayout savedScheduleLayout;
    private Button saveButton;
    private ImageButton backButton;
    private SharedPreferences preferences;
    private Gson gson;
    private List<JadwalModel> jadwalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwalperawatanikan);

        preferences = getSharedPreferences("jadwal_prefs", MODE_PRIVATE);
        gson = new Gson();

        timeInput = findViewById(R.id.timeInput);
        perawatanSpinner = findViewById(R.id.perawatanSpinner);
        repeatSpinner = findViewById(R.id.repeatSpinner);
        savedScheduleLayout = findViewById(R.id.savedScheduleLayout);
        saveButton = findViewById(R.id.saveButton);
        backButton = findViewById(R.id.backButton);

        timeInput.setOnClickListener(v -> showTimePicker());
        saveButton.setOnClickListener(v -> saveSchedule());
        backButton.setOnClickListener(v -> onBackPressed());

        loadSavedSchedules();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        new TimePickerDialog(this, (view, selectedHour, selectedMinute) -> {
            String time = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute);
            timeInput.setText(time);
        }, hour, minute, true).show();
    }

    private void saveSchedule() {
        String time = timeInput.getText().toString().trim();
        String jenis = perawatanSpinner.getSelectedItem().toString();
        String repeat = repeatSpinner.getSelectedItem().toString();

        if (time.isEmpty()) {
            timeInput.setError("Waktu harus diisi");
            return;
        }

        JadwalModel model = new JadwalModel(time, jenis, repeat);
        jadwalList.add(model);
        saveToPrefs();

        addScheduleItemToView(model);
        setAlarm(model);
        timeInput.setText("");
    }

    private void addScheduleItemToView(JadwalModel model) {
        LinearLayout itemContainer = new LinearLayout(this);
        itemContainer.setOrientation(LinearLayout.HORIZONTAL);
        itemContainer.setBackgroundResource(R.drawable.backgroundputih);
        itemContainer.setPadding(16, 16, 16, 16);

        LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        containerParams.setMargins(0, 8, 0, 0);
        itemContainer.setLayoutParams(containerParams);

        TextView scheduleItem = new TextView(this);
        scheduleItem.setText("Waktu: " + model.time + "\nJenis: " + model.jenis + "\nUlangi: " + model.repeat);
        scheduleItem.setTextSize(14f);
        scheduleItem.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        scheduleItem.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        ImageButton deleteButton = new ImageButton(this);
        deleteButton.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
        deleteButton.setBackgroundColor(android.graphics.Color.TRANSPARENT);
        deleteButton.setOnClickListener(v -> {
            savedScheduleLayout.removeView(itemContainer);
            jadwalList.remove(model);
            saveToPrefs();
        });

        itemContainer.addView(scheduleItem);
        itemContainer.addView(deleteButton);
        savedScheduleLayout.addView(itemContainer);
    }

    private void loadSavedSchedules() {
        String json = preferences.getString("jadwal_list", "[]");
        Type type = new TypeToken<ArrayList<JadwalModel>>() {}.getType();
        jadwalList = gson.fromJson(json, type);
        if (jadwalList == null) jadwalList = new ArrayList<>();

        for (JadwalModel model : jadwalList) {
            addScheduleItemToView(model);
            setAlarm(model);
        }
    }

    private void saveToPrefs() {
        String json = gson.toJson(jadwalList);
        preferences.edit().putString("jadwal_list", json).apply();
    }

    private void setAlarm(JadwalModel model) {
        String[] timeParts = model.time.split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("jenis", model.jenis);
        intent.putExtra("type", "perawatan");

        // Tambahkan pesan default yang tidak akan null
        String pesan = "Waktunya melakukan perawatan: " + model.jenis;
        intent.putExtra("pesan", pesan); // ini kunci untuk menghindari null di notifikasi

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                model.time.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            calendar.getTimeInMillis(),
                            pendingIntent
                    );
                } else {
                    Toast.makeText(this, "Tidak diizinkan untuk alarm presisi.", Toast.LENGTH_LONG).show();
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        pendingIntent
                );
            } else {
                alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        pendingIntent
                );
            }
            Toast.makeText(this, "Alarm perawatan dijadwalkan.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "AlarmManager tidak tersedia.", Toast.LENGTH_SHORT).show();
        }
    }

}
