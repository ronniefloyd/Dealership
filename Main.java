/* --------------------------------------------------- 
 *  Author: Team 3 Car Dealership
 *  Written: 2/15/23
 *  Last Updated: 4/18/2023
 *  
 *  Compilation: javac Main.java
 *  Execution: java Main
 *  
 *  This is the main class for JavaFX. Is called at the application start.
 *  Has the methods: main, start, changeScene, getView, setView, and
 *  getGoogleCloudDBConnection
 ---------------------------------------------------*/

package application;
	
import java.sql.*;
import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import backend.Employee;

public class Main extends Application {
	
    private static Stage stg;
    
    private static String view = "SampleSalesperson.fxml";
    // This variable will either be SalespersonView.fxml or ManagerView.fxml
	
	public static Employee currentUser = new Employee();
    // Holds the current Employee using the system, for use on RecordOfSale
    
    @Override
	public void start(Stage primaryStage) {
        /* This method triggers when the application is loaded.
         * It attempts to log in to the first screen which is specified in line 37
         */
		try {
		    stg = primaryStage;
			Parent root = FXMLLoader.load(getClass().getResource("CarLogin.fxml")); // Page application tries to load into
			Scene scene = new Scene(root,1920,1080);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
    public void changeScene(String fxml) throws IOException {
        // Method for changing the scene without using a root
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        stg.getScene().setRoot(root);
    }
    
    public void changeScene(String fxml, Parent root) throws IOException {
        stg.getScene().setRoot(root);
    } // method helps with carrying over textfield info from one UI to the other
    
	public static void main(String[] args) throws Exception {
	    /* Main method: Connects to the database (printing the trace) 
	     * and launches the fxml application.
	     */
	    
	    System.out.println(getGoogleCloudDBConnection());
	    launch(args);
	}
	
	public static String getView() {
	    return view;
	} // Method to get the view variable
	
	public static void setView(boolean hasManagerRights) {
	    if (hasManagerRights) {
	        view = "ManagerViewUISample.fxml";
	    } else if (!hasManagerRights) {
	        view = "SampleSalesperson.fxml";
	    }
	} // Method to set the view variable
	
	public static Employee getCurrentUser() {
	    return currentUser;
	} // getter for the currentUser
	
	public static void setCurrentUser(Employee user) {
	    currentUser = user;
	} // setter for the currentUser

	public static Connection getGoogleCloudDBConnection() throws Exception {

        //Load the MySQL JDBC driver.
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = null;

        try {
	    //IP of connection
            String url = "jdbc:mysql://34.133.59.182/carlot";
            //Assigned username of database.
            String user = "teamaccount";
            //Assigned password to username
            String pass = "475843aoa!!";
            //The getConnection() method is provided by the DriverManager class, 
            //which is responsible for managing and creating database connections.
            conn = DriverManager.getConnection(url, user, pass);
            System.out.println("connected");

           //If connection fails MySQL throws an exception
        } catch (SQLException ex) {
            //handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return conn;
    } // end getGoogleCloudDBConnection
	
}
