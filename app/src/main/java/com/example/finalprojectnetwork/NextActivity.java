package com.example.finalprojectnetwork;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.finalprojectnetwork.databinding.ActivityNextBinding;
import com.example.finalprojectnetwork.modle.DeleteListener;
import com.example.finalprojectnetwork.modle.Summary;
import com.example.finalprojectnetwork.modle.SummaryAdapter;
import com.example.finalprojectnetwork.modle.MyObject;
import com.example.finalprojectnetwork.modle.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.Futures;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.google.firestore.v1.WriteResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Future;

public class NextActivity extends AppCompatActivity {
    ActivityNextBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseStorage reference;
    FirebaseFirestore db;
    List<Summary> summaries;
    SummaryAdapter adapter;
    StorageReference storageReference;
    Uri mainImageURI;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNextBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        reference = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();

        summaries = new ArrayList<>();

        binding.rv.setLayoutManager(new LinearLayoutManager(getBaseContext(), RecyclerView.VERTICAL, false));
        adapter = new SummaryAdapter(summaries, getBaseContext(), new DeleteListener() {
            @Override
            public void deleteId(int id) {
                create();
            }
        });
        binding.rv.setAdapter(adapter);

        getFiles();
    }

    private void getFiles() {
        String document = getIntent().getStringExtra(Util.document);
        String DocumentID = getIntent().getStringExtra("DocumentID");
        String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        db.collection(Util.CollocationCourse)
                .document(document)
                .collection(userId)
                // تحديثات في الوقت الحالي في الفايرستور
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                        summaries.clear();
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
//                                summaries.clear();
//                                String s = dc.getDocument().getData().toString();
//                                Log.d("document: ", s);
                                summaries.add(dc.getDocument().toObject(Summary.class));
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void create() {
        String documentId = getIntent().getStringExtra(Util.document);
        String DocumentID = getIntent().getStringExtra("DocumentID");
        String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

//        db.collection(Util.CollocationCourse)
//                .document(document)
//                .collection(userId)
//                .document(String.valueOf(id))
//                .delete()
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        Toast.makeText(NextActivity.this, "remove", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(NextActivity.this, String.valueOf(id), Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(NextActivity.this, "error delete", Toast.LENGTH_SHORT).show();
//
//            }
//        });
//        updateProfile();
        db.collection(Util.CollocationCourse)
                .document(documentId)
                .collection(userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                db.collection("users").document(userId).collection("favorites")
//                                        .document(document.getId()).delete();
                                db.collection(Util.CollocationCourse).document(documentId).collection(userId)
                                        .document(document.getId()).delete();
                                Toast.makeText(getBaseContext(), "delete", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if(task.isSuccessful()){
//                            summaries.remove(task);
//                            Toast.makeText(NextActivity.this, "remove", Toast.LENGTH_SHORT).show();
//                            Toast.makeText(NextActivity.this, String.valueOf(id), Toast.LENGTH_SHORT).show();
//                        }
//                        else {
//                            Toast.makeText(NextActivity.this, "error delete", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });

    }
//        String document = getIntent().getStringExtra(Util.document);
//        String DocumentID = binding.edDocumentID.getText().toString();
//        String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
//
////        storageReference = reference.getReference("images/" + firebaseUser.getUid() +
////                "/" + mainImageURI.getLastPathSegment());
////
////        StorageTask<UploadTask.TaskSnapshot> uploadTask =
////                storageReference.putFile(mainImageURI);
////        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
////            @Override
////            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
////                storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
////                    @Override
////                    public void onComplete(@NonNull Task<Uri> task) {
////                        profileImageUrl1 = task.getResult().toString();
////                        Glide.with(getBaseContext()).load(profileImageUrl1).transform(new RoundedCorners(8)).
////                                error(R.drawable.img).into(binding.showImg);
////                        Summary summary = new Summary();
////                        summary.setText(DocumentID);
////                        summary.setUserId(userId);
////                        summary.setImage(profileImageUrl1);
//
//                        db.collection( Util.CollocationCourse ).document( document ).collection( userId )
//                                .add( summary ).addOnSuccessListener(aVoid ->
//                                Toast.makeText(getBaseContext(), "success", Toast.LENGTH_SHORT).show());
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                    }
////                });
////            }
////        });
////        updateProfile();
////        uploadImage();
//    }

    private void updateProfile() {
        UserProfileChangeRequest userProfileChangeRequest =
                new UserProfileChangeRequest.Builder()
                        .setPhotoUri(mainImageURI)
                        .build();
        firebaseUser.updateProfile(userProfileChangeRequest)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        firebaseUser.reload();
//                        Glide.with(getBaseContext()).load(firebaseUser.getPhotoUrl()).into(binding.showImg);
                        Toast.makeText(getBaseContext(), "Profile Update for", Toast.LENGTH_SHORT).show();
                    } else {
                        Objects.requireNonNull(task.getException()).printStackTrace();
                        Toast.makeText(getBaseContext(), "null", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
