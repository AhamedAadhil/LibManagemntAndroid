package com.example.librarymanagementapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder>{
    private ArrayList<BookModel> bookModelArrayList;
    private Context context;
    int lastPos=-1;
    private BookClickInterface bookclickInterface;
    public BookAdapter(ArrayList<BookModel> bookModelArrayList, Context context, BookClickInterface bookclickInterface) {
        this.bookModelArrayList = bookModelArrayList;
        this.context = context;
        this.bookclickInterface = bookclickInterface;
    }




    @NonNull
    @Override
    public BookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.singlerow_book,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.ViewHolder holder, int position) {
        BookModel bookModel=bookModelArrayList.get(position);
        holder.bookname.setText(bookModel.getBookname());
        holder.authername.setText(bookModel.getAuthername());
        holder.category.setText(bookModel.getCategory());
        Glide.with(context).load(bookModel.getImguri()).into(holder.img1);
        setAnimation(holder.itemView,position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookclickInterface.onBookclick(holder.getBindingAdapterPosition());
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
        return bookModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView bookname,authername,category;
        private ImageView img1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookname=itemView.findViewById(R.id.booknamesr);
            authername=itemView.findViewById(R.id.authernamesr);
            category=itemView.findViewById(R.id.categorysr);
            img1=itemView.findViewById(R.id.img1);

        }
    }
    public interface BookClickInterface{
        void onBookclick(int positon);
    }
}
