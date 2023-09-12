/* --------------------------------------------------------------------------------- 
 *  Author: Triny Nguyen
 *  
 *  Written: 3/6/2023
 *  Last Updated: 4/23/2023
 *  
 *  Compilation: javac CustomerProfileController.java
 *  Execution: java CustomerProfileController
 *  
 *  Controller class of the Customer Profile UI. The UI page allows the user to 
 *  see and update a specific customer in the database that was searched in the
 *  previous UI. 
 *  
 *  Corresponding fxml file is CustomerProfileUI.fxml
 ---------------------------------------------------------------------------------*/

package application;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.text.Text;
import javafx.scene.control.*;
import javafx.scene.*;
import java.time.LocalDate;
import dao.CustomerDao;
import backend.Address;
import backend.CustomerProfile;


public class CustomerProfileController {
	
	// customerID refers to the driver's license number
	
	// searched customer
	CustomerProfile customer = new CustomerProfile();
	
	// declare UI fields
	@FXML
	private TextField firstName, lastName, phoneNumber, customerID, streetAddress, city, state, zipCode, VINField, yearField, modelField, valueField;
	
	@FXML
    private ChoiceBox<String> makeDropdown, paymentMethod;
	
    @FXML
    private DatePicker salesDate;
	
	@FXML
	private Text updateSuccessful, nullError;
	
	@FXML
	private Button addCusToSale, returnButton, updateProfile;
	    
	@FXML
	public void initialize() { 
		
		// note: customer ID length limit may be incorrect
		
		// input validation through text formatters
		
		// only allows alphabetical characters, dash, and apostrophe
		firstName.setTextFormatter(new TextFormatter<> (change -> {
			if (change.getControlNewText().length() == 1) {
		    	change.setText(change.getText().toUpperCase());
			} // automatically turns first character into upper case
			if ((change.getControlNewText().length() > 40) ||
				(change.getText().matches("[^a-zA-Z'-]"))) {
				return null;
			}
			return change;
		}));
		
		// only allows alphabetical characters, dash, and apostrophe
		lastName.setTextFormatter(new TextFormatter<> (change -> {
			if (change.getControlNewText().length() == 1) {
		    	change.setText(change.getText().toUpperCase());
			} // automatically turns first character into upper case
			if ((change.getControlNewText().length() > 40) ||
				(change.getText().matches("[^a-zA-Z'-]"))) {
				return null;
			}
			return change;
		})); 	
		
		// only allows numbers of up to 10 
		phoneNumber.setTextFormatter(new TextFormatter<> (change -> {
			if ((change.getControlNewText().length() > 10) ||
				(change.getText().matches("[^0-9]"))) {
				return null;
			}
			return change;
		}));	
			
		// only allows a capital letters or numbers of up to 16
		customerID.setTextFormatter(new TextFormatter<> (change -> {
		    	change.setText(change.getText().toUpperCase());					
			if ((change.getControlNewText().length() > 16) ||
				(change.getText().matches("[^A-Z0-9]"))) {
						
				return null;
			}
					
			return change;
		}));
		
		// only allows numbers and regular characters up to 40
		streetAddress.setTextFormatter(new TextFormatter<> (change -> {
			if ((change.getControlNewText().length() > 40) ||
			(change.getText().matches("[^A-Za-z0-9.,\\s]"))) {
			return null;
		}
		return change;
	}));
		
		// only allows alphabetical characters and up to 40 characters
		city.setTextFormatter(new TextFormatter<> (change -> {
			if ((change.getControlNewText().length() > 40) ||
				(change.getText().matches("[^a-zA-Z\\s]"))) {
				return null;
			}
			return change;
		})); 
		
		// only allows alphabetical characters and up to 40 characters
		state.setTextFormatter(new TextFormatter<> (change -> {
			if ((change.getControlNewText().length() > 40) ||
				(change.getText().matches("[^a-zA-Z\\s]"))) {
				return null;
			}
			return change;
		}));
		
		// only allows numbers and up to 5 digits
		zipCode.setTextFormatter(new TextFormatter<> (change -> {
			if ((change.getControlNewText().length() > 5) ||
				(change.getText().matches("[^0-9]"))) {
				return null;
			}
			return change;
		}));
	} // end initialize
	
	// goes to search customer UI
    public void pageReturn(ActionEvent event) throws IOException {
        
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("SearchCustomerUI.fxml"));
    	Parent root = loader.load();
    	
    	SearchCustomerController searchCusController = loader.getController();
    	
    	// passes customer ID to search customer UI
    	searchCusController.showInformation(customerID.getText());

    	Main m = new Main();
    	m.changeScene("SearchCustomerUI.fxml", root);
        
    } // end pageReturn	
    
    // goes to record of sale UI
    public void addCusToSale(ActionEvent event) throws IOException{
    	
        try {
        	
        	CustomerDao cus = new CustomerDao();
        	this.customer = cus.retriveCustomer(customerID.getText());
        	
            // if no changes were made, go to record of sale UI
            if (customer.getFirstName().equals(firstName.getText()) &&
                	customer.getLastName().equals(lastName.getText()) &&
                	customer.getPhoneNum().equals(phoneNumber.getText()) &&
                	customer.getAddress().getStreet().equals(streetAddress.getText()) &&
                  customer.getAddress().getCity().equals(city.getText()) &&
                customer.getAddress().getCountry().equals(state.getText()) &&
              customer.getAddress().getZipCode().equals(zipCode.getText()))
            {
            	FXMLLoader loader = new FXMLLoader(getClass().getResource("RecordOfSaleUI.fxml"));
            	Parent root = loader.load();
            	
            	RecordOfSaleController recSaleController = loader.getController();
            	
            	// passes information to record of sale UI 
            	recSaleController.showInformation(firstName.getText(), lastName.getText(), customerID.getText(), yearField.getText(), makeDropdown.getValue(), 
            	modelField.getText(), VINField.getText(), valueField.getText(), paymentMethod.getValue(), salesDate.getValue());

            	Main m = new Main();
            	m.changeScene("RecordOfSaleUI.fxml", root);
            }
            
            // otherwise, prompt user to save changes before add the customer to a sale
            else {
                // print out error saying please save changes 
            	nullError.setText("Please Save Changes");
            	updateSuccessful.setText(null);
            }
                        
        } catch (Exception e) {
            System.out.println("Error in CustomerProfileController.java");
        }
    } 
    
    // receives information from search customer UI
    public void showInformation(String first, String last, String ID, String phone, String street, String city, String state, 
    		String zip, String year, String make, String model, String VIN, String price, String paymentMethod, LocalDate salesDate) {
    	firstName.setText(first);
    	lastName.setText(last);
    	customerID.setText(ID);
    	phoneNumber.setText(phone);
    	streetAddress.setText(street);
    	this.city.setText(city);
    	this.state.setText(state);
    	zipCode.setText(zip);
    	yearField.setText(year);
    	makeDropdown.setValue(make);
    	modelField.setText(model);
    	VINField.setText(VIN);
    	valueField.setText(price);
    	this.paymentMethod.setValue(paymentMethod);
    	this.salesDate.setValue(salesDate);
    } // end showInformation
    
    public void updateProfile(ActionEvent event) throws IOException {
    	
    	// input validation
    	
    	// if any fields are empty return and print out error message
    	if (customerID.getText().isBlank() || firstName.getText().isBlank() || 
    			lastName.getText().isBlank() || phoneNumber.getText().isBlank() || 
    			streetAddress.getText().isBlank() || city.getText().isBlank() || 
    			state.getText().isBlank() || zipCode.getText().isBlank()) {
        	
    		updateSuccessful.setText(null);
    		nullError.setText("*Error: Please fill out all input fields*");
    		return;
    	} 
    	
    	// if zip is less than 5 digits print out error message
    	if (zipCode.getText().length() < 5) {
    		updateSuccessful.setText(null);
    		nullError.setText("*Error: Please input a valid zip code*");
    		return;	
    	}

    	// if phone number is less than 10 or a series of a repeating number print out error message
    	if (phoneNumber.getText().length() < 10 || isValidPhoneNum() == false) {
    		updateSuccessful.setText(null);
    		nullError.setText("*Error: Please input a valid phone number*");
    		return;	
    	}
    	    	
    	// end input validation
   
    	
    	// update customer information
    	
        try {
        	
        // create address object using inputed address information
        	Address custAddress = new Address(streetAddress.getText(), city.getText(), state.getText(), zipCode.getText(), "");
       
        // create new customer object with inputed customer information
        	CustomerProfile customer = new CustomerProfile(customerID.getText(), firstName.getText(), lastName.getText(), phoneNumber.getText(), custAddress);
           
        // update customer in database
        	CustomerDao dao = new CustomerDao();
            dao.updateCustomer(customer);
            
        // confirmation message
    		nullError.setText(null);
    		updateSuccessful.setText("Changes Saved");
        } catch (Exception e) {
            System.out.println("Error in CustomerProfileController.java");
        }
    } // end updateProfile
    
    // input validation: returns false if phone number is series of repeating number
    public boolean isValidPhoneNum() {
    	char a = phoneNumber.getText().charAt(0);
    	for (int i = 1; i < phoneNumber.getText().length(); i++) {
    	if (phoneNumber.getText().charAt(i) != a)
    		return true;
    	}
    	return false;
    } // end isValidPhoneNum
}
