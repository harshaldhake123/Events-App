package com.example.clubapplication;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class RegisteredEventActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference =database.getReference().child("EventData");

    RecyclerView recyclerView;
    RecyclerView.Adapter myAdapter2;
    RecyclerView.LayoutManager layoutManager;
    EventAdapter1 adapter;

    TextView tvREvents;
    ArrayList<Event> eventList1;
    ArrayList<String>eNames;


    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_registered_event);

            tvREvents = findViewById(R.id.tvREvents);

        final FirebaseAuth Auth;
        Auth=FirebaseAuth.getInstance();
        FirebaseUser user = Auth.getCurrentUser();
        final String emailID =user.getEmail();


            Methods m = new Methods();
            final String userName = m.usernameFromEmail(emailID);

            recyclerView = findViewById(R.id.list);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(RegisteredEventActivity.this);
            recyclerView.setLayoutManager(layoutManager);
            eNames = new ArrayList<String>();

            eventList1 = new ArrayList<Event>();

            databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        check(dataSnapshot1.getKey(),userName);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

            if(!(tvREvents.getText().toString().equals("⚫ Registered Events ⚫"))) {
            tvREvents.setText("⚫ No Events Registered Yet ⚫");
            }
    }

    public void check(String EName, final String UName){
        final String eventName = EName;
        databaseReference.child(eventName).child("Participants").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(UName))
                    add(eventName);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    public void add(String EName)
    {
        DatabaseReference eventRef = databaseReference.child(EName).child("Details");
        eventRef.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Event e = dataSnapshot.getValue(Event.class);
                eventList1.add(e);
                adapter = new EventAdapter1(RegisteredEventActivity.this, eventList1, "1");
                recyclerView.setAdapter(adapter);
                tvREvents.setText("⚫ Registered Events ⚫");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){
            }
        });
    }
}