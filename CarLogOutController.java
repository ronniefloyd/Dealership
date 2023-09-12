/* --------------------------------------------------- 
 *  Author: Team 3 Car Dealership
 *  Written: 2/01/23
 *  Last Updated: 4/18/2023
 *  
 *  Compilation: javac CarLogOutController.java
 *  Execution: java CarLogOutController
 *  
 *  Handles the logout process and screen.
 *  Corresponding fxml file is CarLogOut.fxml
 ---------------------------------------------------*/

package application;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import java.io.IOException;


public class CarLogOutController {
     
    @FXML
    private Button LogoutButton;
   
    public void LogOut(ActionEvent event) throws IOException {
         Platform.exit();
    } // end LogOut method
    
    
 } // end class
