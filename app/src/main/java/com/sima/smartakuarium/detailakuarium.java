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
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.HashMap;
import java.util.Map;

public class detailakuarium extends AppCompatActivity {
    Button btnRefresh;
    TextView tvJam, tvTanggal, tvTds, tvSuhu, tvStatusTds, tvStatusSuhu;
    TextView tvIdealSuhu, tvIdealTds;
    TextView tvTemperatureHistory;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference logRef = db.collection("aquarium_logs");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailakuarium);

        btnRefresh = findViewById(R.id.btnRefresh);

        tvJam = findViewById(R.id.tvJam);
        tvTanggal = findViewById(R.id.tvTanggal);
        tvTds = findViewById(R.id.tvTds);
        tvSuhu = findViewById(R.id.tvSuhu);
        tvStatusTds = findViewById(R.id.tvStatusTds);
        tvStatusSuhu = findViewById(R.id.tvStatusSuhu);
        tvIdealSuhu = findViewById(R.id.tvIdealSuhu);
        tvIdealTds = findViewById(R.id.tvIdealTds);
        tvTemperatureHistory = findViewById(R.id.tvTemperatureHistory);

        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        getLatestLog();
        getTemperatureHistory();
        displayIdealSettings();

        btnRefresh.setOnClickListener(view -> {
            getLatestLog();
            getTemperatureHistory();
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
                        tvTds.setText("TDS: " + (tds != null ? String.format(Locale.getDefault(), "%.2f", tds) : "-") + " ppm");
                        tvSuhu.setText("Suhu: " + (suhu != null ? String.format(Locale.getDefault(), "%.2f", suhu) : "-") + "°C");

                        // Status Suhu
                        String statusSuhu = "-";
                        if (suhu != null) {
                            if (suhu < 25) {
                                statusSuhu = "Rendah";
                                tvStatusSuhu.setTextColor(getResources().getColor(android.R.color.holo_orange_dark)); // Kuning
                            } else if (suhu <= 30) {
                                statusSuhu = "Normal";
                                tvStatusSuhu.setTextColor(getResources().getColor(android.R.color.holo_green_dark)); // Hijau
                            } else {
                                statusSuhu = "Tinggi";
                                tvStatusSuhu.setTextColor(getResources().getColor(android.R.color.holo_red_dark)); // Merah
                            }
                        } else {
                            tvStatusSuhu.setTextColor(getResources().getColor(android.R.color.black));
                        }
                        tvStatusSuhu.setText("Status Suhu: " + statusSuhu);

                        // Status TDS
                        String statusTDS = "-";
                        if (tds != null) {
                            if (tds < 50) {
                                statusTDS = "Rendah";
                                tvStatusTds.setTextColor(getResources().getColor(android.R.color.holo_orange_dark)); // Kuning
                            } else if (tds <= 500) {
                                statusTDS = "Normal";
                                tvStatusTds.setTextColor(getResources().getColor(android.R.color.holo_green_dark)); // Hijau
                            } else {
                                statusTDS = "Tinggi";
                                tvStatusTds.setTextColor(getResources().getColor(android.R.color.holo_red_dark)); // Merah
                            }
                        } else {
                            tvStatusTds.setTextColor(getResources().getColor(android.R.color.black));
                        }
                        tvStatusTds.setText("Status TDS: " + statusTDS);

                    } else {
                        Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                        tvJam.setText("Jam: -");
                        tvTanggal.setText("Tanggal: -");
                        tvTds.setText("TDS: -");
                        tvSuhu.setText("Suhu: -");
                        tvStatusSuhu.setText("Status Suhu: -");
                        tvStatusSuhu.setTextColor(getResources().getColor(android.R.color.black));
                        tvStatusTds.setText("Status TDS: -");
                        tvStatusTds.setTextColor(getResources().getColor(android.R.color.black));
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Gagal mengambil data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    tvJam.setText("Jam: -");
                    tvTanggal.setText("Tanggal: -");
                    tvTds.setText("TDS: -");
                    tvSuhu.setText("Suhu: -");
                    tvStatusSuhu.setText("Status Suhu: -");
                    tvStatusSuhu.setTextColor(getResources().getColor(android.R.color.black));
                    tvStatusTds.setText("Status TDS: -");
                    tvStatusTds.setTextColor(getResources().getColor(android.R.color.black));
                });
    }

    private void getTemperatureHistory() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, -24);
        Date twentyFourHoursAgo = cal.getTime();

        logRef.whereGreaterThanOrEqualTo("date", twentyFourHoursAgo)
                .orderBy("date", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        Map<String, Double> hourlyTemperatures = new HashMap<>();
                        SimpleDateFormat hourFormat = new SimpleDateFormat("HH", Locale.getDefault());
                        SimpleDateFormat fullDateFormat = new SimpleDateFormat("dd/MM HH:mm", Locale.getDefault());

                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            Date logDate = doc.getDate("date");
                            Double suhu = doc.getDouble("suhu_air");
                            if (logDate != null && suhu != null) {
                                String hourKey = hourFormat.format(logDate);
                                hourlyTemperatures.put(hourKey, suhu);
                            }
                        }

                        StringBuilder historyBuilder = new StringBuilder("Riwayat Suhu (24 Jam Terakhir):\n");
                        Calendar displayCal = Calendar.getInstance();
                        for (int i = 0; i < 24; i++) {
                            displayCal.add(Calendar.HOUR_OF_DAY, -1);
                            String hourKey = hourFormat.format(displayCal.getTime());
                            Double temp = hourlyTemperatures.get(hourKey);

                            historyBuilder.append(fullDateFormat.format(displayCal.getTime())).append(" - Suhu: ");
                            if (temp != null) {
                                historyBuilder.append(String.format(Locale.getDefault(), "%.2f", temp)).append("°C\n");
                            } else {
                                historyBuilder.append("Tidak ada data\n");
                            }
                        }
                        tvTemperatureHistory.setText(historyBuilder.toString());

                    } else {
                        tvTemperatureHistory.setText("Riwayat Suhu (24 Jam Terakhir):\nTidak ada data dalam 24 jam terakhir.");
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Gagal mengambil riwayat suhu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    tvTemperatureHistory.setText("Riwayat Suhu (24 Jam Terakhir):\nError memuat riwayat.");
                });
    }

    private void displayIdealSettings() {
        SharedPreferences prefs = getSharedPreferences("AkuariumPrefs", MODE_PRIVATE);
        String minTemp = prefs.getString("minTemp", "");
        String maxTemp = prefs.getString("maxTemp", "");
        String minTds = prefs.getString("minTds", "");
        String maxTds = prefs.getString("maxTds", "");

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
