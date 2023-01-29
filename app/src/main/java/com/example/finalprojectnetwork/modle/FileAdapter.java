package com.example.finalprojectnetwork.modle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.finalprojectnetwork.R;

import java.util.ArrayList;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {
    private ArrayList<String> urls;
    private Context context;

    public FileAdapter(ArrayList<String> urls) {
        this.urls = urls;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //if you need to show image files
        if (fileIsImage(urls.get(position))) {
            Glide.with(context)
                    .load(urls.get(position))
                    .into(holder.imageView);
//            holder.fileIcon.setVisibility(View.GONE);
            holder.fileName.setVisibility(View.GONE);
        } else {
            //otherwise you can show a file icon
//            holder.fileIcon.setVisibility(View.VISIBLE);
            holder.fileName.setText(getFileName(urls.get(position)));
            holder.fileName.setVisibility(View.VISIBLE);
            holder.imageView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return urls.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        //        ImageView fileIcon;
        TextView fileName;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
//            fileIcon = itemView.findViewById(R.id.file_icon);
            fileName = itemView.findViewById(R.id.file_name);
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