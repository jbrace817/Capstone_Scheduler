package model;

public class Contacts {
    private int contactID;
    private String contactName, email;

    /** Default constructor. */
    public Contacts() {

    }

    /** Constructor used to get all contact data from ContactsDao. */
    public Contacts(int contactID, String contactName, String email) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.email = email;
    }

    /*
    public int getContactID() {return contactID;}
    //public String getContactName() {return contactName;}
    public String getEmail() {return email;}
    */
}
