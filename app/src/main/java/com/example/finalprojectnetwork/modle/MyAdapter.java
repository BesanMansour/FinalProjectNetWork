package com.example.finalprojectnetwork.modle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.finalprojectnetwork.R;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
//    private List<MyObject> myObjects;
    private List<StorageReference> myObjects;
    private Context context;

    public MyAdapter(List<StorageReference> myObjects, Context context) {
        this.myObjects = myObjects;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(view);

//        return new MyViewHolder( view );
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        StorageReference object = myObjects.get(position);

        // Use Glide or Picasso to load the image from the URL into the ImageView
        Glide.with(context).load(object).into(holder.imageView);
        holder.textView.setText(object.getName());
    }

    @Override
    public int getItemCount() {
        return myObjects.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            textView = itemView.findViewById(R.id.text_view);
        }
    }
}

