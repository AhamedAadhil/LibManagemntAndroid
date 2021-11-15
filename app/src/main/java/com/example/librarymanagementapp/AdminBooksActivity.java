package com.example.librarymanagementapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.security.auth.Subject;

public class AdminBooksActivity extends AppCompatActivity implements BookAdapter.BookClickInterface{

    private FloatingActionButton fab;
    private RecyclerView RecviewBooks;
    private SpinKitView spinKitView;
    private BookAdapter bookAdapter;
    private RelativeLayout bottomSheetRL;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<BookModel> bookModelArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_books);


        RecviewBooks=findViewById(R.id.RecviewBooks);
        spinKitView=findViewById(R.id.spin_kit);
        fab=findViewById(R.id.floatingActionButton);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("books");
        bookModelArrayList=new ArrayList<>();
        bottomSheetRL=findViewById(R.id.idRlBsheet);
        bookAdapter=new BookAdapter(bookModelArrayList,this,this);

        RecviewBooks.setLayoutManager(new LinearLayoutManager(this));
        RecviewBooks.setAdapter(bookAdapter);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminBooksActivity.this,AdminAddBooksActivity.class));
            }
        });
            spinKitView.setVisibility(View.VISIBLE);
            getAllBooks();



    }
    private void getAllBooks(){
        bookModelArrayList.clear();
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                spinKitView.setVisibility(View.GONE);
                bookModelArrayList.add(snapshot.getValue(BookModel.class));
                bookAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                spinKitView.setVisibility(View.GONE);
                bookAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                spinKitView.setVisibility(View.GONE);
                bookAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                spinKitView.setVisibility(View.GONE);
                bookAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onBookclick(int positon) {
    displayBottomSheet(bookModelArrayList.get(positon));
    }
    public void displayBottomSheet(BookModel bookModel){
        final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(this);
        View layout= LayoutInflater.from(this).inflate(R.layout.bottom_sheet_dialog,bottomSheetRL);
        bottomSheetDialog.setContentView(layout);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.show();

        TextView booknameTV=layout.findViewById(R.id.bookname);
        TextView authenameTV=layout.findViewById(R.id.idbookAuthorname);
        TextView categoryTV=layout.findViewById(R.id.idbookCategory);
        TextView subjectTV=layout.findViewById(R.id.idbooksubject);
        ImageView bookimg=layout.findViewById(R.id.idIVbook);
        Button edit=layout.findViewById(R.id.btnEdit);


        booknameTV.setText(bookModel.getBookname());
        authenameTV.setText(bookModel.getAuthername());
        categoryTV.setText(bookModel.getCategory());
        subjectTV.setText(bookModel.getSubject());
        Picasso.get().load(bookModel.getImguri()).into(bookimg);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminBooksActivity.this,AdminEditBooksActivity.class);
                i.putExtra("book",bookModel);
                startActivity(i);
            }
        });




    }
}