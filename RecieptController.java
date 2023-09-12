/* --------------------------------------------------- 
 *  Author: Team 3 Car Dealership
 *  Written: 4/22/23
 *  Last Updated: 4/25/2023
 *  
 *  Compilation: javac RecieptController.java
 *  Execution: java RecipetController
 *  
 *  Controls the final reciept screen that is showsn after a purchase is made.
 *  Corresponding fxml file: RecipetUI.fxml
 ---------------------------------------------------*/

package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.Date;
import dao.*;

import backend.*;

public class RecieptController {

    @FXML
    private TextField vehiclePriceField, salesTaxField, dealershipFeeField, tagFeeField, totalField, dateOfSaleField, SalespersonIDField, commissionField, couponField;
    @FXML
    private Button SaveButton;
    
    // Variable to return user to previous page.
    private String previousPage = Main.getView();
    // Variable to hold the RecordOfSale
    private static RecordOfSale record;
    
    @FXML
    private void initialize() {
        /* This method initializes the fields with data from the RecordOfSale
         * Note that the data in these fields is uneditable.
         */
        
        /* The testData() method call is only used when we want to debug the controller
         * with test values. In the final product, this line should remain commented out.
         */
        //testData();
        
        this.vehiclePriceField.setText(record.getSoldVehicle().getPrice().toString());
        this.salesTaxField.setText(record.getSalesTax().toString());
        this.dealershipFeeField.setText(record.getDealershipFee().toString());
        this.tagFeeField.setText(record.getTagFee().toString());
        this.totalField.setText(record.calcTotal().toString());
        this.dateOfSaleField.setText(record.getDateOfSaleDate().toString());
        this.SalespersonIDField.setText(record.getEmployee().getEmployeeID().toString());
        this.commissionField.setText(record.getSalespersonCommission(record.getEmployee().getComPercentage()).toString());
        calcCoupon();
        
    }
    

    
    public void pageReturn(ActionEvent event) throws IOException {
        /* Because the transaction is complete, removes the purchased vehicle from the database
         * and recalculates the commission percentage for the employee
         * Then takes the user back the main screen.
         */
        
        removeVehicle();
        recalcCommissionPerc();
                
        Main m = new Main();
        m.changeScene(previousPage);
        
    } // end pageReturn
    
    public void removeVehicle() {
        /* This method removes the vehicle from the database, as it has been purchased.
         * 
         */
        
        Vehicle vehicleToRemove = record.getSoldVehicle();
        VehicleDao dao = new VehicleDao();
        // Try to delete the sold vehicle
        try {
            dao.deleteVehicle(vehicleToRemove);
        } catch (Exception e) {
            System.out.println("Error in deleting vehicle.");
        }
      
    } // end removeVehicle
    
    public void recalcCommissionPerc() {
        /* This method recalculates a salesperson's commission rank
         * It toals up all their sales and compares that to their rank
         */
        try {
            EmployeeDao eDAO = new EmployeeDao();
            RecordOfSaleDao rDAO = new RecordOfSaleDao();
            Employee emp = record.getEmployee();
            Double total = rDAO.getSalespersonSalesTotal(emp.getEmployeeID());
            emp.updateCommission(total);
            eDAO.updateEmployee(emp);
        } catch (Exception e) {
            System.out.println("Error updating commission percentage");
        }
    } // end recalcCommissionPerc
    
    public static void setRecieptRecord(RecordOfSale newRecord) {
        // Sets the recipet record. It can be called from other screens because it is static.
        record = newRecord;
    }
    
    private void calcCoupon() {
        /* This method calculates if a coupon should be issued to a customer.
         * A customer is issued a coupon when the total of their purhcase is above $50,000.00
         */
        if (this.record.calcTotal() >= 50_000.00) {
            this.couponField.setText("Issue a coupon for a year of Discount Car Washes to this customer.");
        } else {
            this.couponField.setText("This purchase does not qualify for a Discount Car Wash coupon.");
        }
    } // end calcCoupon
    
    private void testData() {
        /* This method loads some test data into the fields.
         * It is useful for debugging this controller.
         * In the final product, this method should not be called.
         */
        Vehicle v = new Vehicle("123456789", new Double(100000.00), new Integer(1996), "Ford", "Fiesta", "New", "Broken", "Grey", new Integer(120000), new Date(System.currentTimeMillis()));
        Employee e = new Employee("John", "Doe", false, "12345678");
        e.setEmployeeID(123);
        CustomerProfile c = new CustomerProfile("1234567890", "Alice", "Wonderland", "8431234567", new Address());
        record = new RecordOfSale(v, e, c);
    }
    
}
