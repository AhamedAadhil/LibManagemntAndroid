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
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AdminAddEbooksActivity extends AppCompatActivity {
    private ImageView imageView;
    private EditText bookname;
    private Spinner category,subject;
    private Button addebook;
    private static int IMAGE_REQ=1;
    private Uri imagepath;
    private Uri pdffilepath;
    private SpinKitView spinKitView;

    private StorageReference storageReferencepdf,storageReferenceimg;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_ebooks);



        storageReferencepdf= FirebaseStorage.getInstance().getReference("ebook_pdfs");
        storageReferenceimg=FirebaseStorage.getInstance().getReference("ebook_images");
        databaseReference= FirebaseDatabase.getInstance().getReference("ebooks");

        imageView=findViewById(R.id.imageView2);
        bookname=findViewById(R.id.bookname);
        category=findViewById(R.id.category);
        subject=findViewById(R.id.subject);
        addebook=findViewById(R.id.addebook);
        spinKitView=findViewById(R.id.spin_kit);

        addebook.setEnabled(false);

        category=findViewById(R.id.category);
        String[] category_items={"Select Category","ICT","BST","EGT"};
        ArrayAdapter arrayAdapter=new ArrayAdapter(this,R.layout.style_spinner,category_items);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(arrayAdapter);
        category.setSelection(0);

        subject=findViewById(R.id.subject);
        String[] subject_items={"Select Subject","S1","S2","S3","S4","S5"};
        ArrayAdapter arrayAdapter1=new ArrayAdapter(this,R.layout.style_spinner,subject_items);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subject.setAdapter(arrayAdapter1);
        subject.setSelection(0);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermisson();
            }
        });

        bookname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPDF();
            }
        });


        addebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Selected_category = category.getSelectedItem().toString().trim();
                String Selected_subject=subject.getSelectedItem().toString().trim();
                String bkname=bookname.getText().toString().trim();

                if(TextUtils.isEmpty(Selected_category)||Selected_category==""||Selected_category==null||Selected_category==category_items[0]){
                    category.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(Selected_subject)||Selected_subject==""||Selected_subject==null||Selected_subject==subject_items[0]){
                    subject.requestFocus();
                    return;
                }

                if(imagepath!=null){
                    spinKitView.setVisibility(View.VISIBLE);
                    StorageReference imgupload= storageReferenceimg.child(bkname);
                    StorageReference pdfupload=storageReferencepdf.child(bkname);
                    imgupload.putFile(imagepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imgupload.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    pdfupload.putFile(pdffilepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            pdfupload.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    FirebaseDatabase db=FirebaseDatabase.getInstance();
                                                    DatabaseReference root=db.getReference("ebooks");
                                                   PdfModel pdfModel= new PdfModel(imagepath.toString(),pdffilepath.toString(),bkname,Selected_category,Selected_subject);
                                                    root.child(bkname).setValue(pdfModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                       @Override
                                                       public void onComplete(@NonNull Task<Void> task) {
                                                           spinKitView.setVisibility(View.GONE);
                                                           bookname.setText("");
                                                           category.setSelection(0);
                                                           subject.setSelection(0);
                                                           imageView.setImageResource(R.drawable.ic_addphoto);
                                                           Toast.makeText(AdminAddEbooksActivity.this, "New E-Book Added Successfully", Toast.LENGTH_SHORT).show();
                                                           Intent intent=new Intent(AdminAddEbooksActivity.this,AdminDashboardActivity.class);
                                                           intent.putExtra("ebook",pdfModel);
                                                           startActivity(intent);
                                                       }
                                                   }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(AdminAddEbooksActivity.this, "Upload to Database fail", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                            });

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(AdminAddEbooksActivity.this, "Upload pdf to database fail! ", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AdminAddEbooksActivity.this,"Upload Image to database fail1",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    Toast.makeText(AdminAddEbooksActivity.this, "Please provide any E-book Image", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void selectPDF() {
        Intent i =new Intent();
        i.setType("application/pdf");
        i.setAction(i.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"PDF File select"),12);

    }

    private void requestPermisson() {

        if(ContextCompat.checkSelfPermission(AdminAddEbooksActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){
            selectImage();
        }else{
            ActivityCompat.requestPermissions(AdminAddEbooksActivity.this, new String[]{
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
        if(requestCode==12 && resultCode== Activity.RESULT_OK && data !=null && data.getData()!=null ){
            addebook.setEnabled(true);
            pdffilepath=data.getData();
            bookname.setText(data.getDataString().substring(data.getDataString().lastIndexOf("/")+1));

        }
    }
    public void onBackPressed () {

        final AlertDialog.Builder builder = new AlertDialog.Builder(AdminAddEbooksActivity.this);
        builder.setMessage("Are you sure want to go back to Dashboard");
        builder.setCancelable(true);


        builder.setPositiveButton("YES",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i ){
                startActivity(new Intent(AdminAddEbooksActivity.this,AdminDashboardActivity.class));
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