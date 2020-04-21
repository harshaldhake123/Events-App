package com.example.clubapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.clubapplication.MyEventActivity;
import com.example.clubapplication.AddEventActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Event_fragment extends Fragment {
    Button btnSignOut, btnMyEvents, btnAddEvents, btnRegisteredEvents;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_event, container, false);

        btnSignOut = view.findViewById(R.id.btnSignOut);
        btnMyEvents = view.findViewById(R.id.btnMyEvents);
        btnAddEvents = view.findViewById(R.id.btnAddEvents);
        btnRegisteredEvents = view.findViewById(R.id.btnRegisteredEvents);

        btnAddEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddEventActivity.class);
                startActivity(intent);
            }
        });

        btnMyEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyEventActivity.class);
                startActivity(intent);
            }
        });

        btnRegisteredEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RegisteredEventActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}