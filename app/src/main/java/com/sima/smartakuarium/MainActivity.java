package com.sima.smartakuarium;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnSignIn;
    TextView tvForgotPassword, tvSignUpPrompt;
    ImageView ivTogglePassword;
    boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi komponen
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        ivTogglePassword = findViewById(R.id.ivTogglePassword);
        tvSignUpPrompt = findViewById(R.id.tvSignUpPrompt);

        // Periksa notifikasi logout
        checkLogoutNotification();

        // Toggle show/hide password
        ivTogglePassword.setOnClickListener(v -> togglePasswordVisibility());

        // Login saat tombol Sign In ditekan
        btnSignIn.setOnClickListener(view -> handleLogin());

        // Teks klikable "Sign Up"
        setSignUpClickableText();

        // Teks lupa password, panggil method onForgotPasswordClick saat diklik
        tvForgotPassword.setOnClickListener(this::onForgotPasswordClick);
    }

    private void checkLogoutNotification() {
        // Periksa apakah logout berhasil
        boolean logoutSuccess = getIntent().getBooleanExtra("logout_success", false);
        if (logoutSuccess) {
            // Tampilkan dialog sukses logout
            showSuccessDialog();
        }
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            ivTogglePassword.setImageResource(R.drawable.ic_eye_off);
        } else {
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            ivTogglePassword.setImageResource(R.drawable.ic_eye);
        }
        etPassword.setSelection(etPassword.getText().length());
        isPasswordVisible = !isPasswordVisible;
    }

    private void handleLogin() {
        String inputEmail = etUsername.getText().toString().trim(); // Username = Email
        String inputPassword = etPassword.getText().toString().trim();

        if (inputEmail.isEmpty() || inputPassword.isEmpty()) {
            Toast.makeText(this, "Email atau password tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(inputEmail, inputPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Ambil nama lengkap dari Firestore
                            FirebaseFirestore.getInstance()
                                    .collection("pengguna")
                                    .document(inputEmail)
                                    .get()
                                    .addOnSuccessListener(documentSnapshot -> {
                                        String namaLengkap = documentSnapshot.getString("namaLengkap");
                                        saveUserSession(inputEmail, namaLengkap != null ? namaLengkap : "");

                                        Intent intent = new Intent(MainActivity.this, beranda.class);
                                        intent.putExtra("isLoginSuccessful", true);
                                        startActivity(intent);
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(MainActivity.this, "Gagal ambil data profil", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Login gagal: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void saveUserSession(String username, String namaLengkap) {
        SharedPreferences userSession = getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor sessionEditor = userSession.edit();
        sessionEditor.putString("username", username);
        sessionEditor.putString("namaLengkap", namaLengkap);
        sessionEditor.apply();
    }

    private void setSignUpClickableText() {
        String text = "Don't Have An Account? Sign Up";
        SpannableString spannableString = new SpannableString(text);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(MainActivity.this, register.class);
                startActivity(intent);
            }
        };

        int startIndex = text.indexOf("Sign Up");
        int endIndex = startIndex + "Sign Up".length();
        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvSignUpPrompt.setText(spannableString);
        tvSignUpPrompt.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void showSuccessDialog() {
        Toast.makeText(this, "Logout berhasil!", Toast.LENGTH_SHORT).show();
    }

    // Method untuk menangani klik lupa password
    public void onForgotPasswordClick(View view) {
        Intent intent = new Intent(MainActivity.this, ForgotPassword.class);
        startActivity(intent);
    }
}
