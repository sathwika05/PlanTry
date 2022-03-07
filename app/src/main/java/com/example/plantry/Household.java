package com.example.plantry;

import java.util.ArrayList;

public class Household {
    // data fields
    private String ownerUid;
    private String ownerEmail;
    private ArrayList<String> membersUid;
    private ArrayList<PantryList> list;       // List of many lists (for different categories of pantry lists)
    private ArrayList<Item> shoppingList;

    // no arg-constructor
    public Household(){
        this.ownerUid = "";
        this.ownerEmail = "";
        this.membersUid = new ArrayList<String>();
        this.list = new ArrayList<PantryList>();
        this.shoppingList = new ArrayList<Item>();
    }

    // overloaded constructor
    public Household(String ownerUid, String ownerEmail, ArrayList<String>membersUid, ArrayList<PantryList> list, ArrayList<Item> shoppingList) {
        this.ownerUid = ownerUid;
        this.ownerEmail = ownerEmail;
        this.membersUid = new ArrayList<String>();
        this.list = new ArrayList<PantryList>();
        this.shoppingList = new ArrayList<Item>();
    }

    // getter and setter

    public String getOwnerUid() {
        return ownerUid;
    }

    public void setOwnerUid(String ownerUid) {
        this.ownerUid = ownerUid;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public ArrayList<String> getMembersUid() {
        return membersUid;
    }

    public void setMembersUid(ArrayList<String> membersUid) {
        this.membersUid = membersUid;
    }

    public ArrayList<PantryList> getList() {
        return list;
    }

    public void setList(ArrayList<PantryList> list) {
        this.list = list;
    }

    public ArrayList<Item> getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(ArrayList<Item> shoppingList) {
        this.shoppingList = shoppingList;
    }
}
