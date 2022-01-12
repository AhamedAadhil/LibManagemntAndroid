package com.example.librarymanagementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class UserFindbookActivity extends AppCompatActivity {
    private Spinner category;
    private Button search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_findbook);

        category=findViewById(R.id.category);
        String[] category_items={"Select a Search Method","Title","Category","Subject","Author"};
        ArrayAdapter arrayAdapter=new ArrayAdapter(this,R.layout.style_spinner,category_items);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(arrayAdapter);

        search=findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserFindbookActivity.this,UserSearchResultActivity.class));
            }
        });
    }
}