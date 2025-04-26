package com.sima.smartakuarium;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
        currentTemperatureText = findViewById(R.id.currentTemperatureText);
        targetTempInput = findViewById(R.id.targetTempInput);
        minTempInput = findViewById(R.id.minTempInput);
        maxTempInput = findViewById(R.id.maxTempInput);
        saveButton = findViewById(R.id.saveButton);

        // Tombol kembali
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            finish(); // Menutup aktivitas dan kembali ke halaman sebelumnya
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // (opsional) animasi transisi
        });

        // Set suhu saat ini (dummy - nanti bisa dari sensor/Bluetooth/Internet)
        currentTemperatureText.setText("Suhu Saat Ini: 28°C");

        // Mengambil pengaturan suhu dari SharedPreferences dan menampilkannya
        loadTemperatureSettings();

        // Tombol Simpan ditekan
        saveButton.setOnClickListener(v -> {
            String targetTempStr = targetTempInput.getText().toString().trim();
            String minTempStr = minTempInput.getText().toString().trim();
            String maxTempStr = maxTempInput.getText().toString().trim();

            if (targetTempStr.isEmpty()) {
                Toast.makeText(this, "Masukkan suhu target!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validasi angka (jika diisi)
            double targetTemp = Double.parseDouble(targetTempStr);
            double minTemp = minTempStr.isEmpty() ? 0 : Double.parseDouble(minTempStr);
            double maxTemp = maxTempStr.isEmpty() ? 0 : Double.parseDouble(maxTempStr);

            if (!minTempStr.isEmpty() && targetTemp < minTemp) {
                Toast.makeText(this, "Suhu target tidak boleh lebih kecil dari suhu minimum!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!maxTempStr.isEmpty() && targetTemp > maxTemp) {
                Toast.makeText(this, "Suhu target tidak boleh lebih besar dari suhu maksimum!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Simpan data ke SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("AkuariumPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("targetTemp", targetTempStr);
            editor.putString("minTemp", minTempStr);
            editor.putString("maxTemp", maxTempStr);
            editor.apply(); // Simpan data

            // Menampilkan pesan sukses
            String message = "Suhu target: " + targetTempStr + "°C";
            if (!minTempStr.isEmpty() && !maxTempStr.isEmpty()) {
                message += "\nBatas Min: " + minTempStr + "°C, Maks: " + maxTempStr + "°C";
            }

            Toast.makeText(this, message, Toast.LENGTH_LONG).show();

            // Kosongkan input setelah simpan
            targetTempInput.setText("");
            minTempInput.setText("");
            maxTempInput.setText("");
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
        currentTemperatureText.setText("Suhu Saat Ini: 28°C"); // Bisa diganti dengan data aktual
        targetTempInput.setText(targetTemp);
        minTempInput.setText(minTemp);
        maxTempInput.setText(maxTemp);
    }
}
