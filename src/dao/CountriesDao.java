package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Countries;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CountriesDao {

    /** Creates a list of all country names from the countries DB table. Distinct is used to eliminate duplicates. */
    public static ObservableList<String> getCountryName() throws SQLException {
        String sqlCmd = "SELECT DISTINCT Country FROM Countries;";
        ObservableList<String> allCountriesName = FXCollections.observableArrayList();
        PreparedStatement ps = JdbcDao.getDbConnection().prepareStatement(sqlCmd);
        ResultSet rs = ps.executeQuery();

        while ( rs.next() ) {
            allCountriesName.add(rs.getString("Country"));
        }
        ps.close();
        return allCountriesName;
    }


}
