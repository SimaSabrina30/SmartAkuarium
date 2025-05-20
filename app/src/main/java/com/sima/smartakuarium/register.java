package com.sima.smartakuarium;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class register extends AppCompatActivity {

    EditText etNamaLengkap, etUsername, etPassword;
    Button btnDaftar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etNamaLengkap = findViewById(R.id.etNamaLengkap);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnDaftar = findViewById(R.id.btnDaftar);

        btnDaftar.setOnClickListener(view -> {
            String namaLengkap = etNamaLengkap.getText().toString().trim();
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (namaLengkap.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(register.this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
            } else {
                // ✅ Ganti ke "UserData" agar sesuai dengan yang dibaca ProfileActivity
                SharedPreferences sharedPref = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("namaLengkap", namaLengkap);
                editor.putString("username", username);
                editor.putString("password", password);
                editor.apply();

                Toast.makeText(register.this, "Registrasi berhasil! Silakan login.", Toast.LENGTH_SHORT).show();

                // Pindah ke halaman login
                Intent intent = new Intent(register.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
