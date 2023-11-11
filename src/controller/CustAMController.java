package controller;

import dao.CountriesDao;
import dao.CustomerDao;
import dao.FirstLevelDivisionDao;
import dao.JdbcDao;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.AlertMessage;
import model.Customers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CustAMController implements Initializable {

    @FXML
    public Label custHeaderLbl, custIdLbl, custNameLbl, custPhoneLbl, custAddressLbl, custCountryLbl, custFirstLevelLbl, custPostalLbl;
    @FXML
    public TextField custId,custNameTxt,custPhoneTxt,custAddressTxt,custPostTxt;
    @FXML
    public ComboBox<String> custCountryCombo;
    @FXML
    public ComboBox<String> custFirstLevelCombo;
    @FXML
    public Button custCancelButton, custSaveButton, custBeneButton, beneDeleteButton;
    private Customers customer;
    //public int passCustID;

    /** Initializes the comboboxes on the CustAMController Screen.
     * The addListener method has a functional interface which allows lambda. */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Listener so that when the country combo box is changed it resets the custFirstLevelCombo which division names from the DB.
        custCountryCombo.valueProperty().addListener((observable, oldVal, newVal) -> {
            if (newVal == null) {
                custFirstLevelCombo.getItems().clear();
                custFirstLevelCombo.setDisable(true);
            } else {
                custFirstLevelCombo.setDisable(false);
                try {
                    custFirstLevelCombo.setItems(FirstLevelDivisionDao.getFilteredFlDivName(custCountryCombo.getValue()));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        //Try and Catch used because of SQLException. Retrieves Country name and division names from the DB and lists them in their associated combo boxes.
        try {

            custCountryCombo.setItems(CountriesDao.getCountryName());
            custFirstLevelCombo.setItems(FirstLevelDivisionDao.getFlDivName());

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /** Customizes the CustomerAM.fxml when clicking new customer. Uses Default Constructor on Customers.java */
    public void addNewCust() {
        custHeaderLbl.setText("New Customer");
        custId.setText("Auto");
        customer = new Customers();
        custBeneButton.setVisible(false); //hides beneficiary button if it is in the new customer screen.

    }
    /** Carries customer data from selected item on the Mainform customer table to the CustAMController
     * @param selected holds the data, applies the data to the private "customer" variable to be used in this controller.
     * */
    public void editCustomer(Customers selected) throws SQLException {

        customer = selected;
        custId.setText(String.valueOf(customer.getCustomerID()));
        custPhoneTxt.setText(String.valueOf(customer.getPhone()));
        custNameTxt.setText(String.valueOf(customer.getCustomerName()));
        custAddressTxt.setText(String.valueOf(customer.getAddress()));
        custPostTxt.setText(String.valueOf(customer.getPostalCode()));
        custCountryCombo.setItems(CountriesDao.getCountryName()); //sets and lists countries by name.
        custCountryCombo.getSelectionModel().select(customer.getCountry());
        custFirstLevelCombo.setItems(FirstLevelDivisionDao.getFilteredFlDivName(customer.getCountry())); //sets and lists division name by finding country id.
        custFirstLevelCombo.getSelectionModel().select(customer.getDivision());

    }

    /** Cancel button on CustomerAM.fxml*/
    public void custCancel(ActionEvent actionEvent) {
        AlertMessage.cancelButtons(actionEvent);

    }

    /** Saves modified or new customers to the DB when clicking the save button on CustomerAM.fxml. */
    public void saveCustomer(ActionEvent actionEvent) throws SQLException {

        //Array lists of the different fields and comboboxes used to check if empty and provide error
        TextField[] textFields = {custId,custNameTxt,custPhoneTxt,custAddressTxt,custPostTxt};
        ComboBox[] comboBoxes = {custCountryCombo, custFirstLevelCombo};

        //Checks text fields for missing data.
        for (TextField textField : textFields) {
            if ((textField.getText().isEmpty())) {
                AlertMessage.customerAlerts(1);
                return;
            }
        }

        //Checks combo boxes for missing data.
        for (ComboBox comboBox : comboBoxes) {
            if (comboBox.getSelectionModel().getSelectedItem() == null) {
                AlertMessage.customerAlerts(1);
                return;
            }
        }

        //Sets data to the setters in Customers.java
        customer.setCustomerName(custNameTxt.getText());
        customer.setPhone(custPhoneTxt.getText());
        customer.setAddress(custAddressTxt.getText());
        customer.setLastUpdatedBy(JdbcDao.getCurrentUser().getUserName());
        customer.setPostalCode(custPostTxt.getText());
        customer.setDivision(custFirstLevelCombo.getValue());

        if (customer.getCustomerID() > 0) {
            CustomerDao.updateCustomer(customer);
        } else {
            customer.setCreatedBy(JdbcDao.getCurrentUser().getUserName());
            CustomerDao.addNewCustomer(customer);
        }

        dashboard(actionEvent).close();

    }

    /** Returns to Mainform.fxml after users are done editing or creating appointments. */
    public static Stage dashboard(Event event) { return (Stage) ((Control) event.getSource()).getScene().getWindow();}

    /** Opens the BeneficiaryAMController.fxml screen once the Beneficiary button is clicked. */
    public void newBeneficiary(ActionEvent actionEvent) throws IOException, SQLException {
        int selected = customer.getCustomerID();


        if (custId.getText().equals("Auto")) {
            AlertMessage.customerAlerts(2);
            System.out.println(selected);
        } else {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/BeneficiaryAM.fxml"));
            Parent root = (Parent) loader.load();
            Stage stage = new Stage();
            //Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            stage.initModality(Modality.APPLICATION_MODAL); //blocks events from being delivered to other application windows.
            stage.initStyle(StageStyle.DECORATED); //applies title bar.
            stage.setTitle("Beneficiary");
            stage.setScene(new Scene(root));
            BeneficiaryAMController controller = loader.getController(); //loads CustomerAM.fxml
            //custId.setText(controller.custId.getText());
            controller.updateBeneficiary(selected); //applies addNewCust method to custAMController.
            controller.passCustId = selected;
            AlertMessage.closeRequest(stage); //prompts alert when X on title bar is clicked.
            stage.setX(900);
            stage.setY(400);
            stage.showAndWait();



            //refreshCustomerTable();
        }
    }



}
