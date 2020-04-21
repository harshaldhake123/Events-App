package com.example.clubapplication;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class AddEventActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 2;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();
    FirebaseAuth mAuth;

    EditText etEventName, etVenue, etDate, etDetails;
    Button btnAdd, btnUpload;
    ImageView ivEvent;
    private Uri mImageUri;
    String image = "NULL";
    private StorageReference mStorageRef, mStorageReference;
    Picasso picasso;

    String miUrlOk = "";
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        etDate = findViewById(R.id.etDate);
        etEventName = findViewById(R.id.etEventName);
        etVenue = findViewById(R.id.etVenue);
        etDetails = findViewById(R.id.etDetails);
        btnAdd = findViewById(R.id.btnAddEvent);
        btnUpload = findViewById(R.id.btnUpload);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        ivEvent = findViewById(R.id.ivEvent);

        mStorageRef = FirebaseStorage.getInstance().getReference("EventPhotos");

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String eventName1 = etEventName.getText().toString().trim();
                final String venue = etVenue.getText().toString().trim();
                final String date1 = etDate.getText().toString().trim();
                final String details = etDetails.getText().toString().trim();
                if (!(etEventName.getText().toString().isEmpty() || etVenue.getText().toString().isEmpty() || etDetails.getText().toString().isEmpty() || etDate.getText().toString().isEmpty())) {
                    DatabaseReference databaseRef1 = databaseReference.child("EventData");
                    databaseRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(eventName1)) {
                                Toast.makeText(AddEventActivity.this, "Event Already Exists ", Toast.LENGTH_SHORT).show();
                            } else {
                                addDetails(eventName1, venue, date1, details);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(AddEventActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(AddEventActivity.this, "Fill All Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void addDetails(String eventName, String venue, String date, String details) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        Methods m = new Methods();
        final String organizer = m.usernameFromEmail(user.getEmail());

        DatabaseReference databaseRef2 = databaseReference.child("EventData");
        Event eventData = new Event(eventName, venue, date, organizer, details);
        databaseRef2.child(eventName).child("Details").setValue(eventData);
        if (mImageUri != null) {
            uploadImage_10(eventName);
        }
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.with(this).load(mImageUri).into(ivEvent);
        } else {
            Toast.makeText(AddEventActivity.this, "Failed to Select image", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadImage_10(final String EName) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Posting");
        pd.show();
        if (mImageUri != null) {
            final StorageReference fileReference = mStorageRef.child(EName
                    + "." + getFileExtension(mImageUri));

            uploadTask = fileReference.putFile(mImageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        miUrlOk = downloadUri.toString();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("EventData");
                        reference.child(EName).child("Details").child("image").setValue(String.valueOf(miUrlOk));

                        pd.dismiss();
                        Toast.makeText(AddEventActivity.this, "Event Uploaded Successfully", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(AddEventActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddEventActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(AddEventActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }
}