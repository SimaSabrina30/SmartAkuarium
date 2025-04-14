package com.sima.smartakuarium;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.cardview.widget.CardView;
import androidx.appcompat.app.AppCompatActivity;

public class beranda extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda);

        CardView cardJadwalMakan = findViewById(R.id.cardJadwalMakanIkan);

        cardJadwalMakan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pastikan menggunakan huruf kapital sesuai nama class-nya
                Intent intent = new Intent(beranda.this, jadwalmakanikan.class);
                startActivity(intent);
            }
        });
        CardView cardJadwalPerawatanIkan = findViewById(R.id.cardJadwalPerawatanIkan);
        cardJadwalPerawatanIkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(beranda.this, jadwalperawatanikan.class);
                startActivity(intent);
            }
        });

        // Menambahkan CardView untuk Atur Suhu
        CardView cardAturSuhu = findViewById(R.id.cardAturSuhu);
        cardAturSuhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent untuk membuka halaman atur suhu akuarium
                Intent intent = new Intent(beranda.this, atursuhu.class);
                startActivity(intent);
            }
        });


    }
}
