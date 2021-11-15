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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.regex.Pattern;

public class AdminAddBooksActivity extends AppCompatActivity {

    private ImageView imageView;
    private EditText bookname,authername,contributors,publisher,edition,ISBN,copies,tags;
    private Button addbook;
    private static int IMAGE_REQ=1;
    private Uri imagepath;
    private SpinKitView spinKitView;
    private String bookID;
    private Spinner category,subject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_books);

        imageView=findViewById(R.id.imageView2);
        bookname=findViewById(R.id.bookname);
        authername=findViewById(R.id.auther);
        contributors=findViewById(R.id.contributors);
        copies=findViewById(R.id.copies);
        publisher=findViewById(R.id.publisher);
        edition=findViewById(R.id.edition);
        ISBN=findViewById(R.id.isbn);
        tags=findViewById(R.id.tags);
        addbook=findViewById(R.id.register);
        spinKitView=findViewById(R.id.spin_kit);


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

        addbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String b_name=bookname.getText().toString().trim();
                String a_name=authername.getText().toString().trim();
                String contr=contributors.getText().toString().trim();
                String copy=copies.getText().toString().trim();
                String publish=publisher.getText().toString().trim();
                String edi=edition.getText().toString().trim();
                String _isbn=ISBN.getText().toString().trim();
                String Selected_category = category.getSelectedItem().toString().trim();
                String Selected_subject=subject.getSelectedItem().toString().trim();
                String tag=tags.getText().toString().trim();
                bookID=b_name;

                if(TextUtils.isEmpty(b_name) || b_name.length()<=3){
                    bookname.setError("Book title cannot be Null or less than 4 characters");
                    bookname.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(a_name)||a_name.length()<=5){
                    authername.setError("Author name cannot be Null or less than 6 characters");
                    authername.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(contr)||contr.length()<=5){
                    contributors.setError("Contributors name cannot be Null or less than 6 characters");
                    contributors.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(publish)||publish.length()<=5){
                    publisher.setError("Publisher name cannot be Null or less than 6 characters");
                    publisher.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(edi)||edi.length()<=5){
                    edition.setError("Edition Should be provide and should be higher than 6 characters");
                    edition.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(_isbn)){
                    ISBN.setError("ISBN number is Must");
                    ISBN.requestFocus();
                    return;
                }
                if(_isbn.length()<=10){
                    ISBN.setError("ISBN NO length is short");
                    ISBN.requestFocus();
                    return;
                }


                if(TextUtils.isEmpty(Selected_category)||Selected_category==""||Selected_category==null||Selected_category==category_items[0]){
                    category.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(Selected_subject)||Selected_subject==""||Selected_subject==null||Selected_subject==subject_items[0]){
                    subject.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(copy)){
                    copies.setError("No of Copies Should be provide");
                    copies.requestFocus();
                    return;
                }
                if(copy.equals("0")){
                    copies.setError("Copies cannot be 0");
                    copies.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(tag)){
                    tags.setError("Provide any Tags Here");
                    tags.requestFocus();
                    return;
                }


                if(imagepath!=null){
                    spinKitView.setVisibility(View.VISIBLE);
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference uploader = storage.getReference("book_images").child(bookID);
                    uploader.putFile(imagepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    FirebaseDatabase db=FirebaseDatabase.getInstance();
                                    DatabaseReference root=db.getReference("books");
                                    BookModel model=new BookModel(b_name,a_name,contr,publish,edi,_isbn,Selected_category,Selected_subject,copy,tag,imagepath.toString(),bookID);
                                    root.child(bookID).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            spinKitView.setVisibility(View.GONE);
                                            bookname.setText("");
                                            authername.setText("");
                                            contributors.setText("");
                                            publisher.setText("");
                                            edition.setText("");
                                            ISBN.setText("");
                                            category.setSelection(0);
                                            subject.setSelection(0);
                                            copies.setText("");
                                            tags.setText("");
                                            imageView.setImageResource(R.drawable.ic_ebooks);
                                            Toast.makeText(AdminAddBooksActivity.this, "New Book Added Successfully", Toast.LENGTH_SHORT).show();
                                            Intent intent=new Intent(AdminAddBooksActivity.this,AdminDashboardActivity.class);
                                            intent.putExtra("book",model);
                                            startActivity(intent);
                                        }
                                    });
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AdminAddBooksActivity.this, "New Book Add Fail!", Toast.LENGTH_SHORT).show();
                            spinKitView.setVisibility(View.GONE);
                        }
                    });
                }
                else{
                    Toast.makeText(AdminAddBooksActivity.this, "Please Provide any Book Image", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void requestPermisson() {

        if(ContextCompat.checkSelfPermission(AdminAddBooksActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){
            selectImage();
        }else{
            ActivityCompat.requestPermissions(AdminAddBooksActivity.this, new String[]{
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

        final AlertDialog.Builder builder = new AlertDialog.Builder(AdminAddBooksActivity.this);
        builder.setMessage("Are you sure want to go back to Dashboard");
        builder.setCancelable(true);


        builder.setPositiveButton("YES",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i ){
                startActivity(new Intent(AdminAddBooksActivity.this,AdminDashboardActivity.class));
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