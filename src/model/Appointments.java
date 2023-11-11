package model;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Appointments {

    private int appointmentID, customerID, contactID, userID;
    private String title, description, location, type, createdBy, lastUpdatedBy, contactName;
    private LocalDateTime start, end;
    private Timestamp createDate, lastUpdate;

    /** Default constructor. Used in creating a new appointment.  */
    public Appointments() {

    }
    /** Constructor used to get all appointment data from AppointmentsDAO */
    public Appointments(int appointmentID, int customerID, int contactID, int userID, String title, String description,
                        String location, String type, String createdBy, String lastUpdatedBy, LocalDateTime start,
                        LocalDateTime end, Timestamp createDate, Timestamp lastUpdate, String contactName) {
        this.appointmentID = appointmentID;
        this.customerID = customerID;
        this.contactID = contactID;
        this.userID = userID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.createdBy = createdBy;
        this.lastUpdatedBy = lastUpdatedBy;
        this.start = start;
        this.end = end;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
        this.contactName = contactName;

    }

    /** Copy constructor when editing or creating a new appointment */
    public Appointments(Appointments appt) {
        this.appointmentID = appt.appointmentID;
        this.title = appt.title;
        this.description = appt.description;
        this.location = appt.location;
        this.type = appt.type;
        this.start = appt.start;
        this.end = appt.end;
        this.createdBy = appt.createdBy;
        this.lastUpdate = appt.lastUpdate;
        this.lastUpdatedBy = appt.lastUpdatedBy;
        this.customerID = appt.customerID;
        this.userID = appt.userID;
        this.contactID = appt.contactID;
        this.createDate = appt.createDate;
        this.contactName = appt.lastUpdatedBy;

    }

    /** Getters for Appointment information */
    public int getAppointmentID() {return appointmentID;}
    public int getCustomerID() {return customerID;}
    public int getContactID() {return contactID;}
    public int getUserID() {return userID;}
    public String getTitle() {return title;}
    public String getDescription() {return description;}
    public String getLocation() {return location;}
    public String getType() {return type;}
    public String getCreatedBy() {return createdBy;}
    public String getLastUpdatedBy() {return lastUpdatedBy;}
    public LocalDateTime getStart() {return start;}
    public LocalDateTime getEnd() {return end;}
    public Timestamp getCreateDate() {return createDate;}
    public Timestamp getLastUpdate() {return lastUpdate;}
    public String getContactName() {return contactName;}

    /** Setters for Appointment information */
    public void setAppointmentID(int appointmentID) {this.appointmentID = appointmentID;}
    public void setCustomerID(int customerID) {this.customerID = customerID;}
    public void setContactID(int contactID) {this.contactID = contactID;}
    public void setUserID(int userID) {this.userID = userID;}
    public void setTitle(String title) {this.title = title;}
    public void setDescription(String description) {this.description = description;}
    public void setLocation(String location) {this.location = location;}
    public void setType(String type) {this.type = type;}
    public void setCreatedBy(String createdBy) {this.createdBy = createdBy;}
    public void setLastUpdatedBy(String lastUpdatedBy) {this.lastUpdatedBy = lastUpdatedBy;}
    public void setStart(LocalDateTime start) {this.start = start;}
    public void setEnd(LocalDateTime end) {this.end = end;}
    public void setCreateDate(Timestamp createDate) {this.createDate = createDate;}
    public void setLastUpdate(Timestamp lastUpdate) {this.lastUpdate = lastUpdate;}
    public void setContactName(String contactName) {this.contactName = contactName;}
}
