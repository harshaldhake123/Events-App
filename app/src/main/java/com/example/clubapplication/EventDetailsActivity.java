package com.example.clubapplication;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;


public class EventDetailsActivity extends AppCompatActivity {
    Button btnRegister;
    TextView  tvEventName,tvEventVenue, tvEventDate, tvEventDetails, tvPhoneNo, tvMail;
    ImageView ivEventPhoto;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();
    Picasso picasso;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        btnRegister = findViewById(R.id.registerbtn);

        tvEventDate = findViewById(R.id.tvEventDate);
        tvEventDetails = findViewById(R.id.tvEventDetails);
        tvEventVenue = findViewById(R.id.tvEventVenue);
        tvMail = findViewById(R.id.tvMail);
        tvPhoneNo = findViewById(R.id.tvPhoneNo);
        tvEventName = findViewById(R.id.tvEventName);
        ivEventPhoto = findViewById(R.id.ivEventPhoto);

        final String EventName = getIntent().getStringExtra("EventName");
        tvEventName.setText("⚫ Event Name : " + EventName);
        final String flag = getIntent().getStringExtra("flag");

        if(flag.equals("1"))
        {
            btnRegister.setVisibility(View.GONE);
            btnRegister.setText("Registered");
            btnRegister.setClickable(false);
            btnRegister.setBackgroundResource(R.drawable.btnregistered);
        }

        mAuth = FirebaseAuth.getInstance();
        DatabaseReference dRef1 = database.getReference().child("EventData").child(EventName).child("Details");

        dRef1.child("date").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String date = dataSnapshot.getValue(String.class);
                tvEventDate.setText("⚫ Date : " + date);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        dRef1.child("image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String image = dataSnapshot.getValue(String.class);
                if(!image.equals("NULL"))
                    picasso.with(EventDetailsActivity.this).load(image).into(ivEventPhoto);
                else
                    ivEventPhoto.setImageResource(R.drawable.no);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        dRef1.child("details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String details = dataSnapshot.getValue(String.class);
                tvEventDetails.setText("⚫ Details : " + details);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        dRef1.child("venue").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Venue = dataSnapshot.getValue(String.class);
                tvEventVenue.setText("⚫ Venue : " + Venue);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        dRef1.child("organizer").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String organizer = dataSnapshot.getValue(String.class);
                forEmail(organizer);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Methods m = new Methods();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                m.register(EventName, currentUser.getEmail());
                Toast.makeText(EventDetailsActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                btnRegister.setText("Registered");
                btnRegister.setClickable(false);
                btnRegister.setBackgroundResource(R.drawable.btnregistered);
            }
        });
    }

    public void forEmail(final String userName){
        DatabaseReference userRef5 = databaseReference.child("userData");
        userRef5.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(userName)){
                    User user = new User("Null","Null","Null");
                    tvMail.setText("⚫ Email : "+ user.getEmail());
                    tvPhoneNo.setText("⚫ Contact No : " + user.getPhone());
                }
                else{
                    add(userName);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    public void add(String name){
     DatabaseReference userRef5 = databaseReference.child("userData").child(name);
        userRef5.child("phone").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String PhoneNo = dataSnapshot.getValue(String.class);
                tvPhoneNo.setText("⚫ Contact No : " + PhoneNo);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        userRef5.child("email").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String email = dataSnapshot.getValue(String.class);
                tvMail.setText("⚫ Email : " + email);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    public void callme()
    {
        btnRegister.setClickable(false);
        btnRegister.setBackgroundResource(R.drawable.button_rounded);
    }
}