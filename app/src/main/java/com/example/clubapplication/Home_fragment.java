package com.example.clubapplication;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.clubapplication.R.color.colorPrimaryLight;

public class Home_fragment extends Fragment {
    FirebaseAuth Auth;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    ArrayList<Event> eventList;
    ArrayList<String>eventNames;
    EventAdapter1 adapter;
    LinearLayout l1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view= inflater.inflate(R.layout.layout_home,container,false);
        Auth = FirebaseAuth.getInstance();

        recyclerView = view.findViewById(R.id.myRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        eventList = new ArrayList<>();
        eventNames = new ArrayList<>();


        l1=view.findViewById(R.id.L1);

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading");
        pd.show();

        databaseReference= FirebaseDatabase.getInstance().getReference().child("EventData");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    check(dataSnapshot1.getKey());
                }
                pd.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(),"Failed to load Data",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
    public void check(String EName)
    {
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
        databaseReference1.child("EventData").child(EName).child("Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Event e = dataSnapshot.getValue(Event.class);
                eventList.add(e);
                adapter = new EventAdapter1(getActivity(), eventList, "0");
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){
                Toast.makeText(getActivity(), "Something is Wrong", Toast.LENGTH_SHORT).show();
            }

        });
    }
}
