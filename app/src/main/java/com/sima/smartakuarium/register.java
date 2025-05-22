package com.sima.smartakuarium;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {

    EditText etNamaLengkap, etUsername, etPassword;
    Button btnDaftar;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etNamaLengkap = findViewById(R.id.etNamaLengkap);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnDaftar = findViewById(R.id.btnDaftar);

        db = FirebaseFirestore.getInstance();

        btnDaftar.setOnClickListener(view -> {
            String namaLengkap = etNamaLengkap.getText().toString().trim();
            String email = etUsername.getText().toString().trim(); // username = email
            String password = etPassword.getText().toString().trim();

            if (namaLengkap.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(register.this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseAuth mAuth = FirebaseAuth.getInstance();

            // Buat akun di Firebase Authentication
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Jika berhasil buat akun, simpan info ke Firestore juga
                            Map<String, Object> user = new HashMap<>();
                            user.put("namaLengkap", namaLengkap);
                            user.put("username", email); // Email disimpan sebagai username

                            db.collection("pengguna")
                                    .document(email)
                                    .set(user)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(register.this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(register.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(register.this, "Gagal menyimpan ke Firestore", Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            // Jika gagal buat akun (misalnya email sudah digunakan)
                            Toast.makeText(register.this, "Registrasi gagal: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

    }
}
