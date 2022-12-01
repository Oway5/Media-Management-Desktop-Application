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
import javax.swing.Action;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Optional;
/**
 * Interface that helps the user to logout
 */
public interface LogoutController {

	/**
	 * Logs user out, returning them to the login page
	 * @param e ActionEvent provided by fxml
     * @throws IOException
	 */
    default void logMeOut(ActionEvent e) throws IOException {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirm Logout");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to logout?");

		Optional<ButtonType> confirm = alert.showAndWait();
        if (confirm.get() == ButtonType.OK) { 
			FXMLLoader logoutLoader = new FXMLLoader();
			logoutLoader.setLocation(getClass().getResource("/photoalbum/view/Login.fxml"));

			// FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/photoalbum/view/Login.fxml"));
			// Parent newScene = (Parent) fxmlLoader.load();
			Parent newScene = (Parent) logoutLoader.load();

			Scene adminScene = new Scene(newScene);
			Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
			appStage.setScene(adminScene);
			appStage.show();	      
		} else {
			return;
		}

    }
}
