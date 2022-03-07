/*ABANDON*/

package com.example.plantry;

import java.util.ArrayList;
import java.util.Locale;

public class Categories extends PantryList{
    private String categoryName;

    // no arg constructor
    public Categories(){
        super("", "", new ArrayList<String>(), new ArrayList<PantryList>(), new ArrayList<Item>(), new ArrayList<Item>());
        this.categoryName = "";
    }

    // overloaded constructor
    public Categories(String ownerUid, String ownerEmail, ArrayList<String> membersUid, ArrayList<PantryList> list, ArrayList<Item> shoppingList, ArrayList<Item> item, String categoryName){
        super(ownerUid, ownerEmail, membersUid, list, shoppingList, item);
        this.categoryName = categoryName;
    }

    // setter and getter
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
