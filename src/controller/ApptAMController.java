package controller;

import dao.*;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

/** Class for appointment add/modify controller. The same screen used for Saving and editing appointments. */
public class ApptAMController implements Initializable {
    @FXML
    public Label apptHeaderLbl, apptIdLbl, apptDateLbl, apptCustomerLbl, apptStartLbl, apptEndLbl, apptTitleLbl,
            apptLocationLbl, apptContactLbl, apptUserLbl, apptDescLbl, apptTypeLbl;
    @FXML
    public TextField apptID, apptTitleTxt, appLocationTxt, apptTypeTxt, apptStartTimeTxt, apptEndTimeTxt;
    @FXML
    public DatePicker apptDate;
    @FXML
    public ComboBox<Customers> apptCustCombo;
    @FXML
    public ComboBox<String> apptContactCombo;
    @FXML
    public ComboBox<Users> apptUserCombo;
    @FXML
    public Button apptCancelButton, apptSaveButton;
    @FXML
    public TextArea apptDescTxtArea;
    private Appointments appt;




    /** Override applied via setConverter. Initializes comboboxes and datepicker when controller opens. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //setConverter converts the list from CustomerDao.getAllCustomers() to just reflect the customer names.
        apptCustCombo.setConverter(new StringConverter<Customers>() {
            @Override
            public String toString(Customers customers) {return customers != null ? customers.getCustomerName() : "";}

            @Override
            public Customers fromString(String s) {
                return null;
            }
        });

        //setConverter converts the list from UserDao.getAllUsers() to just reflect the user names.
        apptUserCombo.setConverter(new StringConverter<Users>() {
            @Override
            public String toString(Users users) { return users != null ? users.getUserName() : "";}

            @Override
            public Users fromString(String s) {return null;}
        });

        apptCustCombo.setItems(CustomerDao.getAllCustomers());
        apptUserCombo.setItems(UserDao.getAllUsers());

        //gets the contact names using an ObservableList, and is surrounded by try and catch because SQL exception.
        try {
            apptContactCombo.setItems(ContactsDao.getAllContactName());

        } catch (SQLException e) {
            e.printStackTrace();
        }
        apptDate.setValue(LocalDate.now());

    }

    /** Cancel's any changes made on the ApptAMContrller screen. */
    public void apptCancel(ActionEvent actionEvent) throws IOException {
        AlertMessage.cancelButtons(actionEvent);

    }

    /** Customizes the AppointmentAM.fxml when clicking new appointment. Uses Default Constructor on Appointments.java */
    public void addNewAppt() {
        apptHeaderLbl.setText("New Appointment");
        apptID.setText("Auto");
        apptUserCombo.getSelectionModel().select(JdbcDao.getCurrentUser());
        appt = new Appointments();

    }


    /** Carries appointment data from selected item on the Mainform appointment table to the ApptAMController
     * @param selected holds the data, applies the data to the private "appt" variable to be used in this controller.
     */
    public void editAppt(Appointments selected) throws SQLException {
        this.appt = selected;

        apptID.setText(String.valueOf(appt.getAppointmentID()));
        apptTitleTxt.setText(appt.getTitle());
        appLocationTxt.setText(appt.getLocation());
        apptTypeTxt.setText(appt.getType());
        apptDescTxtArea.setText(appt.getDescription());

        //Searches for customer by ID and gets all related information.
        Customers editCustomer = CustomerDao.searchCustomer(new Customers(appt.getCustomerID()));
        apptCustCombo.getSelectionModel().select(editCustomer); //applies customer name from id found.

        //Searches for user by ID and gets all related information
        Users editUser = UserDao.searchUser(new Users(appt.getUserID()));
        apptUserCombo.getSelectionModel().select(editUser); //applies username from id found

        apptContactCombo.setItems(ContactsDao.getAllContactName()); //gets list of contact names from DB.
        apptContactCombo.getSelectionModel().select(appt.getContactName()); //sets selected contact name from selected appointment;

        apptDate.setValue(appt.getStart().toLocalDate()); //applies date from timestamp of selected appointment
        apptStartTimeTxt.setText(String.valueOf(appt.getStart().toLocalTime())); //applies selected time in HH:mm format
        apptEndTimeTxt.setText(String.valueOf(appt.getEnd().toLocalTime())); //applies selected time in HH:mm format

    }

    /** Saves modified or new appointment to the DB when clicking the save button on AppointmentAM.fxml. */
    public void saveAppt(ActionEvent actionEvent) throws SQLException {
        //Array lists of the different fields and comboboxes used to check if empty and provide error.
        TextField[] textFields = {apptID, apptTitleTxt, appLocationTxt, apptTypeTxt, apptStartTimeTxt, apptEndTimeTxt};
        ComboBox[] comboBoxes = {apptCustCombo, apptUserCombo, apptContactCombo};

        Integer contactID = ContactsDao.getContactId(apptContactCombo.getValue()); //gets contact id from contact name.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm"); //formats timestamp to HH:mm.

        //Checks text fields for missing data.
        for (TextField textField : textFields) {
            if ((textField.getText().isEmpty()) || apptDescTxtArea.getText().trim().length() == 0) {
                AlertMessage.apptAlerts(1);
                return;
            }
        }
        //Checks combo boxes for missing data.
        for (ComboBox comboBox : comboBoxes) {
            if ((comboBox.getSelectionModel().getSelectedItem() == null)) {
                AlertMessage.apptAlerts(1);
                return;
            }
        }

        //Regex is used to verify time is in HH:mm format.
        if(!apptStartTimeTxt.getText().trim().matches("^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$") ||
                !apptEndTimeTxt.getText().trim().matches("^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")) {
            AlertMessage.apptAlerts(2);
            return;
        }

        //sets timestamp of start and end times.
        LocalDateTime start = LocalDateTime.of(apptDate.getValue(), LocalTime.parse(apptStartTimeTxt.getText(), formatter));
        LocalDateTime end = LocalDateTime.of(apptDate.getValue(), LocalTime.parse(apptEndTimeTxt.getText(), formatter));

        //verifies start and end times are within business hours.
        if (!TimeConversion.businesshours(start, end, apptDate.getValue())){
            AlertMessage.apptAlerts(3);
            return;
        }

        //applies data to copy constructor in Appointments.java
        Appointments update = new Appointments(appt);
        update.setStart(start);
        update.setEnd(end);
        update.setCustomerID(apptCustCombo.getSelectionModel().getSelectedItem().getCustomerID());
        update.setTitle(apptTitleTxt.getText());
        update.setUserID(apptUserCombo.getSelectionModel().getSelectedItem().getUserID());
        update.setContactID(contactID);
        update.setLocation(appLocationTxt.getText());
        update.setType(apptTypeTxt.getText());
        update.setDescription(apptDescTxtArea.getText());
        update.setLastUpdatedBy(JdbcDao.getCurrentUser().getUserName());

        //prevents customer appointment overlapping.
        for (Appointments a : AppointmentsDao.getAllAppts()) {
            if (a.getCustomerID() == update.getCustomerID() && a.getAppointmentID() != update.getAppointmentID()) {
                try {
                    boolean b = update.getStart().isBefore(a.getEnd()) && update.getEnd().isAfter(a.getStart());
                    if (b){
                        AlertMessage.apptAlerts(4);
                        return;
                    }
                }catch (Error error){

                }
            }
        }

        if (appt.getAppointmentID() > 0) {

            AppointmentsDao.updateAppt(update);

        } else {
            update.setCreatedBy(JdbcDao.getCurrentUser().getUserName());
            AppointmentsDao.addNewAppt(update);
        }

        dashboard(actionEvent).close();

    }

    /** Returns to Mainform.fxml after users are done editing or creating appointments. */
    public static Stage dashboard(Event event) { return (Stage) ((Control) event.getSource()).getScene().getWindow();}

}
