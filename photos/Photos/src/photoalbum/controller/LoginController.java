/** 
 * 
 * @author Harshith Samayamantula (hs1018)
 * @author Oways Jaffer (omj9)
 * 
 */
package photoalbum.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;
import java.util.Optional;

import javax.swing.Action;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import photoalbum.*;
import photoalbum.Photos.Photos;

//Looks to be complete
/**
 * Class allows control of the login page
 */
public class LoginController{

    @FXML Button loginButton;
    @FXML TextField nameInput;
	/**
	 * Logs user in if inputted user exists
	 * @param input action event provided by fxml
     * @throws IOException
	 */
    public void login(ActionEvent input) throws IOException{
        String username = nameInput.getText().trim();

        if(username.isEmpty() || username == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Empty Input");
			alert.setHeaderText("Please enter a username");
			Optional<ButtonType> buttonClicked=alert.showAndWait();
			if (buttonClicked.get()==ButtonType.OK) {
				alert.close();
			}
			else {
				alert.close();
			}
        }
        else if(username.equals("admin")){
			FXMLLoader adminLoader = new FXMLLoader();
			adminLoader.setLocation(getClass().getResource("/photoalbum/view/Admin.fxml"));

            // FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/photoalbum/view/Admin.fxml"));
			// Parent sceneManager = (Parent) fxmlLoader.load();
			// AdminController admin = fxmlLoader.getController();

			Parent sceneManager = (Parent) adminLoader.load();
			AdminController admin = adminLoader.getController();


			Scene adminScene = new Scene(sceneManager);
			Stage primaryStage = (Stage) ((Node) input.getSource()).getScene().getWindow();
			admin.start();
			primaryStage.setScene(adminScene);
			primaryStage.show();
        }
        else if(Photos.driver.checkUser(username)){

			FXMLLoader nonadminLoader = new FXMLLoader();
			nonadminLoader.setLocation(getClass().getResource("/photoalbum/view/NonAdmin.fxml"));


            // FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/photoalbum/view/NonAdmin.fxml"));
			// Parent sceneManager = (Parent) fxmlLoader.load();
			// NonAdminController admin = fxmlLoader.getController();


			Parent sceneManager = (Parent) nonadminLoader.load();
			NonAdminController nonadmin = nonadminLoader.getController();

			Scene nonAdminScene = new Scene(sceneManager);
			Stage primaryStage = (Stage) ((Node) input.getSource()).getScene().getWindow();
			nonadmin.start();
			primaryStage.setScene(nonAdminScene);
			primaryStage.show();
        }
        else{
            Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Invalid Input - ERROR");
			alert.setContentText("The entered username is invalid!");
			alert.showAndWait();
			return;
        }
    }
}