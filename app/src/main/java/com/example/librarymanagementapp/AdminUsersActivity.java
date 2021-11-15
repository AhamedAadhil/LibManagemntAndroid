package com.example.librarymanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdminUsersActivity extends AppCompatActivity implements UserAdapter.UserClickInterface{

    private FloatingActionButton fab;
    private RecyclerView RecviewBooks;
    private SpinKitView spinKitView;
    private UserAdapter userAdapter;
    private RelativeLayout bottomSheetRL;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<UserModel> userModelArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_users);


        RecviewBooks=findViewById(R.id.RecviewBooks);
        spinKitView=findViewById(R.id.spin_kit);
        fab=findViewById(R.id.floatingActionButton);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("users");
        userModelArrayList=new ArrayList<UserModel>();
        bottomSheetRL=findViewById(R.id.idRlBsheet);


        RecviewBooks.setLayoutManager(new LinearLayoutManager(this));

        //userAdapter=new UserAdapter(userModelArrayList,this,this);
        //RecviewBooks.setAdapter(userAdapter);
        //Toast.makeText(AdminUsersActivity.this, "user adapter"+userAdapter, Toast.LENGTH_SHORT).show();

        //Log.d("TAG", "USerArraylist=============: "+userModelArrayList);

        spinKitView.setVisibility(View.VISIBLE);
        getAllUsers();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminUsersActivity.this,AdminAddUsersActivity.class));
                finish();
            }
        });





    }
    private void getAllUsers(){
        userModelArrayList.clear();
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                spinKitView.setVisibility(View.GONE);
                userModelArrayList.add(snapshot.getValue(UserModel.class));
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                spinKitView.setVisibility(View.GONE);
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                spinKitView.setVisibility(View.GONE);
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                spinKitView.setVisibility(View.GONE);
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userAdapter=new UserAdapter(userModelArrayList,this,this);
        RecviewBooks.setAdapter(userAdapter);
        userAdapter.notifyDataSetChanged();
        //Toast.makeText(AdminUsersActivity.this, "user adapter innerr"+userAdapter, Toast.LENGTH_LONG).show();


    }



    @Override
    public void onUserclick(int positon) {
        displayBottomSheet(userModelArrayList.get(positon));
    }
    public void displayBottomSheet(UserModel userModel){
        final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(this);
        View layout= LayoutInflater.from(this).inflate(R.layout.bottom_sheet_dialog_user,bottomSheetRL);
        bottomSheetDialog.setContentView(layout);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.show();

        TextView usernameTV=layout.findViewById(R.id.username);
        TextView mailTV=layout.findViewById(R.id.idusermail);
        TextView roleTV=layout.findViewById(R.id.iduserrole);
        ImageView userimg=layout.findViewById(R.id.idIVbook);
        Button edit=layout.findViewById(R.id.btnView);


        usernameTV.setText(userModel.getName());
        mailTV.setText(userModel.getEmail());
        roleTV.setText(userModel.getRole());
        //Glide.with(getApplicationContext()).load(userModel.getImguri()).into(userimg);
        Picasso.get().load(userModel.getImguri()).into(userimg);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminUsersActivity.this, AdminDeleteUsersActivity.class);
                i.putExtra("user",userModel);
                //i.putExtra("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                //Log.d("TAG", "uid======: "+FirebaseAuth.getInstance().getCurrentUser().getUid());
                startActivity(i);
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);
        MenuItem menuItem=menu.findItem(R.id.search);
        SearchView searchView=(SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search Here!");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                userAdapter.getFilter().filter(s);


                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}