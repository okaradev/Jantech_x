package com.krypt.Hubble.Users.Customers.Model;

public class ModalBook {
    private final String names;
    private final String place;
    private final String bookingID;
    private final String bookingCost;
    private final String mpesaCode;
    private final String bookingDate;
    private final String bookingStatus;
    private final String deliveryCost;
    private final String dateToDeliver;
    private final String bookingRemarks;

    public ModalBook(String names, String place, String bookingID,
                     String bookingCost, String mpesaCode, String bookingDate,
                     String bookingStatus, String deliveryCost, String dateToDeliver,
                     String bookingRemarks) {
        this.names = names;
        this.place = place;
        this.bookingID = bookingID;
        this.bookingCost = bookingCost;
        this.mpesaCode = mpesaCode;
        this.bookingDate = bookingDate;
        this.bookingStatus = bookingStatus;
        this.deliveryCost = deliveryCost;
        this.dateToDeliver = dateToDeliver;
        this.bookingRemarks = bookingRemarks;
    }

    public String getNames() {
        return names;
    }

    public String getPlace() {
        return place;
    }

    public String getBookingID() {
        return bookingID;
    }

    public String getBookingCost() {
        return bookingCost;
    }

    public String getMpesaCode() {
        return mpesaCode;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public String getDeliveryCost() {
        return deliveryCost;
    }

    public String getDateToDeliver() {
        return dateToDeliver;
    }

    public String getBookingRemarks() {
        return bookingRemarks;
    }
}
