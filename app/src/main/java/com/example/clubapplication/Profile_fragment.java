package com.example.clubapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.zip.Inflater;

import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile_fragment extends Fragment {
    Button btnUpdate,btnSignOut;
    EditText etName,etEmail,etPhone;
    FirebaseAuth mAuth;

    DatabaseReference database1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view= inflater.inflate(R.layout.layout_profile,container,false);

        etName=view.findViewById(R.id.etName);
        etPhone=view.findViewById(R.id.etPhone);
        etEmail=view.findViewById(R.id.etEmail);

        mAuth=FirebaseAuth.getInstance();
        btnUpdate = view.findViewById(R.id.btnUpdate);
        btnSignOut=view.findViewById(R.id.btnSignOut);

        final FirebaseAuth Auth;
        Auth=FirebaseAuth.getInstance();
        FirebaseUser user = Auth.getCurrentUser();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString().trim();
                String email_id = etEmail.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();

                FirebaseUser user=mAuth.getCurrentUser();
                String username1 = usernameFromEmail(user.getEmail());

                if (!(email_id.isEmpty() || phone.isEmpty() || name.isEmpty())){
                    String username2 = usernameFromEmail(email_id);
                    User userdata = new User(email_id,name,phone);
                    database1 = FirebaseDatabase.getInstance().getReference().child("userData");
                    database1.child(username1).removeValue();
                    database1.child(username2).setValue(userdata);
                }
                else {
                    Toast.makeText(getActivity(),"Fill All Fields",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),LoginActivity.class) ;
                Auth.signOut();
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user=mAuth.getCurrentUser();
        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference().child("userData");
        String username1 = usernameFromEmail(user.getEmail());
        DatabaseReference userRef = dRef.child(username1);

        etEmail.setText(user.getEmail());

        userRef.child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value= dataSnapshot.getValue(String.class);
                etName.setText(value);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        userRef.child("phone").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                etPhone.setText(value);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){
            }
        });
    }

    public String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }

    }
}


