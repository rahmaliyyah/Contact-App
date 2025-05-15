package com.example.contactappfirebasepam;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;

public class EditContactActivity extends AppCompatActivity {
    private TextView tvEmail;
    private EditText etName, etNumber;
    private MaterialButton btnSave, btnKeluar;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference contactsRef;

    private String contactId, contactName, contactNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }
        contactId = getIntent().getStringExtra("contactId");
        contactName = getIntent().getStringExtra("contactName");
        contactNumber = getIntent().getStringExtra("contactNumber");

        if (contactId == null) {
            Toast.makeText(this, "Contact ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        String userId = currentUser.getUid();
        contactsRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("contacts");
        tvEmail = findViewById(R.id.tv_email);
        etName = findViewById(R.id.et_edit_name);
        etNumber = findViewById(R.id.et_edit_number);
        btnSave = findViewById(R.id.btn_save);
        btnKeluar = findViewById(R.id.btn_keluar);
        tvEmail.setText(currentUser.getEmail());
        etName.setText(contactName);
        etNumber.setText(contactNumber);
        btnSave.setOnClickListener(v -> updateContact());
        btnKeluar.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(EditContactActivity.this, MainActivity.class));
            finish();
        });
    }
    private void updateContact() {
        String name = etName.getText().toString().trim();
        String number = etNumber.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            etName.setError("Name is required");
            etName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(number)) {
            etNumber.setError("Phone number is required");
            etNumber.requestFocus();
            return;
        }
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", name);
        updates.put("phoneNumber", number);
        contactsRef.child(contactId).updateChildren(updates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(EditContactActivity.this, "Contact updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(EditContactActivity.this, "Failed to update contact", Toast.LENGTH_SHORT).show());
    }
}