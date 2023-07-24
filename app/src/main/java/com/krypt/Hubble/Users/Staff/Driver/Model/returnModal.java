package com.krypt.Hubble.Users.Staff.Driver.Model;

public class returnModal {

    private final String bookingID;
    private final String clientName;
    private final String dateToDeliver;
    private final String location;
    private final String returnStatus;
    private final String county;
    private final String town;

    public returnModal(String bookingID, String clientName, String dateToDeliver,
                       String location, String returnStatus, String county, String town) {
        this.bookingID = bookingID;
        this.clientName = clientName;
        this.dateToDeliver = dateToDeliver;
        this.location = location;
        this.returnStatus = returnStatus;
        this.county = county;
        this.town = town;
    }

    public String getBookingID() {
        return bookingID;
    }

    public String getClientName() {
        return clientName;
    }

    public String getDateToDeliver() {
        return dateToDeliver;
    }

    public String getLocation() {
        return location;
    }

    public String getReturnStatus() {
        return returnStatus;
    }

    public String getCounty() {
        return county;
    }

    public String getTown() {
        return town;
    }
}
