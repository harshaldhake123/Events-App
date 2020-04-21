package com.example.clubapplication;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Methods {
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();


    public void register(String eventName, String userMail) {
        String userName = usernameFromEmail(userMail);
        databaseReference.child("EventData").child(eventName).child("Participants").child(userName).setValue(userMail);
    }

    public String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }
}
