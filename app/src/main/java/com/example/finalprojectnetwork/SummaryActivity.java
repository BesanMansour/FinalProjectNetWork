package com.example.finalprojectnetwork;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.finalprojectnetwork.databinding.ActivitySummaryBinding;
import com.example.finalprojectnetwork.modle.ImageAdapter;
import com.example.finalprojectnetwork.modle.MyAdapter;
import com.example.finalprojectnetwork.modle.MyObject;
import com.example.finalprojectnetwork.modle.User;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class SummaryActivity extends AppCompatActivity {
    ActivitySummaryBinding binding;
    FirebaseStorage reference;
    FirebaseUser currentUser;
    FirebaseFirestore db;
    StorageReference storageReference;
    public FirebaseAuth mAuth;
    String UserId;
    final List<MyObject> imageUrls = new ArrayList<>();

    ImageAdapter urls;
    ArrayList<MyObject> strings = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySummaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        mAuth = FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        reference = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();

        UserId = getIntent().getStringExtra("UserId");
        Toast.makeText(this, UserId+"", Toast.LENGTH_SHORT).show();

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference usersRef = firestore.collection("usersProfile");
        usersRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@Nonnull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

//// Get a reference to the data of each user
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("images/");
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
//                    User user = userSnapshot.getValue(User.class);
//                    imageUrls.add(user.getName());
//                    Toast.makeText(SummaryActivity.this, user.getName()+"", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Toast.makeText(SummaryActivity.this, error.getMessage()+"", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//// Set the adapter to the RecyclerView
////        RecyclerView recyclerView = findViewById(R.id.recycler_view);
//        ImageAdapter adapter = new ImageAdapter(imageUrls);
//        binding.SummaryRv.setAdapter(adapter);
//        binding.SummaryRv.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));


        getFiles(UserId);
//        getInfoByUserUid();

    }

    private void getFiles(String UserId) {

//        ((LoginActivity)getActivity()).showDialog();

        // عشان نجيب البيانات اللي أنا حطيتها
        //getReference :هادا للمكان اللي انا حاطة الداتا تبعتي فيه
        //  مجلد الimages اللي حطيت فيه الصور هادا عبارة عن تشايلد

        reference.getReference().child("images/" + UserId + "/")
                .listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference item : listResult.getItems()) {
                    item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            strings.add(new MyObject( uri.toString() ));
                            urls = new ImageAdapter(strings,getApplicationContext());
                            binding.SummaryRv.setAdapter(urls);
                            binding.SummaryRv.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                                    RecyclerView.VERTICAL, false));


//                            ArrayList<String> url = new ArrayList<>();
//                            urls.add(uri.toString());

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@Nonnull Exception exception) {
                            // Handle any errors
                        }
                    });
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

//        RecyclerView recyclerView = findViewById(R.id.recycler_view);
//        FileAdapter adapter = new FileAdapter(urls,getBaseContext());
//        binding.Rv.setAdapter(adapter);
//        binding.Rv.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
//
//    }

        //-----------------------end storage------------------------------------------
    }

//    private void getInfoByUserUid() {
//        DocumentReference reference=  db.collection("Users")
//                //document() : لانو احنا حطينا البيانات جوا الدوكيومنت
//                .document(currentUser.getUid());
//        reference.collection("info")
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        for (DocumentSnapshot documentSnapshot :
//                                queryDocumentSnapshots.getDocuments()) {
//                            Toast.makeText(SummaryActivity.this, "queryDocumentSnapshots: "+queryDocumentSnapshots, Toast.LENGTH_SHORT).show();
//                            Log.d("queryDocumentSnapshots ",queryDocumentSnapshots.toString());
//                        }
//
//                    }
//                });
//    }
//











//    private void getFiles(){
////        ((LoginActivity)getActivity()).showDialog();
//
//        // عشان نجيب البيانات اللي أنا حطيتها
//        //getReference :هادا للمكان اللي انا حاطة الداتا تبعتي فيه
//        //  مجلد الimages اللي حطيت فيه الصور هادا عبارة عن تشايلد
//        reference.getReference().child("images/" + currentUser.getUid()+ "/")
//                .listAll()
//                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
//                    @Override
//                    public void onSuccess(ListResult listResult) {
//                        // ListResult : رجعلي هنا ليست أف ريزلت اللي حيكون فيها ريفسرنس للصورة اللي احنا حملناها
//                        // List<StorageReference> : بترجع هادي اللي هوا الرابط تبع الصورة اللي انا رفعتها
////                binding.progress.setVisibility(View.GONE);
////                        ((LoginActivity)getActivity()).dismissDialog()
////                       ;
//
//                        Toast.makeText(SummaryActivity.this,"user: "+ currentUser.toString() , Toast.LENGTH_SHORT).show();
//                        Toast.makeText(SummaryActivity.this, "listResult: "+listResult.getItems(), Toast.LENGTH_SHORT).show();
////                        setAdapter(listResult.getItems());
//                        MyAdapter myAdapter = new MyAdapter(listResult.getItems(),getApplicationContext());
//                        binding.SummaryRv.setAdapter(myAdapter);
//                        binding.SummaryRv.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }

//    private void setAdapter(List<MyObject> objects){
//        MyAdapter adapter = new MyAdapter(objects,getApplicationContext());
//        binding.SummaryRv.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL,false));
//        binding.SummaryRv.setAdapter(adapter);
//    }
}