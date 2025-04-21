package com.sima.smartakuarium;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class daftarikan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftarikan);

        // Tombol kembali
        ImageButton backButton = findViewById(R.id.btnBack);
        backButton.setOnClickListener(view -> finish());

        // Tombol Detail Ikan
        Button btnDetail1 = findViewById(R.id.btnDetail1);
        Button btnDetail2 = findViewById(R.id.btnDetail2);

        btnDetail1.setOnClickListener(v -> openDetail());
        btnDetail2.setOnClickListener(v -> openDetail());

        // Manual Bottom Navigation
        LinearLayout navHome = findViewById(R.id.nav_home);
        LinearLayout navSchedule = findViewById(R.id.nav_schedule);
        LinearLayout navProfile = findViewById(R.id.nav_profile);

        navHome.setOnClickListener(v -> {
            Intent intent = new Intent(daftarikan.this, beranda.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        navSchedule.setOnClickListener(v -> {
            // Sudah di halaman ini
        });

        navProfile.setOnClickListener(v -> {
            Intent intent = new Intent(daftarikan.this, ProfileActivity.class);
            startActivity(intent);
        });
    }

    private void openDetail() {
        Intent intent = new Intent(daftarikan.this, detailakuarium.class);
        startActivity(intent);
    }
}
