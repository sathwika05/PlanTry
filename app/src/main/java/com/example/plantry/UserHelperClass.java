package com.example.plantry;

// new user account
public class UserHelperClass {
    String username,email,uid,householdOwner;

    // no arg constructor
    public UserHelperClass() {
        this.username = "";
        this.email = "";
        this.uid = "";
        this.householdOwner = "";
    }

    public UserHelperClass(String username, String email, String uid, String householdOwner) {
        this.username = username;
        this.email = email;
        this.uid = uid;
        this.householdOwner = householdOwner;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getUid() {
        return uid;
    }

    public String getHouseholdOwner() { return householdOwner; }

    public void setEmail(String email) {
        this.email = email;
    }
}
