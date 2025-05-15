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

public class AddContactActivity extends AppCompatActivity {
    private TextView tvEmail;
    private EditText etName, etNumber;
    private MaterialButton btnAdd, btnKeluar;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference contactsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }
        String userId = currentUser.getUid();
        contactsRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("contacts");
        tvEmail = findViewById(R.id.tv_email);
        etName = findViewById(R.id.et_edit_name);
        etNumber = findViewById(R.id.et_edit_number);
        btnAdd = findViewById(R.id.btn_add);
        btnKeluar = findViewById(R.id.btn_keluar);
        tvEmail.setText(currentUser.getEmail());
        btnAdd.setOnClickListener(v -> addContact());
        btnKeluar.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(AddContactActivity.this, MainActivity.class));
            finish();
        });
    }
    private void addContact() {
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
        String contactId = contactsRef.push().getKey();
        if (contactId != null) {
            Contact contact = new Contact(contactId, name, number);
            contactsRef.child(contactId).setValue(contact)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(AddContactActivity.this, "Contact added successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Return to contact list
                    })
                    .addOnFailureListener(e -> Toast.makeText(AddContactActivity.this, "Failed to add contact", Toast.LENGTH_SHORT).show());
        }
    }
}