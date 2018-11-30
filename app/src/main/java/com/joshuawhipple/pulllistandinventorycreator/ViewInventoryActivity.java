package com.joshuawhipple.pulllistandinventorycreator;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewInventoryActivity extends AppCompatActivity {
    private ListView inventoryItemsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_inventory);

        //get the items from the db
        PullListAndInventoryDB db = new PullListAndInventoryDB(this);
        ArrayList<InventoryItem> inventoryItems = db.getInventoryItems();

        //create the data for the listview
        ArrayList<HashMap<String,String>> data = new ArrayList<HashMap<String,String>>();
        for(InventoryItem item: inventoryItems){
            HashMap<String,String> map = new HashMap<>();
            map.put("ItemName", item.getItemName());
            map.put("Volume", item.getVolume() + " " + item.getUnitOfVolume());
            map.put("ItemCode",item.getItemCode());
            data.add(map);
        }

        //create the resource, from, and to variables
        int resource = R.layout.listview_item;
        String[] from = {"ItemName","Volume","ItemCode"};
        int[] to = {R.id.txtItemName,R.id.txtItemVolume,R.id.txtItemCode};

        //create and set the adapter
        inventoryItemsListView = (ListView)findViewById(R.id.inventoryItemsListView);
        SimpleAdapter adapter = new SimpleAdapter(this, data, resource,from,to);
        inventoryItemsListView.setAdapter(adapter);

        //onItemLongClick a prompt will ask the user if they want to add it to the pull list
        inventoryItemsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, final View view, int i, long l) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ViewInventoryActivity.this);
                alertDialog
                        .setMessage("Would you like to add this item to the Pull List?")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Add to the pull list
                                PullListAndInventoryDB db = new PullListAndInventoryDB(ViewInventoryActivity.this);
                                String itemCode = ((TextView)view.findViewById(R.id.txtItemCode)).getText().toString();
                                db.insertItemCodeIntoPullList(getApplicationContext(),itemCode);
                            }
                        }).show();
                return false;
            }
        });
    }

    //create the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.default_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //when clicked, the home icon goes to the mainActivity and closes this activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
        return super.onOptionsItemSelected(item);
    }
}
