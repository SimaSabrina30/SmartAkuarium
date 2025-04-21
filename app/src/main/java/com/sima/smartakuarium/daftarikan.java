package com.sima.smartakuarium;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class daftarikan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftarikan);

        // Tombol kembali
        ImageButton backButton = findViewById(R.id.btnBack);
        backButton.setOnClickListener(view -> finish());

        // Setup tombol detail
        setupDetailButtonListeners();

        // Bottom Navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_schedule);

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, beranda.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_schedule) {
                return true;
            }
            return false;
        });
    }

    private void setupDetailButtonListeners() {
        LinearLayout listContainer = findViewById(R.id.listContainer);
        for (int i = 0; i < listContainer.getChildCount(); i++) {
            View itemView = listContainer.getChildAt(i);
            Button btnDetail = itemView.findViewById(R.id.btnDetail);
            if (btnDetail != null) {
                btnDetail.setOnClickListener(v -> {
                    Intent intent = new Intent(daftarikan.this, detailakuarium.class);
                    startActivity(intent);
                });
            }
        }
    }
}
