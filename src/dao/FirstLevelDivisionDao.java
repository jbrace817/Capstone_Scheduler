package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.FirstLevelDivision;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FirstLevelDivisionDao {

    /* Executes a sql command to retrieve all first level information from first_level_divisions DB table and creates
     * an ObservableList.


    public static ObservableList<FirstLevelDivision> getAllFLDivision() {
        ObservableList<FirstLevelDivision> allFLDivision = FXCollections.observableArrayList();

        try {
            String sqlQuery = "SELECT * FROM first_level_divisions;";
            PreparedStatement ps = JdbcDao.getDbConnection().prepareStatement(sqlQuery);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                allFLDivision.add(
                        new FirstLevelDivision(
                                rs.getInt("Division_ID"),
                                rs.getInt("Country_ID"),
                                rs.getString("Division"),
                                rs.getString("Created_By"),
                                rs.getString("Last_Updated_By"),
                                rs.getTimestamp("Create_Date"),
                                rs.getTimestamp("Last_Update")
                        )
                );

            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allFLDivision;
    }
     */

    /** Creates a list of all division names from the countries DB table. Distinct is used to eliminate duplicates. */
    public static ObservableList<String> getFlDivName() throws SQLException {
        String sqlCmd = "SELECT DISTINCT Division FROM first_level_divisions;";
        ObservableList<String> allFlDivNames = FXCollections.observableArrayList();
        PreparedStatement ps = JdbcDao.getDbConnection().prepareStatement(sqlCmd);
        ResultSet rs = ps.executeQuery();

        while ( rs.next() ) {
            allFlDivNames.add(rs.getString("Division"));
        }
        ps.close();
        return allFlDivNames;
    }

    /** retrieves division_id from the division combobox when adding or updating a customer since it is a foreign key in that table. */
    public static Integer getFLId(String divisionName) throws SQLException {
        int divisionId = 0;
        String sqlCmd = "SELECT Division, Division_ID FROM first_level_divisions WHERE Division = ?;";

        PreparedStatement ps = JdbcDao.getDbConnection().prepareStatement(sqlCmd);
        ps.setString(1, divisionName);
        ResultSet rs = ps.executeQuery();

        while ( rs.next() ) {
            divisionId = rs.getInt("Division_ID");
        }
        ps.close();
        return divisionId;
    }

    /** Creates an Observable list made up of country, country_ID, Division_ID, and Division to be used on the CustAMControllers
     * combo boxes. */
    public static ObservableList<String> getFilteredFlDivName(String country) throws SQLException {
        String sqlCmd = "SELECT countries.Country, countries.Country_ID, first_level_divisions.Division_ID, " +
                "first_level_divisions.Division from countries RIGHT OUTER JOIN first_level_divisions on " +
                "first_level_divisions.Country_ID = countries.Country_ID WHERE countries.Country = ?;";
        ObservableList<String> filteredFlDivNames = FXCollections.observableArrayList();
        PreparedStatement ps = JdbcDao.getDbConnection().prepareStatement(sqlCmd);
        ps.setString(1, country);
        ResultSet rs = ps.executeQuery();

        while ( rs.next() ) {
            filteredFlDivNames.add(rs.getString("Division"));
        }
        ps.close();
        return filteredFlDivNames;
    }
}