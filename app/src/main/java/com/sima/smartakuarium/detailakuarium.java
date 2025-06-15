package com.sima.smartakuarium;

import android.annotation.SuppressLint;
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
    TextView tvJam, tvTanggal, tvPh, tvSuhu;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference logRef = db.collection("aquarium_logs");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailakuarium);

        btnRefresh = findViewById(R.id.btnRefresh);

        // Inisialisasi TextView
        tvJam = findViewById(R.id.tvJam);
        tvTanggal = findViewById(R.id.tvTanggal);
        tvPh = findViewById(R.id.tvTds);
        tvSuhu = findViewById(R.id.tvSuhu);

        // Panggil saat awal
        getLatestLog();

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
                        double suhu = doc.getDouble("suhu_air");
                        double tds = doc.getDouble("tds");

                        Date date = doc.getDate("date");
                        String jam = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(date);
                        String tanggal = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date);

                        tvJam.setText("Jam: " + jam);
                        tvTanggal.setText("Tanggal: " + tanggal);
                        tvPh.setText("TDS: " + tds);
                        tvSuhu.setText("Suhu: " + suhu + "°C");
                    } else {
                        Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Gagal mengambil data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
