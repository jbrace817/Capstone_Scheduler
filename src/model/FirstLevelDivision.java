package model;

import java.sql.Timestamp;

public class FirstLevelDivision {
    private int divisionID, countryID;
    private String division, created_by, lastUpdatedBy;
    private Timestamp createDate, lastUpdate;

    /** Default Contstructor for FirstLevelDivision. */
    public FirstLevelDivision() {

    }

    /** Constructor used to get all First Level Division data from FirstLevelDivisionDao. */
    public FirstLevelDivision(int divisionID, int countryID, String division, String created_by, String lastUpdatedBy,
                              Timestamp createDate, Timestamp lastUpdate) {
        this.divisionID = divisionID;
        this.countryID = countryID;
        this.division = division;
        this.created_by = created_by;
        this.lastUpdatedBy = lastUpdatedBy;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
    }

    /** Getters for First level Division. */
    public int getDivisionID() {return divisionID;}
    public int getCountryID() {return countryID;}
    public String getDivision() {return division;}
    public String getCreated_by(){return created_by;}
    public String getLastUpdatedBy() {return lastUpdatedBy;}
    public Timestamp getCreateDate() {return createDate;}
    public Timestamp getLastUpdate(){return lastUpdate;}

}
