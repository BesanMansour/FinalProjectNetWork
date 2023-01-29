package com.example.finalprojectnetwork.modle;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.finalprojectnetwork.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;


public class UserItemAdapter extends RecyclerView.Adapter<UserItemAdapter.MyViewHolder> {
    List<userItem> userItems;
    Context context;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public UserItemAdapter(List<userItem> userItems, Context context) {
        this.userItems = userItems;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_user, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        userItem s = userItems.get(position);
        holder.name.setText(s.getUserId());
        holder.email.setText(s.getText());
        if (s.getImage() != null && !s.getImage().isEmpty()) {
            Glide.with(context).load(s.getImage()).into(holder.imageView);
        } else {
            Toast.makeText(context, "null image", Toast.LENGTH_SHORT).show();
        }

        if (s.isFavorite()) {
            holder.img_fav.setImageResource(R.drawable.ic_favorite);
        } else {
            holder.img_fav.setImageResource(R.drawable.ic_favorite_border);
        }
        holder.img_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (s.isFavorite()) {
                    removeImageFromFavorites(s);
                } else {
                    addImageToFavorites(s);
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userItems != null ? userItems.size() : 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView, img_fav;
        TextView name, email;

        public MyViewHolder(View itemView) {
            super(itemView);

            // get the reference of item view's
            name = (TextView) itemView.findViewById(R.id.item_summary);
            email = (TextView) itemView.findViewById(R.id.tiem_userId);
            imageView = (ImageView) itemView.findViewById(R.id.itemImg);
            img_fav = (ImageView) itemView.findViewById(R.id.favorites_icon);
        }
    }

    public  void addImageToFavorites(userItem userItem) {
        userItem.setFavorite(true);
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("users").document(userId).collection("favorites")
                .add(userItem)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Image added to favorites successfully with ID: " + documentReference.getId());
                        Toast.makeText(context, "add", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding image to favorites", e);
                    }
                });
    }

    public void removeImageFromFavorites(userItem userItem) {
        userItem.setFavorite(false);
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("users")
                .document(userId)
                .collection("favorites")
                .whereEqualTo("image", userItem.getImage())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                db.collection("users").document(userId).collection("favorites")
                                        .document(document.getId()).delete();
                                Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}