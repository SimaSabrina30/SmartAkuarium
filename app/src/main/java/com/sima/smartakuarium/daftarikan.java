package com.sima.smartakuarium;

import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class daftarikan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftarikan); // pastikan file layout ini sesuai

        // Tombol kembali
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> finish());
    }
}
