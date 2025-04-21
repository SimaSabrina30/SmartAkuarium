package com.sima.smartakuarium;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.cardview.widget.CardView;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.LinearLayout;

public class beranda extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda);

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

        // Navigasi Manual Bottom Navigation
        LinearLayout navSchedule = findViewById(R.id.nav_schedule);
        navSchedule.setOnClickListener(v -> {
            Intent intent = new Intent(beranda.this, daftarikan.class);
            startActivity(intent);
        });

        //LinearLayout navProfile = findViewById(R.id.nav_profile);
      //  navProfile.setOnClickListener(v -> {
           // Intent intent = new Intent(beranda.this, profil.class);
            //startActivity(intent);
        //});

        LinearLayout navHome = findViewById(R.id.nav_home);
        navHome.setOnClickListener(v -> {
            // Sudah di halaman ini, tidak melakukan apa-apa atau bisa refresh
        });
    }
}
