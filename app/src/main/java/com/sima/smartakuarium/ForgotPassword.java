package com.sima.smartakuarium;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private EditText etEmail;
    private Button btnKirim;
    private FirebaseAuth mAuth; // Firebase Authentication

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password); // Pastikan nama file XML-nya sama

        etEmail = findViewById(R.id.etEmail);
        btnKirim = findViewById(R.id.btnKirim);
        mAuth = FirebaseAuth.getInstance();

        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    etEmail.setError("Email tidak boleh kosong");
                    return;
                }

                // Kirim link reset password via Firebase
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(ForgotPassword.this,
                                        "Link reset password telah dikirim ke email kamu.",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(ForgotPassword.this,
                                        "Gagal mengirim reset email. Periksa email dan coba lagi.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
