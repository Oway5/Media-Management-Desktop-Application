package photoalbum.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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


public class LoginController{

    @FXML Button loginButton;
    @FXML TextField nameInput;

    public void login(ActionEvent input) throws IOException{
        String username = nameInput.getText().trim();

        if(username.equals("admin")){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Admin.fxml"));
			Parent sceneManager = (Parent) fxmlLoader.load();
			AdminController admin = fxmlLoader.getController();
			Scene adminScene = new Scene(sceneManager);
			Stage primaryStage = (Stage) ((Node) input.getSource()).getScene().getWindow();
			admin.start();
			primaryStage.setScene(adminScene);
			primaryStage.show();
        }
        //Check if valid user
        boolean validUser = false;
        if(validUser){
            //Load Album
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/NonAdmin.fxml"));
			Parent sceneManager = (Parent) fxmlLoader.load();
			NonAdminController admin = fxmlLoader.getController();
			Scene nonAdminScene = new Scene(sceneManager);
			Stage primaryStage = (Stage) ((Node) input.getSource()).getScene().getWindow();
			admin.start();
			primaryStage.setScene(nonAdminScene);
			primaryStage.show();
        }
        //invalid input
        
    }
}