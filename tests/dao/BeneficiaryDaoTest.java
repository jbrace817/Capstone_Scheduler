package dao;

import model.Beneficiary;
import model.Customers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.swing.*;
import java.sql.JDBCType;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BeneficiaryDaoTest {
//    private BeneficiaryDao beneficiaryDao;
   private Beneficiary beneficiary;
//    List<Beneficiary> beneficiaries = new ArrayList<>();
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());


    @Before
    public void setUp() throws Exception {

        //beneficiary = new Beneficiary(1,1,"Samantha", "204 Diana Drive", "18914", "817-657-9062", "Admin", "test", timestamp, null, "Alabama", "U.S");
        beneficiary = new Beneficiary();
        beneficiary.setCustomerID(1);
        beneficiary.setDivisionID(1);
        beneficiary.setBeneficiaryName("Sam");
        beneficiary.setAddress("204 Diana Drive");
        beneficiary.setPostalCode("18914");
        beneficiary.setPhone("817-657-9062");
        beneficiary.setCreatedBy("Admin");
        beneficiary.setLastUpdatedBy("test");
        beneficiary.setCreateDate(timestamp);
        beneficiary.setLastUpdate(null);
        beneficiary.setDivision("Alabama");
        beneficiary.setCountry("U.S");
    }

    @Test
    public void t1addNewBeneficiary_Test() {
        JdbcDao.startConnection();


        BeneficiaryDao.addNewBeneficiary(beneficiary);
        int id = beneficiary.getCustomerID();
        Assert.assertNotNull(id);


    }



    @Test
    public void t2searchBeneficiary_Test() {
        //List<Beneficiary> beneficiaries = Arrays.asList(beneficiary);
        JdbcDao.getDbConnection();
        BeneficiaryDao.searchBeneficiary(beneficiary);

    }






    @Test
    public void t3deleteBeneficiary_Test() {
        JdbcDao.getDbConnection();
        BeneficiaryDao.deleteBeneficiary(beneficiary);

    }




    @Test
    public void t4beneficiaryExists_Test() throws SQLException {
        JdbcDao.getDbConnection();
        if (BeneficiaryDao.beneficiaryExists(beneficiary.getCustomerID())){
            System.out.println("exists");
        } else {
            System.out.println("not found");
        }
        JdbcDao.closeConnection();
    }
}