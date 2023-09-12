/* --------------------------------------------------- 
 *  Author: Team 3 Car Dealership
 *  Written: 2/08/23
 *  Last Updated: 4/25/2023
 *  
 *  Compilation: javac CreateOrderforManufacturerController.java
 *  Execution: java CreateOrderforManufacturerController
 *  
 *  Handles creating an order for the manufacturer.
 *  Corresponding fxml file is CreateOrderforManufacturer.fxml
 ---------------------------------------------------*/

package application;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.collections.*;
import java.io.IOException;

public class CreateOrderforManufacturerController {
    
    // Initialize UI Fields
    
    // Initialize the Make field with all possible values.
    @FXML
    private ChoiceBox<String> MakeCar;
	final private ObservableList<String> makeList = FXCollections.observableArrayList("Acura", "Alfa Romeo", "Aston Martin", "Audi", "Bentley", "BMW", "Buick", 
	       "Cadillac", "Chevrolet", "Chrysler", "Dodge", "Ferrari", "Fiat", "Ford", "Genesis", "GMC", "Honda", "Hyundai", "Infiniti", "Jaguar", "Jeep", "Kia", 
	       "Lamborghini", "Land Rover", "Lexus", "Lincoln", "Maserati", "Mazda", "McLaren", "Mercedes-Benz", "Mini", "Mitsubishi", "Nissan", "Porsche", "Ram", 
	       "Rolls-Royce", "Subaru", "Tesla", "Toyota", "Volkswagen", "Volvo");
	 
    @FXML
    private TextField ModelCar;
	 
    @FXML
    private Button ReturnButton;
	 
    @FXML
    private Button SendButton;
	 
    @FXML
    private Button ClearButton;
	 
    @FXML
    private TextField OrderID;
	 
    @FXML
    private TextField Year;
	 
    @FXML
    private TextField Color;
	 
    @FXML
    private Label invalidInput; // label which states the incorrect input
	 
    protected String successPrompt = String.format("-fx-text-fill: GREEN;"); // correct input with green font
    protected String failurePrompt = String.format("-fx-text-fill: RED;"); // incorrect input with red font

    private String previousPage = Main.getView();

    public static boolean YearValidator(String yearinput) { // Input validation of Year field
	    
  	return yearinput.matches("^(19|20)[0-9][0-9]$"); // yearinput matches with regular expression
	    
    } // end YearValidator method

    public void initialize() throws IOException {
	    
        // Triggers when the screen starts
        MakeCar.setValue("Select a Make");
        MakeCar.getItems().addAll(makeList);
	    
    }	// end initialize method
    
    public void clear(ActionEvent event) {
	    
	// checks the functionality of the button and resets the input field
        OrderID.clear();
	Year.clear();
	Color.clear();
	MakeCar.setValue("Select a Make");
	ModelCar.clear();
	    
    } // end clear method
	 
    public void send(ActionEvent event) throws IOException {
	    
        String order = OrderID.getText();
	String year = Year.getText();
	String model = ModelCar.getText();
	String color = Color.getText();
	// checks to see if any field is blank/empty
	if(order.isBlank() || year.isBlank() || model.isBlank() || color.isBlank()) {
                invalidInput.setText("Input fields required!");
		OrderID.setStyle(failurePrompt);
		Year.setStyle(failurePrompt);
		ModelCar.setStyle(failurePrompt);
		Color.setStyle(failurePrompt);
	}
	else 
	{   
		// checks to see if everthing is true and matches the pattern
		if(YearValidator(year) == true && model.matches("^[a-zA-Z]{0,40}$") && order.matches("^[0-9]{4}$") && color.matches("^[A-Za-z]{0,40}$")) {
                        Main m = new Main();
			m.changeScene(Main.getView());
		}
		else {
			if(YearValidator(year) != true) {
	 	 	    if(year.length() > 4 || year.length() < 4){ // checks whether the input is greater than 4
	 		 	invalidInput.setText("Year length should be 4 digits!");
	 		 	Year.setStyle(failurePrompt);
				Year.clear(); // clears the incorrect input
	 	 	    }
	 		}
			// Accepts alphabetical characters upto 40 characters allowed
		 	if(!color.matches("^[A-Za-z]{0,40}$")){
		 		Color.setStyle(failurePrompt); // When wrong input, sets incorrect style
		 		Color.clear(); // clears the incorrect input
		 	}
		 	// Accepts numbers between 0 to 9 and 4 digits length only
		 	if(!order.matches("^[0-9]{4}$")) {
		 		OrderID.setStyle(failurePrompt); // When wrong input, sets incorrect style
		 		OrderID.clear(); // clears the incorrect input
		 	}
		 	// Accepts alphabetical characters upto 40 characters allowed
		 	if(!model.matches("^[a-zA-Z]{0,40}$")){
		 		ModelCar.setStyle(failurePrompt); // When wrong input, sets incorrect style
		 		ModelCar.clear(); // clears the incorrect input
		 	}
		}
	}
    } // end send method
	
    public void returnPage(ActionEvent event) throws IOException {
	    
	// returns back to the main page
	Main m = new Main();
	m.changeScene(previousPage);
	    
    } // end returnPage method
}
