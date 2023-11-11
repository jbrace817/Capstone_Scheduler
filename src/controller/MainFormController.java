package controller;

import com.mysql.cj.util.StringUtils;
import dao.AppointmentsDao;
import dao.CustomerDao;
import dao.JdbcDao;
import dao.ReportsDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.AlertMessage;
import model.Appointments;
import model.Customers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainFormController implements Initializable {
    @FXML
    public TableView<Appointments> apptTable;
    @FXML
    public TableColumn<Appointments, Integer> apptIDCol, apptCustIdCol, apptUserIdCol;
    @FXML
    public TableColumn<Appointments, String> apptTitleCol, apptLocationCol, apptDescCol, apptContactCol, apptTypeCol;
    @FXML
    public TableColumn<Appointments, LocalDateTime> apptStartCol, apptEndCol;
    @FXML
    public TableView<Customers> customerTable;
    @FXML
    public TableColumn<Customers, Integer> custIdCol;
    @FXML
    public TableColumn<Customers, String> custNameCol, custAddressCol, custPhoneCol, custCountryCol, custFLDivisionCol;
    @FXML
    public Button dashEditApptButton, dashNewApptButton, dashDeleteButton, dashNewCustButton, dashEditCustButton,
            reportMTButton, reportSchedButton, reportsCustInfoButton;
    public RadioButton apptWeekRadio, apptMonthRadio;
    public TextArea reportTxtArea;
    public TextField contactSearchBox, customerSearchBox;

    /** Initializes the MainFormController to load the appointment data from the Appointments.java file that is in
     * turn retrieved from the AppointmentsDao.getAllAppts method. Same goes for the customer table. */
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //retrieves the data from the DB and applies it to the columns in the appointment table.
        apptIDCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        apptContactCol.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        apptTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        apptDescCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        apptLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        apptTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        apptStartCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        apptEndCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        apptCustIdCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        apptUserIdCol.setCellValueFactory(new PropertyValueFactory<>("userID"));
        refreshApptTable();

        //retrieves the data from the DB and applies it to the columns in the customer table.
        custIdCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        custNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        custAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        custPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        custFLDivisionCol.setCellValueFactory(new PropertyValueFactory<>("division"));
        custCountryCol.setCellValueFactory(new PropertyValueFactory<>("country"));
        refreshCustomerTable();
        fifteenMinAlert(); //checks to see if there is an appointment for the user in 15 minutes or less.

    }

    /** Refreshes the appointment table if changes are made. */
    public void refreshApptTable() {
        apptTable.setItems(AppointmentsDao.getAllAppts());
    }

    /** Refreshes the customer table if changes are made. */
    public void refreshCustomerTable() {
        customerTable.setItems(CustomerDao.getAllCustomers());
    }

    /** Loads the AppointmmentAM.fxml when the new appointment button is clicked. */
    public void newAppt() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AppointmentAM.fxml" ));
        Parent root = (Parent) loader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL); //blocks events from being delivered to other application windows.
        stage.initStyle(StageStyle.DECORATED); //applies title bar.
        stage.setTitle("New Appointment"); //changes window title when opened.
        stage.setScene(new Scene(root));
        ApptAMController newController = loader.getController(); //loads AppointmentAM.fxml
        newController.addNewAppt(); //applies addNewAppt method to apptAMController.
        AlertMessage.closeRequest(stage); //prompts alert when X on title bar is clicked.
        stage.showAndWait();
        refreshApptTable();
    }

    /** Loads the CustomerAM.fxml when the new customer button is clicked. */
    public void newCustomer() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CustomerAM.fxml" ));
        Parent root = (Parent) loader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL); //blocks events from being delivered to other application windows.
        stage.initStyle(StageStyle.DECORATED); //applies title bar.
        stage.setTitle("New Customer");
        stage.setScene(new Scene(root));
        CustAMController controller = loader.getController(); //loads CustomerAM.fxml
        controller.addNewCust(); //applies addNewCust method to custAMController.
        AlertMessage.closeRequest(stage); //prompts alert when X on title bar is clicked.
        stage.showAndWait();
        refreshCustomerTable();
    }

    /** Edit appointment button */
    public void dashEditAppt(ActionEvent actionEvent) throws IOException, SQLException {
        // if no selection is made alert appears
        if (apptTable.getSelectionModel().isEmpty()) {
            AlertMessage.infoBox(Alert.AlertType.WARNING, "You must select an appointment to modify!", "Warning!", "Warning");
            return;
        }
        editAppointment(actionEvent);
    }

    /** Opens AppointmentAM.fxml with the selected information from the appointment table. */
    private void editAppointment(Event event) throws IOException, SQLException {
        Appointments selected = apptTable.getSelectionModel().getSelectedItem(); //gets selected info to populate fields.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AppointmentAM.fxml" ));
        Parent root = (Parent) loader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL); //blocks events from being delivered to other application windows.
        stage.initStyle(StageStyle.DECORATED); //applies title bar.
        stage.setScene(new Scene(root));
        ApptAMController editController = loader.getController(); //Loads AppointmentAM.fxml
        editController.editAppt(selected); //applies selected data frome appointment table to editAppt method on apptAMController.
        AlertMessage.closeRequest(stage);
        stage.showAndWait();
        refreshApptTable();

}

    /** Edit customer button */
    public void dashEditCustomer(ActionEvent actionEvent) throws IOException, SQLException {
        if (customerTable.getSelectionModel().isEmpty()) {
            AlertMessage.infoBox(Alert.AlertType.WARNING, "You must select an customer to modify!", "Warning!", "Warning");
            return;
        }
        editCustomer(actionEvent);

    }

    /** Opens CustomerAM.fxml with the selected information from the customer table. */
    public void editCustomer(Event event) throws IOException, SQLException {
        Customers selected = customerTable.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CustomerAM.fxml" ));
        Parent root = (Parent) loader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL); //blocks events from being delivered to other application windows.
        stage.initStyle(StageStyle.DECORATED); //applies title bar.
        stage.setScene(new Scene(root));
        CustAMController editController = loader.getController(); //Loads CustomerAM.fxml
        editController.editCustomer(selected); //applies selected data from customer table to editAppt method on apptAMController.
        AlertMessage.closeRequest(stage);
        stage.showAndWait();
        refreshCustomerTable();
    }

    /** Delete/cancel appointment button on MainForm of appointment tab. */
    public void deleteAppt(ActionEvent actionEvent) {
        if (apptTable.getSelectionModel().isEmpty()) {
            AlertMessage.infoBox(Alert.AlertType.WARNING, "You must select an Appointment to delete!", "Warning!", "Warning");
            return;
        }
        //shows a confirmation alert of the appointment selected to delete. Lists appointment id and type as requested in Assessment.
        Appointments selected = apptTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Click YES to confirm", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(String.format("This will delete the selected appointment:\nAppointment ID: %s\nType: %s", selected.getAppointmentID(), selected.getType()));
        alert.setContentText("Would you like to continue?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
            AppointmentsDao.deleteAppt(selected);
            refreshApptTable();
        }

    }

    /** Delete customer button on MainForm of customer tab. */
    public void deleteCustomer(ActionEvent actionEvent) {
        if (customerTable.getSelectionModel().isEmpty()) {
            AlertMessage.infoBox(Alert.AlertType.WARNING, "You must select an customer to delete!", "Warning!", "Warning");
            return;
        }
        Customers selected = customerTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Click YES to confirm", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(String.format("This will delete the selected customer:\n%s", selected.getCustomerName()));
        alert.setContentText("Would you like to continue?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
            CustomerDao.deleteCustomer(selected);
            refreshCustomerTable();
            refreshApptTable();
        }

    }

    /** Fifteen minute alert if there is a upcoming appointment for the user that logs in. Shows appointment id, title, type, and description
     * as requested by assessment. */
    private void fifteenMinAlert() {
        Appointments apptAlert = null;
        for (Appointments a : apptTable.getItems()) {
            LocalDateTime n = LocalDateTime.now();
            if (a.getUserID() == JdbcDao.getCurrentUser().getUserID() && a.getStart().isAfter(n)
                    && a.getStart().isBefore(n.plusMinutes(15))){
                apptAlert = a;
            }
        }

        if (apptAlert == null) {
            AlertMessage.infoBox(Alert.AlertType.INFORMATION, "No upcoming Appointments", "", "Info");
        } else {
            AlertMessage.infoBox(Alert.AlertType.INFORMATION, String.format("Appointment ID: %s \nTitle: %s \nType: %s \nDescription: %s",
                            apptAlert.getAppointmentID(), apptAlert.getTitle(), apptAlert.getType(), apptAlert.getDescription()),
                    "You have an appointment in 15 minutes or less", "Info");
        }
    }

    /** filters appointments by month and appointments by week depending on the radiobutton they select.
     * The week radiobutton shows appointments 1 week out from the current date .
     * lambda ObservableList filtered function which takes a java.util.func.Predicate*/
    public void filterAppt(){
        ObservableList<Appointments> filterAppts = AppointmentsDao.getAllAppts();
        FilteredList<Appointments> filteredList = new FilteredList<Appointments>(filterAppts);
        if (apptMonthRadio.isSelected()){
            apptTable.setItems(filteredList.filtered(a -> a.getStart().getMonth() == LocalDate.now().getMonth()));
        } else if (apptWeekRadio.isSelected()){
            LocalDateTime today = LocalDateTime.now();
            LocalDateTime plusWeek = today.plusWeeks(1);
            apptTable.setItems(filteredList.filtered((a -> a.getStart().isAfter(today) && a.getStart().isBefore(plusWeek))));
        } else {
            refreshApptTable();
        }

    }

    /** displays data from DB to create the report required by the assessment
     * to show the total number of customer appointments by type and month */
    public void monthTypeReport(ActionEvent actionEvent) throws SQLException {
        reportTxtArea.setText(ReportsDao.totalAppointments());
    }

    /** displays data from DB to create the report required by the assessment
     * to show a schedule for each contact in your organization that includes appointment ID, title, type
     * and description, start date and time, end date and time, and customer ID */
    public void contactSchedules(ActionEvent actionEvent) throws SQLException {
        reportTxtArea.setText(ReportsDao.contactSchedule());
    }
    /** displays data from DB to create the report of every customer's information and when their account was
     * created or could be used to show when they joined as a customer. */
    public void customerInfo(ActionEvent actionEvent) throws SQLException {
        reportTxtArea.setText(ReportsDao.customerinfo());
    }

    public void exportBeneficiares(ActionEvent actionEvent) {
        ReportsDao.export();
    }

    public void getContact(ActionEvent actionEvent) {
        String n = contactSearchBox.getText();
        boolean numeric = true;
        numeric = n.matches("\\d+");

        ObservableList<Appointments> filterAppts = AppointmentsDao.getAllAppts();
        FilteredList<Appointments> filteredList = new FilteredList<Appointments>(filterAppts);

        if(numeric) {
            apptTable.setItems(filteredList.filtered((a -> a.getAppointmentID() == Integer.parseInt(n))));
        } else {
            apptTable.setItems(filteredList.filtered(a -> a.getContactName().contains(n) || a.getContactName().toLowerCase().contains(n)));
        }

        contactSearchBox.setText("");

    }

    public void getCustomer(ActionEvent actionEvent) {
        String n = customerSearchBox.getText();
        boolean numeric = true;
        numeric = n.matches("\\d+");

        ObservableList<Customers> filterCustomers = CustomerDao.getAllCustomers();
        FilteredList<Customers> filteredList = new FilteredList<Customers>(filterCustomers);

        if (numeric) {
            customerTable.setItems(filteredList.filtered(c -> c.getCustomerID() == Integer.parseInt(n)));
            //System.out.println("true");
        } else {
            customerTable.setItems(filteredList.filtered(c -> c.getCustomerName().contains(n) || c.getCustomerName().toLowerCase().contains(n)));
        }

        customerSearchBox.setText("");

    }

}
