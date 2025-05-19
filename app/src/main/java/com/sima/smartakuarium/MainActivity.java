package com.sima.smartakuarium;

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

import androidx.appcompat.app.AppCompatActivity;

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

        // Toggle show/hide password
        ivTogglePassword.setOnClickListener(v -> {
            if (isPasswordVisible) {
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                ivTogglePassword.setImageResource(R.drawable.ic_eye_off);
            } else {
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                ivTogglePassword.setImageResource(R.drawable.ic_eye);
            }
            etPassword.setSelection(etPassword.getText().length());
            isPasswordVisible = !isPasswordVisible;
        });

        // Login saat tombol Sign In ditekan
        btnSignIn.setOnClickListener(view -> {
            String user = etUsername.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();

            SharedPreferences sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            String savedUser = sharedPref.getString("username", "");
            String savedPass = sharedPref.getString("password", "");

            if (user.equals(savedUser) && pass.equals(savedPass)) {
                Intent intent = new Intent(MainActivity.this, beranda.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(MainActivity.this, "Username atau Password salah", Toast.LENGTH_SHORT).show();
            }
        });

        // Teks lupa password
        tvForgotPassword.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Fitur lupa password belum tersedia", Toast.LENGTH_SHORT).show();
        });

        // Teks klikable "Sign Up"
        String text = "Don't Have An Account? Sign Up";
        SpannableString spannableString = new SpannableString(text);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(MainActivity.this, register.class); // Ganti jika class register kamu punya nama berbeda
                startActivity(intent);
            }
        };

        int startIndex = text.indexOf("Sign Up");
        int endIndex = startIndex + "Sign Up".length();
        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvSignUpPrompt.setText(spannableString);
        tvSignUpPrompt.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
