package com.example.finalprojectnetwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Toast;

import com.example.finalprojectnetwork.databinding.ActivityRegisterBinding;
import com.example.finalprojectnetwork.modle.Util;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        binding.rBtn.setOnClickListener(view -> register());
    }

    private void register() {
        String email = binding.rEmail.getText().toString();
        String password = binding.rPass.getText().toString();
        String major = binding.AREdMajor.getText().toString();
        String username = binding.AREdUserName.getText().toString();
        boolean public_ = binding.switch1.isChecked();

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Save the user's data to Cloud Firestore
                        String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
                        Map<String, Object> data = new HashMap<>();
                        data.put(Util.Email, email);
                        data.put(Util.Major, major);
                        data.put(Util.UserName , username);
                        data.put(Util.public_,public_);

                        FirebaseFirestore.getInstance().collection(Util.CollocationPath).document(userId).set(data)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(RegisterActivity.this, "Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                    finish();
                                })
                                .addOnFailureListener(e -> Toast.makeText(RegisterActivity.this, "errorRegister", Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(RegisterActivity.this, "errorRegister", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());

//        firebaseAuth.sendPasswordResetEmail(email)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()){
//                            Toast.makeText(RegisterActivity.this, "Email sent.", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
    }
}