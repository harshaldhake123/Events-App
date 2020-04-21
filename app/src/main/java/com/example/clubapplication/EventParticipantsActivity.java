package com.example.clubapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class EventParticipantsActivity extends AppCompatActivity {
    ListView eventParticipants;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference =database.getReference().child("EventData");
    ArrayList<String> arrayList;
    TextView tvParicipant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_participants);
        eventParticipants=findViewById(R.id.eventParticipants);

        tvParicipant = findViewById(R.id.tvParticipant);

        arrayList = new ArrayList<String>();
        final String EventName = getIntent().getStringExtra("EventName");

        databaseReference.child(EventName).child("Participants").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    arrayList.add("⚫ "+dataSnapshot1.getKey());
                    ArrayAdapter arrayAdapter = new ArrayAdapter(EventParticipantsActivity.this, android.R.layout.simple_list_item_1, arrayList);
                    eventParticipants.setAdapter(arrayAdapter);
                    tvParicipant.setText("⚫ Participants ⚫");
                }

                if(!(tvParicipant.getText().toString().equals("⚫ Participants ⚫"))) {
                    tvParicipant.setText("⚫ No one have registered for this event ⚫");
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        eventParticipants.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }
}