package com.example.finalprojectnetwork;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.finalprojectnetwork.databinding.FragmentBlankBinding;
import com.example.finalprojectnetwork.modle.FileAdapter;
import com.example.finalprojectnetwork.modle.Image;
import com.example.finalprojectnetwork.modle.ImageAdapter;
import com.example.finalprojectnetwork.modle.MyObject;
import com.example.finalprojectnetwork.modle.Summary;
import com.example.finalprojectnetwork.modle.SummaryAdapter;
import com.example.finalprojectnetwork.modle.User;
import com.example.finalprojectnetwork.modle.UserAdapter;
import com.example.finalprojectnetwork.modle.UserItemAdapter;
import com.example.finalprojectnetwork.modle.Util;
import com.example.finalprojectnetwork.modle.userItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BlankFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_UserId = "ARG_UserId";

    private String mParam1, UserID;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseStorage reference;
    FirebaseFirestore db;
    List<MyObject> strings;

//    List<Summary> summaries;
    List<userItem> userItems;
//    SummaryAdapter adapter;
    UserItemAdapter adapter;

    public BlankFragment() {
    }

    public static BlankFragment newInstance(String param1, String UserID) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_UserId, UserID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            UserID = getArguments().getString(ARG_UserId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentBlankBinding binding = FragmentBlankBinding.inflate(inflater, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        reference = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();
        strings = new ArrayList<>();
        userItems = new ArrayList<>();
        String document = mParam1;

        getFiles();
        binding.rv.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,
                false));
        adapter = new UserItemAdapter(userItems, getContext());
        binding.rv.setAdapter(adapter);
        return binding.getRoot();
    }

    private void getFiles() {
        String document = mParam1;
        db.collection(Util.CollocationCourse)
                .document(document)
                .collection(UserID)
                .addSnapshotListener((value, error) -> {
                    for (DocumentChange dc : value.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {
                            String s = dc.getDocument().getData().toString();
                            Log.d("document: ", s);
                            userItems.add(dc.getDocument().toObject(userItem.class));
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}