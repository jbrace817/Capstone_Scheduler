package model;

import java.sql.Timestamp;

public class Beneficiary extends Customers{

    private int customerID, divisionID;
    private String beneficiaryName, address, postalCode, phone, createdBy, lastUpdatedBy, division, country;
    private Timestamp createDate, lastUpdate;

    public Beneficiary() {
    }

    public Beneficiary(int customerID) {
        this.customerID = customerID;
    }


    public int getCustomerID() {
        return customerID;
    }


    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public Beneficiary(int customerID, int divisionID, String beneficiaryName, String address, String postalCode,
                       String phone, String createdBy, String lastUpdatedBy, Timestamp createDate, Timestamp lastUpdate,
                       String division, String country) {
        this.customerID = customerID;
        this.divisionID = divisionID;
        this.beneficiaryName = beneficiaryName;
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

    @Override
    public int getDivisionID() {
        return divisionID;
    }

    @Override
    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String getPostalCode() {
        return postalCode;
    }

    @Override
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    @Override
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    @Override
    public String getDivision() {
        return division;
    }

    @Override
    public void setDivision(String division) {
        this.division = division;
    }

    @Override
    public String getCountry() {
        return country;
    }

    @Override
    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public Timestamp getCreateDate() {
        return createDate;
    }

    @Override
    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    @Override
    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    @Override
    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}