package com.krypt.Hubble.Users.Staff.Supervisor.Model;

public class ReturnModal {
    private final String bookingNo;
    private final String name;
    private final String bookingStatus;
    private final String county;
    private final String town;


    public ReturnModal(String bookingNo, String name,
                       String bookingStatus,
                       String county, String town) {
        this.bookingNo = bookingNo;
        this.name = name;

        this.bookingStatus = bookingStatus;
        this.county = county;
        this.town = town;

    }

    public String getBookingNo() {
        return bookingNo;
    }

    public String getName() {
        return name;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public String getCounty() {
        return county;
    }

    public String getTown() {
        return town;
    }

}
