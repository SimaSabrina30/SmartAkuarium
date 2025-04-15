package com.sima.smartakuarium;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnSignIn;
    TextView tvForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi view
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        // Tombol Sign In
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = etUsername.getText().toString().trim();
                String pass = etPassword.getText().toString().trim();

                if (user.equals("admin") && pass.equals("admin123")) {
                    // Login berhasil, pindah ke halaman beranda
                    Intent intent = new Intent(MainActivity.this, beranda.class);
                    startActivity(intent);
                    finish(); // Tutup halaman login
                } else {
                    // Login gagal
                    Toast.makeText(MainActivity.this, "Username atau Password salah", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Tombol Lupa Password
        tvForgotPassword.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Fitur lupa password belum tersedia", Toast.LENGTH_SHORT).show();
        });
    }
}
