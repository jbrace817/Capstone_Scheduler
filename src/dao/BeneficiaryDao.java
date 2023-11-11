package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Beneficiary;
import model.Customers;
import model.TimeConversion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class BeneficiaryDao {

    /** Searches and retrieves the Beneficiary's information by Customer_id. */
    public static Beneficiary searchBeneficiary(Beneficiary beneficiaries) {
        Beneficiary found = new Beneficiary();
        String sqlCmd = "SELECT beneficiary.*, first_level_divisions.Division, countries.Country FROM beneficiary " +
                "JOIN first_level_divisions ON beneficiary.Division_ID = first_level_divisions.Division_ID JOIN " +
                "countries ON first_level_divisions.Country_ID = countries.Country_ID WHERE Customer_ID = ?;";
        try {
            PreparedStatement ps = JdbcDao.getDbConnection().prepareStatement(sqlCmd);
            ps.setInt(1,beneficiaries.getCustomerID());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                //creates new customer object using selected data from the customer that is found.
                found = new Beneficiary(
                        rs.getInt("Customer_ID"),
                        rs.getInt("Division_ID"),
                        rs.getString("Beneficiary_Name"),
                        rs.getString("Address"),
                        rs.getString("Postal_Code"),
                        rs.getString("Phone"),
                        rs.getString("created_By"),
                        rs.getString("Last_Updated_By"),
                        rs.getTimestamp("Create_Date"),
                        rs.getTimestamp("Last_Update"),
                        rs.getString("Division"),
                        rs.getString("Country")
                );
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return found;
    }

    /** Updates existing beneficiary information in the DB when data is updated or changed from the GUI of the BeneficiaryAM Controller
     *  and uses FirstLevelDivisionDao.getFLId to update the FK Division_id. */
    public static void updateBeneficiary(Beneficiary beneficiaries) {
        String sqlCmd = "UPDATE beneficiary SET Beneficiary_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update = ?," +
                "Last_Updated_By = ?, Division_ID = ?  WHERE Customer_ID = ?;";

        try{
            PreparedStatement ps = JdbcDao.getDbConnection().prepareStatement(sqlCmd);

            ps.setString(1, beneficiaries.getBeneficiaryName());
            ps.setString(2, beneficiaries.getAddress());
            ps.setString(3, beneficiaries.getPostalCode());
            ps.setString(4, beneficiaries.getPhone());
            ps.setTimestamp(5, Timestamp.valueOf(TimeConversion.utcTime()));
            ps.setString(6, beneficiaries.getLastUpdatedBy());
            ps.setInt(7, FirstLevelDivisionDao.getFLId(beneficiaries.getDivision()));
            ps.setInt(8, beneficiaries.getCustomerID());

            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Creates new beneficiary's in the DB. */
    public static void addNewBeneficiary(Beneficiary beneficiaries) {
        String sqlCmd = "INSERT INTO beneficiary" +
                "(Beneficiary_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Updated_By, Division_ID, Customer_ID)" +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = JdbcDao.getDbConnection().prepareStatement(sqlCmd);
            ps.setString(1, beneficiaries.getBeneficiaryName());
            ps.setString(2, beneficiaries.getAddress());
            ps.setString(3, beneficiaries.getPostalCode());
            ps.setString(4, beneficiaries.getPhone());
            ps.setTimestamp(5, Timestamp.valueOf(TimeConversion.utcTime()));
            ps.setString(6, beneficiaries.getCreatedBy());
            ps.setString(7, beneficiaries.getLastUpdatedBy());
            ps.setInt(8, FirstLevelDivisionDao.getFLId(beneficiaries.getDivision()));
            ps.setInt(9, beneficiaries.getCustomerID());

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Deletes beneficiary's from DB using customer id. */
    public static void deleteBeneficiary(Beneficiary beneficiaries) {
        String sqlCmd = "DELETE FROM beneficiary WHERE Customer_ID = ?;";

        try {
            PreparedStatement ps = JdbcDao.getDbConnection().prepareStatement(sqlCmd);
            ps.setInt(1,beneficiaries.getCustomerID());
            ps.executeUpdate();
            ps.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Verify's that the beneficiary exists by customer id. */
    public static Boolean beneficiaryExists(int beneficiaries) throws SQLException {
        String sqlCmd = "SELECT * FROM beneficiary WHERE Customer_ID = ?;";


            PreparedStatement ps = JdbcDao.getDbConnection().prepareStatement(sqlCmd);
            ps.setInt(1,beneficiaries);
            ResultSet rs = ps.executeQuery();

            while (!rs.next()) {
                return false;
            }


        return true;
    }

}
