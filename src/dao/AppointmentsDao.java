package dao;

//import com.mysql.cj.util.TimeUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointments;
import model.TimeConversion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class AppointmentsDao {

    /** Executes a sql command to retrieve all appointment and contact data from DB. */
    public static ObservableList<Appointments> getAllAppts() {
        ObservableList<Appointments> allAppointments = FXCollections.observableArrayList();

        try {
            String sqlCmd = "SELECT * FROM appointments LEFT JOIN contacts on contacts.Contact_ID = appointments.Contact_ID";
            PreparedStatement ps = JdbcDao.getDbConnection().prepareStatement(sqlCmd);

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                allAppointments.add(
                        new Appointments(
                                rs.getInt("Appointment_ID"),
                                rs.getInt("Customer_ID"),
                                rs.getInt("Contact_ID"),
                                rs.getInt("User_ID"),
                                rs.getString("Title"),
                                rs.getString("Description"),
                                rs.getString("Location"),
                                rs.getString("Type"),
                                rs.getString("Created_By"),
                                rs.getString("Last_Updated_By"),
                                TimeConversion.utcTsToLocal(rs.getTimestamp("Start")),
                                TimeConversion.utcTsToLocal(rs.getTimestamp("End")),
                                rs.getTimestamp("Create_Date"),
                                rs.getTimestamp("Last_Update"),
                                rs.getString("Contact_name")

                        )
                );

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allAppointments;
    }

    /** Updates existing appointments in the DB. */
    public static void updateAppt(Appointments appointment) throws SQLException {
        String sqlCmd = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, " +
                "End = ?, Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? " +
                "WHERE Appointment_ID = ?;";

        try {
            PreparedStatement ps = JdbcDao.getDbConnection().prepareStatement(sqlCmd);

            ps.setString(1, appointment.getTitle());
            ps.setString(2,appointment.getDescription());
            ps.setString(3, appointment.getLocation());
            ps.setString(4,appointment.getType());
            ps.setTimestamp(5, TimeConversion.localTimetoStamp(appointment.getStart()));
            ps.setTimestamp(6, TimeConversion.localTimetoStamp(appointment.getEnd()));
            ps.setTimestamp(7, Timestamp.valueOf(TimeConversion.utcTime()));
            ps.setString(8, appointment.getLastUpdatedBy());
            ps.setInt(9, appointment.getCustomerID());
            ps.setInt(10, appointment.getUserID());
            ps.setInt(11, appointment.getContactID());
            ps.setInt(12, appointment.getAppointmentID());
            ps.executeUpdate();
            ps.close();

        }catch (SQLException e) {
            e.printStackTrace();

        }
    }

    /** Creates new appointments in the DB. */
    public static void addNewAppt(Appointments appointment) {
        String sqlCmd = "INSERT INTO appointments" +
                "(Title, Description, Location, Type, Start, End, Created_By, Last_Updated_By, Create_Date, Customer_ID, User_ID, Contact_ID)" +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = JdbcDao.getDbConnection().prepareStatement(sqlCmd);

            ps.setString(1, appointment.getTitle());
            ps.setString(2,appointment.getDescription());
            ps.setString(3, appointment.getLocation());
            ps.setString(4,appointment.getType());
            ps.setTimestamp(5, TimeConversion.localTimetoStamp(appointment.getStart()));
            ps.setTimestamp(6, TimeConversion.localTimetoStamp(appointment.getEnd()));
            ps.setString(7, appointment.getCreatedBy());
            ps.setString(8, appointment.getLastUpdatedBy());
            ps.setTimestamp(9,Timestamp.valueOf(TimeConversion.utcTime()));
            ps.setInt(10, appointment.getCustomerID());
            ps.setInt(11, appointment.getUserID());
            ps.setInt(12, appointment.getContactID());
            ps.executeUpdate();
            ps.close();

        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Deletes appointments from DB using appoinment id. */
    public static void deleteAppt(Appointments appointment) {
        String sqlCmd = "delete from appointments where Appointment_ID = ?;";

        try {
            PreparedStatement ps = JdbcDao.getDbConnection().prepareStatement(sqlCmd);
            ps.setInt(1,appointment.getAppointmentID());
            ps.executeUpdate();
            ps.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

