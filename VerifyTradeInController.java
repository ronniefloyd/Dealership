/* --------------------------------------------------- 
 *  Author: Team 3 Car Dealership
 *  Written: 3/10/23
 *  Last Updated: 4/25/2023
 *  
 *  Compilation: javac VerifyTradeInController.java
 *  Execution: java VerifyTradeInController
 *  
 *  Retrieves the value of a given trade in
 *  Corresponding fxml file: VerifyTradeInUI.fxml
 ---------------------------------------------------*/

package application;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class VerifyTradeInController {

    // Declare UI Fields
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
    @FXML
    private TextField ValueField;
    
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
        
    } // end initialize method
	
    public static boolean YearValidator(String yearinput) { // Input validation of Year field
  	return yearinput.matches("^(19|20)[0-9][0-9]$"); // yearinput matches with regular expression
    }
	
    public void getValue(ActionEvent event) {
	    
        /* This logic is to fake the functionality of the KellyBlueBook API.
         * We do not have the time to hook up the API, so this is just logic
         * to simulate that. It is ($300,000 - mileage) / (2030-Year)
         */
    	String year = YearField.getText();
	String model = ModelField.getText();
	String mileage = MileageField.getText();
	String color = ColorField.getText();
	// checks to see if any field is blank/empty
	if(year.isBlank() || model.isBlank() || mileage.isBlank() || color.isBlank()) {
	    YearField.setStyle(String.format("-fx-text-fill: RED;"));  // incorrect input with red font
	    ModelField.setStyle(String.format("-fx-text-fill: RED;")); // incorrect input with red font
	    ColorField.setStyle(String.format("-fx-text-fill: RED;")); // incorrect input with red font
	}
	else 
	{   
	    // checks to see if everthing is true and matches the pattern
	    if(YearValidator(year) == true && model.matches("^[a-zA-Z]{0,40}$") && mileage.matches("^[0-9]{0,8}$") && color.matches("^[A-Za-z]{0,40}$")) {
	        Double value = new Double(300_000.00);
	        value = value - new Double(MileageField.getText());
	        value = value / (2030 - new Double(YearField.getText()));
		setValue(value);
	    }
	    else {
		// Checks whether year field is correct
		if(YearValidator(year) != true) {
	 	    if(year.length() > 4 || year.length() < 4){ // checks whether the input is greater than 4
	 	    	YearField.setStyle(String.format("-fx-text-fill: RED;")); // When not correct, incorrect input with red font
			YearField.clear(); // clears the wrong input
	 	    }
	 	}
		// Accepts alphabetical characters upto 40 characters allowed
		if(!color.matches("^[A-Za-z]{0,40}$")){
		    ColorField.setStyle(String.format("-fx-text-fill: RED;")); // When not correct, incorrect input with red font
		    ColorField.clear(); // clears the wrong input
		}
		// Accepts numbers between 0 to 9 and digits length between 0 to 8 only
		if(!mileage.matches("^[0-9]{0,8}$")) {
		    MileageField.setStyle(String.format("-fx-text-fill: RED;")); // When not correct, incorrect input with red font
		    MileageField.clear(); // clears the wrong input
		}
		// Accepts alphabetical characters upto 40 characters allowed
		if(!model.matches("^[a-zA-Z]{0,40}$")){
		    ModelField.setStyle(String.format("-fx-text-fill: RED;")); // When not correct, incorrect input with red font
		    ModelField.clear(); // Clears the wrong input
		}
            }
	}   
    } // end getValue
    
    public void setValue(Double value) { // sets the value for calculation
        this.ValueField.setText("$ " + value);
    } // end setValue
    
    public void clear(ActionEvent event) {
        
        // Clears and resets all the UI fields
        
        YearField.clear();
        MakeDropdown.setValue("Select a Make");
        ModelField.clear();
        MileageField.clear();
        ColorField.clear();
        BodyConDropdown.setValue("Select a Condition");
        MechConDropdown.setValue("Select a Condition");
        ValueField.clear();
        
    }
    
    public void pageReturn(ActionEvent event) throws IOException {
            
        // Takes the user back to the previous page.
        Main m = new Main();
        m.changeScene(previousPage);
            
    } // end pageReturn
    
}
