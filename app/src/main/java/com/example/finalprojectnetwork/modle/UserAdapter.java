package com.example.finalprojectnetwork.modle;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectnetwork.R;
import com.example.finalprojectnetwork.databinding.UserItemBinding;

import com.example.finalprojectnetwork.modle.Listener;
import com.example.finalprojectnetwork.modle.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter < UserAdapter.ViewHolder > {
    UserItemBinding binding;
    Listener listener;

    @NonNull
    List < User > userList;

    public UserAdapter( ) {
    }

    public UserAdapter( @NonNull List < User > userList,Listener listener ) {
        this.userList = userList;
        this.listener = listener;
    }

    public void setUserList(@NonNull List<User> userList) {
        this.userList = userList;
        notifyDataSetChanged();

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder( @NonNull ViewGroup parent , int viewType ) {
        binding= UserItemBinding.inflate
                ( LayoutInflater.from( parent.getContext() ),parent,
                        false );

        return new ViewHolder( binding );
    }

    @SuppressLint( "SetTextI18n" )
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder , int position ) {
        User user = userList.get( position );
        binding.textViewEmail.setText( user.getEmail());
        binding.textViewName.setText( user.getName());
        binding.imageViewPublic.setImageResource( R.drawable.ic_public );
        holder.itemView.setOnClickListener( v ->
                listener.showProfile( holder.getAdapterPosition()) );
    }

    @Override
    public int getItemCount( ) {
        return userList.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(  @NonNull UserItemBinding itemView ) {
            super( itemView.getRoot() );

        }
    }

    }

//    public UserAdapter( @NonNull List < User > userList ) {
//        this.userList = userList;
//    }
//
//    @SuppressLint( "NotifyDataSetChanged" )
//    public void setProductModels ( List<User> users){
//        this.userList=users;
//        notifyDataSetChanged();
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder( @NonNull ViewGroup parent , int viewType ) {
//        binding= UserItemBinding.inflate
//                ( LayoutInflater.from( parent.getContext() ),parent,
//                        false );
//
//        return new ViewHolder( binding );
//    }
//
//    @SuppressLint( "SetTextI18n" )
//    @Override
//    public void onBindViewHolder( @NonNull ViewHolder holder , int position ) {
//        User user = userList.get( position );
//
//        binding.textViewEmail.setText( user.getEmail());
//        binding.textViewName.setText( user.getName());
//        binding.imageViewPublic.setImageResource( R.drawable.ic_public );
//        binding.imageViewPublic.setOnClickListener( v ->
//                listener.showProfile( holder.getAdapterPosition() ) );
//
//    }
//
//    @Override
//    public int getItemCount( ) {
//        return userList.size();
//    }
//
//    static class ViewHolder extends RecyclerView.ViewHolder {
//
//        public ViewHolder(  @NonNull UserItemBinding itemView ) {
//            super( itemView.getRoot() );
//
//        }
//    }
//}
