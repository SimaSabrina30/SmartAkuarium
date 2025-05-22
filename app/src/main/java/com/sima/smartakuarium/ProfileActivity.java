package com.sima.smartakuarium;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProfileActivity extends AppCompatActivity {

    private static final int REQUEST_PICK_IMAGE = 1;

    TextView txtNama, txtEmail;
    ImageView fotoProfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Atur padding agar tidak bentrok dengan status bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Referensi view dari layout
        txtNama = findViewById(R.id.txtNama);
        txtEmail = findViewById(R.id.txtEmail);
        fotoProfil = findViewById(R.id.fotoProfil);
        ImageButton btnBack = findViewById(R.id.btnBack);
        Button btnLogout = findViewById(R.id.btnLogout);

        // Ambil data user dari SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences("UserData", MODE_PRIVATE);
        String namaLengkap = sharedPref.getString("namaLengkap", "-");
        String username = sharedPref.getString("username", "-");
        String fotoUriString = sharedPref.getString("fotoProfilUri", null);

        // Tampilkan ke tampilan
        txtNama.setText(namaLengkap);
        txtEmail.setText(username);

        // Tampilkan gambar jika ada
        if (fotoUriString != null) {
            Uri uri = Uri.parse(fotoUriString);
            fotoProfil.setImageURI(uri);
        }

        // Saat foto diklik, minta izin jika perlu, lalu buka galeri
        fotoProfil.setOnClickListener(v -> handlePhotoSelection());

        // Kembali ke beranda
        btnBack.setOnClickListener(v -> navigateToHome());

        // Logout dengan dialog kustom
        btnLogout.setOnClickListener(v -> handleLogout());
    }

    private void handlePhotoSelection() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q || checkStoragePermission()) {
            openGallery();
        } else {
            requestPermissionLauncher.launch(getRequiredPermission());
        }
    }

    // Fungsi membuka galeri
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*"); // hanya tampilkan gambar
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }

    // Fungsi logout dengan dialog
    private void handleLogout() {
        // Hapus session login
        SharedPreferences userSession = getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor editor = userSession.edit();
        editor.clear();
        editor.apply();

        // Tampilkan dialog logout sukses
        showLogoutSuccessDialog();
    }

    private void showLogoutSuccessDialog() {
        // Inflate layout kustom untuk dialog
        View view = LayoutInflater.from(this).inflate(R.layout.sukses_logout, null);

        // Referensi elemen dalam layout dialog
        Button btnClose = view.findViewById(R.id.btnClose);

        // Buat dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        AlertDialog dialog = builder.create();

        // Set tombol tutup
        btnClose.setOnClickListener(v -> {
            dialog.dismiss();

            // Pindah ke halaman login setelah dialog ditutup
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        dialog.show();
    }

    private void navigateToHome() {
        Intent intent = new Intent(ProfileActivity.this, beranda.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    // Fungsi mengecek izin
    private boolean checkStoragePermission() {
        String permission = getRequiredPermission();
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    // Fungsi menentukan izin yang dibutuhkan berdasarkan versi Android
    private String getRequiredPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return Manifest.permission.READ_MEDIA_IMAGES;
        } else {
            return Manifest.permission.READ_EXTERNAL_STORAGE;
        }
    }

    // Minta izin secara runtime
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    openGallery();
                } else {
                    Toast.makeText(this, "Izin dibutuhkan untuk memilih foto profil", Toast.LENGTH_SHORT).show();
                }
            });

    // Hasil dari memilih gambar
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                fotoProfil.setImageURI(selectedImageUri);

                // Simpan URI ke SharedPreferences
                SharedPreferences.Editor editor = getSharedPreferences("UserData", MODE_PRIVATE).edit();
                editor.putString("fotoProfilUri", selectedImageUri.toString());
                editor.apply();
            }
        }
    }
}
