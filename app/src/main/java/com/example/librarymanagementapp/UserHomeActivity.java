package com.example.librarymanagementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class UserHomeActivity extends AppCompatActivity {
    private View borrow,rturn,reservation,find,notification,account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

      borrow=findViewById(R.id.borrowing);
      rturn=findViewById(R.id.returns);
      reservation=findViewById(R.id.reservation_book);
      find=findViewById(R.id.find_book);
      notification=findViewById(R.id.notification);
      account=findViewById(R.id.account);

      borrow.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

              startActivity(new Intent(UserHomeActivity.this,UserBorrowingActivity.class));
          }
      });
        rturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(UserHomeActivity.this,UserReturnsActivity.class));
            }
        });
        reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(UserHomeActivity.this,UserReservationActivity.class));
            }
        });
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(UserHomeActivity.this,UserFindbookActivity.class));
            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(UserHomeActivity.this,UserNotificationActivity.class));
            }
        });
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(UserHomeActivity.this,UserAccountActivity.class));
            }
        });

    }
}