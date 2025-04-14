package com.sima.smartakuarium;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class atursuhu extends AppCompatActivity {

    private TextView currentTemperatureText;
    private EditText targetTempInput, minTempInput, maxTempInput;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_atursuhu);

        // Menangani window insets untuk edge-to-edge UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inisialisasi View
        currentTemperatureText = findViewById(R.id.currentTemperature);
        targetTempInput = findViewById(R.id.targetTempInput);
        minTempInput = findViewById(R.id.minTempInput);
        maxTempInput = findViewById(R.id.maxTempInput);
        saveButton = findViewById(R.id.saveButton);

        // Set suhu saat ini (dummy - nanti bisa dari sensor/Bluetooth/Internet)
        currentTemperatureText.setText("Suhu Saat Ini: 28°C");

        // Mengambil pengaturan suhu dari SharedPreferences dan menampilkannya
        loadTemperatureSettings();

        // Tombol Simpan ditekan
        saveButton.setOnClickListener(v -> {
            String targetTemp = targetTempInput.getText().toString().trim();
            String minTemp = minTempInput.getText().toString().trim();
            String maxTemp = maxTempInput.getText().toString().trim();

            if (targetTemp.isEmpty()) {
                Toast.makeText(this, "Masukkan suhu target!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Simpan data ke SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("AkuariumPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            // Menyimpan data suhu
            editor.putString("targetTemp", targetTemp);
            editor.putString("minTemp", minTemp);
            editor.putString("maxTemp", maxTemp);
            editor.apply(); // Simpan data

            // Menampilkan pesan
            String message = "Suhu target: " + targetTemp + "°C";
            if (!minTemp.isEmpty() && !maxTemp.isEmpty()) {
                message += "\nBatas Min: " + minTemp + "°C, Maks: " + maxTemp + "°C";
            }

            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Mengambil pengaturan suhu dari SharedPreferences saat halaman dibuka
        loadTemperatureSettings();
    }

    private void loadTemperatureSettings() {
        SharedPreferences sharedPreferences = getSharedPreferences("AkuariumPrefs", MODE_PRIVATE);

        // Mengambil data dari SharedPreferences
        String targetTemp = sharedPreferences.getString("targetTemp", "28"); // Default ke 28°C jika tidak ada data
        String minTemp = sharedPreferences.getString("minTemp", "0");
        String maxTemp = sharedPreferences.getString("maxTemp", "0");

        // Menampilkan data pada UI
        currentTemperatureText.setText("Suhu Saat Ini: 28°C"); // Bisa diganti dengan sensor/Internet
        targetTempInput.setText(targetTemp);
        minTempInput.setText(minTemp);
        maxTempInput.setText(maxTemp);
    }
}
