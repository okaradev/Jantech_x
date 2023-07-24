package com.krypt.Hubble.HUBBLEMODELS;


public class CartModel {

    private final String productID;
    private final String productName;
    private final String quantity;
    private final String price;
    private final String imgUrl;
    private final String itemID;
    private final String subToatl;
    private final String stock;
   // private String days;


    public CartModel(String productID, String productName, String quantity, String price,
                     String imgUrl, String itemID, String subToatl, String stock) {
        this.productID = productID;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.imgUrl = imgUrl;
        this.itemID = itemID;
        this.subToatl = subToatl;
        this.stock = stock;
       // this.days = days;
    }

    public String getProductID() {
        return productID;
    }

    public String getProductName() {
        return productName;
    }


    public String getPrice() {
        return price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getItemID() {
        return itemID;
    }

    public String getSubToatl() {
        return subToatl;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getStock() {
        return stock;
    }


}
