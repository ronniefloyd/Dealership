/* --------------------------------------------------- 
 *  Author: Team 3 Car Dealership
 *  Written: 1/30/23
 *  Last Updated: 4/18/2023
 *  
 *  Compilation: javac RegisterCustomerController.java
 *  Execution: java RegisterCustomerController
 *  
 *  Handles registering a new customer
 *  Corresponding fxml file is RegCustomerUI.fxml
 ---------------------------------------------------*/

package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import java.io.IOException;
import dao.CustomerDao;
import dao.VehicleDao;
import backend.Address;
import backend.CustomerProfile;
import backend.Vehicle;
import javafx.scene.control.TextFormatter;
import javafx.scene.text.Text;

public class RegisterCustomerController {
    
    
    // Declare UI Fields
    @FXML
    private TextField FirstNameField;
    @FXML
    private TextField LastNameField;
    @FXML
    private TextField AddressField;
    @FXML
    private TextField CityField;
    @FXML
    private TextField StateField;
    @FXML
    private TextField ZIPField;
    @FXML
    private TextField PhoneField;
    @FXML
    private TextField DriversLicenseField;
    @FXML
    private Button SaveButton;
    @FXML
    private Button ClearButton;
    @FXML
    private Button ReturnButton;

    @FXML
    private Text updateSuccessful, nullError;
    
    private String previousPage = Main.getView();
    
    @FXML
    public void initialize() {


        // Length limit of 40
		// only allows alphabetical characters, dash, and apostrophe of up to 40 characters
		FirstNameField.setTextFormatter(new TextFormatter<> (change -> {
			if ((change.getControlNewText().length() > 40) ||
				(change.getText().matches("[^a-zA-Z'-]"))) {
				return null;
			}
			return change;
		}));
		
		// only allows alphabetical characters, dash, and apostrophe of up to 40 characters
		LastNameField.setTextFormatter(new TextFormatter<> (change -> {
			if ((change.getControlNewText().length() > 40) ||
				(change.getText().matches("[^a-zA-Z'-]"))) {
				return null;
			}
			return change;
		})); 
		
		// only allows numbers, parenthesis, and dashes
		PhoneField.setTextFormatter(new TextFormatter<> (change -> {
			if (change.getText().matches("[^0-9-()]")) {
				return null;
			}
			return change;
		})); // not correct, add length limit
			
			
		// only allows numbers and uppercase characters up to 16
		DriversLicenseField.setTextFormatter(new TextFormatter<> (change -> {
				if ((change.getControlNewText().length() > 16) ||
				(change.getText().matches("[^A-Z1-9]"))) {
				return null;
			}
			return change;
		})); 
		
		// only allows numbers, regular characters, commas and periods up to 40
		AddressField.setTextFormatter(new TextFormatter<> (change -> {
			if ((change.getControlNewText().length() > 40) ||
			(change.getText().matches("[^A-Za-z1-9.,\\s]"))) {
			return null;
		}
		return change;
	}));

		// only allows alphabetical characters and up to 40 characters
		CityField.setTextFormatter(new TextFormatter<> (change -> {
			if ((change.getControlNewText().length() > 40) ||
				(change.getText().matches("[^a-zA-Z\\s]"))) {
				return null;
			}
			return change;
		}));
		
		// only allows alphabetical characters and up to 40 characters
		StateField.setTextFormatter(new TextFormatter<> (change -> {
			if ((change.getControlNewText().length() > 40) ||
				(change.getText().matches("[^a-zA-Z\\s]"))) {
				return null;
			}
			return change;
		}));
		
		// only allows numbers and up to 5 digits
		ZIPField.setTextFormatter(new TextFormatter<> (change -> {
			if ((change.getControlNewText().length() > 5) ||
				(change.getText().matches("[^0-9]"))) {
				return null;
			}
			return change;
		}));

    }
    
    public void save(ActionEvent event) throws IOException {
        
        /* This method will also need to save all of the inputs to the
         * newly created Customer object.*/
	    
	// input validation
    	
    	// if any fields are empty return and print out error message
    	if (DriversLicenseField.getText().isBlank() || FirstNameField.getText().isBlank() || 
    			LastNameField.getText().isBlank() || PhoneField.getText().isBlank() || 
    			AddressField.getText().isBlank() || CityField.getText().isBlank() || 
    			StateField.getText().isBlank() || ZIPField.getText().isBlank()) {
        	
    		updateSuccessful.setText(null);
    		nullError.setText("*Error: Please fill out all input fields*");
    		return;
    	}
    	
    	// if zip is less than 5 digits print out error message
    	if (ZIPField.getText().length() < 5) {
    		updateSuccessful.setText(null);
    		nullError.setText("*Error: Please input a valid zip code*");
    		return;	
    	}

    	// if phone number is less than 10 or a series of a repeating number print out error message
    	if (PhoneField.getText().length() < 10 || phoneNumIsRepeating() == false) {
    		updateSuccessful.setText(null);
    		nullError.setText("*Error: Please input a valid phone number*");
    		return;	
    	}
    	    	
    	// end input validation
    	
        try {

    	CustomerProfile cus = new CustomerProfile();
        CustomerDao dao = new CustomerDao();
        
        // retrieve customer from database with inputed license number
        cus = dao.retriveCustomer(DriversLicenseField.getText());
        
        // if retrieved customer's license number is equal to the inputed number, customer already exists in database
        // prevent user from saving multiple customers in the database with same license number
    	if (cus.getLicenseNum().equals(DriversLicenseField.getText())) {
    		nullError.setText("Customer Already Registered Under Inputed License Number");
    		return;
    	}
        
        Address custAddress = new Address(AddressField.getText(), CityField.getText(), StateField.getText(), ZIPField.getText(), "");
        
        custAddress.printAddress();
        
        /* Creates a new Customer with the information and a new DAO class.
         * Utilizes the DAO class to save the customer to the DB
         */
        CustomerProfile customer = new CustomerProfile(DriversLicenseField.getText(), FirstNameField.getText(), LastNameField.getText(), PhoneField.getText(), custAddress);
        
        System.out.println(customer.toString());
        
            dao.saveCustomer(customer);
        } catch (Exception e) {
            System.out.println("Error in RegisterCustomerController.java");
        }
        
        // Sends user back to main screen once save is completed
        Main m = new Main();
        m.changeScene(previousPage);
        
    } // end save
    
    public void clear(ActionEvent event) throws IOException {
        
        // Clears and resets all data fields.
        
        FirstNameField.clear();
        LastNameField.clear();
        AddressField.clear();
        CityField.clear();
        StateField.clear();
        ZIPField.clear();
        PhoneField.clear();
        DriversLicenseField.clear();
        nullError.setText("");
        
    } // end clear
    
    public void pageReturn(ActionEvent event) throws IOException {
        // Takes users back to previous page       
        
        Main m = new Main();
        m.changeScene(previousPage);
        
    } // end pageReturn
	
	    // receives information from the search customer UI
    public void showInformation(String licenseNum) {
    	DriversLicenseField.setText(licenseNum);
    }
    
    // input validation: returns false if phone number is series of repeating number
    public boolean phoneNumIsRepeating() {
    	char a = PhoneField.getText().charAt(0);
    	for (int i = 1; i < PhoneField.getText().length(); i++) {
    	if (PhoneField.getText().charAt(i) != a)
    		return true;
    	}
    	return false;
    }
    
}
