package com.example.contactappfirebasepam;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    MaterialButton btnStarted;
    TextView tvLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStarted = findViewById(R.id.btn_started);
        tvLogin = findViewById(R.id.login);

        btnStarted.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SignupActivity.class));
        });

        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });
    }
}
