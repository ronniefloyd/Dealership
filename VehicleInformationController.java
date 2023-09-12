/* --------------------------------------------------------------------------------- 
 *  Author: Triny Nguyen
 *  
 *  Written: 3/6/2023
 *  Last Updated: 4/23/2023
 *  
 *  Compilation: javac VehicleInformationController.java
 *  Execution: java VehicleInformationController
 *  
 *  Controller class of the Vehicle Information UI. The UI page allows the user to 
 *  see and update a specific vehicle in the database that was searched in the
 *  previous UI. The corresponding UI is only accessible by the manager. 
 *  
 *  
 *  Corresponding fxml file is VehicleInformationUI.fxml
 ---------------------------------------------------------------------------------*/

package application;

import java.time.ZoneId;
import java.time.LocalDate;
import java.util.Date;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import backend.Vehicle;
import dao.VehicleDao;

public class VehicleInformationController {

	// searched vehicle
	Vehicle veh = new Vehicle();
	
	// declare UI fields
    @FXML
    private TextField ValueField, VINField, YearField, ModelField, MileageField, ColorField, customerID, custFirstName, custLastName;
    
    @FXML
    private Text nullError, updateSuccessful;
    
    @FXML
    private Button returnButton, purchaseVehicle, saveChanges;
    
    @FXML
    private DatePicker datePutOnLot, salesDate; // time on lot calculated from date put on lot 

    @FXML
    private ChoiceBox<String> MakeDropdown, BodyConDropdown, MechConDropdown, paymentMethod;
    
    @FXML
    private Button SaveButton, ReturnButton;   
    
    // condition choices
    final private ObservableList<String> conditionList = FXCollections.observableArrayList("New", "Excellent", "Good", "Average", "Fair", "Poor", "Broken");
    
    // make choices
    final private ObservableList<String> makeList = FXCollections.observableArrayList("Acura", "Alfa Romeo", "Aston Martin", "Audi", "Bentley", "BMW", "Buick", 
            "Cadillac", "Chevrolet", "Chrysler", "Dodge", "Ferrari", "Fiat", "Ford", "Genesis", "GMC", "Honda", "Hyundai", "Infiniti", "Jaguar", "Jeep", "Kia", 
            "Lamborghini", "Land Rover", "Lexus", "Lincoln", "Maserati", "Mazda", "McLaren", "Mercedes-Benz", "Mini", "Mitsubishi", "Nissan", "Porsche", "Ram", 
            "Rolls-Royce", "Subaru", "Tesla", "Toyota", "Volkswagen", "Volvo");
        
    @FXML
    private void initialize() {
        
        /* This method is called automatically, and initializes the dropdown
         * boxes with the values of the source ObservableLists*/
        
        MakeDropdown.setItems(makeList);
        BodyConDropdown.setItems(conditionList);
        MechConDropdown.setItems(conditionList);
        
        // input validation start

        // only allows alphabetical characters up to 40
        ModelField.setTextFormatter(new TextFormatter<> (change -> {
			if ((change.getControlNewText().length() > 40) ||
				(change.getText().matches("[^a-zA-Z]"))) {
        		return null;
        	}
        	return change;
        })); 
        
        // only allows alphabetical characters up to 40
		ColorField.setTextFormatter(new TextFormatter<> (change -> {
			if ((change.getControlNewText().length() > 40) ||
				(change.getText().matches("[^a-zA-Z]"))) {
				return null;
			}
			return change;
		}));

        // only allows numbers up to 4
		YearField.setTextFormatter(new TextFormatter<> (change -> {
			if ((change.getControlNewText().length() > 4) ||
				(change.getText().matches("[^0-9]"))) {
				return null;
			}
			return change;
		}));
		
        // only allows numbers and periods up to 20
		ValueField.setTextFormatter(new TextFormatter<> (change -> {
			if ((change.getControlNewText().length() > 20) ||
				(change.getText().matches("[^0-9.]"))) {
				return null;
			}
			return change;
		}));
		
        // only allows alphabetical characters and numbers up to 17
		VINField.setTextFormatter(new TextFormatter<> (change -> {
	    	change.setText(change.getText().toUpperCase());
			if ((change.getControlNewText().length() > 17) ||
				(change.getText().matches("[^0-9A-Z]"))) {
				return null;
			}
			return change;
		})); 
		
        // only allows numbers up to 20
		MileageField.setTextFormatter(new TextFormatter<> (change -> {
			if ((change.getControlNewText().length() > 20) ||
				(change.getText().matches("[^0-9]"))) {
				return null;
			}
			return change;
		})); 
		
		// input validation end
        
    } // end initialize method 
    
    // returns to the search vehicle UI
    public void pageReturn(ActionEvent event) throws IOException {
        
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("SearchVehicleUI.fxml"));
    	Parent root = loader.load();
    	
    	SearchVehicleController searchVehController = loader.getController();
    	
    	// passes VIN to search vehicle UI
    	searchVehController.showInformation(VINField.getText());
    	
    	Main m = new Main();
    	m.changeScene("SearchVehicleUI.fxml", root);
        
    } // end pageReturn	
    
        
    // saves changes to the database
    public void saveChanges(ActionEvent event) throws IOException {
    	
    	// input validation
    	
    	// if any fields are empty print out error message
    	if (ModelField.getText().isBlank() || MakeDropdown.getValue() == null || YearField.getText().isBlank() || 
    			ColorField.getText().isBlank() || VINField.getText().isBlank() || ValueField.getText().isBlank() || 
    			MileageField.getText().isBlank() || datePutOnLot.getValue() == null || 
    			BodyConDropdown.getValue() == null || MechConDropdown.getValue() == null) {
        	
    		updateSuccessful.setText(null);
    		nullError.setText("*Error: Please fill out all input fields*");
    		return;
    	}
    			
    	// if year is not between current year and year when cars were invented print out error message
    	else if (yearIsValid() == false) {
        	updateSuccessful.setText(null);
    		nullError.setText("*Error: Please input a valid year*");
    		return;
    	}
    	
    	// if price has more than 2 decimal places print out error message
    	if (!ValueField.getText().matches("^\\d+\\.\\d{0,2}$") && !ValueField.getText().matches("[0-9]*$")) {
        	updateSuccessful.setText(null);
    		nullError.setText("*Error: Please input a valid price*");
    		return;
    	}
    	
    	// end input validation
    	
    	
    	// update vehicle information
        try {
        	// create vehicle object
            Vehicle vehicle = new Vehicle(VINField.getText(), Double.valueOf(ValueField.getText()), Integer.valueOf(YearField.getText()), MakeDropdown.getValue(), 
            		ModelField.getText(), BodyConDropdown.getValue(), MechConDropdown.getValue(), ColorField.getText(), Integer.parseInt(MileageField.getText()), 
            		java.sql.Date.valueOf(datePutOnLot.getValue()));
            
            // search vehicle through dao class
            VehicleDao dao = new VehicleDao();
            dao.updateVehicle(vehicle);
            
        	// confirmation message
    		nullError.setText(null);
    		updateSuccessful.setText("Changes Saved");
        } catch (Exception e) {
            System.out.println("Error in VehicleInformationController.java");
        }        
        
    } // end saveChanges
    
	@FXML
	// goes to record of sales UI
	public void purchaseVeh(ActionEvent event) throws IOException {	
		 
		// send error if changes were made and it wasn't saved
        try {
        	
        	VehicleDao vehicle = new VehicleDao();
        	this.veh = vehicle.retriveVehicle(VINField.getText());

            // if no changes to the input fields were made, add vehicle to record of sale
            if (veh.getVIN().equals(VINField.getText()) &&
            	veh.getValue().toString().equals(ValueField.getText()) &&
            	veh.getModel().equals(ModelField.getText()) &&
            	veh.getMake().equals(MakeDropdown.getValue()) &&
           		veh.getMileage().toString().equals(MileageField.getText()) &&
           		veh.getYear().toString().equals(YearField.getText()) &&
           		veh.getColor().equals(ColorField.getText()) &&
           		veh.getMechCondition().equals(MechConDropdown.getValue()) &&
           		veh.getBodyCondition().equals(BodyConDropdown.getValue()) &&
				veh.getDatePutOnLot().equals(java.sql.Date.valueOf(datePutOnLot.getValue())))
            {
            	
    		    FXMLLoader loader = new FXMLLoader(getClass().getResource("RecordOfSaleUI.fxml"));
    		   	Parent root = loader.load();
    		   	
    		   	RecordOfSaleController recSaleController = loader.getController();

    		   	// passes vehicle information to record of sale
    		   	recSaleController.showInformation(custFirstName.getText(), custLastName.getText(), customerID.getText(), YearField.getText(), MakeDropdown.getValue(), ModelField.getText(), 
    		   			VINField.getText(), ValueField.getText(), paymentMethod.getValue(), salesDate.getValue());
    		   	
    	    	Main m = new Main();
    	    	m.changeScene("RecordOfSaleUI.fxml", root);
            }
            else {
                // otherwise, print out error prompting user to save changes
            	nullError.setText("Please Save Changes");
            	updateSuccessful.setText(null);
            }
                        
        } catch (Exception e) {
            System.out.println("Error in VehicleInformationController.java");
        }
		
	} 
	
	// receives customer information from search vehicle UI
    public void showInformation(String VIN, Integer year, String make, String model, String body, String mech, Integer mileage, String color, Double price, Date date, String first, String last, String cusID, String paymentMethod, LocalDate salesDate) {
    	VINField.setText(VIN);
    	YearField.setText(year.toString());
    	MakeDropdown.setValue(make);
    	ModelField.setText(model);
    	BodyConDropdown.setValue(body);
    	MechConDropdown.setValue(mech);
    	MileageField.setText(mileage.toString());
    	ColorField.setText(color);
    	ValueField.setText(price.toString());
    	LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    	datePutOnLot.setValue(localDate);
    	custFirstName.setText(first);
    	custLastName.setText(last);
    	customerID.setText(cusID);
    	this.paymentMethod.setValue(paymentMethod);
    	this.salesDate.setValue(salesDate);
    } // end showInformation
	
	// returns true if year is greater than 1889 (year cars were invented)
	public boolean yearIsValid() {
		int year = Integer.valueOf(YearField.getText());
		if (year > LocalDate.now().getYear() || year < 1889)
			return false;
		
		else return true;
	} // end yearisValid
}
