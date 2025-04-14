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
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class jadwalperawatanikan extends AppCompatActivity {

    private EditText timeInput;
    private Spinner perawatanSpinner, repeatSpinner;
    private LinearLayout savedScheduleLayout;
    private Button saveButton;
    private ImageButton backButton; // <- Tambahkan variabel untuk tombol kembali

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
        backButton = findViewById(R.id.backButton); // <- Inisialisasi tombol kembali

        // Picker untuk waktu
        timeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        // Tombol simpan jadwal
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSchedule();
            }
        });

        // Tombol kembali
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // <- Kembali ke activity sebelumnya
            }
        });
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, selectedHour, selectedMinute) -> {
                    String time = String.format("%02d:%02d", selectedHour, selectedMinute);
                    timeInput.setText(time);
                },
                hour,
                minute,
                true
        );
        timePickerDialog.show();
    }

    private void saveSchedule() {
        String time = timeInput.getText().toString();
        String jenis = perawatanSpinner.getSelectedItem().toString();
        String repeat = repeatSpinner.getSelectedItem().toString();

        if (time.isEmpty()) {
            timeInput.setError("Waktu harus diisi");
            return;
        }

        // Tambahkan ke daftar
        TextView scheduleItem = new TextView(this);
        scheduleItem.setText("Waktu: " + time + "\nJenis: " + jenis + "\nUlangi: " + repeat);
        scheduleItem.setTextSize(14f);
        scheduleItem.setTextColor(getResources().getColor(android.R.color.white));
        scheduleItem.setPadding(16, 16, 16, 16);

        savedScheduleLayout.addView(scheduleItem);
    }
}
