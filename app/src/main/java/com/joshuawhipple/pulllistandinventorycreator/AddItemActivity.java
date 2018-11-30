package com.joshuawhipple.pulllistandinventorycreator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class AddItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        //set the onClick listeners
        final Button btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Utility.isEmpty((EditText)findViewById(R.id.userTxtItemName)) &&
                        !Utility.isEmpty((EditText)findViewById(R.id.userTxtVolume))) {
                    //create an InventoryItem object
                    InventoryItem inventoryItem = new InventoryItem();

                    //set the itemCode, itemName, and the itemVolume attributes
                    inventoryItem.setItemCode(Utility.getNewItemCode(AddItemActivity.this));
                    inventoryItem.setItemName(((EditText) findViewById(R.id.userTxtItemName)).getText().toString());
                    inventoryItem.setVolume(((EditText) findViewById(R.id.userTxtVolume)).getText().toString());


                    //set the inventoryItem's unit of volume
                    RadioButton rbOunces = (RadioButton) findViewById(R.id.rbOunces);
                    RadioButton rbMilliters = (RadioButton) findViewById(R.id.rbMilliliters);
                    RadioButton rbLiters = (RadioButton) findViewById(R.id.rbLiters);
                    RadioButton rbGallons = (RadioButton) findViewById(R.id.rbGallons);

                    if (rbOunces.isChecked()) {
                        inventoryItem.setUnitOfVolume("Ounce(s)");
                    }
                    if (rbMilliters.isChecked()) {
                        inventoryItem.setUnitOfVolume("mL(s)");
                    }
                    if (rbLiters.isChecked()) {
                        inventoryItem.setUnitOfVolume("Liter(s)");
                    }
                    if (rbGallons.isChecked()) {
                        inventoryItem.setUnitOfVolume("Gallon(s)");
                    }

                    //add the item to the database
                    PullListAndInventoryDB db = new PullListAndInventoryDB(AddItemActivity.this);
                    db.insertInventoryItem(inventoryItem);

                    //go back to the update database page
                    startActivity(new Intent(getApplicationContext(), UpdateInventoryActivity.class));
                    finish();
                }
                else
                    Toast.makeText(getApplicationContext(),
                                   "Please fill in all fields.",
                                   Toast.LENGTH_SHORT).show();
            }
        });

        final Button btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),UpdateInventoryActivity.class));
                finish();
            }
        });
    }
}

