package com.example.librarymanagementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
public class UserAccountActivity extends AppCompatActivity {

    private Button editacc,activitylog,logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);
       logout=findViewById(R.id.logout);
        editacc=findViewById(R.id.editAcc);
        activitylog=findViewById(R.id.activitylog);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(UserAccountActivity.this,LoginActivity.class));
                finish();
            }
        });
        editacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserAccountActivity.this,UserEditAccActivity.class));
            }
        });
        activitylog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserAccountActivity.this,UserActivityLogActivity.class));
            }
        });
    }
}