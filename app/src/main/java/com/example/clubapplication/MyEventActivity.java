package com.example.clubapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

public class MyEventActivity extends AppCompatActivity {

    TextView tvEvent1;
    ListView myEventList;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference =database.getReference().child("EventData");
    ArrayList<String> arrayList;
    int flag =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_event);

        tvEvent1=findViewById(R.id.tvMEvents);
        myEventList=findViewById(R.id.myEventList);

        arrayList = new ArrayList<String>();

        final FirebaseAuth Auth;
        Auth=FirebaseAuth.getInstance();
        FirebaseUser user = Auth.getCurrentUser();

        final String emailID =user.getEmail();
        Methods m = new Methods();
        final String userName = m.usernameFromEmail(emailID);

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


        myEventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyEventActivity.this,EventParticipantsActivity.class);
                intent.putExtra("EventName",arrayList.get(position));
                startActivity(intent);
            }
        });
    }
    public void check(final String EName, final String UName){
        databaseReference.child(EName).child("Details").child("organizer").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String o = dataSnapshot.getValue(String.class);
                if(o.equals(UName)) {
                    arrayList.add(EName);
                    flag=1;
                    tvEvent1.setVisibility(View.GONE);
                    ArrayAdapter arrayAdapter = new ArrayAdapter(MyEventActivity.this, android.R.layout.simple_list_item_1, arrayList);
                    myEventList.setAdapter(arrayAdapter);
                }
                if(flag==0) {
                    tvEvent1.setText("No Events Uploaded Yet");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
