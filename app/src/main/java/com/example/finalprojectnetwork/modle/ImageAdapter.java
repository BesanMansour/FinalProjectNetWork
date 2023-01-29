package com.example.finalprojectnetwork.modle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.finalprojectnetwork.R;

import java.util.ArrayList;
import java.util.List;



public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private List<MyObject> imageUrls;
    Context context;

    public ImageAdapter(List<MyObject> imageUrls, Context context) {
        this.imageUrls = imageUrls;
        this.context=context;
    }

    public ImageAdapter( List < MyObject > strings ) {
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load( imageUrls.get( position ).getImageUrl())
                .into(holder.imageView);


        holder.favorites.setOnCheckedChangeListener( ( buttonView , isChecked ) -> {
            if ( holder.favorites.isChecked() ){
                holder.favorites.setButtonDrawable( R.drawable.ic_favorite );
            }else {
                holder.favorites.setButtonDrawable( R.drawable.ic_favorite_border );
            }
        } );

    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView fileName;
        CheckBox favorites;


        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            fileName = itemView.findViewById(R.id.file_name);
            favorites= itemView.findViewById( R.id.cb_favorites );
//            image2=imageView.findViewById(R.id.image_view_favorites2);

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
}

