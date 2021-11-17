package com.example.librarymanagementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminDashboardActivity extends AppCompatActivity {
    private View users,books,reservations,ebooks,notifications,signout;
    private TextView totalusers,totalbooks,totalEbooks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);


        books=findViewById(R.id.allbooks);
        users=findViewById(R.id.allusers);
        reservations=findViewById(R.id.reservations);
        ebooks=findViewById(R.id.ebooks);
        notifications=findViewById(R.id.notifications);
        signout=findViewById(R.id.signout);
        totalbooks=findViewById(R.id.totalbooks);
        totalusers=findViewById(R.id.totalusers);
        totalEbooks=findViewById(R.id.totalEbooks);

        FirebaseDatabase database = FirebaseDatabase.getInstance();




        DatabaseReference studentsRef = database.getReference("users");
        studentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int counter = (int) snapshot.getChildrenCount()-1;
                String userCount=String.valueOf(counter);
                totalusers.setText("users count: "+userCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference booksRef = database.getReference("books");
        booksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int counter = (int) snapshot.getChildrenCount();
                String booksCount=String.valueOf(counter);
                totalbooks.setText("books count: "+booksCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference ebookRef=database.getReference("ebooks");
        ebookRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int counter=(int) snapshot.getChildrenCount();
                String ebookCount=String.valueOf(counter);
                totalEbooks.setText("Ebooks count: "+ebookCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdminSignoutWarning();

            }
        });

        books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDashboardActivity.this,AdminBooksActivity.class));

            }
        });

        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDashboardActivity.this,AdminUsersActivity.class));
            }
        });

        reservations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDashboardActivity.this,AdminReservationsActivity.class));

            }
        });

        ebooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDashboardActivity.this,AdminEbooksActivity.class));

            }
        });

        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDashboardActivity.this,AdminNotificationsActivity.class));

            }
        });

    }

    @Override

    public void onBackPressed () {

        final AlertDialog.Builder builder = new AlertDialog.Builder(AdminDashboardActivity.this);
        builder.setMessage("Are you sure want to exit from Admin Dashboard");
        builder.setCancelable(true);


        builder.setPositiveButton("YES",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i ){
                startActivity(new Intent(AdminDashboardActivity.this,MainActivity.class));
                finish();

            }
        } );
        builder.setNegativeButton("NO!" ,new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        }  );
        AlertDialog alertDialog = builder.create();

        alertDialog.show();

    }


    public void AdminSignoutWarning () {

        final AlertDialog.Builder builder = new AlertDialog.Builder(AdminDashboardActivity.this);
        builder.setMessage("Are you sure to Signout as Admin?");
        builder.setCancelable(true);


        builder.setPositiveButton("YES",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i ){
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(AdminDashboardActivity.this,LoginActivity.class));
                finish();

            }
        } );
        builder.setNegativeButton("NO!" ,new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        }  );
        AlertDialog alertDialog = builder.create();

        alertDialog.show();

    }


}