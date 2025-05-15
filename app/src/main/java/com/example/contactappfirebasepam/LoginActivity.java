package com.example.contactappfirebasepam;


import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText etEmail, etPass;
    MaterialButton btnLogin;
    TextView tvSignup;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etEmail = findViewById(R.id.et_email);
        etPass = findViewById(R.id.et_pass);
        btnLogin = findViewById(R.id.btn_masuk);
        tvSignup = findViewById(R.id.signup);
        mAuth = FirebaseAuth.getInstance();
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String password = etPass.getText().toString();
            if (!email.isEmpty() && !password.isEmpty()) {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent = new Intent(LoginActivity.this, ContactActivity.class);
                                intent.putExtra("email", user.getEmail());
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(this, "Email and Password cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
        tvSignup.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        });
    }
}
