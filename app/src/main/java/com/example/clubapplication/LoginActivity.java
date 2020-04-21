package com.example.clubapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    public EditText loginEmailId, logInpasswd;
    public Button btnLogIn;
    public TextView signup,forgotpassword;
    public FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        loginEmailId = findViewById(R.id.loginEmail);
        logInpasswd = findViewById(R.id.loginpaswd);
        btnLogIn = findViewById(R.id.btnLogIn);
        signup = findViewById(R.id.TVSignIn);
        forgotpassword=findViewById(R.id.tvforgotpassword);
        pd = new ProgressDialog(LoginActivity.this);
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                checkIfLogin();
            }
        };
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(I);
            }
        });

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pd.setMessage("Loading");
                pd.show();
                userLogin();
            }
        });

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPassword();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }


    void checkIfLogin(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            Toast.makeText(LoginActivity.this, "User logged in ", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(LoginActivity.this, "Login to continue", Toast.LENGTH_SHORT).show();
        }
    }
    void userLogin(){
        final String userEmail = loginEmailId.getText().toString().trim();
        String userPaswd = logInpasswd.getText().toString().trim();
        if (userEmail.isEmpty()) {
            pd.dismiss();
            loginEmailId.setError("Enter Email");
            loginEmailId.requestFocus();
        } else if (userPaswd.isEmpty() || userPaswd.length()<8) {
            pd.dismiss();
            logInpasswd.setError("Enter Password of min length 8!");
            logInpasswd.requestFocus();
        }
        else if (!(userEmail.isEmpty() && userPaswd.isEmpty())) {
            firebaseAuth.signInWithEmailAndPassword(userEmail, userPaswd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (!task.isSuccessful()) {
                        pd.dismiss();
                        Toast.makeText(LoginActivity.this, "Couldn't Login",Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        pd.dismiss();
                    }
                }
            });
        } else {
            Toast.makeText(LoginActivity.this, "Couldn't Log in. Please check your Credentials.", Toast.LENGTH_SHORT).show();
        }
    }

    void forgotPassword(){

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String useremail = loginEmailId.getText().toString();
        if (useremail.isEmpty()) {
            loginEmailId.setError("Enter email");
            loginEmailId.requestFocus();
        }
        else{
            auth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Password Reset Email sent.\nCheck your mail", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Couldn't send Verification Mail", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}