package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customers;
import model.TimeConversion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class CustomerDao {

    /** Executes a sql command to join the customers table, first_level_division table and countries table using their
     * foreign keys of Division_ID and Country_ID. */
    public static ObservableList<Customers> getAllCustomers() {
        ObservableList<Customers> allCustomers = FXCollections.observableArrayList();

        try {
            String sqlQuery = "SELECT customers.*, first_level_divisions.Division, countries.Country FROM customers " +
                    "JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID JOIN " +
                    "countries ON first_level_divisions.Country_ID = countries.Country_ID;";
            PreparedStatement ps = JdbcDao.getDbConnection().prepareStatement(sqlQuery);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                allCustomers.add(
                        new Customers(
                                rs.getInt("Customer_ID"),
                                rs.getInt("Division_ID"),
                                rs.getString("Customer_Name"),
                                rs.getString("Address"),
                                rs.getString("Postal_Code"),
                                rs.getString("Phone"),
                                rs.getString("created_By"),
                                rs.getString("Last_Updated_By"),
                                rs.getTimestamp("Create_Date"),
                                rs.getTimestamp("Last_Update"),
                                rs.getString("Division"),
                                rs.getString("Country")
                        )
                );
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allCustomers;
    }

    /** Uses the same query as the getAllCustomers method above but uses Customer_ID to be specific to that customer. */
    public static Customers searchCustomer(Customers customer) {
        Customers found = new Customers();
        String sqlCmd = "SELECT customers.*, first_level_divisions.Division, countries.Country FROM customers " +
                "JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID JOIN " +
                "countries ON first_level_divisions.Country_ID = countries.Country_ID WHERE Customer_ID = ?;";
        try {
            PreparedStatement ps = JdbcDao.getDbConnection().prepareStatement(sqlCmd);
            ps.setInt(1,customer.getCustomerID());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                //creates new customer object using selected data from the customer that is found.
                found = new Customers(
                        rs.getInt("Customer_ID"),
                        rs.getInt("Division_ID"),
                        rs.getString("Customer_Name"),
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

    /** Updates existing customers in the DB and uses FirstLevelDivisionDao.getFLId to update the FK Division_id. */
    public static void updateCustomer(Customers customer) {
        String sqlCmd = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update = ?," +
                "Last_Updated_By = ?, Division_ID = ?  WHERE Customer_ID = ?;";

        try{
            PreparedStatement ps = JdbcDao.getDbConnection().prepareStatement(sqlCmd);

            ps.setString(1, customer.getCustomerName());
            ps.setString(2, customer.getAddress());
            ps.setString(3, customer.getPostalCode());
            ps.setString(4, customer.getPhone());
            ps.setTimestamp(5, Timestamp.valueOf(TimeConversion.utcTime()));
            ps.setString(6, customer.getLastUpdatedBy());
            ps.setInt(7, FirstLevelDivisionDao.getFLId(customer.getDivision()));
            ps.setInt(8, customer.getCustomerID());

            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /** Creates new customers in the DB. */
    public static void addNewCustomer(Customers customer) {
        String sqlCmd = "INSERT INTO customers" +
                "(Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Updated_By, Division_ID)" +
                "values (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = JdbcDao.getDbConnection().prepareStatement(sqlCmd);
            ps.setString(1, customer.getCustomerName());
            ps.setString(2, customer.getAddress());
            ps.setString(3, customer.getPostalCode());
            ps.setString(4, customer.getPhone());
            ps.setTimestamp(5, Timestamp.valueOf(TimeConversion.utcTime()));
            ps.setString(6, customer.getCreatedBy());
            ps.setString(7, customer.getLastUpdatedBy());
            ps.setInt(8, FirstLevelDivisionDao.getFLId(customer.getDivision()));

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Deletes customers from DB using customer id. */
    public static void deleteCustomer(Customers customer) {
        String sqlCmd = "DELETE FROM customers WHERE Customer_ID = ?;";

        try {
            PreparedStatement ps = JdbcDao.getDbConnection().prepareStatement(sqlCmd);
            ps.setInt(1,customer.getCustomerID());
            ps.executeUpdate();
            ps.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
