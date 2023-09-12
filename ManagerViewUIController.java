package application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;

public class ManagerViewUIController {
	
    @FXML
    private Button CreateOrderDealership;

    @FXML
    private Button CreateOrderManufacturer;

    @FXML
    private Button EnterPurchase;

    @FXML
    private Button RecordNewArrival;

    @FXML
    private Button RegisterCustomer;

    @FXML
    private Button SetWeeklyDiscounts;

    @FXML
    private Button SignOut;

    @FXML
    private Button VerifyTadeValue;

    @FXML
    private Button ViewWeeklyReports;

    public void btnCreOrdManuClicked(ActionEvent event) throws IOException{
    	// goes to Create Order for Manufacturer page.
    	if(event.getSource() == CreateOrderManufacturer) {
            Main m = new Main();
    	    m.changeScene("CreateOrderforManufacturer.fxml");
    	}      
    }
    
    public void btnCreOrderDealClicked(ActionEvent event) throws IOException {
    	// goes to Create Order for Other Dealership page.
	if(event.getSource() == CreateOrderDealership) {
	    Main m = new Main();
	    m.changeScene("CreateOrderforOtherDealership.fxml");
	}
    }

    public void btnEntPurchClicked(ActionEvent event) throws IOException {
        // goes to Enter Purchase page.
    	if(event.getSource() == EnterPurchase) {
    	    Main m = new Main();
    	    m.changeScene("RecordOfSaleUI.fxml");
    	}
    }

    public void btnRecNewArrivClicked(ActionEvent event) throws IOException {
    	// goes to Record New Arrival page.
	if(event.getSource() == RecordNewArrival) {
	    Main m = new Main();
	    m.changeScene("RecordVehicleUI.fxml");
	}
    }

    public void btnRegCustClicked(ActionEvent event) throws IOException {
    	// goes to Register Customer page.
    	if(event.getSource() == RegisterCustomer) {
    	    Main m = new Main();
    	    m.changeScene("RegCustomerUI.fxml");
    	}
    }

    public void btnSetWeekDiscClicked(ActionEvent event) throws IOException {
    	// goes to Set Weekly Discounts page.
    	if(event.getSource() == SetWeeklyDiscounts) {
    	    Main m = new Main();
    	    m.changeScene("SetDiscountsUI.fxml");
    	}
    }

    public void btnSignOutClicked(ActionEvent event) throws IOException {
    	// goes to Sign Out page.
    	if(event.getSource() == SignOut) {
    	    Main m = new Main();
    	    m.changeScene("CarLogout.fxml");
    	}
    }

    public void btnVerTradValClicked(ActionEvent event) throws IOException {
    	// goes to Verify Trade-In Value page.
    	// Had to change the names in the original files.
    	if(event.getSource() == VerifyTadeValue) {
    	    Main m = new Main();
    	    m.changeScene("VerifyTradeInUI.fxml");
    	}
    }

    public void btnViewWeekRepClicked(ActionEvent event) throws IOException {
    	// goes to View Weekly Reports page.
    	// In progress.
    	if(event.getSource() == ViewWeeklyReports) {
    	    Main m = new Main();
    	    m.changeScene("SalesPersonOfTheYearUI.fxml");
    	}
    }
}
