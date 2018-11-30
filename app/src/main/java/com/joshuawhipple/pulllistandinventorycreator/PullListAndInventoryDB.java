package com.joshuawhipple.pulllistandinventorycreator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class PullListAndInventoryDB {
    //database constants
    public static final String DB_NAME = "pullListAndInventory.db";
    public static final int DB_VERSION = 1;

    //pull list table constants
    public static final String PULL_LIST_TABLE = "pull";

    public static final String PULL_CODE = "item_code";
    public static final int PULL_CODE_COL = 0;

    //inventory table constants
    public static final String INVENTORY_TABLE = "inventory";

    public static final String INVENTORY_CODE = "item_code";
    public static final int INVENTORY_CODE_COL = 0;

    public static final String INVENTORY_NAME = "item_name";
    public static final int INVENTORY_NAME_COL = 1;

    public static final String INVENTORY_VOLUME = "volume";
    public static final int INVENTORY_VOLUME_COL = 2;

    public static final String INVENTORY_UNIT = "unit_of_volume";
    public static final int INVENTORY_UNIT_COL = 3;

    //CREATE and DROP TABLE statements
    public static final String CREATE_PULL_TABLE =
                "CREATE TABLE " + PULL_LIST_TABLE + " (" +
                PULL_CODE + " TEXT  PRIMARY KEY NOT NULL UNIQUE);";

    public static final String CREATE_INVENTORY_TABLE =
                 "CREATE TABLE " + INVENTORY_TABLE + " (" +
                 INVENTORY_CODE + " TEXT PRIMARY KEY UNIQUE," +
                 INVENTORY_NAME + " TEXT  NOT NULL, "  +
                 INVENTORY_VOLUME + " TEXT NOT NULL, "   +
                 INVENTORY_UNIT + " TEXT NOT NULL);";

    public static final String DROP_PULL_TABLE =
            "DROP TABLE IF EXISTS " + PULL_LIST_TABLE;

    public static final String DROP_INVENTORY_TABLE =
            "DROP TABLE IF EXISTS " + INVENTORY_TABLE;

    //dbhelper class
    private static class DBHelper extends SQLiteOpenHelper{
        public DBHelper(Context context, String name,
                        SQLiteDatabase.CursorFactory factory,
                        int version){
            super(context,name,factory,version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_INVENTORY_TABLE);
            db.execSQL(CREATE_PULL_TABLE);

            //insert sample inventory item
            db.execSQL("INSERT INTO inventory VALUES ('0','Arizona', '20', 'mL')");

            //insert sample pull list item code
            db.execSQL("INSERT INTO pull VALUES ('0')");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d("Pull List and Inventory", "Updating dv from version " +
            + oldVersion + "to " + newVersion);

            db.execSQL(PullListAndInventoryDB.DROP_INVENTORY_TABLE);
            db.execSQL(PullListAndInventoryDB.DROP_PULL_TABLE);
            onCreate(db);
        }
    }

    //database object and databasse helper object
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    //constructor
    public PullListAndInventoryDB(Context context){
        dbHelper = new DBHelper(context,DB_NAME,null,DB_VERSION);
    }

    //openReadableDB, openWriteableDB, and closeDB
    private void openReadableDB(){
        db = dbHelper.getReadableDatabase();
    }
    private void openWriteableDB(){
        db = dbHelper.getWritableDatabase();
    }
    private void closeDB(){
        if(db != null) db.close();
    }

    //checks if there is a duplicate code
    public Boolean isDublicate(String itemCode){
        String countQuery = "SELECT * FROM " + INVENTORY_TABLE +
                            " WHERE " + INVENTORY_CODE + " LIKE ?;";

        this.openReadableDB();
        Cursor cursor = db.rawQuery(countQuery,new String[]{itemCode});
        int rowCount = cursor.getCount();
        db.close();

        if(rowCount > 0){
            return true;
        } else {
            return false;
        }
    }

    //delete all codes from the pull list table
    public int deleteAllPullListItems(){
        this.openWriteableDB();
        int rowCount = db.delete(PULL_LIST_TABLE,null,null);
        this.closeDB();
        return rowCount;
    }

    //delete code from the pull list table
    public int deletePullListItem(String itemCode){
        String where = PULL_CODE + " LIKE ?";
        String[] whereArgs = {itemCode};

        this.openWriteableDB();
        int rowCount = db.delete(PULL_LIST_TABLE,where,whereArgs);
        this.closeDB();

        return rowCount;
    }

    //delete an item from the inventory table
    public int deleteInventoryItem(String itemCode){
        String where = INVENTORY_CODE + " LIKE ?";
        String[] whereArgs = {itemCode};

        this.openWriteableDB();
        int rowCount = db.delete(INVENTORY_TABLE,where,whereArgs);
        this.closeDB();

        return rowCount;
    }

    //add an item to the inventory table
    public long insertInventoryItem(InventoryItem item){
        ContentValues cv = new ContentValues();
        cv.put(INVENTORY_CODE, item.getItemCode());
        cv.put(INVENTORY_NAME, item.getItemName());
        cv.put(INVENTORY_VOLUME, item.getVolume());
        cv.put(INVENTORY_UNIT, item.getUnitOfVolume());


        this.openWriteableDB();
        long rowID = db.insert(INVENTORY_TABLE,null,cv);
        return rowID;
    }

    //add an itemCode to the pullList table
    public long insertItemCodeIntoPullList(Context context,String itemcode){
        long rowID = 0;
        ContentValues cv = new ContentValues();
        cv.put(INVENTORY_CODE, itemcode);

        this.openWriteableDB();
        try{
            rowID = db.insert(PULL_LIST_TABLE,null,cv);
        } catch (SQLiteConstraintException e){
            Toast.makeText(context,"Already In Pull List",Toast.LENGTH_SHORT).show();
        }

        this.closeDB();

        return rowID;
    }

    //get an item from the inventory table based on its code
    public InventoryItem getInventoryItem(String itemCode){
        String where = INVENTORY_CODE + " LIKE ?";
        String[] whereArgs = {itemCode};

        this.openReadableDB();
        Cursor cursor =
                db.query(
                        INVENTORY_TABLE,
                        null,
                        where,
                        whereArgs,
                        null,
                        null,
                        null);
        cursor.moveToFirst();
        InventoryItem item = getInventoryItemFromCursor(cursor);
        if(cursor != null) cursor.close();
        this.closeDB();

        return item;
    }

    //get inventory items from pull list codes
    public ArrayList<InventoryItem>
    getInventoryItemsFromPullListCodes(ArrayList<String> pullListCodes){
        ArrayList<InventoryItem> inventoryItems = new ArrayList<>();
        for(String itemCode: pullListCodes){
            inventoryItems.add(getInventoryItem(itemCode));
        }
        return inventoryItems;
    }

    //get all items from the inventory table
    public ArrayList<InventoryItem> getInventoryItems(){
        this.openReadableDB();

        Cursor cursor =
                db.query(
                        INVENTORY_TABLE,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);

        ArrayList<InventoryItem> inventoryItems = new ArrayList<>();
        while(cursor.moveToNext()){
            inventoryItems.add(getInventoryItemFromCursor(cursor));
        }

        if(cursor != null) cursor.close();
        this.closeDB();

        return inventoryItems;
    }

    //modify an item in the inventory database

    //get all codes from the pull list table
    public ArrayList<String> getPullListCodes(){
        this.openReadableDB();

        Cursor cursor =
                db.query(
                        PULL_LIST_TABLE,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);

        ArrayList<String> pullListCodes = new ArrayList<>();
        while(cursor.moveToNext()){
            pullListCodes.add(getPullListCodeFromCursor(cursor));//
        }

        if(cursor != null) cursor.close();
        this.closeDB();

        return pullListCodes;
    }

    //get an inventory item from the cursor
    private static InventoryItem getInventoryItemFromCursor(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) {
            Log.i("test","hey");
            return null;
        } else {
            try {
                InventoryItem item = new InventoryItem(
                        cursor.getString(INVENTORY_CODE_COL),
                        cursor.getString(INVENTORY_NAME_COL),
                        cursor.getString(INVENTORY_VOLUME_COL),
                        cursor.getString(INVENTORY_UNIT_COL));

                return item;
            } catch (Exception e) {
                return null;
            }
        }
    }

    //create a cursor object from the pull list codes
    private static String getPullListCodeFromCursor(Cursor cursor){
        if (cursor == null || cursor.getCount() == 0) {
            return null;
        } else {
            try {
                String code = cursor.getString(PULL_CODE_COL);
                return code;
            } catch (Exception e) {
                return null;
            }
        }
    }

    //use for searching the database for a specific item or items
    public ArrayList<InventoryItem> searchForMatchingItems(String userSearch){
        String where = INVENTORY_NAME + " LIKE ?";
        String[] whereArgs = {"%" + userSearch + "%"};

        this.openReadableDB();
        Cursor cursor =
                db.query(
                        INVENTORY_TABLE,
                        null,
                        where,
                        whereArgs,
                        null,
                        null,
                        null);
        ArrayList<InventoryItem> inventoryItems = new ArrayList<>();
        while(cursor.moveToNext()){
            inventoryItems.add(getInventoryItemFromCursor(cursor));
        }

        if(cursor != null) cursor.close();
        this.closeDB();

        return inventoryItems;
    }
}
