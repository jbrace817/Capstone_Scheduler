package main;

import controller.LoginController;
import dao.JdbcDao;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author James Brace
 * Student ID: 000790222
 * Software 2 - C195
 */

public class Main extends Application {

    public static void main(String[] args) {

        JdbcDao.startConnection(); //Calls method to connect database and confirms connectivity.
        launch(args);
        JdbcDao.closeConnection(); //Calls method to close connection to database when application is closed.
    }

    /** Loads login.fxml */
    @Override
    public void start(Stage stage) throws Exception {
        LoginController title = new LoginController();

        Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
        stage.setTitle(title.loginTitle());
        stage.setScene(new Scene(root, 444, 295));
        stage.show();
        stage.centerOnScreen();
    }
}
