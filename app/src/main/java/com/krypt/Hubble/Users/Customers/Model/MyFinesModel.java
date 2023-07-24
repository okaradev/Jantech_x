package com.krypt.Hubble.Users.Customers.Model;

public class MyFinesModel {
    private final String bookingID;
    private final String penaltyID;
    private final String amount;
    private final String mpesaCode;
    private final String remarks;
    private final String penaltyStatus;

    public MyFinesModel(String bookingID, String penaltyID, String amount, String mpesaCode, String remarks, String penaltyStatus) {
        this.bookingID = bookingID;
        this.penaltyID = penaltyID;
        this.amount = amount;
        this.mpesaCode = mpesaCode;
        this.remarks = remarks;
        this.penaltyStatus = penaltyStatus;
    }

    public String getBookingID() {
        return bookingID;
    }

    public String getPenaltyID() {
        return penaltyID;
    }

    public String getAmount() {
        return amount;
    }

    public String getMpesaCode() {
        return mpesaCode;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getPenaltyStatus() {
        return penaltyStatus;
    }
}
