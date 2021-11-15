package com.example.librarymanagementapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.Random;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {



        private static int IMAGE_REQ=1;
        private Uri imagepath;
        private TextView have;
        private ImageView imageView;
        private EditText username,email,password,confirm;
        private Button register;
        private SpinKitView spinKitView;
        private FirebaseAuth mAuth;
        private CheckBox checkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        have = findViewById(R.id.have);
        imageView=findViewById(R.id.imageView2);
        username=findViewById(R.id.username);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        confirm=findViewById(R.id.confirmpassword);
        register=findViewById(R.id.register);
        mAuth=FirebaseAuth.getInstance();
        spinKitView=findViewById(R.id.spin_kit);
        checkBox=findViewById(R.id.showpassword);

        have.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    confirm.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    confirm.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               requestPermisson();
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=username.getText().toString().trim();
                String mail=email.getText().toString().trim();
                String pass = password.getText().toString().trim();
                String cpass= confirm.getText().toString().trim();

                if(TextUtils.isEmpty(name)){
                    username.setError("Username Should be provide");
                    username.requestFocus();
                    return;
                }
                if(name.length()<=2){
                    username.setError("Please provide a Valid name");
                    username.requestFocus();
                    return;
                }
                if(name.matches(".*\\d.*")){
                    username.setError("Name Should contains only Alphabets");
                    username.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(mail)){
                    email.setError("Email address Should be provide");
                    email.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
                    email.setError("It's not a valid Email address");
                    email.requestFocus();
                    return;
                }
                if(!Pattern.compile("ict(18|17|16|19|20|21)([8][0][1-9]|[8][1-6][0-9]|[8][7][0-5])@sjp.ac.lk|egt(18|17|16|19|20|21)([5-6][0][1-9]|[5-6][1-6][0-9]|[5][7-9][0-9])@sjp.ac.lk|bst(18|17|16|19|20|21)([7][0][1-9]|[7][1-7][0-9]|[7][8][0-6])@sjp.ac.lk").matcher(mail).matches()){
                   email.setError("Please use USJ email address Only");
                   email.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    password.setError("You should provide any password");
                    password.requestFocus();
                    return;
                }
                if(pass.length()<6){
                    password.setError("password character should longer than 6 characters");
                    password.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(cpass)){
                    confirm.setError("Enter the same password again");
                    confirm.requestFocus();
                    return;
                }
                if(!TextUtils.equals(pass,cpass)){
                    confirm.setError("passwords are miss matching ");
                    confirm.requestFocus();
                    return;
                }
                if(imagepath!=null){

                    spinKitView.setVisibility(View.VISIBLE);

                    mAuth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseStorage storage = FirebaseStorage.getInstance();
                                StorageReference uploader = storage.getReference("User_images").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                uploader.putFile(imagepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                FirebaseDatabase db=FirebaseDatabase.getInstance();
                                                DatabaseReference root=db.getReference("users");
                                                UserModel model = new UserModel(name,mail,pass,imagepath.toString(),"user");
                                                root.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        spinKitView.setVisibility(View.GONE);
                                                        username.setText("");
                                                        password.setText("");
                                                        email.setText("");
                                                        confirm.setText("");
                                                        imageView.setImageResource(R.drawable.ic_profilepic);
                                                        Toast.makeText(RegistrationActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
                                                        finish();
                                                    }
                                                });

                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(RegistrationActivity.this, "Registration fail", Toast.LENGTH_SHORT).show();
                                        spinKitView.setVisibility(View.GONE);
                                    }
                                });
                            }
                            else{
                                Toast.makeText(RegistrationActivity.this, "Email already Exist", Toast.LENGTH_SHORT).show();
                                spinKitView.setVisibility(View.GONE);
                            }
                            }
                        });
                    }

                else{
                    Toast.makeText(RegistrationActivity.this, "Please select any image", Toast.LENGTH_SHORT).show();
                }






            }
        });

    }

    private void requestPermisson() {

        if(ContextCompat.checkSelfPermission(RegistrationActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
        == PackageManager.PERMISSION_GRANTED){
            selectImage();
        }else{
            ActivityCompat.requestPermissions(RegistrationActivity.this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE},IMAGE_REQ);
            };
        }

    private void selectImage() {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,IMAGE_REQ);
    }


    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       if(requestCode==IMAGE_REQ && resultCode== Activity.RESULT_OK && data !=null && data.getData()!=null){
           imagepath=data.getData();
           Picasso.get().load(imagepath).into(imageView);
       }
    }

    public void onBackPressed () {

        final AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
        builder.setMessage("Are you sure want to Cancel Registration");
        builder.setCancelable(true);


        builder.setPositiveButton("YES",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i ){
            startActivity(new Intent(RegistrationActivity.this,MainActivity.class));
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