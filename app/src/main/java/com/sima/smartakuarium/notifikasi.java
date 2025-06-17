package com.sima.smartakuarium;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class notifikasi extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotifikasiAdapter adapter;
    private List<NotifikasiItem> notifikasiList;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifikasi);

        View mainLayout = findViewById(R.id.main); // pastikan di XML ada ID "main"
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

        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(notifikasi.this, beranda.class));
            finish();
        });

        recyclerView = findViewById(R.id.recyclerViewNotifikasi);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        notifikasiList = new ArrayList<>();
        adapter = new NotifikasiAdapter(notifikasiList);
        recyclerView.setAdapter(adapter);

        sharedPreferences = getSharedPreferences("notifikasi", MODE_PRIVATE);
        loadNotifikasi();
    }

    private void loadNotifikasi() {
        notifikasiList.clear();
        try {
            JSONArray array = new JSONArray(sharedPreferences.getString("histori", "[]"));
            for (int i = array.length() - 1; i >= 0; i--) { // terbaru di atas
                JSONObject obj = array.getJSONObject(i);
                notifikasiList.add(new NotifikasiItem(obj.getString("pesan"), obj.getString("waktu")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }

    public static void simpanNotifikasi(SharedPreferences prefs, String pesan, String waktu) {
        try {
            JSONArray array = new JSONArray(prefs.getString("histori", "[]"));
            JSONObject obj = new JSONObject();
            obj.put("pesan", pesan);
            obj.put("waktu", waktu);
            array.put(obj);
            prefs.edit().putString("histori", array.toString()).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
