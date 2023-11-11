package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    /** Executes a sql command to retrieve all user information from users DB table and creates
     * an ObservableList.
     */
    public static ObservableList<Users> getAllUsers() {
        ObservableList<Users> allUsers = FXCollections.observableArrayList();

        try {
            String sqlQuery = "SELECT * FROM Users;";
            PreparedStatement ps = JdbcDao.getDbConnection().prepareStatement(sqlQuery);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                allUsers.add(
                        new Users(
                                rs.getInt("User_ID"),
                                rs.getString("User_Name"),
                                rs.getString("Password"),
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
        return allUsers;
    }

    /** returns user data that is found by searching for username or User id. */
    public static Users searchUser(Users user){
        Users userFound = new Users();

        String sqlcmd = "select * from users where User_Name = ?;";
        if (user.getUserID() > 0) {
            sqlcmd = "select * from users where User_ID = ?;";
        }

        try {
            PreparedStatement ps = JdbcDao.getDbConnection().prepareStatement(sqlcmd);

            if (user.getUserID() > 0) {
                ps.setInt(1, user.getUserID());
            } else {
                ps.setString(1, user.getUserName());
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                userFound = new Users(
                        rs.getInt("User_ID"),
                        rs.getString("User_Name"),
                        rs.getString("Password"),
                        rs.getString("Created_By") != null ? rs.getString("Created_By") : "",
                        rs.getString("Last_Updated_By") != null ? rs.getString("Last_Updated_By") : "",
                        rs.getTimestamp("Create_Date"),
                        rs.getTimestamp("Last_Update")
                );
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userFound;
    }


}
