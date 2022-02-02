package com.example.simpletodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;

    List<String> items;

    Button btn_add;
    EditText etitem;
    RecyclerView rv_items;
    ItemsAdapter itemsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_add = findViewById(R.id.btn_add);
        etitem = findViewById(R.id.etitem);
        rv_items = findViewById(R.id.rv_items);


        loadItems();
        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d("MainActivity", "Single Click At Position" + position);

                Intent i = new Intent(MainActivity.this, EditActivity.class);

                i.putExtra(KEY_ITEM_TEXT, items.get(position));
                i.putExtra(KEY_ITEM_POSITION, position);

                startActivityForResult(i, EDIT_TEXT_CODE);

            }
        };
        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener(){
            @Override
            public void onItemLongClicked(int position) {
                items.remove(position);
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(),"item was removed",Toast.LENGTH_SHORT).show();
                saveItems();

            }
        };
        ItemsAdapter itemsAdapter = new ItemsAdapter(items, onLongClickListener, onClickListener);
       rv_items.setAdapter(itemsAdapter);
       rv_items.setLayoutManager(new LinearLayoutManager(this));

       btn_add.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
              String todoItem = etitem.getText().toString();

              items.add(todoItem);

              itemsAdapter.notifyItemInserted(items.size()-1);
              etitem.setText("");
              Toast.makeText(getApplicationContext(),"item was added",Toast.LENGTH_SHORT).show();
              saveItems();

           }
       });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);

            int position = data.getExtras().getInt(KEY_ITEM_POSITION);


            items.set(position, itemText);

            itemsAdapter.notifyItemChanged(position);

            saveItems();
            Toast.makeText(getApplicationContext(), "Item updated Successfully", Toast.LENGTH_SHORT).show();

        } else {
            Log.w("MainActivity", "Unknown call to onActivityResult");
        }

    }

    private File getDataFile(){
        return new File(getFilesDir(),"data.txt");
    }
    private void loadItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity","Error reading item", e);
            items = new ArrayList<>();
        }
    }
    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity","Error writing item", e);
        }

    }
}