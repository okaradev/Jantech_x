package com.krypt.Hubble.Users.Customers.Model;

public class PaymentItemsModal {

    private final String itemName;
    private final String price;
    private final String quantity;
    //  private String days;
    private final String subTotal;
    private final String capacity;





    public PaymentItemsModal(String itemName, String price, String quantity, String subTotal, String capacity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;

        this.subTotal = subTotal;
        this.capacity = capacity;
    }



    public String getItemName() {
        return itemName;
    }

    public String getPrice() {
        return price;
    }

    public String getQuantity() {
        return quantity;
    }


    public String getSubTotal() {
        return subTotal;
    }

    public String getCapacity() {
        return capacity;
    }
}
