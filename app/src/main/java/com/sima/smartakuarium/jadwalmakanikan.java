package com.sima.smartakuarium;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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
        ImageButton backButton = findViewById(R.id.backButton); // Tambahan tombol back

        // Fungsi tombol kembali
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(jadwalmakanikan.this, beranda.class);
            startActivity(intent);
            finish(); // Menutup activity sekarang
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

                timeInput.setText(""); // Reset input
            }
        });
    }
}
