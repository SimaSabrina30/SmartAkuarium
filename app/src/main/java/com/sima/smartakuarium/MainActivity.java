package com.sima.smartakuarium;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnSignIn;
    TextView tvForgotPassword;
    ImageView ivTogglePassword;
    boolean isPasswordVisible = false; // Status toggle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi view
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        ivTogglePassword = findViewById(R.id.ivTogglePassword);

        // Toggle password visibility
        ivTogglePassword.setOnClickListener(v -> {
            if (isPasswordVisible) {
                // Sembunyikan password
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                ivTogglePassword.setImageResource(R.drawable.ic_eye);
            } else {
                // Tampilkan password
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                ivTogglePassword.setImageResource(R.drawable.ic_eye);
            }
            etPassword.setSelection(etPassword.getText().length()); // Geser kursor ke akhir
            isPasswordVisible = !isPasswordVisible;
        });

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
