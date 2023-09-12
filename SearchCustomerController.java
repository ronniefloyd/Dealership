/* --------------------------------------------------------------------------------- 
 *  Author: Triny Nguyen
 *  
 *  Written: 3/6/2023
 *  Last Updated: 4/23/2023
 *  
 *  Compilation: javac SearchCustomerController.java
 *  Execution: java SearchCustomerController
 *  
 *  Controller class of the Search Customer UI. The UI page allows the user to 
 *  search a customer in the database using the customer's license number.
 *  
 *  Corresponding fxml file is SearchCustomerUI.fxml
 ---------------------------------------------------------------------------------*/

package application;

import java.io.IOException;
import dao.CustomerDao;
import java.time.LocalDate;
import backend.CustomerProfile;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.text.Text;


public class SearchCustomerController {
	
	// searched customer
	private CustomerProfile customer;

    // Declare UI Fields
    @FXML
    private Text errorText;
    
    @FXML
    private ChoiceBox<String> makeDropdown, paymentMethod;
    
    @FXML
    private DatePicker salesDate;
	
    @FXML
    private TextField customerID, VINField, yearField, modelField, valueField, tempFirst, tempLast, tempID, actualCusID;
    
    @FXML
    private Button searchButton, clearButton, returnButton, registerNewCustomer;
        
    @FXML
    public void initialize() {    	
    	
    	// Text Formatter Input Validation
		// only allows numbers and uppercase characters up to 16
		customerID.setTextFormatter(new TextFormatter<> (change -> {
			change.setText(change.getText().toUpperCase());					
			if ((change.getControlNewText().length() > 16) ||
			(change.getText().matches("[^A-Z0-9]"))) {
			return null;
		}
		return change;
	})); 
    } // end initialize
        
    public void clear(ActionEvent event) {
        
        /* This method clears all of the fields.*/
        
        customerID.clear();
        
    } // end clear
    
    public void pageReturn(ActionEvent event) throws IOException {
    	
        /* This method returns to the record of sale UI while retaining the information of the filled data.*/

    	FXMLLoader loader = new FXMLLoader(getClass().getResource("RecordOfSaleUI.fxml"));
    	Parent root = loader.load();
    	
    	RecordOfSaleController controller = loader.getController();
    	
    	// filled data gets passed to record of sale UI
    	controller.showInformation(tempFirst.getText(), tempLast.getText(), tempID.getText(), yearField.getText(), makeDropdown.getValue(), modelField.getText(), VINField.getText(), valueField.getText(), paymentMethod.getValue(), salesDate.getValue());
    	
    	Main m = new Main();
    	m.changeScene("RecordOfSaleUI.fxml", root);
    } // end pageReturn
    
    public void searchCus(ActionEvent event) throws IOException {   
    	
    	// if nothing is typed, return and print message to the user
    	if (customerID.getText().isBlank()) {
    		errorText.setText("*Please input a driver's license number*");
    		return;
    	}
    	
        try {
        	// searches database for customer with inputed customer ID and sets it to this.customer
            CustomerDao dao = new CustomerDao();            
            this.customer = dao.retriveCustomer(customerID.getText());
            
            // if customer is found open customer profile UI 
            if (customer.getLicenseNum() != null) {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerProfileUI.fxml"));
        	Parent root = loader.load();
        	
        	CustomerProfileController controller = loader.getController();
        	
        	// pass customer information and other additional information to the customer profile UI
        	controller.showInformation(customer.getFirstName(), customer.getLastName(), customer.getLicenseNum(), customer.getPhoneNum(), 
        			customer.getAddress().getStreet(),customer.getAddress().getCity(), customer.getAddress().getCountry(), customer.getAddress().getZipCode()	
        			,yearField.getText(), makeDropdown.getValue(), modelField.getText(), VINField.getText(), valueField.getText(), paymentMethod.getValue(), 
        			salesDate.getValue());
        	
        	Main m = new Main();
        	m.changeScene("CustomerProfileUI.fxml", root);	
            }
            
            // if customer if not found, print error message and prompt user to register customer
            else { 
            	errorText.setText("Customer Not Found, Please Register");
            }
            
        } catch (Exception e) {
    		System.out.println("Error in SearchCustomerController.java");
        	return;
        }
      
    } // end searchCus
	
	// opens RegisterCustomerUI
    public void registerNewCus(ActionEvent event) throws IOException {
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("RegisterCustomerUI.fxml"));
    	Parent root = loader.load();
    	
    	RegisterCustomerController regCusController = loader.getController();
    	
    	// passes inputed customer ID to register customer UI
    	regCusController.showInformation(customerID.getText());
    	
    	Main m = new Main();
    	m.changeScene("RegisterCustomerUI.fxml", root);
    } // end registerNewCus
    
    // receives customer ID from customer profile UI
    public void showInformation(String customerID) {
    	this.customerID.setText(customerID);
    } // end showInformation
    
    // receives recorded sale information from record of sales UI
    public void showInformation(String first, String last, String cusID, String year, String make, String model, String VIN, String price, String paymentMethod, LocalDate salesDate) {
    	tempFirst.setText(first);
    	tempLast.setText(last);
    	tempID.setText(cusID);
    	yearField.setText(year);
    	makeDropdown.setValue(make);
    	modelField.setText(model);
    	VINField.setText(VIN);
    	valueField.setText(price);
    	this.paymentMethod.setValue(paymentMethod);
    	this.salesDate.setValue(salesDate);
    } // end showInformation 
}
