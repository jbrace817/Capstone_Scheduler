package controller;

import dao.JdbcDao;
import dao.UserDao;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.AlertMessage;
import model.Users;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;

import java.net.URL;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private Label loginLbl, userNameLbl, passwordLbl, locationLbl;
    @FXML
    private Button cancelButton, loginButton;
    @FXML
    private TextField userIdField;
    @FXML
    private PasswordField passwordField;
    //directory of Resource Bundle with properties files for System Language.
    private final ResourceBundle RB_LABELS = ResourceBundle.getBundle("i18n/login", Locale.getDefault());



    /** Initializes the LoginController to use the locale, either French or English, in the localeLabels method. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        localeLabels();
    }

    /** Converts label text to locale language. */
    private void localeLabels(){
        /* Location */
        String zone = ZoneId.systemDefault().toString();
        locationLbl.setText(MessageFormat.format(RB_LABELS.getString("label.location"), zone));

        /* Labels and Buttons*/
        loginLbl.setText(RB_LABELS.getString("label.login"));
        userNameLbl.setText(RB_LABELS.getString("label.username"));
        passwordLbl.setText(RB_LABELS.getString("label.password"));
        loginButton.setText(RB_LABELS.getString("button.login"));
        cancelButton.setText(RB_LABELS.getString("button.cancel"));

    }

    /** Sets login screen title for locale language */
    public String loginTitle(){
        String title = RB_LABELS.getString("title.mainLogin");
        return title;
    }

    /** Cancel Button: Exits application. */
    public void cancel(ActionEvent actionEvent) {Platform.exit();}

    /** Login Button: Authenticates user. */
    public void login(ActionEvent actionEvent) throws SQLException, IOException {
        Window o = loginButton.getScene().getWindow();

        if (userIdField.getText().isEmpty()) {
            AlertMessage.infoBox(Alert.AlertType.CONFIRMATION, RB_LABELS.getString("error.userBlank"),null, RB_LABELS.getString("error.title"));
            return;
        }

        if (passwordField.getText().isEmpty()) {
            //viewAlert(Alert.AlertType.ERROR, o, RB_LABELS.getString("error.title"), RB_LABELS.getString("error.passwordBlank"));
            AlertMessage.infoBox(Alert.AlertType.CONFIRMATION, RB_LABELS.getString("error.passwordBlank"),null, RB_LABELS.getString("error.title"));

            return;
        }

        // Uses attemptLogin method to authenticate user.
        boolean logon = attemptLogin(userIdField.getText(), passwordField.getText());
        securityLog(userIdField.getText(), logon); //logs username and if the login was successful: true or failed: false.
        if (!logon) {
            AlertMessage.infoBox(Alert.AlertType.ERROR, RB_LABELS.getString("error.login"), null, RB_LABELS.getString("error.failed"));
        } else {

            System.out.println("Login Successful!");
            nextScene(actionEvent,"Appointments", "/view/MainForm.fxml"); //opens next scene MainForm.fxml if successful.
        }



    }

    /** Attempts to login with username and password found in DB. */
    public static boolean attemptLogin(String userName, String password) throws SQLException {
        String sqlUser = "SELECT * FROM users WHERE User_Name = ? AND Password = ?;";
        try {
            Users user = new Users(userName,password); //Stores the references for username and password in the Users constructor.
            Users lookup = UserDao.searchUser(user); // uses the user variable to search the user in the DB.

            PreparedStatement ps = JdbcDao.getDbConnection().prepareStatement(sqlUser);
            ps.setString(1, userName);
            ps.setString(2, password);
            System.out.println("executing query");
            JdbcDao.loggedInUser(lookup); //passes username to JdbcDAO where it is stored in the currentUser private instance variable for the class.

            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return false ;
    }

    /** Method that displays the next screen/scene MainForm.fxml.
     * In the lambda expression passed to the setOnCloseRequest method the CloseRequest event is represented by the argument evt. */
    private void nextScene(ActionEvent event, String title, String scenePath) throws IOException {

        Parent parent = FXMLLoader.load(getClass().getResource(scenePath));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setOnCloseRequest(evt -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit the application?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.NO) {
                // If no is clicked -> won't close
                evt.consume();
            }
        });
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
        stage.centerOnScreen();


    }
    /** Writes the username and if the login failed or was successful to the security log which is found in the root folder */
    public static void securityLog(String username, Boolean type) {
        try (FileWriter log = new FileWriter("Security_Log.txt", true)) {
            log.write(String.format("%s: Username: %s Success?: %s\n", LocalDateTime.now(), username, type.toString() ));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
