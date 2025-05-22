package com.sima.smartakuarium;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class beranda extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda);

        // Periksa status login dari Intent
        boolean isLoginSuccessful = getIntent().getBooleanExtra("isLoginSuccessful", false);
        if (isLoginSuccessful) {
            // Tampilkan dialog jika login sukses
            showSuccessDialog();
        }

        // Navigasi ke Jadwal Makan Ikan
        CardView cardJadwalMakan = findViewById(R.id.cardJadwalMakanIkan);
        cardJadwalMakan.setOnClickListener(v -> {
            Intent intent = new Intent(beranda.this, jadwalmakanikan.class);
            startActivity(intent);
        });

        // Navigasi ke Jadwal Perawatan Ikan
        CardView cardJadwalPerawatanIkan = findViewById(R.id.cardJadwalPerawatanIkan);
        cardJadwalPerawatanIkan.setOnClickListener(v -> {
            Intent intent = new Intent(beranda.this, jadwalperawatanikan.class);
            startActivity(intent);
        });

        // Navigasi ke Atur Suhu
        CardView cardAturSuhu = findViewById(R.id.cardAturSuhu);
        cardAturSuhu.setOnClickListener(v -> {
            Intent intent = new Intent(beranda.this, atursuhu.class);
            startActivity(intent);
        });

        // Bottom Navigation - Jadwal
        LinearLayout navSchedule = findViewById(R.id.nav_schedule);
        navSchedule.setOnClickListener(v -> {
            Intent intent = new Intent(beranda.this, daftarikan.class);
            startActivity(intent);
        });

        // Bottom Navigation - Profil
        LinearLayout navProfile = findViewById(R.id.nav_profile);
        navProfile.setOnClickListener(v -> {
            Intent intent = new Intent(beranda.this, ProfileActivity.class);
            startActivity(intent);
        });

        // Bottom Navigation - Home
        LinearLayout navHome = findViewById(R.id.nav_home);
        navHome.setOnClickListener(v -> {
            // Sudah di halaman ini, tidak melakukan apa-apa
        });

        // Tombol Notifikasi
        LinearLayout navNotification = findViewById(R.id.nav_notification);
        navNotification.setOnClickListener(v -> {
            Intent intent = new Intent(beranda.this, notifikasi.class);
            startActivity(intent);
        });
    }

    private void showSuccessDialog() {
        // Buat dialog baru
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.sukses_login);
        dialog.setCancelable(false); // Dialog tidak bisa ditutup dengan tombol back

        // Tombol Tutup
        Button btnClose = dialog.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(view -> dialog.dismiss());

        // Tampilkan dialog
        dialog.show();
    }
}
