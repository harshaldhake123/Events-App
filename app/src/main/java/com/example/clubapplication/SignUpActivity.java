package com.example.clubapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaCodec;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.provider.ContactsContract;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.regex.Pattern;


public class SignUpActivity extends AppCompatActivity {
    public EditText etName, etEmailId, etPassword,etPhone;
    Button btnSignUp;
    TextView tvSignIn;
    FirebaseAuth firebaseAuth;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        etName=findViewById(R.id.etName);
        etEmailId = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etPhone=findViewById(R.id.etPhone);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvSignIn = findViewById(R.id.tvSignIn);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(I);
            }
        });
    }
    public void signUp(){
        final String username=etName.getText().toString().trim();
        final String emailID = etEmailId.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();
        final String phone = etPhone.getText().toString().trim();

        if (emailID.isEmpty()) {
            etEmailId.setError("Enter Email");
            etEmailId.requestFocus();
        } else if (password.isEmpty() || password.length() < 8) {
            etPassword.setError("Enter Password of min length 8!");
            etPassword.requestFocus();
        } else if (username.isEmpty()) {
            etName.setError("Enter Name!");
            etName.requestFocus();
        } else if (phone.isEmpty() || phone.length()<8) {
            etPhone.setError("Enter Phone number!");
            etPhone.requestFocus();
        }else if (!(emailID.isEmpty() || password.isEmpty() || username.isEmpty() || phone.isEmpty())) {
            firebaseAuth.createUserWithEmailAndPassword(emailID, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(SignUpActivity.this.getApplicationContext(),
                                "SignUp unsuccessful: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                        String username1 = usernameFromEmail(user.getEmail());

                        User userdata=new User(emailID,username,phone);
                        databaseReference.child("userData").child(username1).setValue(userdata);

                        Toast.makeText(SignUpActivity.this.getApplicationContext(),
                                "SignUp successful: " ,Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent( SignUpActivity.this,MainActivity.class);
                        intent.putExtra("userEmail",emailID);
                        startActivity(intent);
                    }
                }
            });
        }
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

}





