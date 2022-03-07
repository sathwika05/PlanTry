package com.example.plantry;

import static java.lang.Boolean.FALSE;

import android.widget.DatePicker;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Item{
    private String itemName;
    private long expiryDate;
    private String addedBy;
    private Boolean isStaples;
    private Boolean isOnSale;

    // no arg constructor
    public Item() {
        this.itemName = "";
        this.expiryDate = -1;
        this.addedBy = "";
        this.isStaples = FALSE;
        this.isOnSale = FALSE;
    }

    // overloaded constructor
    public Item(String itemName, long expiryDate, String addedBy, Boolean isStaples, Boolean isOnSale) {
        this.itemName = itemName;
        this.expiryDate = expiryDate;
        this.addedBy = addedBy;
        this.isStaples = isStaples;
        this.isOnSale = isOnSale;
    }

    // setter and getter
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public long getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(long expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public Boolean getStaples() {
        return isStaples;
    }

    public void setStaples(Boolean staples) {
        isStaples = staples;
    }

    public Boolean getOnSale() {
        return isOnSale;
    }

    public void setOnSale(Boolean onSale) {
        isOnSale = onSale;
    }

    // Overriding equals and hash code
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int)expiryDate;
        result = prime * result
                + ((itemName == null) ? 0 : itemName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Item other = (Item) obj;
        if (itemName != other.itemName) {
            return false;
        }
        if (itemName == null) {
            if (other.itemName != null) {
                return false;
            }
        } else if (!itemName.equals(other.itemName)) {
            return false;
        }
        return true;
    }

}
