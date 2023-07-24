package com.krypt.Hubble.Users.Staff.Driver.Model;

public class AssignedItems {

    private final String itemName;
    private final String quantity;

    public AssignedItems(String itemName, String quantity) {
        this.itemName = itemName;
        this.quantity = quantity;
    }

    public String getItemName() {
        return itemName;
    }

    public String getQuantity() {
        return quantity;
    }

}
