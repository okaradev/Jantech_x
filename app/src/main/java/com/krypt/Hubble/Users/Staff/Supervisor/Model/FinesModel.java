package com.krypt.Hubble.Users.Staff.Supervisor.Model;

public class FinesModel {
    private final String bookingID;
    private final String clientName;
    private final String penaltyID;
    private final String mpesaCode;
    private final String amount;
    private final String remarks;
    private final String penaltyStatus;

    public FinesModel(String bookingID, String clientName, String penaltyID,
                      String mpesaCode, String amount, String remarks, String penaltyStatus) {
        this.bookingID = bookingID;
        this.clientName = clientName;
        this.penaltyID = penaltyID;
        this.mpesaCode = mpesaCode;
        this.amount = amount;
        this.remarks = remarks;
        this.penaltyStatus = penaltyStatus;
    }

    public String getBookingID() {
        return bookingID;
    }

    public String getClientName() {
        return clientName;
    }

    public String getPenaltyID() {
        return penaltyID;
    }

    public String getMpesaCode() {
        return mpesaCode;
    }

    public String getAmount() {
        return amount;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getPenaltyStatus() {
        return penaltyStatus;
    }
}
