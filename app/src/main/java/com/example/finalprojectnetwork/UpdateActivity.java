package com.example.finalprojectnetwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.finalprojectnetwork.databinding.ActivityUpdateBinding;
import com.example.finalprojectnetwork.modle.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

//////////////////// Net Work Final project //////////////////////////
public class UpdateActivity extends AppCompatActivity {
    ActivityUpdateBinding binding;
    FirebaseFirestore db;
    FirebaseUser user;
    public FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        db=FirebaseFirestore.getInstance();
        user = firebaseAuth.getCurrentUser();


        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(binding.UpdateUser.getText().toString())
                .build();

        binding.Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register(user,userProfileChangeRequest);

            }
        });
    }

    private void register(FirebaseUser firebaseUser, UserProfileChangeRequest userProfileChangeRequest) {
        String password = binding.UpdatePass.getText().toString();
        String major = binding.UpdateMajor.getText().toString();
        String username = binding.UpdateUser.getText().toString();
        boolean public_=binding.switch2.isChecked();

        firebaseUser.updateProfile(userProfileChangeRequest)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
                        Map<String, Object> data = new HashMap<>();
                        data.put(Util.Major, major);
                        data.put(Util.UserName , username);
                        data.put(Util.public_,public_);
                        FirebaseFirestore.getInstance().collection(Util.CollocationPath).document(userId).
                                update(data)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(UpdateActivity.this, "Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                })
                                .addOnFailureListener(e -> Toast.makeText(UpdateActivity.this, "errorRegister", Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(UpdateActivity.this, "errorRegister", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(UpdateActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());

        if (!password.isEmpty()) {
            firebaseUser.updatePassword(password)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(UpdateActivity.this, "password updated", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(UpdateActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                Toast.makeText(UpdateActivity.this, "error password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}