/* --------------------------------------------------- 
 *  Author: Team 3 Car Dealership
 *  Written: 1/25/23
 *  Last Updated: 4/18/2023
 *  
 *  Compilation: javac RecordVehicleController.java
 *  Execution: java RecordVehicleController
 *  
 *  Handles recording new vehicles, both new and used.
 *  Corresponds to fxml file: RecordVehicleUI.fxml
 ---------------------------------------------------*/

package application;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.collections.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import dao.VehicleDao;
import backend.Vehicle;

public class RecordVehicleController {
    
    // Declare UI Fields
    
    @FXML
    private Text nullError;
    @FXML
    private TextField ValueField;
    @FXML
    private TextField VINField;
    @FXML
    private TextField YearField;
    @FXML
    private ChoiceBox<String> MakeDropdown;
    @FXML
    private TextField ModelField;
    @FXML
    private TextField MileageField;
    @FXML
    private TextField ColorField;
    @FXML
    private ChoiceBox<String> BodyConDropdown;
    @FXML
    private ChoiceBox<String> MechConDropdown;
    @FXML
    private Button SaveButton;    
    @FXML
    private Button ClearButton;
    @FXML
    private Button ReturnButton;
    
    // List of all possible conditions
    final private ObservableList<String> conditionList = FXCollections.observableArrayList("New", "Excellent", "Good", "Average", "Fair", "Poor", "Broken");
    // List of all possible makes
    final private ObservableList<String> makeList = FXCollections.observableArrayList("Acura", "Alfa Romeo", "Aston Martin", "Audi", "Bentley", "BMW", "Buick", 
            "Cadillac", "Chevrolet", "Chrysler", "Dodge", "Ferrari", "Fiat", "Ford", "Genesis", "GMC", "Honda", "Hyundai", "Infiniti", "Jaguar", "Jeep", "Kia", 
            "Lamborghini", "Land Rover", "Lexus", "Lincoln", "Maserati", "Mazda", "McLaren", "Mercedes-Benz", "Mini", "Mitsubishi", "Nissan", "Porsche", "Ram", 
            "Rolls-Royce", "Subaru", "Tesla", "Toyota", "Volkswagen", "Volvo");
    
    private String previousPage = Main.getView();
    
    
    @FXML
    private void initialize() {
        
        /* This method is called automatically, and initializes the dropdown
         * boxes with the values of the source ObservableLists*/
        
        MakeDropdown.setValue("Select a Make");
        MakeDropdown.setItems(makeList);
        
        BodyConDropdown.setValue("Select a Condition");
        BodyConDropdown.setItems(conditionList);
        MechConDropdown.setValue("Select a Condition");
        MechConDropdown.setItems(conditionList);
     
     
      // start input validation

     // only allows alphabetical characters up to 40
     ModelField.setTextFormatter(new TextFormatter<> (change -> {
      if ((change.getControlNewText().length() > 40) || (change.getText().matches("[^a-zA-Z]"))) {
        		return null;
        	}
        	return change;
     })); 
        
     // only allows alphabetical characters up to 40
		   ColorField.setTextFormatter(new TextFormatter<> (change -> {
      if ((change.getControlNewText().length() > 40) || (change.getText().matches("[^a-zA-Z]"))) {
         return null;
			      }
			      return change;
	    	}));

		      // only allows numbers up to 4
		   YearField.setTextFormatter(new TextFormatter<> (change -> {
			    if ((change.getControlNewText().length() > 4) || (change.getText().matches("[^0-9]"))) {
				    return null;
			     }
		     	return change;
		    }));
		
     // only allows numbers and periods up to 20
		   ValueField.setTextFormatter(new TextFormatter<> (change -> {
      if ((change.getControlNewText().length() > 20) ||	(change.getText().matches("[^0-9.]"))) {
       return null;
			    }
			     return change;
	  	}));
		
     // only allows alphabetical characters and numbers up to 17
		  VINField.setTextFormatter(new TextFormatter<> (change -> {
     change.setText(change.getText().toUpperCase());
     if ((change.getControlNewText().length() > 17) || (change.getText().matches("[^0-9A-Z]"))) {
      return null;
      }
			   return change;
		 })); 
		
     // only allows numbers up to 20
		  MileageField.setTextFormatter(new TextFormatter<> (change -> {
			  if ((change.getControlNewText().length() > 20) ||	(change.getText().matches("[^0-9]"))) {
			  	return null;
	  		}
	  		return change;
	  	})); 
     
     // end input validation
     
     
        
    } // end initialize method
    
    public void save(ActionEvent event) throws IOException {
        
        /* Saves the vehicle to the database. Creates a vehicle with the values inputted by the user,
         * then creates a DAO instance to save it to database.
         */
    	
    	
   	// input validation
    	
    	// if any fields are empty print out error message
    	if (ModelField.getText().isBlank() || MakeDropdown.getValue() == null ||  MakeDropdown.getValue() == "Select a Make" || YearField.getText().isBlank() || 
    			ColorField.getText().isBlank() || VINField.getText().isBlank() || ValueField.getText().isBlank() || MileageField.getText().isBlank() || 
    			BodyConDropdown.getValue() == null || BodyConDropdown.getValue() == "Select a Condition" || MechConDropdown.getValue() == null || 
    			MechConDropdown.getValue() == "Select a Condition") {
        	
    		nullError.setText("*Error: Please fill out all input fields*");
    		return;
    	}
    			
    	// if year is not between current year and year when cars were invented print out error message
    	else if (yearIsValid() == false) {
    		nullError.setText("*Error: Please input a valid year*");
    		return;
    	}
    	
    	// if price has more than 2 decimal places print out error message
    	if (!ValueField.getText().matches("^\\d+\\.\\d{0,2}$") && !ValueField.getText().matches("[0-9]*$")) {
    		nullError.setText("*Error: Please input a valid price*");
    		return;
    	}
    	// end input validation
    	
    	
        try {
        	Vehicle veh = new Vehicle();
            VehicleDao dao = new VehicleDao();
            
            // retrieve vehicle from database with inputed VIN number
            veh = dao.retriveVehicle(VINField.getText());
            
            // if retrieved vehicle's VIN number is equal to the inputed number, vehicle already exists in database
            // prevent user from saving multiple vehicles in the database with same license number
        	if (veh.getVIN().equals(VINField.getText())) {
        		nullError.setText("Vehicle Already Registered Under Inputed VIN");
        		return;
        	}
        	
        Vehicle vehicle = new Vehicle(VINField.getText(), Double.valueOf(ValueField.getText()), Integer.valueOf(YearField.getText()), MakeDropdown.getValue(),
                ModelField.getText(), BodyConDropdown.getValue(), MechConDropdown.getValue(), ColorField.getText(), Integer.valueOf(MileageField.getText()), new Date());
        
        System.out.println(vehicle.getVIN() + " " + vehicle.getValue() + " " + vehicle.getYear() + " " + vehicle.getMake() + " " + vehicle.getModel() + " " +vehicle.getBodyCondition() + " " + vehicle.getMechCondition() + " " + vehicle.getColor() +
        " " + vehicle.getMileage() + " " + vehicle.getDatePutOnLot());
            dao.saveVehicle(vehicle);

        } catch (Exception e) {
            System.out.println("Error in RecordVehicleController.java");
        }
        
        Main m = new Main();
        m.changeScene(Main.getView());
        
    } // end save
    
    public void clear(ActionEvent event) {
        
        /* This method clears all of the fields.*/
        
        ValueField.clear();
        VINField.clear();
        YearField.clear();
        MakeDropdown.setValue("Select a Make");
        ModelField.clear();
        MileageField.clear();
        ColorField.clear();
        BodyConDropdown.setValue("Select a Condition");
        MechConDropdown.setValue("Select a Condition");
        nullError.setText("");
        
    } // end clear
    
    public void pageReturn(ActionEvent event) throws IOException {
        // Returns to the previous page
        
        
        Main m = new Main();
        m.changeScene(previousPage);
        
    } // end pageReturn
	
    // receives information from search vehicle UI
    public void showInformation(String VIN) {
    	VINField.setText(VIN);
    } // end showInformation
    
	
	// returns true if year is greater than 1889 (year cars were invented)
	public boolean yearIsValid() {
		int year = Integer.valueOf(YearField.getText());
		if (year > LocalDate.now().getYear() || year < 1889)
			return false;
		
		else return true;
	} // end yearIsValid
    
} // end RecordVehicleController
