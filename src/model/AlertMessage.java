package model;

import controller.ApptAMController;
import controller.MainFormController;
import dao.JdbcDao;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.Optional;

public class AlertMessage {

    /** Message Boxes
     * @param type allows you to choose what type of Alert you want it to be, e.g. warning, informational,error, etc.
     * @param infoMessage is where I place the message for the alert.
     * @param headerText is the header text
     * @param title is the title in the border of the alert.*/
    public static void infoBox(Alert.AlertType type, String infoMessage, String headerText, String title) {
        Alert alert = new Alert(type);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }

    /** This method applies the switch block which allows me to choose which case I want to be shown in the alert used
     * for ApptAMController.
     * @param code associated the integer and the message I want to use throughout my code.*/
    public static void apptAlerts (int code) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error adding Appointment");
        alert.setHeaderText("Cannot Save Appointment");

        switch (code) {
            case 1 : {
                alert.setContentText("Missing data!Please review");
                break;
            }
            case 2 : {
                alert.setContentText("Please check Start and End Times of your appointment.");
                break;
            }
            case 3: {
                alert.setContentText("Business hours are between 8 AM and 10 PM EST.");
                break;
            }
            case 4: {
                alert.setContentText("The customer has another appointment at this time.\nPlease review.");
            }

        }
        alert.showAndWait();
    }

    /** This method applies the switch block which allows me to choose which case I want to be shown in the alert used for
     * CustAMController.
     * @param code associated the integer and the message I want to use throughout my code.*/
    public static void customerAlerts (int code) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error adding Appointment");
        alert.setHeaderText("Cannot Save Appointment");

        switch (code){
            case 1 : {
                alert.setContentText("Missing data! Please review.");
                break;
            }
            case 2 : {
                alert.setContentText("Please save customer information first!");
            }

        }
        alert.showAndWait();
    }


    /** This message prompts a message when clicking the X on the Title bar in the ApptAMContoller and CustAMController.
     * In the lambda expression passed to the setOnCloseRequest method the CloseRequest event is represented by the argument evt.
     * @param stage allows me to use the name of the stage I chose, so that when I click the X on that stage the message will appear.*/
    public static void closeRequest(Stage stage) {
        stage.setOnCloseRequest(evt -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "All changes will be discarded.", ButtonType.YES, ButtonType.NO);
            alert.setHeaderText("Are you sure you want to cancel?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.NO) {
                // If no is clicked -> won't close
                evt.consume();
            }
        });

    }

    /** Alert appears when the cancel buttons are clicked on the ApptAMController or CustAMController. */
    public static void cancelButtons(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "All changes will be discarded.", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("Are you sure you want to cancel?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.YES) {

            ApptAMController.dashboard(actionEvent).close();
        }
    }

    public static void beneficiaryAlerts (int code) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Error saving data");
        alert.setHeaderText("Could not save Beneficiary information.");

        switch (code){
            case 1 : {
                alert.setTitle("Report created");
                alert.setHeaderText("Report created successfully!");
                alert.setContentText("Please check Reports folder");
                break;
            }
            case 2 : {
                alert.setContentText("Please save customer information first!");
                break;
            }
            case 3 : {
                alert.setTitle("Data saved");
                alert.setHeaderText("Beneficiary information saved");
                alert.setContentText("Success!");
                break;
            }
            case 4 : {
                alert.setContentText("Please review and try again.");
                break;
            }
            case 5 : {
                alert.setTitle("Error");
                alert.setHeaderText("Export Failed!");
                alert.setContentText("Please review and try again");
                break;
            }
        }
        alert.showAndWait();
    }
}
