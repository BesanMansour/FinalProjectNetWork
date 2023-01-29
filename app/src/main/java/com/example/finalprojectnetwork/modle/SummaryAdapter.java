package com.example.finalprojectnetwork.modle;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.example.finalprojectnetwork.NextActivity;
import com.example.finalprojectnetwork.R;
import com.example.finalprojectnetwork.utils.GlideApp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;

public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.MyViewHolder> {
    List<Summary> summaries;
    Context context;
    onItemClickListener mListener;
    DeleteListener listener;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public SummaryAdapter(List<Summary> summaries, Context context,DeleteListener listener) {
        this.summaries = summaries;
        this.context = context;
        this.listener = listener;

    }

    public SummaryAdapter(Activity activity) {
        this.context = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // set the data in items
        Summary s = summaries.get(position);
        holder.name.setText(s.getUserId());
        holder.email.setText(s.getText());
        if (s.getImage() != null && !s.getImage().isEmpty()) {
            Glide.with(context).load(s.getImage()).into(holder.imageView);
        } else {
            Toast.makeText(context, "null image", Toast.LENGTH_SHORT).show();
        }

//        if (fileIsImage(s.getImage())) {
//            Glide.with(context).load(s.getImage()).into(holder.imageView);
//            holder.file.setVisibility(View.GONE);
//        } else {
//            //otherwise you can show a file icon
////            holder.fileIcon.setVisibility(View.VISIBLE);
//            holder.file.setText(getFileName(s.getImage()));
//            holder.file.setVisibility(View.VISIBLE);
//            holder.imageView.setVisibility(View.GONE);
//        }



        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.deleteId(holder.getAdapterPosition());
//                String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
//                db.collection(Util.CollocationCourse)
//                        .document(Util.document)
//                        .collection(userId)
//                        .document(summaries.get(holder.getAdapterPosition()).getUserId())
//                        .delete()
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if (task.isSuccessful()){
//                                    summaries.remove(holder.getAdapterPosition());
//                                    notifyDataSetChanged();
//                                    Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show();
//                                }else{
//                                    Toast.makeText(context, "error delete", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return summaries != null ? summaries.size() : 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        ImageView imageView, img_fav, delete;
        TextView name, email , file;

        public MyViewHolder(View itemView) {
            super(itemView);

            // get the reference of item view's
            name = (TextView) itemView.findViewById(R.id.item_summary);
            email = (TextView) itemView.findViewById(R.id.tiem_userId);
            imageView = (ImageView) itemView.findViewById(R.id.itemImg);
            img_fav = (ImageView) itemView.findViewById(R.id.favorites_icon);
            delete = (ImageView) itemView.findViewById(R.id.delete);
            file = (TextView) itemView.findViewById(R.id.item_file);
        }



        @Override
        public void onClick(View view) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Select Action");
            MenuItem doWhatever = contextMenu.add(Menu.NONE, 1, 1, "Do whatever");
            MenuItem delete = contextMenu.add(Menu.NONE, 2, 2, "Delete");

            doWhatever.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {

                    switch (menuItem.getItemId()) {
                        case 1:
                            mListener.onWhatEverClick(position);
                        case 2:
                            mListener.onDeleteClick(position);
                    }
                }
            }
            return false;
        }
    }

    private boolean fileIsImage(String url){
        String[] imageExtensions={".jpg",".jpeg",".png",".gif"};
        for(String ext:imageExtensions){
            if(url.endsWith(ext))
                return true;
        }
        return false;
    }

    private String getFileName(String url){
        return url.substring(url.lastIndexOf('/')+1);
    }


    public interface onItemClickListener {
        void onItemClick(int position);

        void onWhatEverClick(int position);

        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        mListener = listener;
    }
}




