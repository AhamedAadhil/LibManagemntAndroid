package com.example.librarymanagementapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> implements Filterable {
    private ArrayList<UserModel> userModelArrayList;
    private ArrayList<UserModel> userModelArrayListFull;
    private Context context;
    int lastPos=-1;
    private UserClickInterface userclickInterface;


    public UserAdapter(ArrayList<UserModel> userModelArrayList, Context context, UserClickInterface userclickInterface) {
        this.userModelArrayListFull = userModelArrayList;
        this.userModelArrayList=new ArrayList<>(userModelArrayListFull);
        //this.userModelArrayList=userModelArrayList;
        //this.userModelArrayListFull=new ArrayList<>(userModelArrayList);
        this.context = context;
        this.userclickInterface = userclickInterface;

    }




    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.singlerow_user,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        UserModel userModel=userModelArrayList.get(position);
        holder.username.setText(userModel.getName());
        holder.mail.setText(userModel.getEmail());
        holder.role.setText(userModel.getRole());
        Glide.with(context).load(userModel.getImguri()).into(holder.img1);
        setAnimation(holder.itemView,position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userclickInterface.onUserclick(holder.getBindingAdapterPosition());
            }
        });

    }
        private void setAnimation(View itemView,int positon){
        if(positon>lastPos){
            Animation animation= AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            itemView.setAnimation(animation);
            lastPos=positon;
        }
        }
    @Override
    public int getItemCount() {
        return userModelArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return userFilter;
    }

        private final Filter userFilter= new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
               ArrayList<UserModel> filteredUserList=new ArrayList<>();
                if(charSequence == null || charSequence.length()==0||charSequence==""){
                   filteredUserList.addAll(userModelArrayListFull);
                   Log.d("TAG", "full=======================: "+userModelArrayListFull);
                    Log.d("TAG", "half=======================: "+userModelArrayList);
               }else{
                   String filtetPattern=charSequence.toString().toLowerCase().trim();
                   for(UserModel userModel:userModelArrayListFull){
                       if(userModel.getName().toLowerCase().contains(filtetPattern)||userModel.getRole().toLowerCase().contains(filtetPattern)||userModel.getEmail().toLowerCase().contains(filtetPattern)){
                           filteredUserList.add(userModel);
                       }
                   }
               }
               FilterResults results= new FilterResults();
               results.values=filteredUserList;
               results.count=filteredUserList.size();
               return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                userModelArrayList.clear();
                userModelArrayList.addAll((Collection<? extends UserModel>) filterResults.values);
                notifyDataSetChanged();
            }
        };

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView username,mail,role;
        private ImageView img1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.usernamesr);
            mail=itemView.findViewById(R.id.useremailsr);
            role=itemView.findViewById(R.id.userrolesr);
            img1=itemView.findViewById(R.id.img1);

        }
    }
    public interface UserClickInterface{
        void onUserclick(int positon);
    }
}
