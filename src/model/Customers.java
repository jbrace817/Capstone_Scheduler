package model;

import java.sql.Timestamp;

public class Customers {
    private int customerID, divisionID;
    private String customerName, address, postalCode, phone, createdBy, lastUpdatedBy, division, country;
    private Timestamp createDate, lastUpdate;

    /** Default Constructor. Used when adding a new customer. */
    public Customers() {

    }

    /** Constructor used to get all customer data from customerDAO */
    public Customers(int customerID, int divisionID, String customerName, String address, String postalCode,
                     String phone, String createdBy, String lastUpdatedBy, Timestamp createDate, Timestamp lastUpdate,
                     String division, String country) {
        this.customerID = customerID;
        this.divisionID = divisionID;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.createdBy = createdBy;
        this.lastUpdatedBy = lastUpdatedBy;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
        this.division = division;
        this.country = country;

    }

    /** Constructor to get the customer ID when Editing an existing appointment. */
    public Customers(int customerID) {this.customerID = customerID;}



    /** Getters for customer information */
    public int getCustomerID(){return customerID;}
    public int getDivisionID(){return divisionID;}
    public String getCustomerName(){return customerName;}
    public String getAddress() {return address;}
    public String getPostalCode() {return postalCode;}
    public String getPhone() {return phone;}
    public String getCreatedBy() {return createdBy;}
    public String getLastUpdatedBy() {return lastUpdatedBy;}
    public Timestamp getCreateDate() {return createDate;}
    public Timestamp getLastUpdate() {return lastUpdate;}
    public String getCountry() {return country;}
    public String getDivision() {return division;}

    /** Setters for customer information */
    public void setCustomerID(int customerID) {this.customerID = customerID;}
    public void setDivisionID(int divisionID) {this.divisionID = divisionID;}
    public void setCustomerName(String customerName) {this.customerName = customerName;}
    public void setAddress(String address) {this.address = address;}
    public void setPostalCode(String postalCode) {this.postalCode = postalCode;}
    public void setPhone(String phone) {this.phone = phone;}
    public void setCreatedBy(String createdBy) {this.createdBy = createdBy;}
    public void setLastUpdatedBy(String lastUpdatedBy) {this.lastUpdatedBy = lastUpdatedBy;}
    public void setCreateDate(Timestamp createDate) {this.createDate = createDate;}
    public void setLastUpdate(Timestamp lastUpdate) {this.lastUpdate = lastUpdate;}
    public void setCountry(String country) {this.country = country;}
    public void setDivision(String division) {this.division = division;}

}
