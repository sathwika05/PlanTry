package com.example.plantry;

import java.util.ArrayList;

public class PantryList extends Household {
    private ArrayList<Item> item;

    // no arg constructor
    public PantryList() {
        super("", "", new ArrayList<String>(), new ArrayList<PantryList>(), new ArrayList<Item>());
        this.item = new ArrayList<Item>();
    }

    // overloaded constructor
    public PantryList(String ownerUid, String ownerEmail, ArrayList<String> membersUid, ArrayList<PantryList> list, ArrayList<Item> shoppingList, ArrayList<Item> item) {
        super(ownerUid, ownerEmail, membersUid, list, shoppingList);
        this.item = new ArrayList<Item>();
    }

    // setter and getter
    public ArrayList<Item> getItem() {
        return item;
    }

    public void setItem(ArrayList<Item> item) {
        this.item = item;
    }
}
