package com.sima.smartakuarium;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    private TextView txtNama, txtEmail;
    private ImageView fotoProfil;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Inset biar tidak nabrak status bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inisialisasi View
        txtNama = findViewById(R.id.txtNama);
        txtEmail = findViewById(R.id.txtEmail);
        fotoProfil = findViewById(R.id.fotoProfil);
        ImageButton btnBack = findViewById(R.id.btnBack);
        Button btnLogout = findViewById(R.id.btnLogout);

        // Ambil data dari SharedPreferences
        sharedPref = getSharedPreferences("UserData", MODE_PRIVATE);
        String namaLengkap = sharedPref.getString("namaLengkap", "-");
        String username = sharedPref.getString("username", "-");
        String fotoUriString = sharedPref.getString("fotoProfilUri", null);

        // Tampilkan data
        txtNama.setText(namaLengkap);
        txtEmail.setText(username);

        if (fotoUriString != null) {
            try {
                Uri uri = Uri.parse(fotoUriString);
                fotoProfil.setImageURI(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Klik foto untuk ubah
        fotoProfil.setOnClickListener(v -> handlePhotoSelection());

        // Klik nama untuk mengubah
        txtNama.setOnClickListener(v -> {
            EditText input = new EditText(ProfileActivity.this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setText(txtNama.getText().toString());

            new AlertDialog.Builder(ProfileActivity.this)
                    .setTitle("Ubah Nama Lengkap")
                    .setView(input)
                    .setPositiveButton("Simpan", (dialog, which) -> {
                        String namaBaru = input.getText().toString().trim();
                        if (!namaBaru.isEmpty()) {
                            txtNama.setText(namaBaru);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("namaLengkap", namaBaru);
                            editor.apply();
                            Toast.makeText(ProfileActivity.this, "Nama berhasil diperbarui", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ProfileActivity.this, "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Batal", null)
                    .show();
        });

        // Tombol kembali
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, beranda.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        // Tombol logout
        btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.clear();
            editor.apply();
            showLogoutSuccessDialog();
        });
    }

    private void handlePhotoSelection() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q || checkStoragePermission()) {
            openGallery();
        } else {
            requestPermissionLauncher.launch(getRequiredPermission());
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }

    private boolean checkStoragePermission() {
        String permission = getRequiredPermission();
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private String getRequiredPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return Manifest.permission.READ_MEDIA_IMAGES;
        } else {
            return Manifest.permission.READ_EXTERNAL_STORAGE;
        }
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    openGallery();
                } else {
                    Toast.makeText(this, "Izin dibutuhkan untuk memilih foto profil", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                fotoProfil.setImageURI(selectedImageUri);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("fotoProfilUri", selectedImageUri.toString());
                editor.apply();
            }
        }
    }

    private void showLogoutSuccessDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.sukses_logout, null);
        Button btnClose = view.findViewById(R.id.btnClose);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        AlertDialog dialog = builder.create();

        btnClose.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        dialog.show();
    }
}
