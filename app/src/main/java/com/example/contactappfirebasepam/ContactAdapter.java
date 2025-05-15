package com.example.contactappfirebasepam;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private List<Contact> contactList;
    private Context context;
    private DatabaseReference contactsRef;
    public ContactAdapter(Context context) {
        this.context = context;
        this.contactList = new ArrayList<>();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.contactsRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("contacts");
    }
    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        holder.tvName.setText(contact.getName());
        holder.tvNumber.setText(contact.getPhoneNumber());
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditContactActivity.class);
            intent.putExtra("contactId", contact.getId());
            intent.putExtra("contactName", contact.getName());
            intent.putExtra("contactNumber", contact.getPhoneNumber());
            context.startActivity(intent);
        });
        holder.btnCall.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + contact.getPhoneNumber()));
            context.startActivity(callIntent);
        });
        holder.tvDelete.setOnClickListener(v -> {
            contactsRef.child(contact.getId()).removeValue()
                    .addOnSuccessListener(aVoid -> {
                        int adapterPosition = holder.getAdapterPosition();
                        if (adapterPosition != RecyclerView.NO_POSITION) {
                            contactList.remove(adapterPosition);
                            notifyItemRemoved(adapterPosition);
                        }
                        Toast.makeText(context, "Contact deleted successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> Toast.makeText(context, "Failed to delete contact", Toast.LENGTH_SHORT).show());
        });
    }
    @Override
    public int getItemCount() {
        return contactList.size();
    }
    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
        notifyDataSetChanged();
    }
    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvNumber, tvDelete;
        MaterialButton btnEdit, btnCall;
        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvNumber = itemView.findViewById(R.id.tv_number);
            tvDelete = itemView.findViewById(R.id.tv_delete);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnCall = itemView.findViewById(R.id.btn_call);
        }
    }
}