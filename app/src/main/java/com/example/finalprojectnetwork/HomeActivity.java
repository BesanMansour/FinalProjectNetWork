package com.example.finalprojectnetwork;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static com.example.finalprojectnetwork.modle.Util.UserId;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.finalprojectnetwork.databinding.ActivityHomeBinding;
import com.example.finalprojectnetwork.modle.FragmentAdapter;
import com.example.finalprojectnetwork.modle.Listener;
import com.example.finalprojectnetwork.modle.SummaryAdapter;
import com.example.finalprojectnetwork.modle.User;
import com.example.finalprojectnetwork.modle.UserAdapter;
import com.example.finalprojectnetwork.modle.Util;
import com.example.finalprojectnetwork.modle.User;
import com.example.finalprojectnetwork.modle.UserAdapter;
import com.example.finalprojectnetwork.modle.Util;
import com.example.finalprojectnetwork.modle.userItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;
    FirebaseAuth firebaseAuth;
    UserAdapter userAdapter;
    FirebaseFirestore db;
    public static List<User> users;
    public static ArrayList<String> strings;
    String x ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        users = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        strings= new ArrayList <>(  );
//        binding.HomeFavourites.setOnClickListener(view -> startActivity(new Intent(getBaseContext(),FavouriteActivity.class)));
        binding.imageView2.setOnClickListener(view -> startActivity(new Intent(getBaseContext(), PersonActivity.class)));
        retrieveUserData();
        getUsers();
        ArrayList<String> tab = new ArrayList<>();
        tab.add("NetWork");
        tab.add("SoftWare");
        tab.add("WEB");
        tab.add("WorkShop");

        ArrayList<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < tab.size(); i++) {
            fragments.add(BlankFragment2.newInstance(tab.get(i), getUsers(),users.size()));

        }
        FragmentAdapter adapter = new FragmentAdapter(this, fragments);
        binding.VP.setAdapter(adapter);
        new TabLayoutMediator(binding.TL, binding.VP, (tab2, position) -> tab2.setText(tab.get(position))).attach();

    }

    private String getUsers() {
        CollectionReference usersRef = db.collection(Util.CollocationPath);
        usersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    User user = document.toObject(User.class);
                    if(user.isPublic()){
                    users.add(user);
                    }
                    UserAdapter userAdapter = new UserAdapter(users, UserId -> {
                        Intent intent = new Intent(getApplicationContext(), UserCourseActivity.class);
                        intent.putExtra( Util.UserId , task.getResult().getDocuments().get(UserId).getId());
                        x=task.getResult().getDocuments().get(UserId).getId();
                        startActivity(intent);
                    });
                    userAdapter.setUserList(users);

                    binding.Recycler.setAdapter(userAdapter);
                    binding.Recycler.setLayoutManager(new LinearLayoutManager
                            (this, RecyclerView.VERTICAL, false));
                }

            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
        return x;
    }





    private void getFiles() {
//        for ( int i = 0 ; i < users.size() ; i++ ) {
//            String document = tab.;
//            db.collection(Util.CollocationCourse)
//                    .document(document)
//                    .collection(x)
//                    .addSnapshotListener((value, error) -> {
//                        for ( DocumentChange dc : value.getDocumentChanges()) {
//                            if (dc.getType() == DocumentChange.Type.ADDED) {
//                                String s = dc.getDocument().getData().toString();
//                                Log.d("document: ", s);
//                                userItems.add(dc.getDocument().toObject( userItem.class));
//                            }
//                            adapter.notifyDataSetChanged();
//                        }
//                    });
//        }

    }


    @SuppressLint("SetTextI18n")
    private void retrieveUserData() {
        String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        FirebaseFirestore.getInstance().collection(Util.CollocationPath).document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    String email = documentSnapshot.getString(Util.Email);
                    String major = documentSnapshot.getString(Util.Major);
                    String username = documentSnapshot.getString(Util.UserName);
                    // Use the retrieved data as needed
                    Toast.makeText(this, email + "\n" + major + "\n" + username, Toast.LENGTH_SHORT).show();
                    binding.tv.setText(username + "\n" + email + "\n" + major);
                })
                .addOnFailureListener(e -> Toast.makeText(HomeActivity.this,
                        "Error retrieving data", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}