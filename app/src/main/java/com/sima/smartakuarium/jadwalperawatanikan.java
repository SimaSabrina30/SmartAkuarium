package com.sima.smartakuarium;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Calendar;

public class jadwalperawatanikan extends AppCompatActivity {

    private EditText timeInput;
    private Spinner perawatanSpinner, repeatSpinner;
    private LinearLayout savedScheduleLayout;
    private Button saveButton;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwalperawatanikan);

        // Inisialisasi view
        timeInput = findViewById(R.id.timeInput);
        perawatanSpinner = findViewById(R.id.perawatanSpinner);
        repeatSpinner = findViewById(R.id.repeatSpinner);
        savedScheduleLayout = findViewById(R.id.savedScheduleLayout);
        saveButton = findViewById(R.id.saveButton);
        backButton = findViewById(R.id.backButton);

        // Picker untuk waktu
        timeInput.setOnClickListener(v -> showTimePicker());

        // Tombol simpan jadwal
        saveButton.setOnClickListener(v -> saveSchedule());

        // Tombol kembali
        backButton.setOnClickListener(v -> onBackPressed());
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        new TimePickerDialog(
                this,
                (view, selectedHour, selectedMinute) -> {
                    String time = String.format("%02d:%02d", selectedHour, selectedMinute);
                    timeInput.setText(time);
                },
                hour,
                minute,
                true
        ).show();
    }

    private void saveSchedule() {
        String time = timeInput.getText().toString().trim();
        String jenis = perawatanSpinner.getSelectedItem().toString();
        String repeat = repeatSpinner.getSelectedItem().toString();

        if (time.isEmpty()) {
            timeInput.setError("Waktu harus diisi");
            return;
        }

        // Container item jadwal
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

        // Teks jadwal
        TextView scheduleItem = new TextView(this);
        scheduleItem.setText("Waktu: " + time + "\nJenis: " + jenis + "\nUlangi: " + repeat);
        scheduleItem.setTextSize(14f);
        scheduleItem.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        scheduleItem.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        // Tombol hapus
        ImageButton deleteButton = new ImageButton(this);
        deleteButton.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
        deleteButton.setBackgroundColor(android.graphics.Color.TRANSPARENT);
        deleteButton.setOnClickListener(v -> savedScheduleLayout.removeView(itemContainer));

        itemContainer.addView(scheduleItem);
        itemContainer.addView(deleteButton);
        savedScheduleLayout.addView(itemContainer);

        timeInput.setText(""); // reset input
    }
}
