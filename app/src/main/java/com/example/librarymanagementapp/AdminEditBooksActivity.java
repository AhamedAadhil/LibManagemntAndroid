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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class AdminEditBooksActivity extends AppCompatActivity {

    private ImageView imageView;
    private EditText bookname,authername,contributors,publisher,edition,ISBN,copies,tags;
    private Button updatebook,deletebook;
    private static int IMAGE_REQ=1;
    private Uri imagepath;
    private SpinKitView spinKitView;
    private String bookID;
    private Spinner category,subject;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private BookModel bookModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_books);

        firebaseDatabase=FirebaseDatabase.getInstance();
        imageView=findViewById(R.id.imageView2);
        bookname=findViewById(R.id.bookname);
        authername=findViewById(R.id.auther);
        contributors=findViewById(R.id.contributors);
        copies=findViewById(R.id.copies);
        publisher=findViewById(R.id.publisher);
        edition=findViewById(R.id.edition);
        ISBN=findViewById(R.id.isbn);
        tags=findViewById(R.id.tags);
        updatebook=findViewById(R.id.update);
        deletebook=findViewById(R.id.delete);
        spinKitView=findViewById(R.id.spin_kit);
        bookModel=getIntent().getParcelableExtra("book");


        category=findViewById(R.id.category);
        String[] category_items={"Select Category","ICT","BST","EGT"};
        ArrayAdapter arrayAdapter=new ArrayAdapter(this,R.layout.style_spinner,category_items);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(arrayAdapter);


        subject=findViewById(R.id.subject);
        String[] subject_items={"Select Subject","S1","S2","S3","S4","S5"};
        ArrayAdapter arrayAdapter1=new ArrayAdapter(this,R.layout.style_spinner,subject_items);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subject.setAdapter(arrayAdapter1);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermisson();
            }
        });


        if(bookModel!=null){
            Picasso.get().load(bookModel.getImguri()).into(imageView);
            bookname.setText(bookModel.getBookname());
            authername.setText(bookModel.getAuthername());
            contributors.setText(bookModel.getContributors());
            copies.setText(bookModel.getCopies());
            publisher.setText(bookModel.getPublisher());
            edition.setText(bookModel.getEdition());
            ISBN.setText(bookModel.getISBN());
            tags.setText(bookModel.getTag());
            String cat=bookModel.getCategory();
            String sub=bookModel.getSubject();
            bookID=bookModel.getBookid();

            for (int i = 0; i < category.getCount(); i++) {
                if (category.getItemAtPosition(i).equals(cat)) {
                    category.setSelection(i);
                    break;
                }}

            for (int i = 0; i < subject.getCount(); i++) {
                if (subject.getItemAtPosition(i).equals(sub)) {
                    subject.setSelection(i);
                    break;
                }}

        }

        databaseReference=firebaseDatabase.getReference("books").child(bookModel.getBookid());


        updatebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imagepath!=null) {
                    spinKitView.setVisibility(View.VISIBLE);
                    //String imgpath2=imagepath.toString().trim();
                    String b_name = bookname.getText().toString().trim();
                    String a_name = authername.getText().toString().trim();
                    String contr = contributors.getText().toString().trim();
                    String copy = copies.getText().toString().trim();
                    String publish = publisher.getText().toString().trim();
                    String edi = edition.getText().toString().trim();
                    String _isbn = ISBN.getText().toString().trim();
                    String Selected_category = category.getSelectedItem().toString().trim();
                    String Selected_subject = subject.getSelectedItem().toString().trim();
                    String tag = tags.getText().toString().trim();


                    Map<String, Object> map = new HashMap<>();
                    map.put("bookname",b_name);
                    map.put("authername", a_name);
                    map.put("contributors", contr);
                    map.put("publisher", publish);
                    map.put("edition", edi);
                    map.put("ISBN", _isbn);
                    map.put("category",Selected_category );
                    map.put("subject", Selected_subject);
                    map.put("copies", copy);
                    map.put("tag", tag);
                    map.put("imguri",imagepath.toString());
                    map.put("bookid",bookID);

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            spinKitView.setVisibility(View.GONE);
                            databaseReference.updateChildren(map);
                            Toast.makeText(AdminEditBooksActivity.this, "Updated Successfully!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AdminEditBooksActivity.this,AdminDashboardActivity.class));
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(AdminEditBooksActivity.this, "Update Fail!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{

                    Toast.makeText(AdminEditBooksActivity.this, "Please Provide any Image", Toast.LENGTH_SHORT).show();
                }
            }
        });

        deletebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteClick();

            }
        });
    }

    private void requestPermisson() {

        if(ContextCompat.checkSelfPermission(AdminEditBooksActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){
            selectImage();
        }else{
            ActivityCompat.requestPermissions(AdminEditBooksActivity.this, new String[]{
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

        final AlertDialog.Builder builder = new AlertDialog.Builder(AdminEditBooksActivity.this);
        builder.setMessage("Are you sure want to Cancel Edit");
        builder.setCancelable(true);


        builder.setPositiveButton("YES",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i ){

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

    public void onDeleteClick () {

        final AlertDialog.Builder builder = new AlertDialog.Builder(AdminEditBooksActivity.this);
        builder.setMessage("Are you sure want to Delete this book permanently?");
        builder.setCancelable(true);


        builder.setPositiveButton("YES",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i ){
                databaseReference.removeValue();
                Toast.makeText(AdminEditBooksActivity.this, "Course Deleted!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AdminEditBooksActivity.this,AdminDashboardActivity.class));
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