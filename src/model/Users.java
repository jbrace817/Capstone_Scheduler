package model;

import dao.JdbcDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Users {
    private int userID;
    private String userName, password, createdBy, lastUpdatedBy;
    private Timestamp createDate, lastUpdate;

    /** Constructor used to get all user data from userDao. */
    public Users(int userID, String userName, String password, String createdBy, String lastUpdatedBy, Timestamp createDate, Timestamp lastUpdate){
        this.userID = userID;
        this.userName = userName;
        this.password = password;
        this.createdBy = createdBy;
        this.lastUpdatedBy = lastUpdatedBy;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;

    }

    /** Used to search for users by userID. */
    public Users(int userID) {this.userID = userID;}

    public Users(String username, String password) {
        this.userName = username;
        this.password = password;

    }

    /** Default constructor for Users. */
    public Users() {

    }

    /** Getters for users. */

    public int getUserID() {return userID;}
    public String getUserName() {return userName;}
    public String getPassword() {return password;}
    public String getCreatedBy() {return createdBy;}
    public String getLastUpdatedBy() {return lastUpdatedBy;}
    public Timestamp getCreateDate() {return createDate;}
    public Timestamp getLastUpdate() {return lastUpdate;}

}
