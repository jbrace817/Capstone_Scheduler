package dao;





import model.Users;

import javax.swing.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;



public class JdbcDao {
    private static final String CONFIG_FILE = "config.properties";
    private static Connection dbConnect = null;
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static Users currentUser;
    private static boolean loggedIn;

    /** Connects DB when application is started. */
    public static Connection startConnection() {

        File configFile = new File(CONFIG_FILE);

        try{
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);

            String jdbcUrl = props.getProperty("jdbcUrl");
            String dbUserName = props.getProperty("dbUserName");
            String dbPassword = props.getProperty("dbPassword");
            reader.close();

            dbConnect = DriverManager.getConnection(jdbcUrl, dbUserName, dbPassword);
            System.out.println(String.format("Connected to database %s successfully.", dbConnect.getCatalog()));

        }catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.PLAIN_MESSAGE);
        }catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.PLAIN_MESSAGE);
        }
        return dbConnect;
    }

    /** Closes Connection to Database. */
    public static void closeConnection(){
        try{
            dbConnect.close();
            System.out.print("Connection Closed.");
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Failed to close database connection.");
        }
    }

    /** Allows quick access to SQL DB Connection. Helps with load time. */
    public static Connection getDbConnection() {return dbConnect;}


    public static boolean isLoggedIn() {return loggedIn;}

    /** Brings over logged in username from the attemptLogin method on the login controller. Applies the username to
     * the instance variable "currentUser" for this class. */
    public static void loggedInUser(Users user) {
        currentUser = user;
        loggedIn = true;
        //System.out.print(currentUser);
    }

    /** Allows other methods to access who the current user is. */
    public static Users getCurrentUser() {return currentUser;}




}
