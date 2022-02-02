package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    EditText etitem;
    Button btn_Save;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_activty);

        etitem = findViewById(R.id.etitem);
        btn_Save = findViewById(R.id.btn_Save);

        getSupportActionBar().setTitle("Edit item");

       etitem.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));
       btn_Save.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent();

               intent.putExtra(MainActivity.KEY_ITEM_TEXT, etitem.getText().toString());
               intent.putExtra(MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));

               setResult(RESULT_OK, intent);

               finish();
           }
       });

    }
}