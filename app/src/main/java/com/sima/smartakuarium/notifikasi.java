package com.sima.smartakuarium;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class notifikasi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifikasi);

        // Mengatur padding untuk memastikan konten tidak tumpang tindih dengan sistem bar
        View mainLayout = findViewById(R.id.main);
        ViewCompat.setOnApplyWindowInsetsListener(mainLayout, (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(
                    systemBarsInsets.left,
                    systemBarsInsets.top,
                    systemBarsInsets.right,
                    systemBarsInsets.bottom
            );
            return WindowInsetsCompat.CONSUMED;
        });

        // Menangani tombol kembali ke halaman beranda
        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(notifikasi.this, beranda.class);
            startActivity(intent);
            finish(); // Menutup halaman notifikasi
        });
    }
}