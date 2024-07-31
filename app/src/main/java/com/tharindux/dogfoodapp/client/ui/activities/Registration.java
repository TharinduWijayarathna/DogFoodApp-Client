package com.tharindux.dogfoodapp.client.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import com.tharindux.dogfoodapp.client.R;
import com.tharindux.dogfoodapp.client.model.User;

public class Registration extends AppCompatActivity {
    private FirebaseAuth mAuth;
    public static User user;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        firebaseFirestore = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText editTextText = findViewById(R.id.editTextText);
                EditText editTextTextEmailAddress = findViewById(R.id.editTextTextEmailAddress);
                EditText editTextTextPassword = findViewById(R.id.editTextTextPassword);

                String name = editTextText.getText().toString();
                String email = editTextTextEmailAddress.getText().toString();
                String password = editTextTextPassword.getText().toString();

                if(name.length()>0){
                    if(email.length()>0){
                        if(password.length()>0){

                            Toast.makeText(Registration.this, "Wait", Toast.LENGTH_LONG).show();

                            mAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                FirebaseUser user = mAuth.getCurrentUser();

                                                Map<String, Object> U = new HashMap<>();
                                                Map<String, Object> fields = new HashMap<>();
                                                Map<String, Object> Data = new HashMap<>();

                                                Data.put("name", name.toString());
                                                Data.put("email", email.toString());
                                                Data.put("password", password.toString());
                                                Data.put("Address Line 1", "null");
                                                Data.put("Address Line 2", "null");
                                                Data.put("ProfilePic", "null");
                                                Data.put("status", "Active");

                                                U.put("Details", Data);
                                                U.put("Cart", fields);
                                                U.put("PaymentHistory", fields);
                                                firebaseFirestore.collection("User").document(email)
                                                        .set(U)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Intent i = new Intent(Registration.this, MainLayout.class);
//                                                    i.putExtra("email",email);
                                                                startActivity(i);
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(Registration.this, e.toString(),
                                                                        Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
//                                    updateUI(user);
                                            } else {
                                                // If sign in fails, display a message to the user.
                                                System.out.println("createUserWithEmail:failure"+ task.getException());
                                                Toast.makeText(Registration.this, task.getException().toString(),
                                                        Toast.LENGTH_SHORT).show();
//                                    updateUI(null);
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            System.out.println(e);
                                        }
                                    });
                        }else {
                            Toast.makeText(Registration.this, "Please enter the Password", Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(Registration.this, "Please enter the email", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(Registration.this, "Please enter the name", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

}






