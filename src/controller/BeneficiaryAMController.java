package controller;

import dao.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.AlertMessage;
import model.Beneficiary;
import model.Customers;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class BeneficiaryAMController extends CustAMController{
    private Beneficiary beneficiaries;
    int passCustId;

    @FXML
    public TextField custId,custNameTxt,custPhoneTxt,custAddressTxt,custPostTxt;
    @FXML
    public ComboBox<String> custCountryCombo;
    @FXML
    public ComboBox<String> custFirstLevelCombo;


    /** Uses inheritance to reuse the parent classes initialize method . */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
    }

    public void updateBeneficiary(int selected) throws SQLException {

        Beneficiary editBene = BeneficiaryDao.searchBeneficiary(new Beneficiary(selected));

        System.out.println(editBene.getBeneficiaryName());

        if (editBene.getBeneficiaryName() == null ) {
            custHeaderLbl.setText("New Beneficiary");
            custId.setText(String.valueOf(selected));
            beneficiaries = new Beneficiary();
            beneDeleteButton.setVisible(false); //hides delete button if in the new beneficiary screen.

        } else {
            custHeaderLbl.setText("Update Beneficiary");
            custId.setText(String.valueOf(editBene.getCustomerID()));
            custPhoneTxt.setText(String.valueOf(editBene.getPhone()));
            custNameTxt.setText(String.valueOf(editBene.getBeneficiaryName()));
            custAddressTxt.setText(String.valueOf(editBene.getAddress()));
            custPostTxt.setText(String.valueOf(editBene.getPostalCode()));
            custCountryCombo.setItems(CountriesDao.getCountryName()); //sets and lists countries by name.
            custCountryCombo.getSelectionModel().select(editBene.getCountry());
            custFirstLevelCombo.setItems(FirstLevelDivisionDao.getFilteredFlDivName(editBene.getCountry())); //sets and lists division name by finding country id.
            custFirstLevelCombo.getSelectionModel().select(editBene.getDivision());
        }

    }


    /** Uses polymorphism to share information and behavior of the CustomerAMController parent class while incorporating its own functionality. */
    @Override
    public void saveCustomer(ActionEvent actionEvent) throws SQLException {
        System.out.println(passCustId);
        Beneficiary editBene = BeneficiaryDao.searchBeneficiary(new Beneficiary(passCustId));
        beneficiaries = editBene;

        //Array lists of the different fields and comboboxes used to check if empty and provide error
        TextField[] textFields = {custId,custNameTxt,custPhoneTxt,custAddressTxt,custPostTxt};
        ComboBox[] comboBoxes = {custCountryCombo, custFirstLevelCombo};

        System.out.println(BeneficiaryAMController.this.custNameTxt);

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
        beneficiaries.setCustomerID(passCustId);
        beneficiaries.setBeneficiaryName(custNameTxt.getText());
        beneficiaries.setPhone(custPhoneTxt.getText());
        beneficiaries.setAddress(custAddressTxt.getText());
        beneficiaries.setLastUpdatedBy(JdbcDao.getCurrentUser().getUserName());
        beneficiaries.setPostalCode(custPostTxt.getText());
        beneficiaries.setDivision(custFirstLevelCombo.getValue());

        System.out.println(beneficiaries.getCustomerID());
        try {
            Boolean exists = BeneficiaryDao.beneficiaryExists(passCustId);
            if (exists) {
                BeneficiaryDao.updateBeneficiary(beneficiaries);
                AlertMessage.beneficiaryAlerts(3);
            } else {
                beneficiaries.setCreatedBy(JdbcDao.getCurrentUser().getUserName());
                BeneficiaryDao.addNewBeneficiary(beneficiaries);
                AlertMessage.beneficiaryAlerts(3);
            }
        } catch (SQLException e) {
//            System.out.println(e.getMessage());
            AlertMessage.beneficiaryAlerts(4);
            JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.PLAIN_MESSAGE);
        }

        dashboard(actionEvent).close();

    }

    /** Deletes beneficiary's from DB using DAO interface. */
    public void deleteBene(ActionEvent actionEvent) {

        Beneficiary editBene = BeneficiaryDao.searchBeneficiary(new Beneficiary(passCustId));
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Click YES to confirm", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(String.format("This will delete the selected beneficiary:\n%s", editBene.getBeneficiaryName()));
        alert.setContentText("Would you like to continue?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
            BeneficiaryDao.deleteBeneficiary(editBene);
            dashboard(actionEvent).close();
        }

    }
}
