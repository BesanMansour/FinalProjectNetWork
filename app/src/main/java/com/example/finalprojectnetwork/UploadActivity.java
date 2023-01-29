package com.example.finalprojectnetwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.finalprojectnetwork.databinding.ActivityUplodeBinding;
import com.example.finalprojectnetwork.modle.ImageAdapter;
import com.example.finalprojectnetwork.modle.Summary;
import com.example.finalprojectnetwork.modle.Util;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class UploadActivity extends AppCompatActivity {
    ActivityUplodeBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseStorage storage;
    FirebaseFirestore db;
    private static final int GALLERY_INTENT = 1000;
    StorageReference storageReference;
    Uri mainImageURI;
    public static String profileImageUrl1;
    ArrayList<String> strings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUplodeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();

        String document = getIntent().getStringExtra(Util.document);
        String DocumentID = binding.edDocumentID.getText().toString();


        binding.btnNext.setOnClickListener(v -> {
            Intent intent = new Intent(UploadActivity.this, NextActivity.class);
            intent.putExtra(Util.document, document);
            intent.putExtra("DocumentID", DocumentID);
            startActivity(intent);
        });
        binding.btnUplode2.setOnClickListener(view -> create());

        binding.image.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
//            image/*
            intent.setType("*/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select file"), GALLERY_INTENT);
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mainImageURI = data.getData();
            if (mainImageURI.getPath() != null) {
//                mainImageURI.getPath():  امتداد الصورة
                strings.add(mainImageURI.getPath());
                Log.d("mainImageURI: ",mainImageURI.getPath());
            }
        }
    }
    private void create(){
        String document = getIntent().getStringExtra(Util.document);
        String DocumentID = binding.edDocumentID.getText().toString();
        String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        storageReference = storage.getReference("images/" + firebaseUser.getUid() +
                "/" + mainImageURI.getLastPathSegment());

        StorageTask<UploadTask.TaskSnapshot> uploadTask =
                storageReference.putFile(mainImageURI);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        profileImageUrl1 = task.getResult().toString();
                        Glide.with(getBaseContext()).load(profileImageUrl1).transform(new RoundedCorners(8)).
                                error(R.drawable.img).into(binding.showImg);
                        Summary summary = new Summary();
                        summary.setText(DocumentID);
                        summary.setUserId(userId);
                        summary.setImage(profileImageUrl1);

                        db.collection( Util.CollocationCourse ).document( document ).collection( userId )
                                .add( summary ).addOnSuccessListener(aVoid ->
                                Toast.makeText(getBaseContext(), "success", Toast.LENGTH_SHORT).show());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });
        updateProfile();
        uploadImage();
    }
    //------------------------------------------------------------------------------------------------------------------------------
    private void updateProfile() {
        UserProfileChangeRequest userProfileChangeRequest =
                new UserProfileChangeRequest.Builder()
                        .setPhotoUri(mainImageURI)
                        .build();
        firebaseUser.updateProfile(userProfileChangeRequest)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        firebaseUser.reload();
                        Glide.with(getBaseContext()).load(firebaseUser.getPhotoUrl()).into(binding.showImg);
                        Toast.makeText(getBaseContext(), "Profile Update for", Toast.LENGTH_SHORT).show();
                    } else {
                        Objects.requireNonNull(task.getException()).printStackTrace();
                        Toast.makeText(getBaseContext(), "null", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //---------------------------start storage------------------------------------
    private void uploadImage() {

        storageReference = storage.getReference("images/" + firebaseUser.getUid() +
                "/" + mainImageURI.getLastPathSegment());

        StorageTask<UploadTask.TaskSnapshot> uploadTask =
                storageReference.putFile(mainImageURI);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(getBaseContext(), "Image Uploaded!!", Toast.LENGTH_SHORT).show();
            storageReference.getDownloadUrl().addOnCompleteListener(task -> {
                profileImageUrl1 = task.getResult().toString();
                Glide.with(getBaseContext()).load(profileImageUrl1).transform(new RoundedCorners(8)).
                        error(R.drawable.img).into(binding.showImg);
                Log.i("UploadActivity", profileImageUrl1);
            });
        }).addOnFailureListener(e -> {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), "Image Uploaded Failed!!", Toast.LENGTH_SHORT).show();
        });
    }

    private void getFiles() {
        String document = getIntent().getStringExtra(Util.document);
        String DocumentID = binding.edDocumentID.getText().toString().trim();
        String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        DocumentReference docRef = db.collection(Util.CollocationCourse).document(document)
                .collection(userId).document(DocumentID);

        storage.getReference().child("images/" + firebaseUser.getUid() + "/")
                .listAll().addOnSuccessListener(listResult -> {
            for (StorageReference item : listResult.getItems()) {
                item.getDownloadUrl().addOnSuccessListener(uri -> {
                    strings.add(uri.toString());
//                            urls = new ImageAdapter(strings, getApplicationContext());
//                            binding.RVField.setAdapter(urls);
//                            binding.RVField.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));

                }).addOnFailureListener(exception -> {
                    // Handle any errors
                });
            }
        })
                .addOnFailureListener(e -> Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show());
//
//        //-----------------------end storage------------------------------------------
//        String document = getIntent().getStringExtra(Util.document);
//        String DocumentID = binding.edDocumentID.getText().toString().trim();
//        String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
//        Map<String, Object> data = new HashMap<>();
//        data.put("strings", strings);

//        FirebaseFirestore.getInstance().collection(Util.CollocationCourse).document(document).
//                collection(userId).document(DocumentID)
//                .set(data)
//                .addOnSuccessListener(aVoid -> startActivity(new Intent(getApplicationContext(), LoginActivity.class)))
//                .addOnFailureListener(e ->
//                        Toast.makeText(UploadActivity.this, "errorRegister", Toast.LENGTH_SHORT).show());
//
//        DocumentReference docRef = db.collection(Util.CollocationCourse).document(document)
//                .collection(userId).document(DocumentID);
//        docRef.get().addOnSuccessListener(documentSnapshot -> {
//            documentSnapshot.getData().get("strings");
//            ArrayList<String> data1 = (ArrayList<String>) documentSnapshot.getData().get("strings");
//            binding.RVField.setAdapter(new ImageAdapter(data1, UploadActivity.this));
//            binding.RVField.setLayoutManager(new GridLayoutManager(UploadActivity.this, 3));
    };
//    private void create() {
//        updateProfile();
//        uploadImage();
//
//
//        String document = getIntent().getStringExtra(Util.document);
//        String DocumentID = binding.edDocumentID.getText().toString();
//        String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
//        StorageReference storageRef = storage.getReference().child("images/image.jpg");
//        InputStream stream = null;
//        try {
//             stream = new FileInputStream(new File(mainImageURI.getPath()));
//            UploadTask uploadTask = storageRef.putStream(stream);
//            uploadTask.addOnFailureListener( exception -> {
//                // Handle unsuccessful uploads
//            } ).addOnSuccessListener( taskSnapshot -> {
//                // Handle successful uploads
//            } );
//
//            Task <Uri> urlTask = uploadTask.continueWithTask( task -> {
//                if (!task.isSuccessful()) {
//                    throw task.getException();
//                }
//
//                // Continue with the task to get the download URL
//                return storageRef.getDownloadUrl();
//            } ).addOnCompleteListener( task -> {
//                if (task.isSuccessful()) {
//                    Uri downloadUri = task.getResult();
//
//                    // Save the download URL to Firestore
//                    Map<String, Object> data = new HashMap<>();
//                    data.put("image_url", downloadUri.toString());
//                    db.collection(
//                                    Util.CollocationCourse).document(document).
//                            collection(userId).document(DocumentID).set(data);
//                } else {
//                    // Handle failures
//                }
//            } );
//        } catch ( FileNotFoundException e ) {
//            e.printStackTrace( );
//        }
//
////        UploadTask uploadTask = storageRef.putStream(stream);
////        uploadTask.addOnFailureListener(new OnFailureListener() {
////            @Override
////            public void onFailure(@NonNull Exception exception) {
////                // Handle unsuccessful uploads
////            }
////        }).addOnSuccessListener(new OnSuccessListener <UploadTask.TaskSnapshot>() {
////            @Override
////            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
////                // Handle successful uploads
////            }
////        });
////
////        Task <Uri> urlTask = uploadTask.continueWithTask( new Continuation
////                <UploadTask.TaskSnapshot, Task<Uri>>() {
////            @Override
////            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
////                if (!task.isSuccessful()) {
////                    throw task.getException();
////                }
////
////                // Continue with the task to get the download URL
////                return storageRef.getDownloadUrl();
////            }
////        }).addOnCompleteListener(new OnCompleteListener <Uri>() {
////            @Override
////            public void onComplete(@NonNull Task<Uri> task) {
////                if (task.isSuccessful()) {
////                    Uri downloadUri = task.getResult();
////
////                    // Save the download URL to Firestore
////                    Map<String, Object> data = new HashMap<>();
////                    data.put("image_url", downloadUri.toString());
////                    db.collection(
////                                    Util.CollocationCourse).document(document).
////                            collection(userId).document(DocumentID).set(data);
////                } else {
////                    // Handle failures
////                }
////            }
////        });
//
//
//
//
////        Map<String , Object> data = new HashMap<>();
////        data.put("strings", strings);
////
////        DocumentReference documentReference = db.collection(
////                Util.CollocationCourse).document(document).
////                collection(userId).document(DocumentID);
////        documentReference.set(data).addOnSuccessListener(aVoid ->
////                Toast.makeText(getBaseContext(), "success", Toast.LENGTH_SHORT).show());
//
////        DocumentReference docRef = db.collection(Util.CollocationCourse).document(document)
////                .collection(userId).document(DocumentID);
////        docRef.get().addOnSuccessListener( documentSnapshot -> {
////            documentSnapshot.getData().get( "strings" );
////            ArrayList<String> data1 = ( ArrayList < String > ) documentSnapshot.getData().get( "strings" );
////            binding.RVField.setAdapter( new ImageAdapter( data1 ,UploadActivity.this ) );
////            binding.RVField.setLayoutManager( new GridLayoutManager( UploadActivity.this,3 ) );
////        } );
//    }



}