package dao;

import model.AlertMessage;
import model.TimeConversion;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public class ReportsDao {

    /** Executes an SQL Query to create the report required by the assessment
     * to show the total number of customer appointments by type and month
     */
    public static String totalAppointments() throws SQLException {
        String sqlCmd = "select Type, Month(Start) as \"MonthID\", MONTHNAME(Start) as \"Month\", COUNT(*) as \"Total\" from appointments group by Type,Month(Start) order by Month";
        String sqlCmd2 = "SELECT MONTHNAME(Start) as \"Month\", Month(Start) as \"MonthID\", year(Start) as \"Year\", COUNT(MONTH(Start)) as \"Total\" from appointments group BY Month order by month";
        StringBuilder headers = new StringBuilder(); //allows mutable strings.
        headers.append("Total number of appointments per month and by type");
        PreparedStatement ps2 = JdbcDao.getDbConnection().prepareStatement(sqlCmd2);
        ResultSet rs2 = ps2.executeQuery();



        while(rs2.next()) {

            PreparedStatement ps = JdbcDao.getDbConnection().prepareStatement(sqlCmd);
            ResultSet rs = ps.executeQuery();
            headers.append(String.format("\n\nTotal appointments for %s, %s: %d", rs2.getString("Month"), rs2.getString("Year"), rs2.getInt("Total")));
            int count = 0;
            int month = 0;
            while(rs.next()) {

                if (rs.getInt("MonthID") > month ) {
                    //month = rs.getInt("MonthID");
                    if (rs.getInt("MonthID") == rs2.getInt("MonthID")) {
                        headers.append(String.format("    \n  %s: %d", rs.getString("Type"), rs.getInt("total")));

                    }

                }

            }
            ps.close();
        }

        ps2.close();
        return headers.toString();
    }

    /** Executes an SQL Query to create the report required by the assessment
     * to show a schedule for each contact in your organization that includes appointment ID, title, type
     * and description, start date and time, end date and time, and customer ID
     */
    public static String contactSchedule() throws SQLException {
        String sqlCmd = "SELECT * FROM appointments LEFT JOIN contacts on contacts.Contact_ID = appointments.Contact_ID order by appointments.Contact_ID;";
        StringBuilder headers = new StringBuilder(); //allows mutable strings.
        headers.append("Contacts Schedule Report\n\n");

        PreparedStatement ps = JdbcDao.getDbConnection().prepareStatement(sqlCmd);

        ResultSet rs = ps.executeQuery();

        int contact = 0;
        while(rs.next()) {

            if (rs.getInt("Contact_ID") > contact) {
                headers.append(String.format("%s: \n\n", rs.getString("Contact_Name")));
                contact = rs.getInt("Contact_ID");
            }
            headers.append(String.format(" Appointment ID: %d\n Title: %s\n Type: %s\n Start date and time: %s\n " +
                    "Customer ID: %d\n\n", rs.getInt("Appointment_ID"), rs.getString("Title"),
                    rs.getString("Type"), TimeConversion.utcTsToLocal(rs.getTimestamp("start")), rs.getInt("Customer_ID")));

        }
        ps.close();
        return headers.toString();
    }

    /** Executes an SQL Query to reate the report of every customer's information and when their account was
     * created or could be used to show when they joined as a customer.
     */
    public static String customerinfo() throws SQLException{
        String sqlCmd = "SELECT customers.*, date_format(customers.Create_Date, '%M %d, %Y') as ShowDate, " +
                "first_level_divisions.Division, countries.Country FROM customers JOIN first_level_divisions ON " +
                "customers.Division_ID = first_level_divisions.Division_ID JOIN countries ON first_level_divisions.Country_ID = countries.Country_ID;";
        StringBuilder headers = new StringBuilder(); //allows mutable strings.
        headers.append("Customer List: \n\n");

        PreparedStatement ps = JdbcDao.getDbConnection().prepareStatement(sqlCmd);

        ResultSet rs = ps.executeQuery();
        while (rs.next()){

            headers.append(String.format("Customer Name: %s\nPhone: %s\nCountry: %s\nDivision: %s\nCustomer since: %s\n\n",
                    rs.getString("Customer_Name"), rs.getString("Phone"), rs.getString("Country"),
                            rs.getString("Division"), rs.getString("ShowDate")));
        }

        ps.close();
        return headers.toString();


    }

    public static void export() {
        try {

            LocalDateTime current = LocalDateTime.now();
            DateTimeFormatter format = DateTimeFormatter.ofPattern("MM_dd_yyyy_HHmmss");
            String formatedDateTime = current.format(format);
            PrintWriter pw = new PrintWriter(new File("Reports/beneficiaries_" + formatedDateTime + ".csv"));
            StringBuilder sb = new StringBuilder();

            String sqlCmd = "SELECT beneficiary.*, first_level_divisions.Division, countries.Country, customers.Customer_Name FROM beneficiary \n" +
                    "JOIN first_level_divisions ON beneficiary.Division_ID = first_level_divisions.Division_ID JOIN \n" +
                    "countries ON first_level_divisions.Country_ID = countries.Country_ID join customers on beneficiary.Customer_ID = customers.Customer_ID;";
            PreparedStatement ps = JdbcDao.getDbConnection().prepareStatement(sqlCmd);
            ResultSet rs = ps.executeQuery();
            sb.append("Customer ID");
            sb.append(",");
            sb.append("Primary Customer Name");
            sb.append(",");
            sb.append("Beneficiary Name");
            sb.append(",");
            sb.append("Address");
            sb.append(",");
            sb.append("Postal Code");
            sb.append(",");
            sb.append("Phone");
            sb.append(",");
            sb.append("Created Date");
            sb.append(",");
            sb.append("Created By");
            sb.append(",");
            sb.append("Last Update");
            sb.append(",");
            sb.append("Last Updated By");
            sb.append(",");
            sb.append("Division");
            sb.append(",");
            sb.append("Country");
            sb.append("\r\n");

            while(rs.next()){
                sb.append(rs.getString("Customer_ID"));
                sb.append(",");
                sb.append(rs.getString("Customer_name"));
                sb.append(",");
                sb.append(rs.getString("Beneficiary_Name"));
                sb.append(",");
                sb.append(rs.getString("Address"));
                sb.append(",");
                sb.append(rs.getString("Postal_Code"));
                sb.append(",");
                sb.append(rs.getString("Phone"));
                sb.append(",");
                sb.append(rs.getString("Create_Date"));
                sb.append(",");
                sb.append(rs.getString("Created_By"));
                sb.append(",");
                sb.append(rs.getString("Last_update"));
                sb.append(",");
                sb.append(rs.getString("Last_Updated_By"));
                sb.append(",");
                sb.append(rs.getString("Division"));
                sb.append(",");
                sb.append(rs.getString("Country"));
                sb.append("\r\n");

            }
            pw.write(sb.toString());
            pw.close();
            AlertMessage.beneficiaryAlerts(1);

        } catch (Exception e) {
            AlertMessage.beneficiaryAlerts(5);
        }
    }


}



