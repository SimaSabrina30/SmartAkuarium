package com.sima.smartakuarium;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class detailakuarium extends AppCompatActivity {
    Button btnRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailakuarium);

        btnRefresh = findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(view -> {
            Toast.makeText(this, "Data diperbarui!", Toast.LENGTH_SHORT).show();
        });
    }
}
