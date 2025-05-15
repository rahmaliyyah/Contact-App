package com.example.contactappfirebasepam;


import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {
    EditText etEmail, etPass;
    MaterialButton btnSignup;
    TextView tvLogin;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        etEmail = findViewById(R.id.et_email);
        etPass = findViewById(R.id.et_pass);
        btnSignup = findViewById(R.id.btn_daftar);
        tvLogin = findViewById(R.id.login);
        mAuth = FirebaseAuth.getInstance();
        btnSignup.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String password = etPass.getText().toString();
            if (!email.isEmpty() && !password.isEmpty()) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                finish();
                            } else {
                                Toast.makeText(this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(this, "Email and Password cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        });
    }
}
