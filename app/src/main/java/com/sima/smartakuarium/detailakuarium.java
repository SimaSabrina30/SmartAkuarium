package com.sima.smartakuarium;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class detailakuarium extends AppCompatActivity {
    Button btnRefresh;
    TextView tvJam, tvTanggal, tvTds, tvSuhu, tvStatusTds, tvStatusSuhu;
    TextView tvIdealSuhu, tvIdealTds; // !!! INI YANG DITAMBAHKAN !!!

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference logRef = db.collection("aquarium_logs");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailakuarium);

        btnRefresh = findViewById(R.id.btnRefresh);

        // Initialize TextViews for monitored data
        tvJam = findViewById(R.id.tvJam);
        tvTanggal = findViewById(R.id.tvTanggal);
        tvTds = findViewById(R.id.tvTds);
        tvSuhu = findViewById(R.id.tvSuhu);
        tvStatusTds = findViewById(R.id.tvStatusTds);
        tvStatusSuhu = findViewById(R.id.tvStatusSuhu);

        // !!! INI YANG DITAMBAHKAN: Initialize TextViews for ideal data !!!
        tvIdealSuhu = findViewById(R.id.tvIdealSuhu);
        tvIdealTds = findViewById(R.id.tvIdealTds);
        // !!! AKHIR DARI PENAMBAHAN !!!


        // Set up back button click listener
        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        // Get the latest log data from Firebase
        getLatestLog();
        // Display the saved ideal settings in the "Air Ideal" section
        displayIdealSettings();

        btnRefresh.setOnClickListener(view -> {
            getLatestLog();
        });
    }

    private void getLatestLog() {
        logRef.orderBy("date", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        QueryDocumentSnapshot doc = (QueryDocumentSnapshot) queryDocumentSnapshots.getDocuments().get(0);
                        Double suhu = doc.getDouble("suhu_air");
                        Double tds = doc.getDouble("tds");

                        Date date = doc.getDate("date");
                        String jam = (date != null) ? new SimpleDateFormat("HH:mm", Locale.getDefault()).format(date) : "-";
                        String tanggal = (date != null) ? new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date) : "-";

                        tvJam.setText("Jam: " + jam);
                        tvTanggal.setText("Tanggal: " + tanggal);
                        tvTds.setText("TDS: " + (tds != null ? String.format(Locale.getDefault(), "%.2f", tds) : "-") + " ppm"); // Format TDS
                        tvSuhu.setText("Suhu: " + (suhu != null ? String.format(Locale.getDefault(), "%.2f", suhu) : "-") + "°C"); // Format Suhu

                        // Optionally, here you could compare current values with ideal values
                        // and update tvStatusTds and tvStatusSuhu (e.g., "Normal", "Tinggi", "Rendah")
                        // For now, these will remain "-" or as set by displayIdealSettings if not cleared here.
                        tvStatusSuhu.setText("Status Suhu: -"); // Reset status, or implement comparison logic
                        tvStatusTds.setText("Status TDS: -"); // Reset status, or implement comparison logic

                    } else {
                        Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                        // Clear current readings if no data
                        tvJam.setText("Jam: -");
                        tvTanggal.setText("Tanggal: -");
                        tvTds.setText("TDS: -");
                        tvSuhu.setText("Suhu: -");
                        tvStatusSuhu.setText("Status Suhu: -");
                        tvStatusTds.setText("Status TDS: -");
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Gagal mengambil data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    // Clear current readings on failure
                    tvJam.setText("Jam: -");
                    tvTanggal.setText("Tanggal: -");
                    tvTds.setText("TDS: -");
                    tvSuhu.setText("Suhu: -");
                    tvStatusSuhu.setText("Status Suhu: -");
                    tvStatusTds.setText("Status TDS: -");
                });
    }

    private void displayIdealSettings() {
        // Load settings from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("AkuariumPrefs", MODE_PRIVATE);
        String minTemp = prefs.getString("minTemp", "");
        String maxTemp = prefs.getString("maxTemp", "");
        String minTds = prefs.getString("minTds", "");
        String maxTds = prefs.getString("maxTds", "");

        // Construct and set text for Ideal Suhu
        StringBuilder suhuIdealBuilder = new StringBuilder("Suhu: ");
        if (!minTemp.isEmpty() && !maxTemp.isEmpty()) {
            suhuIdealBuilder.append(minTemp).append(" - ").append(maxTemp).append("°C");
        } else if (!minTemp.isEmpty()) {
            suhuIdealBuilder.append("Min ").append(minTemp).append("°C");
        } else if (!maxTemp.isEmpty()) {
            suhuIdealBuilder.append("Max ").append(maxTemp).append("°C");
        } else {
            suhuIdealBuilder.append("Belum diatur");
        }
        tvIdealSuhu.setText(suhuIdealBuilder.toString());

        // Construct and set text for Ideal TDS
        StringBuilder tdsIdealBuilder = new StringBuilder("TDS: ");
        if (!minTds.isEmpty() && !maxTds.isEmpty()) {
            tdsIdealBuilder.append(minTds).append(" - ").append(maxTds).append(" ppm");
        } else if (!minTds.isEmpty()) {
            tdsIdealBuilder.append("Min ").append(minTds).append(" ppm");
        } else if (!maxTds.isEmpty()) {
            tdsIdealBuilder.append("Max ").append(maxTds).append(" ppm");
        } else {
            tdsIdealBuilder.append("Belum diatur");
        }
        tvIdealTds.setText(tdsIdealBuilder.toString());
    }
}