package com.example.contactappfirebasepam;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends AppCompatActivity {
    private TextView tvEmail, tvOption;
    private MaterialButton btnKeluar;
    private RecyclerView recyclerView;
    private ContactAdapter contactAdapter;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference contactsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
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
        tvOption = findViewById(R.id.tv_option);
        btnKeluar = findViewById(R.id.btn_keluar);
        recyclerView = findViewById(R.id.recycle_contact);
        tvEmail.setText(currentUser.getEmail());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactAdapter = new ContactAdapter(this);
        recyclerView.setAdapter(contactAdapter);
        loadContacts();
        tvOption.setOnClickListener(v -> {
            startActivity(new Intent(ContactActivity.this, AddContactActivity.class));
        });
        btnKeluar.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(ContactActivity.this, MainActivity.class));
            finish();
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadContacts();
    }
    private void loadContacts() {
        contactsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Contact> contacts = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Contact contact = snapshot.getValue(Contact.class);
                    if (contact != null) {
                        contacts.add(contact);
                    }
                }
                contactAdapter.setContactList(contacts);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}