package com.sima.smartakuarium;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class beranda extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda);

        // Periksa apakah login berhasil
        Intent intent = getIntent();
        boolean isLoginSuccessful = intent.getBooleanExtra("isLoginSuccessful", false);
        if (isLoginSuccessful) {
            showLoginSuccessPopup();
        }

        // Navigasi ke Jadwal Makan Ikan
        CardView cardJadwalMakan = findViewById(R.id.cardJadwalMakanIkan);
        cardJadwalMakan.setOnClickListener(v -> {
            Intent intentToJadwalMakan = new Intent(beranda.this, jadwalmakanikan.class);
            startActivity(intentToJadwalMakan);
        });

        // Navigasi ke Jadwal Perawatan Ikan
        CardView cardJadwalPerawatanIkan = findViewById(R.id.cardJadwalPerawatanIkan);
        cardJadwalPerawatanIkan.setOnClickListener(v -> {
            Intent intentToPerawatan = new Intent(beranda.this, jadwalperawatanikan.class);
            startActivity(intentToPerawatan);
        });

        // Navigasi ke Atur Suhu
        CardView cardAturSuhu = findViewById(R.id.cardAturSuhu);
        cardAturSuhu.setOnClickListener(v -> {
            Intent intentToAturSuhu = new Intent(beranda.this, atursuhu.class);
            startActivity(intentToAturSuhu);
        });

        // Bottom Navigation - Jadwal
        LinearLayout navSchedule = findViewById(R.id.nav_schedule);
        navSchedule.setOnClickListener(v -> {
            Intent intentToSchedule = new Intent(beranda.this, daftarikan.class);
            startActivity(intentToSchedule);
        });

        // Bottom Navigation - Profil
        LinearLayout navProfile = findViewById(R.id.nav_profile);
        navProfile.setOnClickListener(v -> {
            Intent intentToProfile = new Intent(beranda.this, ProfileActivity.class);
            startActivity(intentToProfile);
        });

        // Bottom Navigation - Home
        LinearLayout navHome = findViewById(R.id.nav_home);
        navHome.setOnClickListener(v -> {
            // Sudah di halaman ini, tidak melakukan apa-apa
        });

        // Tombol Notifikasi
        LinearLayout navNotification = findViewById(R.id.nav_notification);
        navNotification.setOnClickListener(v -> {
            Intent intentToNotification = new Intent(beranda.this, notifikasi.class);
            startActivity(intentToNotification);
        });
    }

    private void showLoginSuccessPopup() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.sukses_login);

        // Ambil referensi tombol tutup
        Button btnClose = dialog.findViewById(R.id.btnClose);

        // Set aksi untuk tombol tutup
        btnClose.setOnClickListener(v -> dialog.dismiss());

        // Tampilkan dialog
        dialog.show();
    }
}
