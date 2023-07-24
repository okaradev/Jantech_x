package com.krypt.Hubble.HUBBLEMODELS;

public class DriversModel {

    private final String driverID;
    private final String driverName;
    private final String orderID;

    public DriversModel(String driverID, String driverName, String orderID) {
        this.driverID = driverID;
        this.driverName = driverName;
        this.orderID = orderID;
    }

    public String getDriverID() {
        return driverID;
    }

    public String getDriverName() {

        return driverName;
    }

    public String getOrderID() {
        return orderID;
    }
}
