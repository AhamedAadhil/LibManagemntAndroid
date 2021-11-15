package com.example.librarymanagementapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AdminDeleteUsersActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView name,email,password,role;
    private Button delete;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_delete_users);

        imageView=findViewById(R.id.imageView2);
        name=findViewById(R.id.username);
        email=findViewById(R.id.emailaddress);
        password=findViewById(R.id.password);
        role=findViewById(R.id.role);
        delete=findViewById(R.id.delete);
        userModel= getIntent().getParcelableExtra("user");
        String uid=getIntent().getParcelableExtra("uid");
        Log.d("TAG", "onCreate: "+uid);

        auth=FirebaseAuth.getInstance();
        user=FirebaseAuth.getInstance().getCurrentUser();

       //databaseReference=firebaseDatabase.getReference("users").child(uid);

        if(userModel!=null){
            //Glide.with(getApplicationContext()).load(userModel.getImguri()).into(imageView);
            Picasso.get().load(userModel.getImguri()).placeholder(R.drawable.ic_profilepic).into(imageView);
            name.setText(userModel.getName());
            email.setText(userModel.getEmail());
            password.setText(userModel.getPassword());
            role.setText(userModel.getRole());
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //onDeleteClick();
            }
        });
    }

    public void onDeleteClick () {

        final AlertDialog.Builder builder = new AlertDialog.Builder(AdminDeleteUsersActivity.this);
        builder.setMessage("Are you sure want to Delete this User permanently?");
        builder.setCancelable(true);


        builder.setPositiveButton("YES",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i ){

                databaseReference.removeValue();
                Toast.makeText(AdminDeleteUsersActivity.this, "User Deleted!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AdminDeleteUsersActivity.this,AdminDashboardActivity.class));
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