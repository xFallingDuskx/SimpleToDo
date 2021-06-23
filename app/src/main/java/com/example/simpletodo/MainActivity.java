package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.*;

public class MainActivity extends AppCompatActivity {

    List<String> items;
    Button btnAdd;
    EditText etItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;

    // Called by Android when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // The load and display the layout from the file rest/alyout/activity_main.xml
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);

        loadItems();

//    Mock data:
//        items = new ArrayList<>();
//        items.add("But milk");
//        items.add("Go to the gym");
//        items.add("Have fun!");


        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                Log.d("MainActivity" , String.valueOf(position));
                // Delete the item from the model
                items.remove(position);
                // Notify the adapter at which position it was deleted
                itemsAdapter.notifyItemRemoved(position);
                // Notify the user that an item has been removed
                Toast.makeText(getApplicationContext(), "Item was removed!", Toast.LENGTH_LONG).show();
                saveItems();
            }
        };

        itemsAdapter = new ItemsAdapter(items, onLongClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        // When someone has clicked on the ADD button
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Grab the content from the etItem and convert from an Edible to a String
                String todoItem = etItem.getText().toString();
                // Add item to model
                items.add(todoItem);
                // Notify adapter that an item was inserted at a certain position
                itemsAdapter.notifyItemInserted(items.size() - 1);
                // Update/Cleat the etITem
                etItem.setText("");
                // Embellishment; notify user that the item was added successfully using a toast
                // getting context, deciding what text, setting the duration, then displaying with .show()
                Toast.makeText(getApplicationContext(), "Item was added!", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });
    }

    // Create e data file
    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }

    // Load items by reading every line in the data file
    private void loadItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }

    // Save items by writing the data file
    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }
    }

    // add other methods here
    // To access a string (my_string) in res/values/strings.xml
    // XML: @string/my_string
    // Java: R.string.my_string

//    The three most important methods are:
//    onCreate - Called to create an activity. Usually sets the xml layout to use as the interface.
//    onPause - Called when leaving an activity. Usually where any needed data is stored for later.
//    onResume - Called when returning to an activity. Any stored data is restored here.
}