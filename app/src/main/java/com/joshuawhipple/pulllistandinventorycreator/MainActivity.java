package com.joshuawhipple.pulllistandinventorycreator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //View Inventory button
        final Button btnViewInventory = findViewById(R.id.btnViewInventory);
        btnViewInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start viewInventoryActivity and finish this activity
                startActivity(new Intent(getApplicationContext(), ViewInventoryActivity.class));
                finish();
            }
        });

        //View Pull List button
        final Button btnViewPullList = findViewById(R.id.btnViewPullList);
        btnViewPullList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start PullListActivity and finish this activity
                startActivity(new Intent(getApplicationContext(), PullListActivity.class));
                finish();
            }
        });

        //Update Inventory button
        final Button btnUpdateInventory = findViewById(R.id.btnUpdateInventory);
        btnUpdateInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start UpdateInventory and finish this activity
                startActivity(new Intent(getApplicationContext(), UpdateInventoryActivity.class));
                finish();
            }
        });

        //Search Inventory button
        final Button btnSearchInventory = findViewById(R.id.btnSearchInventory);
        btnSearchInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start SearchActivity and finish this activity
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                finish();
            }
        });
    }
}
