package com.example.finalprojectnetwork;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalprojectnetwork.databinding.FragmentBlank2Binding;
import com.example.finalprojectnetwork.modle.MyObject;
import com.example.finalprojectnetwork.modle.User;
import com.example.finalprojectnetwork.modle.UserAdapter;
import com.example.finalprojectnetwork.modle.UserItemAdapter;
import com.example.finalprojectnetwork.modle.Util;
import com.example.finalprojectnetwork.modle.userItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class BlankFragment2 extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_UserId = "ARG_UserId";
    private static final String ARG_Users = "ARG_Users";

    private String mParam1, UserID;
    private int anInt;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseStorage reference;
    FirebaseFirestore db;
    List<MyObject> strings1;
    public static List<User> users;
    public static ArrayList<String> strings;
    String x ;

    //    List<Summary> summaries;
    List<userItem> userItems;
    //    SummaryAdapter adapter;
    UserItemAdapter adapter;

    public BlankFragment2() {
    }

    public static BlankFragment2 newInstance(String param1 , String users , int users1 ) {
        BlankFragment2 fragment = new BlankFragment2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_UserId, users);
        args.putInt( ARG_Users,users1 );
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            UserID = getArguments().getString(ARG_UserId);
            anInt =getArguments().getInt( ARG_Users );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentBlank2Binding binding = FragmentBlank2Binding.inflate(inflater, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        reference = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();
        strings1 = new ArrayList<>();
        userItems = new ArrayList<>();
        users=new ArrayList <>(  );
        String document = mParam1;

//        getFiles();
        binding.rv.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,
                false));
        adapter = new UserItemAdapter(userItems, getContext());
        binding.rv.setAdapter(adapter);
        getUsers();
        return binding.getRoot();
    }
    private void getUsers() {
        CollectionReference usersRef = db.collection(Util.CollocationPath);
        usersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for ( QueryDocumentSnapshot document : task.getResult()) {
                    User user = document.toObject(User.class);
                    if(user.isPublic()){
                        users.add(user);
                    }
                        for ( int i = 0 ; i < users.size() ; i++ ) {
                            String document1 = mParam1;
                            db.collection(Util.CollocationCourse)
                                    .document(document1)
                                    .collection(task.getResult().getDocuments().get( i).getId())
                                    .addSnapshotListener((value, error) -> {
                                        for ( DocumentChange dc : value.getDocumentChanges()) {
                                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                                String s = dc.getDocument().getData().toString();
                                                Log.d("document: ", s);
                                                userItems.add(dc.getDocument().toObject( userItem.class));
                                            }
                                            adapter.notifyDataSetChanged();
                                        }
                                    });
                        }
                }

            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
    }

}