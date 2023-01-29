package com.example.finalprojectnetwork;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.finalprojectnetwork.databinding.ActivityPersonBinding;
import com.example.finalprojectnetwork.modle.ImageAdapter;
import com.example.finalprojectnetwork.modle.MyObject;
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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PersonActivity extends AppCompatActivity {
    ActivityPersonBinding binding;
    public FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPersonBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();


        binding.update.setOnClickListener(view -> startActivity(new Intent(getBaseContext(),UpdateActivity.class)));
        binding.Favourites.setOnClickListener(view -> startActivity(new Intent(getBaseContext(),FavouriteActivity.class)));
        binding.btnLogout.setOnClickListener(view -> {
            mAuth.signOut();
            Intent intent = new Intent(PersonActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(PersonActivity.this, "You have been logged out", Toast.LENGTH_SHORT).show();
        });
        binding.btnCourse.setOnClickListener(view ->startActivity(new Intent(PersonActivity.this,CourseActivity.class)));
    }

}
//TODO: reference.getDownloadUrl() : هادا بجيب رابط الصورة اللي انا ضفتها


//        binding.upload.setOnClickListener(view -> {
//            // Create an intent to open the file picker
//            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//            intent.setType("*/*");
//            intent.addCategory(Intent.createChooser(intent,"select file"),CATEGORY_OPENABLE);
//
//            // Start the file picker activity
//            startActivityForResult(intent, PICK_FILE_REQUEST);
//        });
//
//// ...
//        // Create a reference to the file to be uploaded
//        if (fileUri != null) {
//            StorageReference fileReference = reference.getReference().child(System.currentTimeMillis()
//                    + "." + getFileExtension(String.valueOf(fileUri)));
//
//// Use the putFile() method to upload the file to Cloud Storage
//            UploadTask uploadTask = fileReference.putFile(fileUri);
//
//// Register observers to listen for when the upload is completed or if it fails
//            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    // Upload was successful, so get the download URL for the file
//                    taskSnapshot.getMetadata().getReference().getDownloadUrl()
//                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                @Override
//                                public void onSuccess(Uri downloadUrl) {
//                                    // Save the download URL to the database
//                                    // (for example, to save it to a Firestore document)
//                                    saveDownloadUrlToDatabase(downloadUrl.toString());
//                                }
//                            });
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//                    // Upload failed
//                    Toast.makeText(getApplicationContext(), "Upload failed!", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//        }
//    }
//
//    private void saveDownloadUrlToDatabase(String downloadUrl) {
//        // Create a new document in the "files" collection
//        Map<String, Object> file = new HashMap<>();
//        file.put("url", downloadUrl);
//
//        // Add the new document to the "files" collection
//        db.collection("files").add(file)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        // Document added successfully
//                        Toast.makeText(getApplicationContext(), "File saved to database", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        // Error adding document
//                        Toast.makeText(getApplicationContext(), "Error saving file to database", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // Check which request we're responding to
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_FILE_REQUEST) {
//            // Make sure the request was successful
//            if (resultCode == RESULT_OK) {
//                // The user selected a file. Get the URI of the file.
//                fileUri = data.getData();
//                // لمن ترجع الداتا نعمل الابلود
//            }
//        }
//    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//        reference.getReference().child(user.getUid())
//        .listAll()
//        .addOnSuccessListener(new OnSuccessListener<ListResult>() {
//            @Override
//            public void onSuccess(ListResult listResult) {
//
//            }
//        })
//        .addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(PersonActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//        //اختيار صورة من المعرص
//        ActivityResultLauncher<String> ar1 =
//                registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
//                    @Override
//                    public void onActivityResult(Uri result) {
////                        binding.picImg.setImageURI(result);
//                    }
//                });
//
//        binding.upload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                        ar1.launch("image/*");
//            }
//        });


//TODO : File/ privet_/ Favourite/ RV-ForAnotherUser
//TODO: List For Image/File
