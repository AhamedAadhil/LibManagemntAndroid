package com.example.librarymanagementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private TextView reg;
    private EditText username,password;
    private Button register;
    private  String uid;
    private FirebaseAuth fAuth;
    private FirebaseDatabase db;
    private SpinKitView spinKitView;
    private CheckBox checkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username=findViewById(R.id.editTextTextPersonName);
        password=findViewById(R.id.editTextTextPersonName2);
        register=findViewById(R.id.button3);
        fAuth=FirebaseAuth.getInstance();
        db=FirebaseDatabase.getInstance();
        spinKitView=findViewById(R.id.spin_kit);
        checkBox=findViewById(R.id.showpassword);

        reg=findViewById(R.id.textView7);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
                finish();
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uname=username.getText().toString().trim();
                String pass=password.getText().toString().trim();

                if(TextUtils.isEmpty(uname)){
                    username.setError("Provide Username to login");
                    username.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    password.setError("Provide password to login");
                    password.requestFocus();
                    return;
                }
                spinKitView.setVisibility(View.VISIBLE);
                fAuth.signInWithEmailAndPassword(uname,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = fAuth.getCurrentUser();
                            uid=user.getUid();
                            DatabaseReference databaseReference=db.getReference("users");
                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        if(snapshot.child(uid).child("role").getValue().equals("admin")){
                                            spinKitView.setVisibility(View.GONE);
                                            Toast  .makeText(LoginActivity.this, "Hello Admin", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(LoginActivity.this, AdminDashboardActivity.class));
                                            finish();
                                        }

                                    else {
                                            spinKitView.setVisibility(View.GONE);
                                        Toast.makeText(LoginActivity.this, "Successfully Login! ", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(LoginActivity.this,UserHomeActivity.class));
                                            finish();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });




                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "Incorrect Credentials!", Toast.LENGTH_LONG).show();
                        spinKitView.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user=fAuth.getCurrentUser();
        if(user!=null && !user.getUid().equals("ncP7yehkq0cVZ6nP7r37saTpcqe2")){
            startActivity(new Intent(LoginActivity.this,UserHomeActivity.class));
            this.finish();
        }
        else if(user!=null && user.getUid().equals("ncP7yehkq0cVZ6nP7r37saTpcqe2")){
            startActivity(new Intent(LoginActivity.this, AdminDashboardActivity.class));
            this.finish();
        }
    }


}