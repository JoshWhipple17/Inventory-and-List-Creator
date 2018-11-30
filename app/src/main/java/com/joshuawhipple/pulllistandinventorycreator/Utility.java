package com.joshuawhipple.pulllistandinventorycreator;

import android.content.Context;
import android.widget.EditText;

public class Utility {

    //creates an Item code for a new Inventory Item
    public static String getNewItemCode(Context context){
        PullListAndInventoryDB db = new PullListAndInventoryDB(context);
        int itemCode = 0;
        while(db.isDublicate(String.valueOf(itemCode)) == true){
            itemCode++;
        }
        return String.valueOf(itemCode);
    }

    //checks if the EditText view is empty, if so return true, if not return false
    public static boolean isEmpty(EditText input){
        if(input.getText().toString().trim().equals(""))
           return true;
        else
            return false;
    }
}
