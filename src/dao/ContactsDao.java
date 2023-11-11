package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contacts;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContactsDao {

    /** Creates a list of all contact names data from the contact DB table. Distinct is used to eliminate duplicates. */
    public static ObservableList<String> getAllContactName() throws SQLException {
        String sqlCmd = "SELECT DISTINCT Contact_name FROM contacts;";
        ObservableList<String> allContactName = FXCollections.observableArrayList();
        PreparedStatement ps = JdbcDao.getDbConnection().prepareStatement(sqlCmd);
        ResultSet rs = ps.executeQuery();

        while ( rs.next() ) {
            allContactName.add(rs.getString("Contact_Name"));
        }
        ps.close();
        return allContactName;
    }

    /** Finds contacts ID from DB using contacts name.
     * @param contactName is the value listed in "apptContactCombo" combo box.
     * */
    public static Integer getContactId(String contactName) throws SQLException {
        int contactId = 0;
        String sqlCmd = "SELECT Contact_ID, Contact_Name FROM contacts WHERE Contact_Name = ?;";

        PreparedStatement ps = JdbcDao.getDbConnection().prepareStatement(sqlCmd);
        ps.setString(1, contactName);
        ResultSet rs = ps.executeQuery();

        while ( rs.next() ) {
            contactId = rs.getInt("Contact_ID");
        }
        ps.close();
        return contactId;
    }
}
