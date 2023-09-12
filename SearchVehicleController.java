/* --------------------------------------------------------------------------------- 
 *  Author: Triny Nguyen
 *  
 *  Written: 3/6/2023
 *  Last Updated: 4/23/2023
 *  
 *  Compilation: javac SearchVehicleController.java
 *  Execution: java SearchVehicleController
 *  
 *  Controller class of the Search Vehicle UI. The UI page allows the user to 
 *  search a vehicle in the database using the vehicle's VIN number.
 *  
 *  Corresponding fxml file is SearchVehicleUI.fxml
 ---------------------------------------------------------------------------------*/

package application;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import java.time.LocalDate;
import javafx.scene.text.Text;
import backend.Vehicle;
import dao.VehicleDao;


public class SearchVehicleController {
	
	// searched vehicle
	private Vehicle veh;
	
	// Declare UI Fields
	
    @FXML
    private TextField VINField, mileageField, valueField, 
    customerID, custFirstName, custLastName, tempYear, tempModel, tempVIN, tempValue;
    
    @FXML
    private ChoiceBox<String> paymentMethod, makeDropdown, bodyConDropdown, mechConDropdown, tempMake;
	
    @FXML
    private DatePicker salesDate;
    
    @FXML
    private Button searchButton, clearButton, returnButton, purchaseVehButton, openVehInfo, registerVeh;    
    
    @FXML
    private Text vehInfo;
    
    @FXML
    private void initialize() {
        
        /* This method is called automatically, and initializes the dropdown
         * boxes with the values of the source ObservableLists*/
    	
        // only allows alphabetical characters and numbers up to 17
    	VINField.setTextFormatter(new TextFormatter<> (change -> {
	    	change.setText(change.getText().toUpperCase());
			if ((change.getControlNewText().length() > 17) ||
				(change.getText().matches("[^0-9A-Z]"))) {
				return null;
			}
			return change;
		})); 
    } // end initialize method
    
   
    public void clear(ActionEvent event) {
        /* This method clears all of the fields.*/
    	VINField.clear();
    	vehInfo.setText(null);
    	this.veh = null;
    } // end clear
    
    // returns to record of sale UI
    public void pageReturn(ActionEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("RecordOfSaleUI.fxml"));
    	Parent root = loader.load();
    	
    	// passes information back to record of sale UI
    	RecordOfSaleController controller = loader.getController();
    	controller.showInformation(custFirstName.getText(), custLastName.getText(), customerID.getText(), tempYear.getText(), tempMake.getValue(), tempModel.getText(), tempVIN.getText(), tempValue.getText(), paymentMethod.getValue(), salesDate.getValue());
    	
    	Main m = new Main();
    	m.changeScene("RecordOfSaleUI.fxml", root);

    } // end pageReturn
    
    public void searchVehicle(ActionEvent event) throws IOException {
    	
    	// if input field is empty return and print out error message
    	if (VINField.getText().isBlank()){
    		vehInfo.setText("*Please input a VIN Number*");
    		return;
    	}
    	
        try {
        
        // search database for vehicle with inputed VIN
            VehicleDao dao = new VehicleDao();
            this.veh = dao.retriveVehicle(VINField.getText());
            
        // if VIN of searched vehicle is null, print message
            if (veh.getVIN() == null || veh.getVIN().equals("No VIN Specified")) {
        		vehInfo.setText("No Vehicle Found");
            }
            else {
                // prints information of the searched vehicle
            	vehInfo.setText("	\r\n"
            			+ veh.getYear() + " "
            			+ veh.getMake() + " "
            			+ veh.getModel() + " "
                    	+ "\r\n\nColor:  "
            			+ veh.getColor()
            			+ "\r\n\nBody Condition:  "
            			+ veh.getBodyCondition()
            			+ "\r\n\nMechanical Condition:  "
            			+ veh.getMechCondition() 
            			+ "\r\n\nMileage:  "
            			+ veh.getMileage()
            			+ "\r\n\nPrice:  $"
            			+ String.format("%,.2f", veh.getValue())
            			+ "\r\n\nVIN:  "
            			+ veh.getVIN());
            }
        } catch (Exception e) {
        	System.out.println("Error in SearchVehicleController.java");
        	return;
        }
      }
    
    // only accessible for managers
    // goes to the register vehicle UI
    public void registerVeh(ActionEvent event) throws IOException {
    
    	// if input field is empty return and print out error message
    	if (this.veh == null){
    		vehInfo.setText("*Please Search Before Trying To Register*");
    		return;
    	}
    	
    	// prevents user from trying to register a new vehicle without searching, since VIN should be unique
    	if (veh.getVIN().toString().equals(VINField.getText()) || vehInfo.getText().equals("No Vehicle Found")) {    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("RecordVehicleUI.fxml"));
    	Parent root = loader.load();
    	
    	// passes inputed VIN to register vehicle UI
    	RecordVehicleController controller = loader.getController();
    	controller.showInformation(VINField.getText());
    	
    	Main m = new Main();
    	m.changeScene("RecordVehicleUI.fxml", root);
    	}
    	
    	// if no vehicle is searched, prevent user from registering vehicle
    	else {
    		vehInfo.setText("*Please Search Before Trying To Register*");
    	}

    } // end registerVeh
    
    // only accessible for managers
    // opens vehicle information UI of searched vehicle
    public void openVehInfo(ActionEvent event) throws IOException{ 
    	
    	// if no VIN inputed
    	if (this.veh == null) {
    		vehInfo.setText("*Please Search Before Trying To Open Vehicle Information*");
    		return;
    	}
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("VehicleInformationUI.fxml"));
    	Parent root = loader.load();
    	
    	// passes searched vehicle VIN to the vehicle information UI
    	VehicleInformationController controller = loader.getController();
    	controller.showInformation(veh.getVIN(), veh.getYear(), veh.getMake(), veh.getModel(), veh.getBodyCondition(), 
    			veh.getMechCondition(), veh.getMileage(), veh.getColor(), veh.getPrice(), veh.getDatePutOnLot(), custFirstName.getText(), 
    			custLastName.getText(), customerID.getText(), paymentMethod.getValue(), salesDate.getValue());

    	Main m = new Main();
    	m.changeScene("VehicleInformationUI.fxml", root);
    	
	} // end openVehInfo
	
	@FXML
	// goes to the record of sales UI
	public void purchaseVeh(ActionEvent event) throws IOException {	
    	// if no VIN searched
    	if (this.veh == null) {
    		vehInfo.setText("*Please Search Before Trying To Add To Sale*");
    		return;
    	}
    	
    	// if the vehicle with the specified VIN is not in the database, prevent user from adding it to sale
    	if (veh.getVIN().equals("No VIN Specified")) {
    		vehInfo.setText("Please Input a Registered VIN");
    		return;
    	}
    	
		try {
			
		    FXMLLoader loader = new FXMLLoader(getClass().getResource("RecordOfSaleUI.fxml"));
		   	Parent root = loader.load();
		   	RecordOfSaleController recSaleController = loader.getController();
		   	
			// sends searched vehicle information as well as customer information to record of sale UI
		   	recSaleController.showInformation(custFirstName.getText(), custLastName.getText(), customerID.getText(), veh.getMake(), veh.getModel(), 
		   			veh.getYear().toString(), veh.getVIN(), veh.getValue().toString(), paymentMethod.getValue(), salesDate.getValue());
	    	
	    	Main m = new Main();
	    	m.changeScene("RecordOfSaleUI.fxml", root);
        } catch (Exception e) {
        	System.out.println("Error in SearchVehicleController.java");
        	return;
        }		
	} // end purchaseVeh
	
	// receives information from vehicle information UI
    public void showInformation (String VIN) {
    	VINField.setText(VIN);
    } // end showInformation
    
    // receives car information from record of sales UI
    public void showInformation(String first, String last, String cusID, String year, String make, String model, String VIN, String price, String paymentMethod, LocalDate salesDate) {
    	custFirstName.setText(first);
    	custLastName.setText(last);
    	customerID.setText(cusID);
    	tempYear.setText(year);
    	tempMake.setValue(make);
    	tempModel.setText(model);
    	tempVIN.setText(VIN);
    	tempValue.setText(price);
    	this.paymentMethod.setValue(paymentMethod);
    	this.salesDate.setValue(salesDate);
    } // end showInformation
    
} // end RecordVehicleController
