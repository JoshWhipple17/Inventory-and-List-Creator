package com.joshuawhipple.pulllistandinventorycreator;

import android.content.DialogInterface;
import android.content.Intent;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class UpdateInventoryActivity extends AppCompatActivity {
    private ListView updateInventoryItemsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_inventory);

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
        updateInventoryItemsListView = (ListView)findViewById(R.id.updateInventoryItemsListView);
        SimpleAdapter adapter = new SimpleAdapter(this, data, resource,from,to);
        updateInventoryItemsListView.setAdapter(adapter);

        //onItemLongClick a prompt will ask the user if they want to add it to the pull list
        updateInventoryItemsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, final View view, int i, long l) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(UpdateInventoryActivity.this);
                alertDialog
                        .setMessage("Would you like to delete this item from Inventory?")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //create a db object
                                PullListAndInventoryDB db = new PullListAndInventoryDB(UpdateInventoryActivity.this);

                                //create a variable that holds the items itemCode
                                String itemCode = ((TextView)view.findViewById(R.id.txtItemCode)).getText().toString();

                                //delete item from inventoryTable
                                db.deleteInventoryItem(itemCode);

                                //delete item from pullListTable
                                db.deletePullListItem(itemCode);
                            }
                        }).show();
                return false;
            }
        });
    }

    //create the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.update_inventory_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //when clicked, the home icon goes to the mainActivity and closes this activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_add_item:
                startActivity(new Intent(getApplicationContext(),AddItemActivity.class));
                finish();
                return true;
            case R.id.menu_home:
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
