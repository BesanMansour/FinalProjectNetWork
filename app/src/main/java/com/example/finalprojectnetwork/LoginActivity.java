package com.example.finalprojectnetwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.finalprojectnetwork.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.RegisterBtn.setOnClickListener(view -> startActivity(new Intent(getBaseContext(), RegisterActivity.class)));
        binding.LoginBtn.setOnClickListener(view -> Login());
    }

    private void Login() {
        String email = binding.LoginEmail.getText().toString();
       String  pass = binding.LoginPass.getText().toString();
        // وهادا الريسبونس
        firebaseAuth.signInWithEmailAndPassword(email, pass)
                // عشان نعرف انو العملية تمت بنجاح
                //onComplete : العملية نجحت بغض النظر نجحت ولا فشلت
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // getUid : هادا التوكن اللي بميز كل يوزر
                        //AuthResult : بترجع يوزر كيف بدنا نجيبو
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        finish();
                    } else {
                        Toast.makeText( LoginActivity.this, Objects.requireNonNull( task.getException( ) ).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } )
        .addOnFailureListener( e -> Toast.makeText( LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show() );

    }
    @Override
    protected void onStart() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        super.onStart();
        if (firebaseUser!=null){
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            finish();
        }
    }
}
