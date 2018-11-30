package com.joshuawhipple.pulllistandinventorycreator;

public class InventoryItem {
    private String itemName;
    private String volume;
    private String unitOfVolume;
    private String itemCode;

    //constructors
    public InventoryItem(){
        itemName = "";
        volume = "";
        unitOfVolume = "";
        itemCode = "";
    }
    public InventoryItem(String itemCode, String itemName, String volume,
                         String unitOfVolume){
        this.itemName = itemName;
        this.volume = volume;
        this.unitOfVolume = unitOfVolume;
        this.itemCode = itemCode;
    }

    //getter methods
    public String getItemName(){
        return this.itemName;
    }
    public String getVolume(){
        return this.volume;
    }
    public String getUnitOfVolume(){
        return this.unitOfVolume;
    }
    public String getItemCode(){
        return this.itemCode;
    }

    //setter methods
    public void setItemName(String itemName){
        this.itemName = itemName;
    }
    public void setVolume(String volume){
        this.volume = volume;
    }
    public void setUnitOfVolume(String unitOfVolume){
        this.unitOfVolume = unitOfVolume;
    }
    public void setItemCode(String itemCode){
        this.itemCode = itemCode;
    }






}
