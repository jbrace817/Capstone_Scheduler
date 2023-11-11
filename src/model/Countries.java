package model;


import java.sql.Timestamp;

public class Countries {
    public int countryID;
    private String country, createdBy, lastUpdatedBy;
    private Timestamp createDate, lastUpdate;

    /** Default Contstructor for Countries. */
    public Countries() {

    }

    /** Constructor used to get all country data from countriesDao. */
    public Countries(int countryID, String country, String createdBy, String lastUpdatedBy, Timestamp createDate, Timestamp lastUpdate) {
        this.countryID = countryID;
        this.country = country;
        this.createdBy = createdBy;
        this.lastUpdatedBy = lastUpdatedBy;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
    }


    /*
    public Countries(int countryID) {this.countryID = countryID;}
    public int getCountryID() {return countryID;}
    public String getCountry() {return  country;}
    */

}
