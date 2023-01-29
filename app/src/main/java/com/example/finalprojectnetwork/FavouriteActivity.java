package com.example.finalprojectnetwork;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.finalprojectnetwork.databinding.ActivityFavouriteBinding;
import com.example.finalprojectnetwork.modle.MyObject;
import com.example.finalprojectnetwork.modle.Summary;
import com.example.finalprojectnetwork.modle.SummaryAdapter;
import com.example.finalprojectnetwork.modle.UserItemAdapter;
import com.example.finalprojectnetwork.modle.Util;
import com.example.finalprojectnetwork.modle.userItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FavouriteActivity extends AppCompatActivity {
    ActivityFavouriteBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseStorage reference;
    FirebaseFirestore db;
    List<MyObject> strings;

    //    List<Summary> summaries;
    List<userItem> userItems;
    //    SummaryAdapter adapter;
    UserItemAdapter adapter;
    userItem u;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavouriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        reference = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();
        strings = new ArrayList<>();
        userItems = new ArrayList<>();

        binding.FavRv.setLayoutManager(new LinearLayoutManager(getBaseContext(), RecyclerView.VERTICAL, false));
        adapter = new UserItemAdapter(userItems, getBaseContext());
        binding.FavRv.setAdapter(adapter);

        getFiles();
    }

    private void getFiles() {
        String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        db.collection("users").document(userId).collection("favorites")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        userItems.clear();
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
//                                String s = dc.getDocument().getData().toString();
//                                Log.d("document: ", s);
                                userItems.add(dc.getDocument().toObject(userItem.class));
                            }
                            adapter.notifyDataSetChanged();
                        }

                    }
                });
    }

}